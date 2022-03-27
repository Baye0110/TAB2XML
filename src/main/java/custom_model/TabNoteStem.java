package custom_model;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;

// For GUITARS, BASS
public class TabNoteStem extends Group {
	// Creates the noteStem that is displayed under guitar/bass tabs

	
	/**
	 * @param height	the spacing between the stafflines
	 * @param type		the type of this note (1 = whole, 2 = half, 4 = quarter, 8 = 8th, 16 = 16th, ...)
	 * @param dot
	 */
	public TabNoteStem(double height, int type, int dot) {
		if (type == 2) {  // Creates the Stem for a half note
			// A line with half length
			Line stem = new Line(0, height, 0, height * 2);
			stem.setStrokeWidth(height/15);
			this.getChildren().add(stem);
		}
		if (type == 4) {  // Creates the stem for a quarter note
			// A line with full length:  (2 * height)
			Line stem = new Line(0, 0, 0, height * 2);
			stem.setStrokeWidth(height/15);
			this.getChildren().add(stem);
		}
		if (type >= 8) {  // Creates the stem for an eighth, 16th, 32th, 64th or 128th note
			// A line of full length: (2 * height)
			Line stem = new Line(0, 0, 0, height * 2);
			stem.setStrokeWidth(height/15);
			this.getChildren().add(stem);
			
			// Add the number of tails/flags accordng to the note (8th = 1 tail, 16th = 2 tail, 32nd = 3 tails...) 
			int tails = 0;
			int copy_type = type;
			type /= 4;
			while (type != 1) {
				tails += 1;
				type /= 2;
			}
			
			// Creates the NoteTail (all the tails that should be attached to the stem
			NoteTail nt = new NoteTail(height*0.6, tails, false);
			// Correct the position of the tail by rotating 180 degrees and flipping it horizontally
			nt.setRotate(180);
			nt.setScaleX(-1);
			this.getChildren().add(nt);
			type = copy_type;
		}
		
		// Add all the necessary dots to the note
		double baseDotDistance = height;
		// The radius for each dot
		double dotRad = height/6.5;
		
		// calculating the spacing between each dot (spacing decreases as number of dots increase)
		for (int i = 0; i < dot; i++) {
			baseDotDistance *= 2.0/3;
		}
		
		// For the number of dots specified, create the black circle at the appropriate position
		for (int i = 0; i < dot; i++) {
			Ellipse circle = new Ellipse(baseDotDistance*(i + 1), height/2 + (type <= 2 ? height: 0), dotRad, dotRad);
			this.getChildren().add(circle);
		}
		System.out.println("");
	}

}
