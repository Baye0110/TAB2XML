package custom_model;

import java.util.ArrayList;
import java.util.List;

import custom_component_data.Measure;
import custom_component_data.Note;
import custom_component_data.Score;
import custom_model.note.NoteUnit;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

// For ALL INSTRUMENTS
public class SheetScore extends VBox{

	List<ScoreLine> lines;
	List<Double> noteTimings;
	boolean isPlaying;
	double songTempo;
	
	// Puts together all the ScoreLine Objects (ScoreLine = All the measures belonging to 1 line)

	
	/** Creates the SheetScore object
	 * 
	 * @param score			The Score XML parsing object which has all the data for the entire music piece.
	 * @param lineSize		The spacing between the staffLines (the height of the noteheads)
	 * @param pageWidth		The width of the page.
	 */
	public SheetScore(Score score, double lineSize, double pageWidth) {
		MusicMeasure.measureCount = 0;
		NoteUnit.pressed = null;
		this.songTempo = 60;
		
		this.lines = new ArrayList<>();
		
		// Creates an invisible rectangle to add empty space to the top.
		Rectangle topBuffer = new Rectangle(pageWidth, lineSize * 2);
		topBuffer.setStroke(Color.WHITE);
		topBuffer.setOpacity(0);
		this.getChildren().add(topBuffer);
		
		// This stores all the measures which will eventually be put into 1 line
		List<MusicMeasure> cumulated = new ArrayList<>();
		
		// The length of all the measures in the above ArrayList.
		double length = 0;
		
		// Create each ScoreLine of the music by iterating through each Measure XML parsed object.
		for (Measure m: score.getParts().get(0).getMeasures()) {
			// The GUI Measure is stored in "mGUI" and is created based on the instrument of this score. (TAB OR PERCUSSION)
			MusicMeasure mGUI = null;
			if (score.getParts().get(0).getMeasures().get(0).getTab()) {
				mGUI = new TabMeasure(lineSize, m, cumulated.isEmpty());
			}
			else {
				mGUI = new StaffMeasure(lineSize, m, cumulated.isEmpty());
			}
			
			/* If the length of the MusicMeasure (GUI Measures) in the cumulated array surpasses the page width
			 * then create a horizontal line of measures (Create a ScoreLine with the MusicMeasures in 'cumulated' array
			 */
	        if (length >= pageWidth) {
	        	ScoreLine sl1 = new ScoreLine(cumulated, pageWidth);
	        	this.lines.add(sl1);
	        	// Reset the cumulated ArrayList with the newly 'mGUI' as its first element and initialize the length
	        	cumulated = new ArrayList<>();
	        	cumulated.add(mGUI);
	        	length = mGUI.minWidth;
	        	this.getChildren().add(sl1);
	        }
	        /* If the length of the MusicMeasure in the 'cumulated' array will surpass the page width if we also add on
	         * this new measure, then we create a horizontal line of measures (Create a ScoreLine with the MusicMeasures in
	         * in 'cumulated' + mGUI)
	         */
	        else if (length + mGUI.minWidth >= pageWidth) {
	        	cumulated.add(mGUI);
	        	ScoreLine sl1 = new ScoreLine(cumulated, pageWidth);
	        	this.lines.add(sl1);
	        	// reset the cumulated array, and set the length to 0
	        	cumulated = new ArrayList<>();
	        	length = 0;
	        	this.getChildren().add(sl1);
	        }
	        /*
	         * Otherwise, add the mGUI to the 'cumulated' ArrayList, and add its width.
	         */
	        else { 
	        	cumulated.add(mGUI);
	        	length += mGUI.minWidth;
	        }
		}
		
		/*
		 * If at the end the ArrayList is not empty, then simply put all the remaining measures on one line
		 * which stretches the entire page width.
		 */
		if (!cumulated.isEmpty()) {
			ScoreLine sl1 = new ScoreLine(cumulated, pageWidth);
			this.lines.add(sl1);
			this.getChildren().add(sl1);
		}
		
		// Add an invisible rectangle at the bottom of the Score as a buffer.
		Rectangle bottomBuffer = new Rectangle(pageWidth, lineSize * 5);
		bottomBuffer.setStroke(Color.WHITE);
		bottomBuffer.setOpacity(0);
		this.getChildren().add(bottomBuffer);
		
		// Set the spacing between each line in the music.
		this.setSpacing(lineSize * 2.5);
	}
	
	
	public double getMeasurePosition(int measureNum) {
		double pos = 0;
		
		boolean measureFound = false;
		for (int i = 0; i < this.lines.size() && !measureFound; i++) {
			for (MusicMeasure m: this.lines.get(i).measures) {
				if (m.measureNum == measureNum) {
					measureFound = true;
					break;
				}
			}
			if (!measureFound) {
				pos += this.lines.get(i).minHeight(0) + this.getSpacing() + 8;
			}
		}
		
		return pos;
	}
	
