package custom_model.rest;

import java.io.FileInputStream;
import java.io.IOException;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class QuarterRest extends Group{
	double height;
	double width;
	
	// Restore this class
	
	public QuarterRest(double height) throws IOException{
		Image rest = new Image(new FileInputStream("src/main/resources/image_assets/quarter_Rest.png"));
		ImageView restView = new ImageView(rest);
		restView.setFitHeight(3 * height);
		restView.setFitWidth(height);
		this.getChildren().add(restView);
		
		this.height = 3 * height;
		this.width = height;
	}
}
