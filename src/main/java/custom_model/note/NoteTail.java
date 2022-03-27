package custom_model.note;

import java.io.FileInputStream;
import java.io.IOException;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;

// For ALL INSTRUMENTS
public class NoteTail extends Group {

	// The class which creates the tail/flag displayed at the top of the note stem.
	
	double width;	
	
	/**
	 * 
	 * @param height		The spacing between the staff lines
	 * @param num			The number of tails/flags there should be
	 * @param stemDown		The direction of the stem.
	 */
	public NoteTail(double height, int num, boolean stemDown) {
		try {

			// Get the image file of the note_Tail.png
			Image tailImg = new Image(new FileInputStream("src/main/resources/image_assets/note_Tail.png"));
			
			// For each tail that we should display, create Image from the imagefile, set the correct width and height, and the correct position
			for (int i = 0; i < num; i++) {
				ImageView tailImgView = new ImageView(tailImg);
				tailImgView.setFitWidth(height * 2);
				tailImgView.setFitHeight(height * 3);
				tailImgView.setY(0 - i*height);
				this.getChildren().add(tailImgView);
			}
			
			// This is a line that we attach to the tails so that we can extend the original note.
			Line support = new Line(0, 0, 0, 0 - (num-1)*height);
			support.setStrokeWidth(height/15);
			support.setTranslateX(0 - support.minWidth(0)*0.5);
			this.getChildren().add(support);
			
			// If the note is upside down, then flips the tail, and correct the positioning.
			if (stemDown) {
				this.setRotate(180);
				this.setTranslateX(0 - this.minWidth(0));
				this.setTranslateY(0 - height * 2);
			}
			
			// Set the correct width
			this.width = this.minWidth(0);
		}
		// This exception happens if we can't find the image file, so nothing will display
		catch (IOException e) {
			System.out.println("Note Tail of Number " + num + " was failed to be created.");
		}
		
	}

}
