package custom_model;

import javafx.scene.Group;

abstract public class BoxedUnit extends NoteUnit{
	
	public static int currMeasureNoteNum = 0;
	
	// Is this part of a chord, is this a grace note?
	boolean grace;

}
