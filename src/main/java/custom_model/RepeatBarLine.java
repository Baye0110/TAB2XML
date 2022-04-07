package custom_model;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class RepeatBarLine extends Group {
	private double firstLineWidth;
	
	//constructor of gui element
	RepeatBarLine(double lineSpacing, int numOfLines) {
		
		double n = 0 + numOfLines * lineSpacing;
		
		//drawing vertical lines
		Line v1 = new Line();
		double lineWidth = 1.0/6.0*lineSpacing;
		this.firstLineWidth = lineWidth;
		v1.setStartX(10);
		v1.setStartY(0 + lineWidth/2);
		v1.setEndX(10);
		v1.setEndY(n - lineSpacing - lineWidth/2);
		v1.setStrokeWidth(lineWidth);
		
		this.getChildren().add(v1);  
		
		//the second line has a higher Stroke Width
		Line v2 = new Line();
		lineWidth = 1.0/3.0* lineSpacing;
		v2.setStartX(0);
		v2.setStartY(0 + lineWidth/2);	
		v2.setEndX(0);	
		v2.setEndY(n - lineSpacing - lineWidth/2);
		v2.setStrokeWidth(lineWidth);
		
		this.getChildren().add(v2); 
		
		//drawing the first dot (upper dot)
		 Circle circle = new Circle();
		 circle.setCenterX(20);
		 circle.setCenterY(lineSpacing/2.0);	
		 circle.setRadius(1.0/6.0*lineSpacing);
		 circle.setFill(Color.BLACK);

		 this.getChildren().add(circle); 
		 
		 //drawing the second dot
		 Circle circle2 = new Circle();
		 circle2.setCenterX(20);
		 circle2.setCenterY((n-lineSpacing) - lineSpacing/2.0);	//(vertical line height - lineSpacing)/2
		 circle2.setRadius(1.0/6.0*lineSpacing);
		 circle2.setFill(Color.BLACK);
		 
		 this.getChildren().add(circle2);
	}

	public double getFirstLineWidth() {
		return firstLineWidth;
	}
	
			
}
