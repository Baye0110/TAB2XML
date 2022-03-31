package custom_model.note;

import custom_component_data.Note;
import custom_model.notehead.NoteHead;
import custom_model.rest.Rest;
import javafx.scene.Group;
import javafx.scene.layout.BorderWidths;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;

// DRUMS
public class DisplayNote extends DisplayUnit{
	double preceding;
	double trailing;
	double parenthesesDisplacement;
	
	boolean isChord;
	boolean isNormalSide;
	static double dotScale = 3;
	static double dotDistanceScale = 1.5;
	double noteHeadWidth;
	
	/**
	 * 
	 * @param height		The space between the staffLines
	 * @param note			The Note XML parsed data
	 * @param hasFlip		Is this Note in a chord with a flipped note (advanced - don't need to know)
	 * @param isFlip		Is this Note a flipped note (advanced - don't need to know)
	 */
	public DisplayNote(double height, Note note, boolean hasFlip, boolean isFlip) {
		// Set the basic values
		this.setTop(0);
		this.setBottom(0);
		
		// The spacingType is (0.5 = breve, 1 = whole, 2 = half, 4 = quarter, 8 = 8th, ...)
		this.setSpacingType(note.getType() != 0 ? note.getType() : 0.5);
		// This is the position on the staff which was already calculated during the XML parsing.
		this.setPosition(note.getPosition());
		
		// If the note was a rest note
		if (note.getRest()) {
			// Generate the rest note, and set the dimensions for the note
			Rest rest = new Rest(height, note.getType());
			this.setHeight(rest.getHeight());
			this.setWidth(rest.getWidth());
			this.getChildren().add(rest);
			
			// Add the number of dots that are needed for this rest.
			this.addDots(height, note);
			// Don't do anything else for rest notes.
			return;
		}
		
		this.setWidth(0);
		this.setHeight(0);
		
		// If this note is parenthesized add the Left-size parentheses
		if (note.getParentheses()) {
			this.addParentheses(1, height);
		}
		
		// Is the stem supposed to point down?
		boolean stemDown = note.getStem() != null && note.getStem().equals("down");
		
		// Create the note head based on the (line spacing - height, the type of the note, and the shape of the notehead)
		NoteHead head = new NoteHead(height, note.getType(), note.getNotehead());
		// Set the width of the notehead
		this.noteHeadWidth = head.getWidth();
		
		// If the note is supposed to be on the opposite side as normal
		if ((isFlip && !stemDown) || (hasFlip && !isFlip && stemDown)) {
			head.setTranslateX(this.getWidth() + head.getWidth());
			this.setWidth(this.getWidth() + head.getWidth() * 2);
		}
		// Otherwise for a normal note, increment the width of the this object, and set the X position of the notehead
		else {
			head.setTranslateX(this.getWidth());
			this.setWidth(this.getWidth() + head.getWidth());
		}
		
		// Add the notehead to the object.
		this.getChildren().add(head);
		
		// Get the type of stem specified in the XML
		String stemType = note.getStem();
		
		// Only create the stem if: 1. the stem is specified in the XML, it is note a breve/whole note, its not flipped
		if (stemType != null && !stemType.equals("none") && note.getType() != 0 && note.getType() != 1 &&  !hasFlip) {
			// Creating the stem going up
			if (stemType.equals("up")) {
				Line stem = new Line(this.getWidth(), 0 - 3*height, this.getWidth(), head.getStemPosition());
				stem.setStrokeWidth(height/15);
				stem.setTranslateX(0 - stem.minWidth(0)*1.25);
				this.setWidth(this.getWidth() + stem.minWidth(0));
				this.getChildren().add(stem);
			}
			// Creating the stem going down
			else if (stemType.equals("down")) {
				Line stem = new Line(0, 4*height, 0, head.getStemPosition());
				stem.setStrokeWidth(height/15);
				stem.setTranslateX(0 - stem.minWidth(0)*1.25);
				this.setWidth(this.getWidth() + stem.minWidth(0));
				this.getChildren().add(stem);
			}
		}
		
		// Set the value for the edge of the note stem.
		if (stemType != null && !stemType.equals("none") && note.getType() != 0 && note.getType() != 1) {
			if (stemType.equals("up")) {
				this.setTop(0 - 3*height);
			}
			else {
				this.setTop(4 * height);
			}
		}
		
		// is this a normal note (that is not flipped)
		this.isNormalSide = !hasFlip || (hasFlip && !isFlip && !stemDown) || (hasFlip && isFlip && stemDown);
		
		// If this is a normal note, but is part of a chord that has a flipped note, then accord for that in the width and the trailing
		if (this.isNormalSide && hasFlip) {
			this.setWidth(this.getWidth() + head.getWidth());
			this.trailing += head.getWidth();
		}
		
		// Add the specified number of dots into the note
		this.addDots(height, note);
		
		// Add the left parentheses
		if (note.getParentheses()) {		
			this.addParentheses(2, height);
		}
		
		// Set the dimensions of the note
		this.setWidth(this.minWidth(0));
		this.setHeight(this.minHeight(0));
	}
	
