package custom_model;


import javafx.scene.*;
import javafx.scene.shape.Polygon;

public class SlantLine extends Group {
	
	/*
	 *  for Rotation parameter, input either "true" or "false". Input "false" as default option, 
	 *  "true" will flip to the opposite side
	 */
	
	/*
	 * the rotateDeg parameter allow adjustment on how steep a user want the slant to look. Common inputs 
	 * usually range from 20.0 - 40.0
	 */
	public SlantLine(double slantLength, double slantWidth, double rotateDeg ,boolean rotation) {
		
		Polygon slant = new Polygon();
		slant.getPoints().addAll(new Double[] {
				0.0, 0.0,
				0.0, slantWidth,
				slantLength, rotateDeg + slantWidth,
				slantLength, rotateDeg,
		});
		
		if(rotation == true) {
			slant.setScaleX(-1);
		}
		this.getChildren().add(slant);
		
	}
	
	

}
