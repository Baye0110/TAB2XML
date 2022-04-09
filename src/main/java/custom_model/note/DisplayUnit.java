package custom_model.note;

import custom_model.SheetScore;
import javafx.scene.Group;
import javafx.scene.shape.Line;

// DRUMS ONLY
public abstract class DisplayUnit extends NoteUnit{
	// ABSTRACT CLASS for the DisplayNote, and DisplayChord
	
	// All the instance variables which are common to both DisplayNote and DisplayChord
	private double top;
	private double bottom;
	private double height;
	private int position;
	boolean grace;
	private boolean rest;
	public static int currMeasureNoteNum = 0;
	Group box;
	
	Line stem;
	
	
	public int getPosition() {
		return this.position;
	}
	
	public void setPosition(int position) {
		this.position = position;
	}
	
	public double getTop() {
		return this.top;
	}
	
	public void setTop(double top) {
		this.top = top;
	}
	
	public double getBottom() {
		return this.bottom;
	}
	
	public void setBottom(double bottom) {
		this.bottom = bottom;
	}
	
	public double getHeight() {
		return this.height;
	}
	
	public void setHeight(double height) {
		this.height = height;
	}
	

	public Group getBox() {
		return this.box;
	}
	
	public Line getStem() {
		return this.stem;
	}

	public abstract void extendStaff(int positions, double height);
	
	public abstract void addTails(double height, boolean stemDown);
	
	public boolean getGrace() {
		return this.grace;
	}

	public abstract void generateBox();
	
	public abstract void extendStemForBeam();
	
	public void setRest(boolean rest) {
		this.rest = rest;
	}
	
	public boolean getRest() {
		return this.rest;
	}
}

