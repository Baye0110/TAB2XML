package custom_model.notehead;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class HalfNoteHead extends Group{
	// Create the NoteHead with the duration of a halfnote
	double width;
	double stemPosition;
	
	public HalfNoteHead(double height) {
		// This note is a solid Black notehead with a white hole in the middle
		
		// The FullNoteHead functions as the black oval background
		FullNoteHead background = new FullNoteHead(height);
		this.getChildren().add(background);
		
		// The Ellipse functions as the white hole in the center of the notehead
		Ellipse hole = new Ellipse();
		hole.setCenterX(background.getCenterX());
		hole.setCenterY(height/2);
		hole.setRotate(155);
		hole.setRadiusX(height * 0.6);
		hole.setRadiusY(height * 0.25);
		hole.setFill(Color.WHITE);
		this.getChildren().add(hole);
		
		// Set the width dimension
		this.width = this.minWidth(0);
		
		// Set the position of the stem to be halfway down the right side of the notehead
		this.stemPosition = height * 0.5;
	}
}
