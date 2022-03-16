package custom_model.notehead;

import javafx.scene.Group;
import javafx.scene.paint.Color;

public class NoteHead extends Group{
	double width;
	double height;
	double stemPosition;

	public NoteHead(double height, int type, String notehead) {
		if (notehead != null && notehead.equals("x")) {
			CrossNoteHead x = null;
			if (type < 2)
				x = new CrossNoteHead(height, Color.BLACK, 2);
			else 
				x = new CrossNoteHead(height, Color.BLACK, 1);
			this.width = x.width;
			this.height = height;
			this.stemPosition = x.stemPosition;
			this.getChildren().add(x);
			
			if (type < 2) {
				CrossNoteHead inner = new CrossNoteHead(height * 0.9, Color.WHITE, 1);
				inner.setTranslateY(height * 0.05);
				inner.setTranslateX(height * 0.06);
				this.getChildren().add(inner);
			}
			return;
		}
		
		switch(type) {
		case 0:
			BreveNoteHead breve = new BreveNoteHead(height);
			this.width = breve.width;
			this.height = height;
			this.getChildren().add(breve);
			return;
		
		case 1:
			WholeNoteHead whole = new WholeNoteHead(height);
			this.width = whole.width;
			this.height = height;
			this.getChildren().add(whole);
			return;
			
		case 2:
			HalfNoteHead half = new HalfNoteHead(height);
			this.width = half.width;
			this.height = height;
			this.stemPosition = half.stemPosition;
			this.getChildren().add(half);
			return;
			
		default:
			FullNoteHead full = new FullNoteHead(height);
			this.width = full.width;
			this.height = height;
			this.stemPosition = full.stemPosition;
			this.getChildren().add(full);
			return;
		}		
	}
	
	public double getHeight() {
		return this.height;
	}
	
	public double getWidth() {
		return this.width;
	}
	
	public double getStemPosition() {
		return this.stemPosition;
	}
}
