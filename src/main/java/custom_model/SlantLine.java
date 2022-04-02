package custom_model;


import javafx.scene.*;
import javafx.scene.shape.Polygon;

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
