package custom_model.notehead;

import javafx.scene.shape.Ellipse;

public class FullNoteHead extends Ellipse{
	// Restore this class
	
	double width;
	double stemPosition;
	
	public FullNoteHead(double height) {
		this.setRadiusX(height * 0.65);
		this.setRadiusY(height * 0.45);
		this.setRotate(155);
		this.width = this.minWidth(0);
		
		this.setCenterX(this.width/2);
		this.setCenterY(height * 0.5);
		
		this.stemPosition = height * 0.5;		
	}
}
