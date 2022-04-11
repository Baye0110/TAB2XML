package custom_model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import custom_component_data.Measure;
import custom_component_data.Note;
import custom_component_data.Score;
import custom_model.note.NoteUnit;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

// For ALL INSTRUMENTS
public class SheetScore extends VBox{

	List<ScoreLine> lines;
	List<Double> noteTimings;
	List<TiedPair> interMeasureTieds;
	ScrollPane sp;
	boolean isPlaying;
	double songTempo;
	public static double lineSize = 10.0; 
	public static double pageWidth = 1045.0;
	public static double measureSpacing = 0.0;
	boolean threadKilled;
	double sheetHeight;
	Pane naming;
	List<Text> titles;
	
	private class TiedPair {
		private int measureNum;
		private ArcLine arc;
		
		private TiedPair(int measureNum, ArcLine arc) {
			this.measureNum = measureNum;
			this.arc = arc;
		}
	}
	
	// Puts together all the ScoreLine Objects (ScoreLine = All the measures belonging to 1 line)

	
	/** Creates the SheetScore object
	 * 
	 * @param score			The Score XML parsing object which has all the data for the entire music piece.
	 */
	public SheetScore(Score score) {
		MusicMeasure.measureCount = 0;
		NoteUnit.pressed = null;
		this.songTempo = 60;
		this.threadKilled = true;
		
		boolean isPercussion = score.getParts().get(0).getMeasures().get(0).getPercussion();
		double currentY = 0.0;
		
		this.lines = new ArrayList<>();
		this.interMeasureTieds = new ArrayList<>();
		List<ArcLine> arcs = new ArrayList<ArcLine>();
		
		// Creates an invisible rectangle to add empty space to the top.
		Rectangle topBuffer = new Rectangle(pageWidth, lineSize * 6);
		currentY += lineSize * 2.5;
		topBuffer.setStroke(Color.WHITE);
		topBuffer.setOpacity(0);
		this.getChildren().add(topBuffer);
		
		
		// Set up for the Title and Artist Name
		this.naming = new Pane();
		this.getChildren().add(naming);
		this.titles = new ArrayList<>();
		this.titles.add(null);  this.titles.add(null);
		
		// This stores all the measures which will eventually be put into 1 line
		List<MusicMeasure> cumulated = new ArrayList<>();
		
		// The length of all the measures in the above ArrayList.
		double length = 0;
		boolean tiedRunOffMeasure = false;
		
		// Create each ScoreLine of the music by iterating through each Measure XML parsed object.
		for (int i = 0; i < score.getParts().get(0).getMeasures().size(); i++ ) {
			Measure m = score.getParts().get(0).getMeasures().get(i);
			// The GUI Measure is stored in "mGUI" and is created based on the instrument of this score. (TAB OR PERCUSSION)
			MusicMeasure mGUI = null;
			if (score.getParts().get(0).getMeasures().get(0).getTab()) {
				mGUI = new TabMeasure(lineSize, m, cumulated.isEmpty());
			}
			else {
				mGUI = new StaffMeasure(lineSize, m, cumulated.isEmpty());
			}
			
//			/* If the length of the MusicMeasure (GUI Measures) in the cumulated array surpasses the page width
//			 * then create a horizontal line of measures (Create a ScoreLine with the MusicMeasures in 'cumulated' array
//			 */
//	        if (length >= pageWidth) {
//	        	ScoreLine sl1 = new ScoreLine(cumulated, pageWidth);
//	        	this.lines.add(sl1);
//	        	// Reset the cumulated ArrayList with the newly 'mGUI' as its first element and initialize the length
//	        	cumulated = new ArrayList<>();
//	        	cumulated.add(mGUI);
//	        	length = mGUI.minWidth;
//	        	this.getChildren().add(sl1);
//	        }
	        /* If the length of the MusicMeasure in the 'cumulated' array will surpass the page width if we also add on
	         * this new measure, then we create a horizontal line of measures (Create a ScoreLine with the MusicMeasures in
	         * in 'cumulated' + mGUI)
	         */
			if (tiedRunOffMeasure) {
				cumulated.add(mGUI);
	        	length += mGUI.minWidth;
	        	mGUI.getNotes().get(0).setInterTiedEnd(arcs);
	        	this.interMeasureTieds.get(this.interMeasureTieds.size()-1).arc.setEndNote(mGUI.getNotes().get(0));
	        	if (mGUI.getRunOffTied()) {
	        		tiedRunOffMeasure = true;
	        		List<ArcLine> tied = mGUI.getNotes().get(mGUI.getNotes().size()-1).addTied(mGUI, false);
		        	for (ArcLine arc: tied) {
		        		this.interMeasureTieds.add(new TiedPair(i, arc));
		        		arcs.add(arc);
		        	}
	        	}
	        	else {
	        		tiedRunOffMeasure = false;
	        	}
	        	
			}
			else if (mGUI.getRunOffTied()) {
				if (cumulated.size() > 0) {
					ScoreLine sl1 = new ScoreLine(cumulated, pageWidth);
//					sl1.setTranslateY(currentY + (sl1.maxMeasureHeight - (mGUI.numStaffLines-1) * lineSize));
//					currentY += sl1.maxMeasureHeight + measureSpacing;
		        	this.lines.add(sl1);
		        	this.getChildren().add(sl1);
				}
				
	        	// reset the cumulated array, and set the length to new measure
				cumulated = new ArrayList<>();
	        	cumulated.add(mGUI);
	        	length = mGUI.minWidth;
	        	// set the runOffTied
	        	tiedRunOffMeasure = true;
	        	
	        	List<ArcLine> tied = mGUI.getNotes().get(mGUI.getNotes().size()-1).addTied(mGUI, false);
	        	for (ArcLine arc: tied) {
	        		this.interMeasureTieds.add(new TiedPair(i, arc));
	        		arcs.add(arc);
	        	}
	          	
			}
			else if (length + mGUI.minWidth >= pageWidth) {
	        	cumulated.add(mGUI);
	        	ScoreLine sl1 = new ScoreLine(cumulated, pageWidth);
//	        	sl1.setTranslateY(currentY + (sl1.maxMeasureHeight - (mGUI.numStaffLines-1) * lineSize));
//				currentY += sl1.maxMeasureHeight + measureSpacing;
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
		
		this.setTiedSpacings(score.getParts().get(0).getMeasures().get(0).getPercussion());
		
		// Add an invisible rectangle at the bottom of the Score as a buffer.
		Rectangle bottomBuffer = new Rectangle(pageWidth, lineSize * 5);
		bottomBuffer.setTranslateY(currentY);
		currentY += lineSize * 5;
		bottomBuffer.setStroke(Color.WHITE);
		bottomBuffer.setOpacity(0);
		this.getChildren().add(bottomBuffer);
		
		this.sheetHeight = currentY;
		// Set the spacing between each line in the music.
		double minimumSpacing = 0.0;
		if (isPercussion)
			minimumSpacing = 0.0;
		else 
			minimumSpacing = 0.0;
		minimumSpacing += measureSpacing;
		
		this.setSpacing(minimumSpacing);
		
		this.sheetHeight = 0.0;
		for (int i = 0; i < this.getScoreLines().size() - 1; i++) {
			sheetHeight += this.getScoreLines().get(i).prefHeight(0) + SheetScore.measureSpacing;
		}
	}
	
	
	public double getMeasurePosition(int measureNum) {
		double pos = 0;
		
//		boolean measureFound = false;
//		for (int i = 0; i < this.lines.size() && !measureFound; i++) {
//			for (MusicMeasure m: this.lines.get(i).measures) {
//				if (m.measureNum == measureNum) {
//					measureFound = true;
//					break;
//				}
//			}
//			if (!measureFound) {
//				pos += this.lines.get(i).minHeight(0) + this.getSpacing();
//			}
//		}
		
		int measure = 0;
		for (int i = 0; i < this.lines.size(); i++) {
			measure += this.lines.get(i).getMeasures().size();
			if (measure < measureNum) {
				pos += this.lines.get(i).prefHeight(0) + SheetScore.measureSpacing;
			}
			else {
				break;
			}
		}
		
		return pos;
	}
	
	public void generateBasePlayTimings(Score score) {
		this.noteTimings = new ArrayList<>();
		int counter = 0;
		
		for (Measure m: score.getParts().get(0).getMeasures()) {
			double graceTime = 0.0;
			
			for (Note n: m.getNotes()) {
				if (n.getGrace() && !n.getChord()) {
					this.noteTimings.add(1.0/48.0);
					graceTime += 1.0/48.0;
					counter ++;
				}
				else if (!n.getChord()) {
					double duration = n.getType() != 0 ? (1.0/n.getType()) : 2;
					double dotDuration = duration/2.0;
					for (int i = 0; i < n.getDot(); i++) {
						duration += dotDuration;
						dotDuration /= 2.0;
					}
					if (graceTime != 0.0) {
						duration -= graceTime;
						graceTime = 0.0;
					}
					this.noteTimings.add(duration);
					
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
		this.threadKilled = false;
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
	
	public List<ScoreLine> getScoreLines() {
		return this.lines;
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
	
	public void removeAllHighlight() {
		List<MusicMeasure> measures = this.getMeasureList();
		for (MusicMeasure m: measures) {
			for (NoteUnit n: m.getNotes()) {
				if (n.getHighlighted())
					n.toggleHighlight();
			}
		} 
	}
	
	public boolean getThreadKilled() {
		return this.threadKilled;
	}
	
	public void setTiedSpacings(boolean drums) {
		List<MusicMeasure> measures = this.getMeasureList();
		for (int i = 0; i < this.interMeasureTieds.size(); i++) {
			this.interMeasureTieds.get(i).arc.setPositionXInterMeasure(measures.get(this.interMeasureTieds.get(i).measureNum).minWidth, drums);
		}
	}
	
	public double getSheetHeight() {
		return this.sheetHeight;
	}
	
	public void setScrollPane(ScrollPane sp) {
		this.sp = sp;
		this.sp.setVmax(this.getSheetHeight() - SheetScore.lineSize * 3);
	}
	
	public ScrollPane getScrollPane() {
		return this.sp;
	}
	
	public void setTitle(String title) {
		Line placeholder = new Line(0,0,0,lineSize * 5); placeholder.setStroke(Color.TRANSPARENT);
		this.naming.getChildren().add(placeholder);
		
		Text titleLabel = new Text();
		titleLabel.setText(title);
		titleLabel.setFont(Font.font(MusicMeasure.customizefont, FontWeight.EXTRA_BOLD, lineSize * 4.5));
		titleLabel.setFill(Color.BLACK);
		titleLabel.setWrappingWidth(titleLabel.minWidth(0) > pageWidth - 200 ? pageWidth - 200 : 0);
		titleLabel.setTranslateX(pageWidth/2 - titleLabel.minWidth(0)/2);
		titleLabel.setTranslateY(titleLabel.minHeight(0) + lineSize);
		
		if (this.titles.get(0) != null) {
			this.naming.getChildren().remove(this.titles.get(0));
		}
		
		this.titles.set(0, titleLabel);
		this.naming.getChildren().add(titleLabel);
	}
	
	public void setAuthor(String author) {
		Line placeholder = new Line(0,0,0,lineSize * 2); placeholder.setStroke(Color.TRANSPARENT);
		this.naming.getChildren().add(placeholder);
		
		Text authorLabel = new Text();
		authorLabel.setText(author);
		authorLabel.setFont(Font.font(MusicMeasure.customizefont, FontWeight.BOLD, FontPosture.ITALIC, lineSize * 1.7));
		authorLabel.setFill(Color.BLACK);
		authorLabel.setTranslateX(pageWidth - authorLabel.minWidth(0));
		authorLabel.setTranslateY(authorLabel.minHeight(0));
		
		if (this.titles.get(1) != null) {
			this.naming.getChildren().remove(this.titles.get(1));
		}
		
		this.titles.set(1, authorLabel);
		this.naming.getChildren().add(authorLabel);
	}
	
	public Pane getNaming() {
		return this.naming;
	}
}
