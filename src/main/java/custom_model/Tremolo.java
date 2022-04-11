package custom_model;

import javafx.scene.*;
import javafx.scene.shape.Polygon;

public class Tremolo extends Group{
	
	private double length;
	private double width;
	private double rotateDeg;
	private int numlines;
	
	
	public double getLength() {
		return length;
	}

	public double getWidth() {
		return width;
	}

	public double getRotateDeg() {
		return rotateDeg;
	}

	public int getnumLines() {
		return numlines;
	}

	public Tremolo(double slantLength, double slantWidth, double rotateDeg, boolean rotation, int numLines, double spacing) {
		this.length = slantLength;
		this.width = slantWidth;
		this.rotateDeg = rotateDeg;
		this.numlines = numLines;
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
		this.getChildren().add(slant1);
		
		// 12.0 is the fixed distance between each slant, could be changed later
		double shift = slantWidth + spacing;
		for(int i = 0; i < numLines-1; i++) {
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
			shift += slantWidth + spacing;
		}
	}
	
	
	public double calculatePosition(double totalHeight) {
		double startDist = 0.0;
		double TremoloHeight = (this.getWidth() * this.getnumLines()) + (12.0 * (this.getnumLines()-1)) + this.getRotateDeg();
		startDist = (totalHeight - TremoloHeight) / 2;
		return startDist;
	}
}
