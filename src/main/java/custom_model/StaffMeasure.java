package custom_model;

import java.util.ArrayList;
import java.util.List;

import custom_component_data.Measure;
import custom_component_data.Note;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class StaffMeasure extends MusicMeasure{
	public static final double START_DISTANCE = 30;
	public static final int UPPER_PADDING = 3;

	List<DisplayNote> notes;
	
	public StaffMeasure(double height, Measure measure, boolean start) {
		double currentDistance = START_DISTANCE;
		List<Note> notes = measure.getNotes();
		this.notes = new ArrayList<>();
		measure.generatePositions();
		
		if (start) {
			// Create a clef symbol
		}
		
		if (measure.getTimeDisplay()) {
			Text beat = new Text();
			beat.setText(Integer.toString(measure.getTimeSignature()[0]));
			beat.setFont(Font.font(height * (measure.getStaffLines()-1) * 0.675));
			beat.setX(currentDistance);
			beat.setY(height * (UPPER_PADDING + (measure.getStaffLines()-1)/2.0));
			this.getChildren().add(beat);
			
			Text beatType = new Text();
			beatType.setText(Integer.toString(measure.getTimeSignature()[1]));
			beatType.setFont(Font.font(height * (measure.getStaffLines()-1) * 0.675));
			beatType.setX(currentDistance);
			beatType.setY(height * (UPPER_PADDING + measure.getStaffLines()-1));
			this.getChildren().add(beatType);
			
			currentDistance += beat.minWidth(0) + 50;
		}
		
		for (int i = 0; i < notes.size(); i++) {
			boolean isChord = i + 1 < notes.size() && notes.get(i+1).getChord();
			DisplayNote note = new DisplayNote(height, notes.get(i), isChord);
			Note currentNote = notes.get(i);
			
			note.setTranslateX(currentDistance);
			
			
			double pos_y = height*(measure.getStaffLines() + UPPER_PADDING-1.5) - (height/2)*currentNote.getPosition() - note.parenthesesDisplacement;
			
			if (currentNote.getRest())
				pos_y = height*UPPER_PADDING + (height*(measure.getStaffLines()-1) - note.height)/2 ;
			
			System.out.println(currentNote.getPosition());
			
			note.setTranslateY(pos_y); 
			if (isChord) {
				currentDistance += 0;
			} else {
				currentDistance += note.width + wholeDistance/note.spacingType;
				this.spacing += wholeDistance/note.spacingType;
			}
			this.notes.add(note);
		}
		
		this.minWidth = currentDistance;
		
		this.barLines = new ArrayList<Line>();
		for (int i = 0; i < measure.getStaffLines(); i++) {
			Line barLine = new Line();
			barLine.setStartX(0);
			barLine.setStartY(height * (UPPER_PADDING+i));
			barLine.setEndX(this.minWidth);
			barLine.setEndY(height * (UPPER_PADDING+i));
			barLine.setStroke(Paint.valueOf("0x222"));
			barLine.setStrokeWidth(0.6);
			
			this.barLines.add(barLine);
			this.getChildren().add(barLine);
		}
		
		Line end = new Line();
		end.setStartX(this.minWidth);
		end.setStartY(UPPER_PADDING*height);
		end.setEndX(this.minWidth);
		end.setEndY((UPPER_PADDING-1 + measure.getStaffLines()) * height);
		end.setStrokeWidth(2);
		this.barLines.add(end);
		this.getChildren().add(end);
		
		for (DisplayNote note: this.notes) {
			this.getChildren().add(note);
		}
	}
	
	public void setSpacing(double scale) {
		if (this.notes.size() == 0)
			return;
		
		double current = this.notes.get(0).getTranslateX();
		this.spacing = 0;
		
		for (int i = 0; i < this.notes.size(); i++) {
			DisplayNote currNote = this.notes.get(i);
			currNote.setTranslateX(current);
			if (!currNote.isChord) {
				current += currNote.width + (this.wholeDistance/currNote.spacingType);
				this.spacing += this.wholeDistance/currNote.spacingType;
			}
		}
		
		for (int i = 0; i < this.barLines.size()-1; i++) {
			this.barLines.get(i).setEndX(current);
		}
		
		Line end = this.barLines.get(this.barLines.size()-1);
		end.setStartX(current);
		end.setEndX(current);
	}
	
	public void setBaseDistance(double scale) {
		this.wholeDistance *= scale;
	}

}
