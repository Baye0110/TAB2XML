package custom_model;

import java.util.ArrayList;
import java.util.List;

import custom_component_data.Measure;
import custom_component_data.Note;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TabMeasure extends MusicMeasure {
	public static final double START_DISTANCE = 30;
	
	List<BoxedText> labels;
	
	public TabMeasure (double size, Measure m, boolean start) {
		double currentDistance = START_DISTANCE;
		List<Note> notes = m.getNotes();
		this.labels = new ArrayList<BoxedText>();
		
		if (start) {
			// Add the clef
		}
		
		if (m.getTimeDisplay()) {
			Text beat = new Text();
			beat.setText(Integer.toString(m.getTimeSignature()[0]));
			beat.setFont(Font.font(size * (m.getStaffLines()-1) * 0.675));
			beat.setX(currentDistance);
			beat.setY(size * (2 + (m.getStaffLines()-1)/2.0));
			this.getChildren().add(beat);
			
			Text beatType = new Text();
			beatType.setText(Integer.toString(m.getTimeSignature()[1]));
			beatType.setFont(Font.font(size * (m.getStaffLines()-1) * 0.675));
			beatType.setX(currentDistance);
			beatType.setY(size * (2 + m.getStaffLines()-1));
			this.getChildren().add(beatType);
			
			currentDistance += beat.minWidth(0) + 50;
		}
		
		for (int i = 0; i < notes.size(); i++) {
			double type = notes.get(i).getType() != 0 ? notes.get(i).getType() : 0.5;
			boolean isChord = i + 1 < notes.size() && notes.get(i+1).getChord();
			BoxedText fret = new BoxedText("" + notes.get(i).getNotation().getFret(), size, type, isChord);
			Note currentNode = notes.get(i);
			
			fret.setTranslateX(currentDistance);
			fret.setTranslateY(size * (currentNode.getNotation().getString() + 0.5)); 
			if (isChord) {
				currentDistance += 0;
			} else {
				currentDistance += fret.minWidth(0) + wholeDistance/type;
				this.spacing += wholeDistance/type;
			}
			labels.add(fret);
		}
		this.minWidth = currentDistance;
		
		this.barLines = new ArrayList<Line>();
		for (int i = 0; i < m.getStaffLines(); i++) {
			Line barLine = new Line();
			barLine.setStartX(0);
			barLine.setStartY(size * (2+i));
			barLine.setEndX(this.minWidth);
			barLine.setEndY(size * (2+i));
			barLine.setStroke(Paint.valueOf("0x777"));
			barLine.setStrokeWidth(0.6);
			
			this.barLines.add(barLine);
			this.getChildren().add(barLine);
		}
		
		Line end = new Line();
		end.setStartX(this.minWidth);
		end.setStartY(2*size);
		end.setEndX(this.minWidth);
		end.setEndY((1 + m.getStaffLines()) * size);
		end.setStrokeWidth(2);
		this.barLines.add(end);
		this.getChildren().add(end);
		
		for (BoxedText label: this.labels) {
			this.getChildren().add(label);
		}
		
	}
	
	public void setSpacing(double scale) {
		double current = this.labels.get(0).getTranslateX();
		this.spacing = 0;
		
		for (int i = 0; i < this.labels.size(); i++) {
			BoxedText currLabel = this.labels.get(i);
			currLabel.setTranslateX(current);
			if (!currLabel.chord) {
				current += currLabel.minWidth(0) + (this.wholeDistance/currLabel.type);
				this.spacing += this.wholeDistance/currLabel.type;
			}
		}
		
		for (int i = 0; i < this.barLines.size()-1; i++) {
			this.barLines.get(i).setEndX(current);
		}
		
		Line end = this.barLines.get(this.barLines.size()-1);
		end.setStartX(current);
		end.setEndX(current);
	}

}
