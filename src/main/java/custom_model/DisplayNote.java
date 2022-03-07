package custom_model;

import custom_component_data.Note;
import custom_model.notehead.NoteHead;
import custom_model.rest.Rest;
import javafx.scene.Group;
import javafx.scene.layout.BorderWidths;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;

public class DisplayNote extends DisplayUnit{
	// Restore this class
	
	double preceding;
	double trailing;
	double parenthesesDisplacement;
	
	boolean isChord;
	boolean isNormalSide;
	static double dotScale = 3;
	static double dotDistanceScale = 1.5;
	double noteHeadWidth;
	
	public DisplayNote(double height, Note note, boolean hasFlip, boolean isFlip) {
		this.top = 0;
		this.bottom = 0;
		this.spacingType = note.getType() != 0 ? note.getType() : 0.5;
		this.position = note.getPosition();
		
		if (note.getRest()) {
			Rest rest = new Rest(height, note.getType());
			this.height = rest.getHeight();
			this.width = rest.getWidth();
			this.getChildren().add(rest);
			
			this.addDots(height, note);
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
		
		boolean stemDown = note.getStem() != null && note.getStem().equals("down");
		
		NoteHead head = new NoteHead(height, note.getType(), note.getNotehead());
		this.noteHeadWidth = head.getWidth();
		if ((isFlip && !stemDown) || (hasFlip && !isFlip && stemDown)) {
			head.setTranslateX(this.width + head.getWidth());
			this.width += head.getWidth() * 2;
		}
		else {
			head.setTranslateX(this.width);
			this.width += head.getWidth();
		}
		
		this.getChildren().add(head);
		
		String stemType = note.getStem();
		if (stemType != null && !stemType.equals("none") && note.getType() != 0 && note.getType() != 1 &&  !hasFlip) {
			if (stemType.equals("up")) {
				Line stem = new Line(this.width, 0 - 3*height, this.width, head.getStemPosition());
				stem.setStrokeWidth(height/15);
				stem.setTranslateX(0 - stem.minWidth(0)*1.25);
				this.width += stem.minWidth(0);
				this.getChildren().add(stem);
			}
			else if (stemType.equals("down")) {
				Line stem = new Line(0, 4*height, 0, head.getStemPosition());
				stem.setStrokeWidth(height/15);
				stem.setTranslateX(0 - stem.minWidth(0)*1.25);
				this.width += stem.minWidth(0);
				this.getChildren().add(stem);
			}
		}
		if (stemType != null && !stemType.equals("none") && note.getType() != 0 && note.getType() != 1) {
			if (stemType.equals("up")) {
				this.top = 0 - 3*height;
			}
			else {
				this.top = 4 * height;
			}
		}
		
		this.isNormalSide = !hasFlip || (hasFlip && !isFlip && !stemDown) || (hasFlip && isFlip && stemDown);
		
		if (this.isNormalSide && hasFlip) {
			this.width += head.getWidth();
			this.trailing += head.getWidth();
		}
		
		this.addDots(height, note);
		
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
		this.parenthesesDisplacement = (parentheses.minHeight(0) - height)/2;
		this.getChildren().add(parentheses);

		this.width += parentheses.minWidth(0);
		
		if (type == 1) {
			this.preceding = parentheses.minWidth(0);
		}		
		else if (type == 2) {
			this.trailing = parentheses.minWidth(0);
			parentheses.setRotate(180);
		}
	}
	
	
	public void addDots(double height, Note note) {
		for (int i = 0; i < note.getDot(); i++) {
			double pos_x = this.width + height/dotScale*(1.5); 
			double radius = (height/dotScale) / 2;
			this.width += (height/dotScale)*2;
			this.trailing += (height/dotScale)*2;
			
			Ellipse dot = new Ellipse(pos_x, height/2, radius, radius);
			this.getChildren().add(dot);
		}
		
		this.trailing += (height/dotScale) / 2;
	}
	
	
	public void extendStaff(int positions, double height) {
		for (int i = 0; i < (this.position - positions)/2; i ++) {
			System.out.println(positions);
			System.out.println("my_pos: " + this.position);
			double start_x = this.preceding + (this.isNormalSide ? 0 - this.noteHeadWidth * 0.25 : this.noteHeadWidth * 0.75);
			double end_x = start_x + this.noteHeadWidth * 1.5;
			double pos_y = position%2 == 0 ? height * (0.5+i) : height * (1+i);
			
			Line extension = new Line(start_x, pos_y, end_x, pos_y);
			extension.setStrokeWidth(height/15);
			this.getChildren().add(extension);
		}
	}
	
	public void addTails(double height, boolean stemDown) {
		if (this.spacingType >= 8) {
			int log_spacingType = 0;
			for (; this.spacingType > Math.pow(2, log_spacingType); log_spacingType++);
			log_spacingType -= 2;
			
			NoteTail nt = new NoteTail(height, log_spacingType, stemDown);
			nt.setTranslateY(top);
			
			if (!stemDown) {
				nt.setTranslateX(this.preceding + this.noteHeadWidth - height/15);
				this.width += nt.width > this.trailing ? nt.width - this.trailing : 0;
				this.trailing = nt.width > this.trailing ? nt.width : this.trailing;
			}
			else {
				this.width += nt.width > this.preceding ? nt.width - this.preceding : 0;
				this.preceding = nt.width > this.preceding ? nt.width : this.preceding;
			}
			
			this.getChildren().add(nt);
			
		}
	}
}
