package custom_model;

import java.util.ArrayList;
import java.util.List;

import custom_component_data.Measure;
import custom_component_data.Note;
import custom_model.note.DisplayChord;
import custom_model.note.DisplayNote;
import custom_model.note.DisplayUnit;
import custom_model.note.NoteUnit;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;

public class StaffMeasure extends MusicMeasure{
	
	public static final int UPPER_PADDING = 0;
	
	public StaffMeasure(double height, Measure measure, boolean start) {
		super(height, measure, start);
		if (measure.getNotes().size() == 0) {
			this.generateBarLines(height, measure.getStaffLines());
			this.maxHeight = height * (measure.getStaffLines());
			return;
		}
		DisplayUnit.currMeasureNoteNum = 0;
		
		List<Note> notes = measure.getNotes();
		this.notes = new ArrayList<>();
		measure.generatePositions();
		
		for (int i = 0; i < notes.size(); i++) {
			boolean isChord = i + 1 < notes.size() && notes.get(i+1).getChord();
			
			DisplayUnit currentUnit = null;
			
			if (isChord) {
				List<Note> chordNotes = new ArrayList<>();
				chordNotes.add(notes.get(i));
				
				int noteNum = 1;
				while (isChord) {
					chordNotes.add(notes.get(i + 1));
					noteNum += 1;
					isChord = i + noteNum < notes.size() && notes.get(i + noteNum).getChord();					
				}
				
				i += chordNotes.size() - 1;
				
				currentUnit = new DisplayChord(height, chordNotes);
			}
			else {
				double noteHeight = notes.get(i).getGrace() ? height * 0.65 : height;
				currentUnit = new DisplayNote(height, notes.get(i), false, false, false);	
				currentUnit.addTails(height, notes.get(i).getStem() != null && notes.get(i).getStem().equals("down"));
				currentUnit.generateBox();
				currentUnit.getChildren().add(currentUnit.getBox());
			}
			
			
			currentUnit.extendStaff((measure.getStaffLines()-1)*2, height);
					
			currentUnit.setTranslateX(currentDistance);
			
			double pos_y = height*(measure.getStaffLines() + UPPER_PADDING-1.5) - (height/2)*currentUnit.getPosition();
			if (notes.get(i).getRest())
				currentUnit.setTranslateY(height*UPPER_PADDING + (height*(measure.getStaffLines()-1) - currentUnit.getHeight())/2);
			else 
				currentUnit.setTranslateY(currentUnit.getTranslateY() + pos_y);
			
			//System.out.println(currentUnit.getPosition());
			
			double spaceAdded = wholeNoteSpacing/currentUnit.getSpacingType();
			currentDistance += currentUnit.getWidth() +  spaceAdded;
			this.spacing += spaceAdded;
			this.notes.add(currentUnit);
		}
		
		
		if (measure.getIsRepeatStop()) {
			this.generateEndRepeat(height, measure.getStaffLines(), measure.getBarLineRight().getRepeatNum());
			this.minWidth = this.currentDistance;
		}
		else {
			try {
				this.minWidth = currentDistance + this.notes.get(0).minWidth(0)/2;
			}catch(Exception e) {
				System.out.println("Invalid note!");
			}
		}

		for (NoteUnit noteUnit: this.notes) {
			DisplayUnit note = (DisplayUnit) noteUnit;
			this.maxHeight = note.getTranslateY() + note.getTop() > this.maxHeight ? note.getTranslateY() + note.getTop() : this.maxHeight;
			this.getChildren().add(note);
		}
		
		this.generateBarLines(height, measure.getStaffLines());
//		this.barLines = new ArrayList<Line>();
//		for (int i = 0; i < measure.getStaffLines(); i++) {
//			Line barLine = new Line();
//			barLine.setStartX(0);
//			barLine.setStartY(height * (UPPER_PADDING+i));
//			barLine.setEndX(this.minWidth);
//			barLine.setEndY(height * (UPPER_PADDING+i));
//			barLine.setStroke(Paint.valueOf("0x222"));
//			barLine.setStrokeWidth(0.6);
//			
//			this.barLines.add(barLine);
//			this.getChildren().add(barLine);
//		}
//		
//		Line end = new Line();
//		end.setStartX(this.minWidth);
//		end.setStartY(UPPER_PADDING*height);
//		end.setEndX(this.minWidth);
//		end.setEndY((UPPER_PADDING-1 + measure.getStaffLines()) * height);
//		end.setStrokeWidth(2);
//		this.barLines.add(end);
//		this.getChildren().add(end);
		
	}
	
	public void setSpacing(double scale) {
		if (this.notes.size() == 0) {
			this.minWidth = this.minWidth * scale;
			// extend the X positions of the staff barLines to match the new width of the measure
			for (int i = 0; i < this.barLines.size()-1; i++) {
				this.barLines.get(i).setEndX(this.minWidth);
			}
			
			// For the last measure ending line, we set both the start and end X position to the new width of the measure
			Line end = this.barLines.get(this.barLines.size()-1);
			end.setStartX(this.minWidth);
			end.setEndX(this.minWidth);
			return;
		}
			
		
		double current = this.notes.get(0).getTranslateX();
		this.spacing = 0;
		
		for (int i = 0; i < this.notes.size(); i++) {
			NoteUnit currNote = this.notes.get(i);
			currNote.setTranslateX(current);
			current += currNote.minWidth(0) + (this.wholeNoteSpacing/currNote.getSpacingType());
			this.spacing += this.wholeNoteSpacing/currNote.getSpacingType();
		}
		
		if (this.endRepeat != null) {
			this.endRepeat.get(0).setTranslateX(current);
			this.endRepeat.get(1).setTranslateX(current);
			
			current += this.endRepeat.get(1).minWidth(0) - ((RepeatBarLine) this.endRepeat.get(1)).getFirstLineWidth()/2;
		}
		else {
			// add some extra padding for the end of the measure
			current += this.notes.get(0).minWidth(0)/2;
		}
		
		for (int i = 0; i < this.barLines.size()-1; i++) {
			this.barLines.get(i).setEndX(current);
		}
		
		if (this.endRepeat != null) {
			this.getChildren().remove(this.barLines.get(this.barLines.size()-1));
		}
		else {
			Line end = this.barLines.get(this.barLines.size()-1);
			end.setStartX(current);
			end.setEndX(current);
		}
	}

}