	/** Add the parentheses (type==1 : left,  type==2 : right)
	 * 
	 * @param type
	 * @param height
	 */
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
		
		parentheses.setTranslateX(this.getWidth());
		this.parenthesesDisplacement = (parentheses.minHeight(0) - height)/2;
		this.getChildren().add(parentheses);

		this.setWidth(this.getWidth() + parentheses.minWidth(0));
		
		if (type == 1) {
			this.preceding = parentheses.minWidth(0);
		}		
		else if (type == 2) {
			this.trailing = parentheses.minWidth(0);
			parentheses.setRotate(180);
		}
	}
	
	/**Add the dots to the note
	 * 
	 * @param height
	 * @param note
	 */
	public void addDots(double height, Note note) {
		for (int i = 0; i < note.getDot(); i++) {
			double pos_x = this.getWidth() + height/dotScale*(1.5); 
			double radius = (height/dotScale) / 2;
			this.setWidth(this.getWidth() + (height/dotScale)*2);
			this.trailing += (height/dotScale)*2;
			
			Ellipse dot = new Ellipse(pos_x, height/2, radius, radius);
			this.getChildren().add(dot);
		}
		
		this.trailing += (height/dotScale) / 2;
	}
	
	
	/**
	 * Extends the staff to support notes which are too hight
	 */
	public void extendStaff(int positions, double height) {
		for (int i = 0; i < (this.getPosition() - positions)/2; i ++) {
			double start_x = this.preceding + (this.isNormalSide ? 0 - this.noteHeadWidth * 0.25 : this.noteHeadWidth * 0.75);
			double end_x = start_x + this.noteHeadWidth * 1.5;
			double pos_y = this.getPosition()%2 == 0 ? height * (0.5+i) : height * (1+i);
			
			Line extension = new Line(start_x, pos_y, end_x, pos_y);
			extension.setStrokeWidth(height/15);
			this.getChildren().add(extension);
		}
	}
	
	/** Adds the NoteTails
	 * 
	 */
	public void addTails(double height, boolean stemDown) {
		if (this.getSpacingType() >= 8) {
			int log_spacingType = 0;
			for (; this.getSpacingType() > Math.pow(2, log_spacingType); log_spacingType++);
			log_spacingType -= 2;
			
			NoteTail nt = new NoteTail(height, log_spacingType, stemDown);
			nt.setTranslateY(this.getTop());
			
			if (!stemDown) {
				nt.setTranslateX(this.preceding + this.noteHeadWidth - height/15);
				this.setWidth(this.getWidth() + (nt.width > this.trailing ? nt.width - this.trailing : 0));
				this.trailing = nt.width > this.trailing ? nt.width : this.trailing;
			}
			else {
				this.setWidth(this.getWidth() + (nt.width > this.preceding ? nt.width - this.preceding : 0));
				this.preceding = nt.width > this.preceding ? nt.width : this.preceding;
			}
			
			this.getChildren().add(nt);
			
		}
	}
	
	public void toggleHighlight() {
		
	}
}
