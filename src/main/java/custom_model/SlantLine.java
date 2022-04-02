package custom_model;


import javafx.scene.*;
import javafx.scene.shape.Polygon;

public class SlantLine extends Group {
	
	/*
	 *  for Rotation parameter, input either "true" or "false". Input "false" as default option, 
	 *  "true" will flip to the opposite side
	 */
	public SlantLine(double slantLength, double slantWidth, boolean rotation) {
		
		Polygon slant = new Polygon();
		slant.getPoints().addAll(new Double[] {
				0.0, 0.0,
				0.0, slantWidth,
				slantLength, 20.0 + slantWidth,
				slantLength, 20.0,
		});
		
		if(rotation == true) {
			slant.setScaleX(-1);
		}
		this.getChildren().add(slant);
		
	}
	

}
