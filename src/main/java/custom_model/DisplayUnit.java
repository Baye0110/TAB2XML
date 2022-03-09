package custom_model;

import javafx.scene.Group;

public abstract class DisplayUnit extends Group{
	// Restore this class
	
	double width;
	double top;
	double bottom;
	double height;
	double spacingType;
	int position;
	
	public double getWidth() {
		return this.width;
	}
	
	public int getPosition() {
		return this.position;
	}
	
	public double getSpacingType() {
		return this.spacingType;
	}

	public abstract void extendStaff(int positions, double height);
	
	public abstract void addTails(double height, boolean stemDown);
}
