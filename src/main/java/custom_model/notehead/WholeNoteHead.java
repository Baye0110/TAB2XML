package custom_model.notehead;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class WholeNoteHead extends Group{
	double width;
	
	public WholeNoteHead(double height) {
		Ellipse background = new Ellipse();
		background.setRadiusX(height * 0.7);
		background.setRadiusY(height/2);
		background.setCenterX(height * 0.7);
		background.setCenterY(height/2);
		this.width = background.minWidth(0);
		this.getChildren().add(background);
		
		Ellipse hole = new Ellipse();
		hole.setRadiusX(height * 0.52);
		hole.setRadiusY(height * 0.27);
		hole.setRotate(60);
		hole.setCenterX(background.getCenterX());
		hole.setCenterY(height/2);
		hole.setFill(Color.WHITE);
		this.getChildren().add(hole);
		
	}
}
