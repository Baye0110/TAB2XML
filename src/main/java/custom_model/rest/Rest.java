package custom_model.rest;

import java.io.IOException;

import javafx.scene.Group;

public class Rest extends Group{
	// Create the appropriate type of rest 	
	double width;
	double height;
	
	public Rest(double height, int type) {
		// depending on the duration of the rest, create the correct Rest using the 5 classes in "custom_mode.rest" package
		switch(type) {
			case 1: // if duration is 1, create WholeRest
				WholeRest wholeRest = new WholeRest(height);
				this.getChildren().add(wholeRest);
				this.width = wholeRest.width;
				this.height = wholeRest.height;
				break;
			case 2: // if duration is 2, create HalfRest
				HalfRest halfRest = new HalfRest(height);
				this.getChildren().add(halfRest);
				this.width = halfRest.width;
				this.height = halfRest.height;
				break;
			case 4: // If duration is 4, create QuarterRest
				try {
					QuarterRest qRest = new QuarterRest(height);
					this.getChildren().add(qRest);
					this.width = qRest.width;
					this.height = qRest.height;
				} catch (IOException e) {
					System.out.println("IOException: The quarter rest is image not loading properly.");
				}
				break;
			default: // Create 8th. 16th, 32nd, 64th rest
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
