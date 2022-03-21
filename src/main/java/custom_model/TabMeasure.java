package custom_model;

import java.util.ArrayList;
import java.util.List;

import custom_component_data.Measure;
import custom_component_data.Note;
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
	List<BoxedText> labels;
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
		
		// Get the list of Notes (from XML parsing)
		List<Note> notes = m.getNotes();
		
		// Initialize the arrays
		this.labels = new ArrayList<BoxedText>();
		this.stems = new ArrayList<TabNoteStem>();
		
		// initialize the height of the staff based on the number of lines
		this.maxHeight = size * (m.getStaffLines());
		
		// Create the BoxedTest for the guitar numbers for each Note.
		for (int i = 0; i < notes.size(); i++) {
			// Get the XML parsed note data
			Note currentNote = notes.get(i);
			
			// Get the values of the note
			double type = notes.get(i).getType() != 0 ? currentNote.getType() : 0.5;  // the duration of the note (inverse: small number = long duration)
			boolean isChord = i + 1 < notes.size() && notes.get(i+1).getChord();      // is this note a chord			
			
			// set the height of the note to be small if it is a "Grace note"
			double noteSize = currentNote.getGrace() ? size * 0.65 : size;			  
			
			// Create the BoxedText for the guitar number by giving the correct values
			BoxedText fret = new BoxedText("" + currentNote.getNotation().getFret(), noteSize, type, isChord, currentNote.getGrace());
			// Set the correct position of the BoxedText, and the correct String based on the space between staffLines
			fret.setTranslateX(currentDistance);
			fret.setTranslateY(size * (currentNote.getNotation().getString() - 1.5) + (size - noteSize)); 
			labels.add(fret);
			
			// For the all notes create the noteStem which is displayed under the staff
			//    Exceptions: 'Grace Notes' and the 2nd,3rd,4th,.. notes in a chord
			if (!currentNote.getChord() && !currentNote.getGrace()) {
				
				// Create the stem Object, set its position
				TabNoteStem stem = new TabNoteStem(size, notes.get(i).getType(), notes.get(i).getDot());
				stem.setTranslateX(currentDistance + (fret.minWidth(0)/2));
				stem.setTranslateY(size * (m.getStaffLines()));
				// Store the noteStem
				this.stems.add(stem);
			}
			
			// NOTE: the variable currentDistance tracks the X position of the next note.
			
			// If the next note is a chord, then make sure this note is on top of the next note, NOT BESIDE IT
			if (isChord) {
				currentDistance += 0;
			} else {
				currentDistance += fret.minWidth(0) + wholeDistance/type;
				this.spacing += wholeDistance/type;
			}
			
		}
		// Get the whole width of the measure (added a little bit of padding)
		try {
			this.minWidth = currentDistance + labels.get(0).minWidth(0)/2;
		}catch(Exception e) {
			System.out.println("Invalid input");
		}
		
		// Create all the staff lines representing the strings.
		this.barLines = new ArrayList<Line>();
		for (int i = 0; i < m.getStaffLines(); i++) {
			Line barLine = new Line();
			barLine.setStartX(0);
			barLine.setStartY(size * i);
			barLine.setEndX(this.minWidth);
			barLine.setEndY(size * i);
			barLine.setStroke(Paint.valueOf("0x777"));
			barLine.setStrokeWidth(0.6);
			
			this.barLines.add(barLine);
			this.getChildren().add(barLine);
		}
		
		// Create the line at the end of the measure (to show that the measure has ended
		Line end = new Line();
		end.setStartX(this.minWidth);
		end.setStartY(0);
		end.setEndX(this.minWidth);
		end.setEndY((m.getStaffLines() - 1) * size);
		end.setStrokeWidth(2);
		this.barLines.add(end);
		this.getChildren().add(end);
		
		// Attach all the BoxedText numbers, and the noteStems to the TabMeasure to display
		for (BoxedText label: this.labels) {
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
		// Gets the X position for the first Note
		double current = this.labels.get(0).getTranslateX();
		
		// Redoes the spacing for this measure
		this.spacing = 0;
		
		// Keeps track of which stem we are on.
		int stemNum = 0;
		
		// Set the correct spacing for each BoxedText number in "this.labels"
		for (int i = 0; i < this.labels.size(); i++) {
			// Get the ith textbox, and set the correct X position to put it
			BoxedText currLabel = this.labels.get(i);
			currLabel.setTranslateX(current);
			
			// If the current note is not a chord, then we calculate the new X position for the next note
			if (!currLabel.chord) {
				// If the note is also not a "grace note", then we can also add the TabNoteStem under the staff
				if (!currLabel.grace) {
					this.stems.get(stemNum).setTranslateX(current + (currLabel.minWidth(0)/2));
					stemNum ++;
				}
				
				current += currLabel.minWidth(0) + (this.wholeDistance/currLabel.type);
				this.spacing += this.wholeDistance/currLabel.type;
			}
		}
		
		// add some extra padding for the end of the measure
		current += this.labels.get(0).minWidth(0)/2;
		
		// extend the X positions of the staff barLines to match the new width of the measure
		for (int i = 0; i < this.barLines.size()-1; i++) {
			this.barLines.get(i).setEndX(current);
		}
		
		// For the last measure ending line, we set both the start and end X position to the new width of the measure
		Line end = this.barLines.get(this.barLines.size()-1);
		end.setStartX(current);
		end.setEndX(current);
	}

}
