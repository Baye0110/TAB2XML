package custom_model.notehead;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class HalfNoteHead extends Group{
	// Restore this class
	
	double width;
	double stemPosition;
	
	public HalfNoteHead(double height) {
		FullNoteHead background = new FullNoteHead(height);
		this.getChildren().add(background);
		
		Ellipse hole = new Ellipse();
		hole.setCenterX(background.getCenterX());
		hole.setCenterY(height/2);
		hole.setRotate(155);
		hole.setRadiusX(height * 0.6);
		hole.setRadiusY(height * 0.25);
		hole.setFill(Color.WHITE);
		this.getChildren().add(hole);
		
		this.width = this.minWidth(0);
		this.stemPosition = height * 0.5;
	}
}
