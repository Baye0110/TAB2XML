package custom_model.notehead;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class WholeNoteHead extends Group{
	// Create a notehead for the duration of a whole note
	double width;
	
	public WholeNoteHead(double height) {
		// This Notehead is similar to the HalfNoteHead with a slightly different shape
		
		// This Ellipse serves as the black oval background
		Ellipse background = new Ellipse();
		background.setRadiusX(height * 0.7);
		background.setRadiusY(height/2);
		background.setCenterX(height * 0.7);
		background.setCenterY(height/2);
		this.getChildren().add(background);
		
		// This Ellipse serves as the white hold inside the black oval
		Ellipse hole = new Ellipse();
		hole.setRadiusX(height * 0.52);
		hole.setRadiusY(height * 0.27);
		hole.setRotate(60);
		hole.setCenterX(background.getCenterX());
		hole.setCenterY(height/2);
		hole.setFill(Color.WHITE);
		this.getChildren().add(hole);
		
		// This sets the width dimension
		this.width = background.minWidth(0);
		
		// Note: There is no stem position because the Whole Note has no stems
	}
}
