package custom_model.rest;

import java.io.FileInputStream;
import java.io.IOException;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class QuarterRest extends Group{
	// Create a Rest for duration of quarter note
	
	double height;
	double width;
	
	public QuarterRest(double height) throws IOException{
		// Load the image of the Quarter Rest and display it.
		Image rest = new Image(new FileInputStream("src/main/resources/image_assets/quarter_Rest.png"));
		ImageView restView = new ImageView(rest);
		
		// Set the dimensions of the image
		restView.setFitHeight(3 * height);
		restView.setFitWidth(height);
		this.getChildren().add(restView);
		
		// Store the values of the dimensions 
		this.height = 3 * height;
		this.width = height;
	}
}
