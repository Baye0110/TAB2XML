package custom_model;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;

/**
 * Example Class: How to Build GUI Element Classes! This Class makes a Smily Face
 * 
 * 1. extends the Group
 * 2. To add GUI elements that you create as a part of this class, use: this.getChildren().add( name of GUI element );
 * 
 *
 */
public class ExampleClass extends Group{
	int data;

	public ExampleClass(Color smileColor, Color eyeColor) {
		/**
		 * The 2 arguments for this constructor are used to set the color of the smile and the eyes
		 * 
		 * First 2 Ellipses: Create the 1st eye
		 * Last 2 Ellipses: Create the 2nd eye
		 * Line: The mouth
		 */
		
		Ellipse eyeOuter1 = new Ellipse();  // The first eye of the smiley face
		eyeOuter1.setRadiusX(50);
		eyeOuter1.setRadiusY(200);
		eyeOuter1.setCenterX(50);  
		// OR WE CAN USE:   eye1.setCenterX(eye1.minWidth(0) / 2);
		eyeOuter1.setCenterY(200);
		// OR WE CAN USE:   eye1.setCenterY(eye1.minHeight(0) / 2);
		eyeOuter1.setFill(Color.WHITE);  // The inside color
		eyeOuter1.setStroke(Color.BLACK); // The border color
		this.getChildren().add(eyeOuter1);
		
		Ellipse eyeInner1 = new Ellipse();  // The first eye of the smiley face
		eyeInner1.setRadiusX(30);
		eyeInner1.setRadiusY(100);
		eyeInner1.setCenterX(50);  
		eyeInner1.setCenterY(300);
		eyeInner1.setFill(eyeColor);
		this.getChildren().add(eyeInner1);
		
		Ellipse eyeOuter2 = new Ellipse();  // The first eye of the smiley face
		eyeOuter2.setRadiusX(50);
		eyeOuter2.setRadiusY(200);
		eyeOuter2.setCenterX(200);  
		// OR WE CAN USE:   eye1.setCenterX(eye1.minWidth(0) / 2);
		eyeOuter2.setCenterY(200);
		// OR WE CAN USE:   eye1.setCenterY(eye1.minHeight(0) / 2);
		eyeOuter2.setFill(Color.WHITE);  // The inside color
		eyeOuter2.setStroke(Color.BLACK); // The border color
		this.getChildren().add(eyeOuter2);
		
		Ellipse eyeInner2 = new Ellipse();  // The first eye of the smiley face
		eyeInner2.setRadiusX(30);
		eyeInner2.setRadiusY(100);
		eyeInner2.setCenterX(200);  
		eyeInner2.setCenterY(300);
		eyeInner2.setFill(eyeColor);
		this.getChildren().add(eyeInner2);
		
		
		Line smile = new Line();
		smile.setStartX(75);  smile.setStartY(500);  // (75, 500)
		smile.setEndX(175);   smile.setEndY(500);    // (175, 500)
		smile.setStroke(smileColor);
		smile.setStrokeWidth(15);
		this.getChildren().add(smile);		
		
	}
	
	
}
