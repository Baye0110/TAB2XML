package custom_model.note;

import custom_model.Bend;
import javafx.scene.Group;

abstract public class BoxedUnit extends NoteUnit{
	
	public static int noteCounter = 0;
	private Bend bend;
	private boolean isChord;
	
	public Bend getBend() {
		return this.bend;
	}
	
	public void setBend(Bend bend) {
		this.bend = bend;
	}
	
	public void setBendPositionX() {
		this.bend.setTranslateX(this.getTranslateX() + this.getWidth());
	}
	
	public void setIsChord(boolean isChord) {
		this.isChord = isChord;
	}
	
	public boolean getIsChord() {
		return this.isChord;
	}
	
}
