package custom_model.notehead;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;

public class CrossNoteHead extends Group{
	double width;
	double stemPosition;
	
	public CrossNoteHead(double height, Color color, double strokeScale) {
		// Set the dimension of the note
		this.width = height * 1.2;
		
		// Where to attach the stem (add the top of the note)
		this.stemPosition = 0;
				
		// Create the 2 diagonal lines, that make the 'X' shape
		// Set the color of the line, and make the Line shape 'round' on the edges
		Line l1 = new Line(0, height/1.05, this.width, 0);
		l1.setStrokeWidth(height/9 * strokeScale);
		l1.setStroke(color);
		l1.setStrokeLineCap(StrokeLineCap.ROUND);
		Line l2 = new Line(0, 0, this.width, height/1.05);
		l2.setStrokeWidth(height/9 * strokeScale);
		l2.setStroke(color);
		l2.setStrokeLineCap(StrokeLineCap.ROUND);
		
		// Make it easier to click a cross notehead
		Rectangle clickableBackground = new Rectangle();
		clickableBackground.setX(0); clickableBackground.setY(0);
		clickableBackground.setWidth(this.width);
		clickableBackground.setHeight(height / 1.05);
		clickableBackground.setFill(Color.TRANSPARENT);
		this.getChildren().add(clickableBackground);
		
		// Add the lines on top of the background
		this.getChildren().add(l1);
		this.getChildren().add(l2);
		
		// Set the dimension of the width
		this.width = this.minWidth(0);
	}

}
