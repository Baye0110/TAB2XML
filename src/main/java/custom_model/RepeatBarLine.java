package custom_model;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class RepeatBarLine extends Group {
	
	//constructor of gui element
	RepeatBarLine(int lineSpacing, int numOfLines) {
		// New Addition: Adjust this value to what it was before the deletion of the for loop for unnecessary horizontal lines
		int n = 50 + (numOfLines * lineSpacing);
		
		//drawing vertical lines
		Line v1 = new Line();
		v1.setStartX(75);
		v1.setStartY(50);
		v1.setEndX(75);
		v1.setEndY(n- lineSpacing);
		v1.setStrokeWidth(2);
		
		this.getChildren().add(v1);  // CHANGE: This line is added to the Class directly now
		
		//the second line has a higher Stroke Width
		Line v2 = new Line();
		v2.setStartX(60);
		v2.setStartY(50);
		v2.setEndX(60);
		v2.setEndY(n - lineSpacing);
		v2.setStrokeWidth(5);
		
		this.getChildren().add(v2); // CHANGE: This line is added to the Class directly now
		
		//drawing the first dot (upper dot)
		 Circle circle = new Circle();
		 circle.setCenterX(85);
		 circle.setCenterY((50 + (50 + lineSpacing))/2);
		 circle.setRadius(5);
		 circle.setFill(Color.BLACK);

		 this.getChildren().add(circle); // CHANGE: This line is added to the Class directly now
		 
		 //drawing the second dot
		 Circle circle2 = new Circle();
		 circle2.setCenterX(85);
		 circle2.setCenterY(2*lineSpacing + (50 + (50 + lineSpacing))/2);
		 circle2.setRadius(5);
		 circle2.setFill(Color.BLACK);
		 
		 this.getChildren().add(circle2); // CHANGE: This line is added to the Class directly now
	}
	
			
}
