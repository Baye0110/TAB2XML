package custom_model.note;

import java.util.ArrayList;
import java.util.List;

import custom_component_data.Note;
import custom_model.ArcLine;
import custom_model.MusicMeasure;
import custom_model.SheetScore;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

// For GUTIAR, BASS 
public class BoxedText extends BoxedUnit{	
	// The space on the sides of the textbox
	static final int PADDING_LEFT = 8; 
	Text label;
	Rectangle container;
	public static String customizefont = "Calibri";
	boolean isChord;
	
	public BoxedText(String text, double size, double type, boolean grace, boolean chord, int measure, Note data) {
		// Create the actual textbox with the given number in "String text" argument
		this.label = new Text(text);
		this.data = data;
		label.setFont(Font.font(customizefont,size*1.25));	// Set the font size 
		
		// Set the position of text based on the padding on the top and on the sides of the box
		label.setX((size + PADDING_LEFT - label.minWidth(0))/2.00);
		label.setY((size*0.95 - label.minHeight(0)/2)/2 + label.minHeight(0)/2);
		
		// Create a Rectangle which is a white background of the textbox
		this.container = new Rectangle();
		container.setWidth(size + PADDING_LEFT);
		container.setHeight(size*0.93);
		container.setFill(Color.WHITE);	
		
		// Add both the text and the background container to the object.
		this.getChildren().add(container);
		this.getChildren().add(label);	
		
		// Set the fields based on the arguments
		this.setWidth(container.getWidth());
		this.setSpacingType(type);
		this.setGrace(grace);
		this.setMeasure(measure);

		// If this is not part of a chord, (it is an individual note) then track its number and
		// attach an function which is activated when the note is clicked.
		if (!chord) {
			BoxedUnit.noteCounter ++;
			this.noteNum = BoxedUnit.noteCounter;
			
			// creates the function which will highlight this note when clicked, and if this note 
			// is double-clicked, it will toggle the highlight. Also keeps track of the latest highlighted
			// note in the "NoteUnit.pressed" variable.
			this.setOnMouseClicked(e -> {
				if (this == NoteUnit.pressed) {
					NoteUnit.pressed = null;
					this.toggleHighlight();
				}
				else {
					this.toggleHighlight();
					NoteUnit.pressed = this;
				}
			});
		} else {
			this.setMeasure(-1);
			this.setNoteNum(-1);
		}
	}
	
	// Highlight the note on and off, depending on if it was already highlighted before, or has yet to be highlighted
	// "this.highlighted" is a boolean value storing whether or not this note has already been highlighted.
	public void toggleHighlight() {
		if (!this.isHighlighted) {
			this.label.setStroke(Color.DEEPSKYBLUE);
			this.label.setStrokeWidth(1.0);
			this.container.setStroke(Color.DEEPSKYBLUE);
			this.container.setStrokeWidth(1.0);
			this.isHighlighted = true;
			if (!this.getIsChord() && pressed != null) {
				pressed.toggleHighlight();
			}	
			if (this.getNoteNum() > -1) {
				System.out.println("Selected Note: \t Measure - " + this.getMeasure() + ",  Note - " + this.getNoteNum());
			}
		}
		else {
			this.label.setStroke(null);
			this.label.setStrokeWidth(1.0);
			this.container.setStroke(null);
			this.isHighlighted = false;
		}
		
	}
	
	public List<ArcLine> addTied(MusicMeasure m, boolean withinMeasure) {
		List<ArcLine> arcs = new ArrayList<>();
		ArcLine arc = new ArcLine(SheetScore.lineSize * 0.9, 5, true);
		arc.setTranslateY(this.getTranslateY() - SheetScore.lineSize);
		arc.setStartNote(this);
		if (withinMeasure)
			m.getTieds().add(arc);
		m.getChildren().add(arc);
		arcs.add(arc);
		return arcs;
	}

	@Override
	public void setTiedEnd(MusicMeasure m) {
		m.getTieds().get(m.getTieds().size()-1).setEndNote(this);
	}
	
	@Override
	public void setInterTiedEnd(List<ArcLine> arcs) {
		arcs.get(arcs.size()-1).setEndNote(this);		
	}
}