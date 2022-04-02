package custom_model;

import java.awt.Color;

import javafx.scene.*;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class SlantLine extends Group {
	
	// spacing between 2 connected notes = slant length
	// height difference between 2 connected notes = x-coordinate rotation degree
	public SlantLine(double slantLength, double slantWidth) {
		
		Polygon slant = new Polygon();
		slant.getPoints().addAll(new Double[] {
				100.0, 100.0,
				100.0, 100.0 + slantWidth,
				100.0 + slantLength, 70.0 + slantWidth,
				100.0 + slantLength, 70.0,
		});
		this.getChildren().add(slant);
		
	}
	

}
