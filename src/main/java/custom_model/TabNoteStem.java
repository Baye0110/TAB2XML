package custom_model;

import javafx.scene.Group;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;

public class TabNoteStem extends Group {
	
	public TabNoteStem(double height, int type, int dot) {
		if (type == 2) {
			Line stem = new Line(0, height, 0, height * 2);
			stem.setStrokeWidth(height/15);
			this.getChildren().add(stem);
		}
		if (type == 4) {
			Line stem = new Line(0, 0, 0, height * 2);
			stem.setStrokeWidth(height/15);
			this.getChildren().add(stem);
		}
		if (type >= 8) {
			Line stem = new Line(0, 0, 0, height * 2);
			stem.setStrokeWidth(height/15);
			this.getChildren().add(stem);
			
			int tails = 0;
			int copy_type = type;
			type /= 4;
			while (type != 1) {
				tails += 1;
				type /= 2;
			}
			NoteTail nt = new NoteTail(height*0.6, tails, false);
			nt.setRotate(180);
			nt.setScaleX(-1);
			this.getChildren().add(nt);
			type = copy_type;
		}
		
		double baseDotDistance = height;
		double dotRad = height/6.5;
		for (int i = 0; i < dot; i++) {
			baseDotDistance *= 2/3;
		}
		for (int i = 0; i < dot; i++) {
			Ellipse circle = new Ellipse(height*0.5 + baseDotDistance*(i+1), height/2 + (type <= 2 ? height: 0), dotRad, dotRad);
			this.getChildren().add(circle);
		}
	}

}