	public void generateBasePlayTimings(Score score) {
		this.noteTimings = new ArrayList<>();
		int counter = 0;
		
		for (Measure m: score.getParts().get(0).getMeasures()) {
			for (Note n: m.getNotes()) {
				if (n.getGrace()) {
					noteTimings.add(1.0/96);
					counter ++;
				}
				else if (!n.getChord()) {
					double duration = n.getType() != 0 ? (1.0/n.getType()) : 2;
					double dotDuration = duration/2.0;
					for (int i = 0; i < n.getDot(); i++) {
						duration += dotDuration;
						dotDuration /= 2.0;
					}
					noteTimings.add(duration);
					
					if (n.getTimeModification() != null) {
						double scaleFactor =  ((double) n.getTimeModification().get("normal")) / n.getTimeModification().get("actual");
						noteTimings.set(counter, noteTimings.get(counter) * scaleFactor);
					}
					counter ++;
				}
			}
			if (m.getNotes().size() < 1) {
				double measureLength = m.getTimeSignature()[0] / (double) m.getTimeSignature()[1];
				noteTimings.set(counter - 1, noteTimings.get(counter - 1) + measureLength);
			}
		}
		
		for (int i = 0; i < this.noteTimings.size(); i++) {
			noteTimings.set(i, noteTimings.get(i) * 4000);
		}
	}
	
	public void setTempoOnTimings(int tempo) {
		double wholenoteToMillisecond = this.songTempo/tempo;
		for (int i = 0; i < this.noteTimings.size(); i++) {
			this.noteTimings.set(i, this.noteTimings.get(i) * wholenoteToMillisecond);
		}
		this.songTempo = tempo;
		System.out.println(this.noteTimings);
	}
	
	public void startHighlight() {
		this.isPlaying = true;
		
		int notePressed; int measureOfNote;
		if (NoteUnit.pressed == null) {
			notePressed = measureOfNote = 0;
		}
		else {
			notePressed = NoteUnit.pressed.getNoteNum() - 1;
			measureOfNote = NoteUnit.pressed.getMeasure() - 1;
			NoteUnit.pressed.toggleHighlight();
		}
		
		PlaybackGUILinker linkerThread = new PlaybackGUILinker(this, measureOfNote, notePressed);		
		linkerThread.start();
//		List<MusicMeasure> measures = this.getMeasureList();
//		int timingsNumber = this.getTimingOfNote(measureOfNote, notePressed, measures);
//		for (int i = measureOfNote; i < measures.size(); i++) {
//			MusicMeasure measure = measures.get(i);
//			
//			int j = (i == measureOfNote) ? notePressed : 0;
//			for (; j < measure.notes.size() && this.playing; j++) {
//				measure.notes.get(j).toggleHighlight();
//				//Thread.sleep((long) ((double) sheet.timings.get(timingsNumber)));
//									double start = System.currentTimeMillis();
//									while (System.currentTimeMillis() - start < this.timings.get(timingsNumber)) {
//									}
//									System.out.println("Done Waiting!");
//				timingsNumber ++;
//			}
//		}
	}
	
	public void stopHighLight() {
		this.isPlaying = false;
	}
	
	public List<MusicMeasure> getMeasureList() {
		List<MusicMeasure> measures = new ArrayList<>();
		for (ScoreLine line: this.lines) {
			for(MusicMeasure m: line.measures) {
				measures.add(m);
			}
		}
		return measures;
	}
	
	public int getTimingOfNote(int measureNum, int noteNum, List<MusicMeasure> measures) {
		int timingCount = 0;
		
		for (int i = 0; i < measureNum; i++) {
			timingCount += measures.get(i).notes.size();
		}
		timingCount += noteNum;
		
		return timingCount;		
	}
	
	public void resetLinker() {
		NoteUnit.pressed = null;
	}
}
