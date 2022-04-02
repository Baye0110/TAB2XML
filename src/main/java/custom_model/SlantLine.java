package custom_model;


import javafx.scene.*;
import javafx.scene.shape.Polygon;

public class SlantLine extends Group {
	
	// for Rotation parameter, input either "left" or "right" depends on the side of the slant
	public SlantLine(double slantLength, double slantWidth, String rotation) {
		
		Polygon slant = new Polygon();
		slant.getPoints().addAll(new Double[] {
				100.0, 100.0,
				100.0, 100.0 + slantWidth,
				100.0 + slantLength, 70.0 + slantWidth,
				100.0 + slantLength, 70.0,
		});
		
		if(rotation.equals("right")) {
			slant.setScaleX(-1);
		}
		this.getChildren().add(slant);
		
	}
	

}
