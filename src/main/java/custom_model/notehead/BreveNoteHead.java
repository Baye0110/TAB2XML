package custom_model.notehead;

import javafx.scene.Group;
import javafx.scene.shape.Line;

public class BreveNoteHead extends Group{
	// Create the Notehead for the note with the duration of 2 whole notes.
	
	double width;
	
	public BreveNoteHead(double height) {
		// Create the 2 lines on the left of the noteheaed
		Line l1 = new Line(0, 0, 0, height);
		l1.setStrokeWidth(3);
		this.getChildren().add(l1);
		Line l2 = new Line(height/8, 0, height/8, height);
		l2.setStrokeWidth(3);
		this.getChildren().add(l2);
		
		// Create the notehead (which is the same as the WholeNoteHead)
		WholeNoteHead middle = new WholeNoteHead(height);
		middle.setTranslateX(height/8);
		this.getChildren().add(middle);
		
		// Create the 2 lines on the right of the notehead
		double widthSoFar = height/8 + middle.width;
		Line r1 = new Line(widthSoFar, 0, widthSoFar, height);
		r1.setStrokeWidth(3);
		this.getChildren().add(r1);
		Line r2 = new Line(widthSoFar + height/8, 0, widthSoFar + height/8, height);
		r2.setStrokeWidth(3);
		this.getChildren().add(r2);
		
		// Set the width dimension of this note.
		this.width = this.minWidth(0);
	}
}
