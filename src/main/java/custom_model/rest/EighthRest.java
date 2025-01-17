package custom_model.rest;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;

public class EighthRest extends Group{
	// Creates a rest with the duration of an eighth note
	double height;
	double width;
	
	public EighthRest(double height) {
		// Create the outer Arc (Arc = Slice of an Ellipse)
		Arc arc1 = new Arc();
		arc1.setCenterX(height * 0.39);
		arc1.setCenterY(0 - height * 0.25);
		arc1.setRadiusX(height * 0.54);
		arc1.setRadiusY(height * 0.75);
		arc1.setStartAngle(230);
		arc1.setLength(105);
		arc1.setType(ArcType.OPEN);
		this.getChildren().add(arc1);
		
		// Create the inner Arc (this is white, so it covers up the outer arc which makes it look like a curved line)
		Arc arc2 = new Arc();
		arc2.setCenterX(height * 0.39);
		arc2.setCenterY(0 - height * 0.25);
		arc2.setRadiusX(height * 0.45);
		arc2.setRadiusY(height * 0.65);
		arc2.setStartAngle(230);
		arc2.setLength(105);
		arc2.setType(ArcType.OPEN);
		arc2.setFill(Color.WHITE);
		this.getChildren().add(arc2);
		
		// The circle in the eighth rest
		Ellipse circle = new Ellipse(height * 0.25, height * 0.25, height * 0.25, height * 0.25);
		this.getChildren().add(circle);
		
		// The line in the eighth rest
		Line l1 = new Line(this.minWidth(0)/1.05, 0, this.minWidth(0)/2, height*2);
		l1.setStrokeWidth(height/10);
		this.getChildren().add(l1);
		
		// Store the dimensions of this rest
		this.width = this.minWidth(0);
		this.height = height;
	}

}
