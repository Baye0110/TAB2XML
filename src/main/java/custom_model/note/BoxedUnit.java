package custom_model.note;

import custom_model.Bend;
import javafx.scene.Group;

abstract public class BoxedUnit extends NoteUnit{
	
	public static int noteCounter = 0;
	private Bend bend;
	
	public Bend getBend() {
		return this.bend;
	}
	
	public void setBend(Bend bend) {
		this.bend = bend;
	}
	
	public void setBendPositionX() {
		this.bend.setTranslateX(this.getTranslateX() + this.getWidth());
	}
	
}
