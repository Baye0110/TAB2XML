package custom_model;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

// For GUTIAR, BASS 
public class BoxedText extends Group{	
	// The space on the sides of the textbox
	static final int PADDING_LEFT = 8; 
	// The width of the textbox
	double size;
	// The duration of the note (0 = breve, 1 = whole, 2 = half, 4 = quarter, 8 = 8th, 16 = 16th, ...)
	double type;
	
	// Is this part of a chord, is this a grace note?
	boolean chord;
	boolean grace;
	
	BoxedText(String text, double size, double type, boolean chord, boolean grace) {
		// Create the actual textbox with the given number in "String text" argument
		Text label = new Text(text);
		label.setFont(Font.font(size*0.675));	// Set the font size 
		
		// Set the position of text based on the padding on the top and on the sides of the box
		label.setX((size + PADDING_LEFT - label.minWidth(0))/2.00);
		label.setY((size*0.93 - label.minHeight(0)/2)/2 + label.minHeight(0)/2);
		
		// Create a Rectangle which is a white background of the textbox
		Rectangle container = new Rectangle();
		container.setWidth(size + PADDING_LEFT);
		container.setHeight(size*0.93);
		container.setFill(Color.WHITE);	
		
		// Add both the text and the background container to the object.
		this.getChildren().add(container);
		this.getChildren().add(label);	
		
		// Set the fields based on the arguments
		this.size = container.getWidth();
		this.type = type;
		this.chord = chord;
		this.grace = grace;
	}
}