package custom_model.note;

import javafx.scene.Group;

// DRUMS ONLY
public abstract class DisplayUnit extends NoteUnit{
	// ABSTRACT CLASS for the DisplayNote, and DisplayChord
	
	// All the instance variables which are common to both DisplayNote and DisplayChord
	double top;
	double bottom;
	double height;
	int position; // IMPORTANT
	
	public double getWidth() {
		return this.width;
	}
	
	public int getPosition() {
		return this.position;
	}
	
	public double getTop() {
		return this.top;
	}
	
	public double getHeight() {
		return this.height;
	}

	public abstract void extendStaff(int positions, double height);
	
	public abstract void addTails(double height, boolean stemDown);
}
