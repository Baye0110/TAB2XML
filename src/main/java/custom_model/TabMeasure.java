package custom_model;

import java.util.ArrayList;
import java.util.List;

import custom_component_data.Measure;
import custom_component_data.Note;
import custom_component_data.Slide;
import custom_model.note.BoxedChord;
import custom_model.note.BoxedText;
import custom_model.note.BoxedUnit;
import custom_model.note.NoteUnit;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;

// For GUITAR, BASS
public class TabMeasure extends MusicMeasure {	
	// Create the measures for the guitar
	/* 1. Create all the textboxes for the numbers
	 * 2. Position the textboxes correctly.
	 * 4. Correct the spacing
	 * 3. Create the Stafflines
	 */ 

	// Store 2 objects:
	//   1. labels = Store the textboxes with the numbers
	//   2. stems  = Store the noteStems which are displayed under the staff
	List<TabNoteStem> stems;
	
	/**
	 * 
	 * @param size   The space between the staff lines (height)
	 * @param m 	 The Measure XML parsing object which has the data for all the notes
	 * @param start	 Is this the 'first' measure in the line (useful for the super constructor
	 */
	public TabMeasure (double size, Measure m, boolean start) {
		// Initialize the Clef and the Time Signature (4/4, 3/4, 7/8, ...) which are in the MusicMeasure superclass
		super(size, m, start);
		
		if (m.getNotes().size() == 0) {
			this.generateBarLines(size, m.getStaffLines());
			this.maxHeight = size * (m.getStaffLines());
			return;
		}
		
		// Get the list of Notes (from XML parsing)
		List<Note> notes = m.getNotes();
		
		// Initialize the arrays
		this.notes = new ArrayList<NoteUnit>();
		this.stems = new ArrayList<TabNoteStem>();
		this.links = new ArrayList<NoteLinker>();
		
		// initialize the height of the staff based on the number of lines
		this.maxHeight = size * (m.getStaffLines());
		
		// Reset the count of notes
		BoxedUnit.noteCounter = 0;
		
		// Create the BoxedTest for the guitar numbers for each Note.
		for (int i = 0; i < notes.size(); i++) {
			Note currentNote = notes.get(i);
			
			List<Note> unitParts = new ArrayList<>();
			unitParts.add(currentNote);
			BoxedUnit boxedUnit = null;
			
			boolean isChord = i + 1 < notes.size() && notes.get(i + 1).getChord();
			while(isChord) {
				i++;
				unitParts.add(notes.get(i));
				isChord = i + 1 < notes.size() && notes.get(i + 1).getChord();
			}
			
			int string = currentNote.getNotation().getString();
			if (unitParts.size() == 1) {
				double type = currentNote.getType() != 0 ? currentNote.getType() : 0.5;
				if (currentNote.getGrace()) {
					boxedUnit = new BoxedText("" + currentNote.getNotation().getFret(), size*0.65, type, true, false, this.measureNum, currentNote);
					boxedUnit.setTranslateY(size * 0.35);
				}
				else {
					boxedUnit = new BoxedText("" + currentNote.getNotation().getFret(), size, type, false, false, this.measureNum, currentNote);
				}
			}
			else {
				boxedUnit = new BoxedChord(size, unitParts, this.measureNum, unitParts.get(0).getGrace());
				for (Note note: unitParts) {
					if (note.getNotation().getString() < string) {
						string = note.getNotation().getString();
					}
				}
			}
			
			if (!currentNote.getGrace()) {
				TabNoteStem stem = new TabNoteStem(size, notes.get(i).getType(), notes.get(i).getDot());
				stem.setTranslateX(currentDistance + (boxedUnit.minWidth(0)/2));
				stem.setTranslateY(size * m.getStaffLines());
				this.stems.add(stem);
			}
			
			boxedUnit.setTranslateX(currentDistance);
			boxedUnit.setTranslateY(size * (string - 1.5) + boxedUnit.getTranslateY());
			this.notes.add(boxedUnit);
			
			if (currentNote.getNotation().getSlides().size() != 0) {
				List<Slide> slides = currentNote.getNotation().getSlides();
				boolean init = false; 
				boolean end = false;
				for (Slide slide: slides) {
					if (slide.getType().equals("start")) 
						init = true;
					if (slide.getType().equals("stop"))
						end = true;
				}
				if (init) {
					double y_pos = (-0.5 + currentNote.getNotation().getString()) * size;
					SlideLink slide = new SlideLink(this.currentDistance + boxedUnit.getWidth(), y_pos);
					slide.setTranslateX(this.currentDistance + boxedUnit.getWidth());
					slide.setTranslateY(y_pos);
					slide.setStartVal(currentNote.getNotation().getFret());
					this.links.add(slide);
				}
				if (end) {
					NoteLinker link = this.links.get(this.links.size()-1);
					int j = 1;
					while (link.getClass() != SlideLink.class) {
						link = this.links.get(this.links.size()-(++j));
					}
					SlideLink slide = (SlideLink) link;
					slide.endVal = currentNote.getNotation().getFret();
					slide.setLength(this.currentDistance - slide.getLength());
					if (slide.endVal > slide.startVal) {
						slide.setHeight(0 - size);
					}
					else {
						slide.setHeight(size);
						slide.setTranslateY(slide.getTranslateY() - size);
					}
					this.getChildren().add(slide);
				}
			}
			
			double type = currentNote.getType() == 0 ? 0.5 : currentNote.getType();
			currentDistance += boxedUnit.minWidth(0) + wholeNoteSpacing/type;
			this.spacing += wholeNoteSpacing/type;
			
			
		}
//			// Get the XML parsed note data
//			Note currentNote = notes.get(i);
//			
//			// Get the values of the note
//			double type = notes.get(i).getType() != 0 ? currentNote.getType() : 0.5;  // the duration of the note (inverse: small number = long duration)
//			boolean isChord = i + 1 < notes.size() && notes.get(i+1).getChord();      // is this note a chord			
//			
//			// set the height of the note to be small if it is a "Grace note"
//			double noteSize = currentNote.getGrace() ? size * 0.65 : size;			  
//			
//			// Create the BoxedText for the guitar number by giving the correct values
//			BoxedText fret = new BoxedText("" + currentNote.getNotation().getFret(), noteSize, type, isChord, currentNote.getGrace());
//			// Set the correct position of the BoxedText, and the correct String based on the space between staffLines
//			fret.setTranslateX(currentDistance);
//			fret.setTranslateY(size * (currentNote.getNotation().getString() - 1.5) + (size - noteSize)); 
//			labels.add(fret);
//
//			// For the all notes create the noteStem which is displayed under the staff
//			//    Exceptions: 'Grace Notes' and the 2nd,3rd,4th,.. notes in a chord
//			if (!currentNote.getChord() && !currentNote.getGrace()) {
//				
//				// Create the stem Object, set its position
//				TabNoteStem stem = new TabNoteStem(size, notes.get(i).getType(), notes.get(i).getDot());
//				stem.setTranslateX(currentDistance + (fret.minWidth(0)/2));
//				stem.setTranslateY(size * (m.getStaffLines()));
//				// Store the noteStem
//				this.stems.add(stem);
//			}
//			
//			
//			// NOTE: the variable currentDistance tracks the X position of the next note.
//			
//			// If the next note is a chord, then make sure this note is on top of the next note, NOT BESIDE IT
//			if (isChord) {
//				currentDistance += 0;
//			} else {
//				currentDistance += fret.minWidth(0) + wholeDistance/type;
//				this.spacing += wholeDistance/type;
//			}
		// Get the whole width of the measure (added a little bit of padding)
		
		// Create all the staff lines representing the strings.
//		this.barLines = new ArrayList<Line>();
//		for (int i = 0; i < m.getStaffLines(); i++) {
//			Line barLine = new Line();
//			barLine.setStartX(0);
//			barLine.setStartY(size * i);
//			barLine.setEndX(this.minWidth);
//			barLine.setEndY(size * i);
//			barLine.setStroke(Paint.valueOf("0x777"));
//			barLine.setStrokeWidth(0.6);
//			
//			this.barLines.add(barLine);
//			this.getChildren().add(barLine);
//		}
//		
//		// Create the line at the end of the measure (to show that the measure has ended
//		Line end = new Line();
//		end.setStartX(this.minWidth);
//		end.setStartY(0);
//		end.setEndX(this.minWidth);
//		end.setEndY((m.getStaffLines() - 1) * size);
//		end.setStrokeWidth(2);
//		this.barLines.add(end);
//		this.getChildren().add(end);
		
		if (m.getIsRepeatStop()) {
			this.generateEndRepeat(size, m.getStaffLines(), m.getBarLineRight().getRepeatNum());
			this.minWidth = this.currentDistance;
		}
		else {
			try {
				this.minWidth = currentDistance + this.notes.get(0).minWidth(0)/2;
			}catch(Exception e) {
				System.out.println("Invalid input");
			}
		}
		
		this.generateBarLines(size, m.getStaffLines());
		
		// Attach all the BoxedText numbers, and the noteStems to the TabMeasure to display
		for (NoteUnit label: this.notes) {
			this.getChildren().add(label);
		}
		
		for (TabNoteStem stem: this.stems) {
			this.getChildren().add(stem);
		}
		
		//this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		
		// Sets the correct height of the measure which takes into account the stems under the measure
		this.setMinHeight(this.stems.get(0).getTranslateY() + this.stems.get(0).minHeight(0));
	}
	
	
	/** Redos the spacing between notes, so that the measures will take up the entire width of the page
	 * (Similar to the 'justified' alignment in Word
	 * 
	 * @param scale		By how much we want to multiply the default spacing.
	 */
	public void setSpacing(double scale) {
		if (this.notes.size() == 0) {
			this.minWidth = this.minWidth * scale;
			// extend the X positions of the staff barLines to match the new width of the measure
			for (int i = 0; i < this.barLines.size()-1; i++) {
				this.barLines.get(i).setEndX(this.minWidth);
			}
			
			// For the last measure ending line, we set both the start and end X position to the new width of the measure
			Line end = this.barLines.get(this.barLines.size()-1);
			end.setStartX(this.minWidth);
			end.setEndX(this.minWidth);
			return;
		}
			
		// Gets the X position for the first Note
		double current = this.notes.get(0).getTranslateX();
		
		// Redoes the spacing for this measure
		this.spacing = 0;
		
		// Keeps track of which stem we are on.
		int stemNum = 0;
		
		// Keep track of the links
		int linkNum = 0;
		
		// Set the correct spacing for each BoxedText number in "this.labels"
		for (int i = 0; i < this.notes.size(); i++) {
			// Get the ith textbox, and set the correct X position to put it
			NoteUnit currLabel = this.notes.get(i);
			currLabel.setTranslateX(current);
			
			// If the note is also not a "grace note", then we can also add the TabNoteStem under the staff
			if (!currLabel.getGrace()) {
				this.stems.get(stemNum).setTranslateX(current + (currLabel.minWidth(0)/2));
				stemNum ++;
			}
			
			if (currLabel.getData().getNotation().getSlides().size() != 0) {
				List<Slide> slides = currLabel.getData().getNotation().getSlides();
				boolean init = false; 
				boolean end = false;
				for (Slide slide: slides) {
					if (slide.getType().equals("start")) 
						init = true;
					if (slide.getType().equals("stop"))
						end = true;
				}
				if (end) {
					NoteLinker link = this.links.get(linkNum-1); 
					int j = 1;
					while(link.getClass() != SlideLink.class) {
						link = this.links.get(linkNum-(++j));
					}
					SlideLink slide = (SlideLink) link;
					slide.setLength(current - slide.getTranslateX());
					System.out.println("end: " + slide.endVal);
				}
				if (init) {
					this.links.get(linkNum).setTranslateX(current + currLabel.getWidth());
					linkNum++;
				}
			}
				
			current += currLabel.minWidth(0) + (this.wholeNoteSpacing/currLabel.getSpacingType());
			this.spacing += this.wholeNoteSpacing/currLabel.getSpacingType();
		}
		
		if (this.endRepeat != null) {
			this.endRepeat.get(0).setTranslateX(current);
			this.endRepeat.get(1).setTranslateX(current);
			
			current += this.endRepeat.get(1).minWidth(0) - ((RepeatBarLine) this.endRepeat.get(1)).getFirstLineWidth()/2;
		}
		else {
			// add some extra padding for the end of the measure
			current += this.notes.get(0).minWidth(0)/2;
		}
		
		// extend the X positions of the staff barLines to match the new width of the measure
		for (int i = 0; i < this.barLines.size()-1; i++) {
			this.barLines.get(i).setEndX(current);
		}
		
		// For the last measure ending line, we set both the start and end X position to the new width of the measure
		Line end = this.barLines.get(this.barLines.size()-1);
		end.setStartX(current);
		end.setEndX(current);
		if (this.endRepeat != null) {
			this.getChildren().remove(end);
		}
	}

}
