package custom_model.notehead;

import javafx.scene.shape.Ellipse;

public class FullNoteHead extends Ellipse{
	// Create a notehead, for notes of duration (quarter, 8th, 16th, 32nd, 64th, 128th)
	// This creates the solid, black oval notehead
	// Extends 'Ellipse' which is the oval shape
	double width;
	double stemPosition;
	
	public FullNoteHead(double height) {
		// Set the size of the shape, and rotate it 
		this.setRadiusX(height * 0.65);
		this.setRadiusY(height * 0.45);
		this.setRotate(155);
		this.width = this.minWidth(0);
		
		// Set the Position of the Notehead
		this.setCenterX(this.width/2);
		this.setCenterY(height * 0.5);
		
		// Set the position of the stem to start halfway down the right of the notehead.
		this.stemPosition = height * 0.5;		
	}
}
