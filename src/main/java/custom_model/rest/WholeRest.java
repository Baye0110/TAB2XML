package custom_model.rest;

import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class WholeRest extends Group{
	// Creates a Rest with duration of a whole note
	
	double width;
	double height;
	
	public WholeRest(double height) {
		// set the width of rectangle
		this.width = height * 1.4;
		
		// create the rectangle part of the rest
		Rectangle mainBody = new Rectangle(height * 0.25, 0, height  * 0.9, height * 0.65);
		this.getChildren().add(mainBody);
		this.height = height * 0.65;
		
		// create the line which is on top of the rectangle
		Line top = new Line(0, 0, this.width, 0);
		top.setStrokeWidth(3);
		this.getChildren().add(top);
	}
}
