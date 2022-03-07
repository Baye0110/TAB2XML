package custom_model.rest;

import javafx.scene.Group;

public class SmallRest extends Group {
	// Restore this class
	
	double height;
	double width;
	
	public SmallRest(double height, int type) {
		int num = -2;
		while (type != 1) {
			type /= 2;
			num += 1;
		}
		this.height = height;
		this.width = height * 0.7;
		
		for (int i = 0; i < num; i++) {
			EighthRest rest = new EighthRest(height);
			rest.setTranslateX((rest.width - 7)/4*(num - i - 1));
			rest.setTranslateY(height * i);
			
			this.height += height;
			this.width += height * 0.3;
			
			this.getChildren().add(rest);
		}
	}

}
