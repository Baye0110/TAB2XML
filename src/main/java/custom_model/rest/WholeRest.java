package custom_model.rest;

import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class WholeRest extends Group{
	// Restore this class
	
	double width;
	double height;
	
	public WholeRest(double height) {
		this.width = height * 1.4;
		
		Rectangle mainBody = new Rectangle(height * 0.25, 0, height  * 0.9, height * 0.65);
		this.getChildren().add(mainBody);
		this.height = height * 0.65;
		
		Line top = new Line(0, 0, this.width, 0);
		top.setStrokeWidth(3);
		this.getChildren().add(top);
	}
}
