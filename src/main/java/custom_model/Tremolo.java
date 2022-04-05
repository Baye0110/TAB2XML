package custom_model;

import javafx.scene.*;
import javafx.scene.shape.Polygon;

public class Tremolo extends Group{
	public Tremolo(double slantLength, double slantWidth, double rotateDeg, boolean rotation, int numLines) {
		Polygon slant1 = new Polygon();
		slant1.getPoints().addAll(new Double[] {
				0.0, 0.0,
				0.0, slantWidth,
				slantLength, rotateDeg + slantWidth,
				slantLength, rotateDeg,
		});
		
		Polygon slant2 = new Polygon();
		slant2.getPoints().addAll(new Double[] {
				0.0, slantWidth + 15.0,
				0.0, 2*slantWidth + 15.0,
				slantLength, rotateDeg + 2*slantWidth + 15.0,
				slantLength, rotateDeg + slantWidth + 15.0,
		});
		
		Polygon slant3 = new Polygon();
		slant3.getPoints().addAll(new Double[] {
				0.0, 2*slantWidth + 30.0,
				0.0, 3*slantWidth + 30.0,
				slantLength, rotateDeg + 3*slantWidth + 30.0,
				slantLength, rotateDeg + 2*slantWidth + 30.0,
		});
		
		/*Polygon slant1 = new Polygon();
		slant1.getPoints().addAll(new Double[] {
				0.0, 0.0,
				0.0, slantWidth,
				slantLength, rotateDeg + slantWidth,
				slantLength, rotateDeg,
		});
		
		Polygon slant2 = new Polygon();
		slant2.getPoints().addAll(slant1.getPoints() + (slantWidth + 15.0));
		
		if(rotation == true) {
			slant1.setScaleX(-1);
			slant2.setScaleX(-1);
			slant3.setScaleX(-1);
		}*/
		this.getChildren().add(slant1);
		this.getChildren().add(slant2);
		this.getChildren().add(slant3);
		
	}

}
