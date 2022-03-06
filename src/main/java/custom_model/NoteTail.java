package custom_model;

import java.io.FileInputStream;
import java.io.IOException;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;

public class NoteTail extends Group {
	double width;	
	
	public NoteTail(double height, int num, boolean stemDown) {
		try {
			Image tailImg = new Image(new FileInputStream("src/main/resources/image_assets/note_tail.png"));
			for (int i = 0; i < num; i++) {
				ImageView tailImgView = new ImageView(tailImg);
				tailImgView.setFitWidth(height * 2);
				tailImgView.setFitHeight(height * 3);
				tailImgView.setY(0 - i*height);
				this.getChildren().add(tailImgView);
			}
			Line support = new Line(0, 0, 0, 0 - (num-1)*height);
			support.setStrokeWidth(height/15);
			support.setTranslateX(0 - support.minWidth(0)*0.5);
			this.getChildren().add(support);
			
			if (stemDown) {
				this.setRotate(180);
				this.setTranslateX(0 - this.minWidth(0));
				this.setTranslateY(0 - height * 2);
			}
			
			this.width = this.minWidth(0);
		}
		catch (IOException e) {
			System.out.println("Note Tail of Number " + num + " was failed to be created.");
		}
		
	}

}
