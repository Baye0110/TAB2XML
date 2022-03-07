package custom_model.rest;

import java.io.IOException;

import javafx.scene.Group;

public class Rest extends Group{
	// Restore this class
	
	double width;
	double height;
	
	public Rest(double height, int type) {
		switch(type) {
			case 1:
				WholeRest wholeRest = new WholeRest(height);
				this.getChildren().add(wholeRest);
				this.width = wholeRest.width;
				this.height = wholeRest.height;
				break;
			case 2:
				HalfRest halfRest = new HalfRest(height);
				this.getChildren().add(halfRest);
				this.width = halfRest.width;
				this.height = halfRest.height;
				break;
			case 4:
				try {
					QuarterRest qRest = new QuarterRest(height);
					this.getChildren().add(qRest);
					this.width = qRest.width;
					this.height = qRest.height;
				} catch (IOException e) {
					System.out.println("IOException: The quarter rest is image not loading properly.");
				}
				break;
			default:
				SmallRest sRest = new SmallRest(height, type);
				this.getChildren().add(sRest);
				this.width = sRest.width;
				sRest.height = sRest.height;
				break;
		}
	}
	
	public double getHeight() {
		return this.height;
	}
	
	public double getWidth() {
		return this.width;
	}
}
