package custom_model;

import javafx.scene.*;
import javafx.scene.shape.Polygon;

public class SlantLine extends Group {
	
	public SlantLine(double lineWidth, double slantLength) {
		Polygon polygon = new Polygon();
		polygon.getPoints().addAll(new Double[]{
			    0.0, 0.0,
			    20.0, 10.0,
			    10.0, 20.0 });
	}
	

}
