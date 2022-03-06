package custom_model.notehead;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

public class CrossNoteHead extends Group{
	double width;
	double stemPosition;
	
	public CrossNoteHead(double height, Color color, double strokeScale) {
		this.width = height * 1.2;
		this.stemPosition = 0;
				
		Line l1 = new Line(0, height/1.05, this.width, 0);
		l1.setStrokeWidth(height/9 * strokeScale);
		l1.setStroke(color);
		l1.setStrokeLineCap(StrokeLineCap.ROUND);
		Line l2 = new Line(0, 0, this.width, height/1.05);
		l2.setStrokeWidth(height/9 * strokeScale);
		l2.setStroke(color);
		l2.setStrokeLineCap(StrokeLineCap.ROUND);
		this.getChildren().add(l1);
		this.getChildren().add(l2);
		
		this.width = this.minWidth(0);
	}

}
