package custom_model;

import javafx.scene.Group;

// DRUMS ONLY
public abstract class DisplayUnit extends NoteUnit{
	// ABSTRACT CLASS for the DisplayNote, and DisplayChord
	
	// All the instance variables which are common to both DisplayNote and DisplayChord
	double top;
	double bottom;
	double height;
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
