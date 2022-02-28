package custom_model;

import custom_component_data.Note;
import custom_model.notehead.NoteHead;
import custom_model.rest.Rest;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;

public class DisplayNote extends Group{
	double width;
	double top;
	double bottom;
	double height;
	double parenthesesDisplacement;
	double spacingType;
	boolean isChord;
	static double dotScale = 3;
	static double dotDistanceScale = 1.5;
	
	public DisplayNote(double height, Note note, boolean isChord) {
		this.parenthesesDisplacement = 0;
		this.top = 0;
		this.bottom = 0;
		this.spacingType = note.getType() != 0 ? note.getType() : 0.5;
		this.isChord = isChord;
		
		if (note.getRest()) {
			Rest rest = new Rest(height, note.getType());
			this.height = rest.getHeight();
			this.width = rest.getWidth();
			this.getChildren().add(rest);
			return;
		}
		
		// create the notehead
		// add the appropriate stem in the identified direction
		// add the number of dots
		// add the parentheses
		// add the tremolo
		// do the grace note
		this.width = 0;
		this.height = 0;
		
		if (note.getParentheses()) {
			this.addParentheses(1, height);
		}
		
		NoteHead head = new NoteHead(height, note.getType(), note.getNotehead());
		head.setTranslateX(this.width);
		this.getChildren().add(head);
		this.width += head.getWidth();
		
		String stemType = note.getStem();
		if (stemType != null && !stemType.equals("none") && note.getType() != 0 && note.getType() != 1) {
			if (stemType.equals("up")) {
				Line stem = new Line(this.width, 0 - 3*height, this.width, head.getStemPosition());
				stem.setStrokeWidth(height/15);
				stem.setTranslateX(0 - stem.minWidth(0)*1.25);
				this.width += stem.minWidth(0);
				this.getChildren().add(stem);
			}
			else if (stemType.equals("down")) {
				Line stem = new Line(0, 4*height, 0, head.getStemPosition());
				stem.setStrokeWidth(3);
				stem.setTranslateX(0 - stem.minWidth(0));
				this.width += stem.minWidth(0);
				this.getChildren().add(stem);
			}
		}
		
		for (int i = 0; i < note.getDot(); i++) {
			double pos_x = this.width + height/dotScale*(1.5); 
			double radius = (height/dotScale) / 2;
			this.width += (height/dotScale)*2;
			
			Ellipse dot = new Ellipse(pos_x, head.getHeight()/2, radius, radius);
			this.getChildren().add(dot);
		}
		
		if (note.getParentheses()) {		
			this.addParentheses(2, height);
		}
		
		this.width = this.minWidth(0);
		this.height = this.minHeight(0);
	}
	
	private void addParentheses(int type, double height) {
		Arc ring = new Arc();
		Arc shadow = new Arc();
		Group parentheses = new Group(ring, shadow);
		
		ring.setCenterX(height * 1.3);
		ring.setCenterY(height / 2);
		ring.setRadiusX(height * 1.3);
		ring.setRadiusY(height);
		ring.setStartAngle(135);
		ring.setLength(90);
		
		shadow.setCenterX(height * 1.3);
		shadow.setCenterY(height / 2);
		shadow.setRadiusX(height * 1.2);
		shadow.setRadiusY(height * 1.1);
		shadow.setStartAngle(135);
		shadow.setLength(90);
		shadow.setFill(Color.WHITE);
		
		parentheses.setTranslateX(this.width);
		this.getChildren().add(parentheses);

		this.width += parentheses.minWidth(0);
		this.parenthesesDisplacement = (parentheses.minHeight(0) - height)/2;
		
		if (type == 2) {
			parentheses.setRotate(180);
		}
	}
}
