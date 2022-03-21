package custom_model;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

// For GUTIAR, BASS 
public class BoxedText extends BoxedUnit{	
	// The space on the sides of the textbox
	static final int PADDING_LEFT = 8; 
	Text label;
	Rectangle container;
	
	BoxedText(String text, double size, double type, boolean grace, boolean chord, int measure) {
		// Create the actual textbox with the given number in "String text" argument
		this.label = new Text(text);
		label.setFont(Font.font(size*0.88));	// Set the font size 
		
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
		this.width = container.getWidth();
		this.spacingType = type;
		this.grace = grace;
		this.measure = measure;

		if (!chord) {
			BoxedUnit.currMeasureNoteNum ++;
			this.noteNum = BoxedUnit.currMeasureNoteNum;
			
			this.setOnMouseClicked(e -> {
				if (this == NoteUnit.pressed) {
					NoteUnit.pressed = null;
					this.toggleHighlight();
				}
				else {
					this.toggleHighlight();
					NoteUnit.pressed = this;
					System.out.println("Selected Note: \t Measure - " + this.measure + ",  Note - " + this.noteNum);
				}
			});
		}
	}
	
	public void toggleHighlight() {
		if (!this.highlighted) {
			this.label.setStroke(Color.DEEPSKYBLUE);
			this.label.setStrokeWidth(1.0);
			this.container.setStroke(Color.DEEPSKYBLUE);
			this.container.setStrokeWidth(1.0);
			this.highlighted = true;
			if (pressed != null) {
				pressed.toggleHighlight();
			}
		}
		else {
			this.label.setStroke(null);
			this.label.setStrokeWidth(1.0);
			this.container.setStroke(null);
			this.highlighted = false;
		}
		
		
		
	}
}