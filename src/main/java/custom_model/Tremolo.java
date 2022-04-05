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
		
		if(rotation == true) {
			slant1.setScaleX(-1);
		}
		
		// 15.0 is the fixed distance between each slant, could be changed later
		double shift = slantWidth + 15.0;
		for(int i = 0; i < numLines; i++) {
			// create a copy of the original slant
			Polygon slantCopy = new Polygon();
			slantCopy.getPoints().addAll(slant1.getPoints());
			// shift down the copy by a fixed distance
			slantCopy.setTranslateY(shift);
			// rotate if needed
			if(rotation == true) {
				slantCopy.setScaleX(-1);
			}
			// add to scene
			this.getChildren().add(slantCopy);
			/*
			 *  the original slant is always the starting point so each copy will be shifted further 
			 *  => shift distance increases per copy
			 */
			shift +=  slantWidth + 15.0;
		}
	}
}
