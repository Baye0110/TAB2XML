package custom_model.note;

import custom_component_data.Note;
import javafx.scene.Group;
import javafx.scene.Node;

abstract public class NoteUnit extends Group{
	protected double width;
	protected double spacingType;
	protected boolean isHighlighted; // IMPORTANT
	protected boolean grace;
	protected int noteNum; // IMPORTANT
	protected int measure; // IMPORTANT
	protected Note data;
	protected boolean isTied;
	
	public static NoteUnit pressed = null; // IMPORTANT
	
	abstract public void toggleHighlight();
	
	public double getWidth() {
		return this.width;
	}
	
	public void setWidth(double width) {
		this.width = width;
	}
	
	public int getNoteNum() {
		return this.noteNum;
	}
	
	public void setNoteNum(int num) {
		this.noteNum = num;
	}
	
	public int getMeasure() {
		return this.measure;
	}
	
	public void setMeasure(int measure) {
		this.measure = measure;
	}
	
	public boolean getGrace() {
		return this.grace;
	}
	
	public void setGrace(boolean grace) {
		this.grace = grace;
	}
	
	public double getSpacingType() {
		return this.spacingType;
	}
	
	public void setSpacingType(double spacingType) {
		this.spacingType = spacingType;
	}
	
	public Note getData() {
		return this.data;
	}
	
}
