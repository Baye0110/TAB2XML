package custom_model.rest;

import javafx.scene.Group;

public class SmallRest extends Group {
	// Creates all rests with duration of eighth note and less (8th. 16th. 32th, 64th, 128th)
	double height;
	double width;
	
	public SmallRest(double height, int type) {
		// An Xth rest is composed of (log_2 X)-2 eighth rest, so
		// We calculate the number of 8th rests
		int num = -2;
		while (type != 1) {
			type /= 2;
			num += 1;
		}
		
		// set the base dimensions for 1 eighth rest
		this.height = height;
		this.width = height * 0.7;
		
		for (int i = 0; i < num; i++) {
			// create an eighth rest
			EighthRest rest = new EighthRest(height);
			rest.setTranslateX((rest.width - 7)/4*(num - i - 1));
			rest.setTranslateY(height * i);
			
			// add the dimensions as you add more eighth rests
			this.height += height;
			this.width += height * 0.3;
			
			this.getChildren().add(rest);
		}
	}

}
