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
		
		int n = 0 + numOfLines * lineSpacing;
		
		//drawing vertical lines
		Line v1 = new Line();
		v1.setStartX(75);
		v1.setStartY(50);
		v1.setEndX(75);
		v1.setEndY(n- lineSpacing);
		v1.setStrokeWidth(2);
		
		this.getChildren().add(v1);  
		
		//the second line has a higher Stroke Width
		Line v2 = new Line();
		v2.setStartX(0);
		v2.setStartY(0);	
		v2.setEndX(0);	
		v2.setEndY(n - lineSpacing);
		v2.setStrokeWidth(1/3* lineSpacing);
		
		this.getChildren().add(v2); 
		
		//drawing the first dot (upper dot)
		 Circle circle = new Circle();
		 circle.setCenterX(85);
		 circle.setCenterY(lineSpacing/2);	
		 circle.setRadius(1/6*lineSpacing);
		 circle.setFill(Color.BLACK);

		 this.getChildren().add(circle); 
		 
		 //drawing the second dot
		 Circle circle2 = new Circle();
		 circle2.setCenterX(85);
		 circle2.setCenterY(2*lineSpacing + (50 + (50 + lineSpacing))/2);
		 circle2.setRadius(1/6*lineSpacing);
		 circle2.setFill(Color.BLACK);
		 
		 this.getChildren().add(circle2);
	}
	
			
}
