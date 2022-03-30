package custom_model;

import javafx.scene.Group;
import javafx.scene.Node;

abstract public class NoteUnit extends Group{
	double width;
	double spacingType;
	boolean highlighted;
	boolean grace;
	int noteNum;
	int measure;
	
	public static NoteUnit pressed = null;
	
	abstract public void toggleHighlight();
	
	public int getNoteNum() {
		return this.noteNum;
	}
	
	public int getMeasure() {
		return this.measure;
	}
	
}