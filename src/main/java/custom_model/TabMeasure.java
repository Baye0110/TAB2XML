package custom_model;

import java.util.ArrayList;
import java.util.List;

import custom_component_data.Measure;
import custom_component_data.Note;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;

public class TabMeasure extends MusicMeasure {	
	// Restore this Class

	List<BoxedText> labels;
	List<TabNoteStem> stems;
	
	public TabMeasure (double size, Measure m, boolean start) {
		super(size, m, start);
		List<Note> notes = m.getNotes();
		this.labels = new ArrayList<BoxedText>();
		this.stems = new ArrayList<TabNoteStem>();
		this.maxHeight = size * (m.getStaffLines());
//		
//		if (start) {
//			currentDistance += this.generateTabClef(size * (m.getStaffLines() - 1), currentDistance, m.getStaffLines());
//		}
		
//		if (m.getTimeDisplay()) {
//			Text beat = new Text();
//			beat.setText(Integer.toString(m.getTimeSignature()[0]));
//			beat.setFont(Font.font(size * (m.getStaffLines()-1) * 0.675));
//			beat.setX(currentDistance);
//			beat.setY(size * (2 + (m.getStaffLines()-1)/2.0));
//			this.getChildren().add(beat);
//			
//			Text beatType = new Text();
//			beatType.setText(Integer.toString(m.getTimeSignature()[1]));
//			beatType.setFont(Font.font(size * (m.getStaffLines()-1) * 0.675));
//			beatType.setX(currentDistance);
//			beatType.setY(size * (2 + m.getStaffLines()-1));
//			this.getChildren().add(beatType);
//			
//			List<Text> timeVertical = new ArrayList<>();
//			timeVertical.add(beat);
//			timeVertical.add(beatType);
//			MusicMeasure.alignVerticalText(timeVertical);
//			
//			currentDistance += beat.minWidth(0) + 50;
//		}
		
		for (int i = 0; i < notes.size(); i++) {
			Note currentNote = notes.get(i);
			double type = notes.get(i).getType() != 0 ? currentNote.getType() : 0.5;
			boolean isChord = i + 1 < notes.size() && notes.get(i+1).getChord();
			
			double noteSize = currentNote.getGrace() ? size * 0.65 : size;
			
			BoxedText fret = new BoxedText("" + currentNote.getNotation().getFret(), noteSize, type, isChord, currentNote.getGrace());
			
			if (!currentNote.getChord() && !currentNote.getGrace()) {
				TabNoteStem stem = new TabNoteStem(size, notes.get(i).getType(), notes.get(i).getDot());
				stem.setTranslateX(currentDistance + (fret.minWidth(0)/2));
				stem.setTranslateY(size * (m.getStaffLines() + 2));
				stems.add(stem);
			}
			
			
			
			fret.setTranslateX(currentDistance);
			fret.setTranslateY(size * (currentNote.getNotation().getString() + 0.5) + (size - noteSize)); 
			if (isChord) {
				currentDistance += 0;
			} else {
				currentDistance += fret.minWidth(0) + wholeDistance/type;
				this.spacing += wholeDistance/type;
			}
			labels.add(fret);
		}
		this.minWidth = currentDistance + labels.get(0).minWidth(0)/2;
		
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
		
		for (TabNoteStem stem: this.stems) {
			this.getChildren().add(stem);
		}
		
	}
	
	public void setSpacing(double scale) {
		double current = this.labels.get(0).getTranslateX();
		this.spacing = 0;
		
		int chordNum = 0;
		for (int i = 0; i < this.labels.size(); i++) {
			BoxedText currLabel = this.labels.get(i);
			currLabel.setTranslateX(current);
			if (!currLabel.chord) {
				if (!currLabel.grace) {
					this.stems.get(chordNum).setTranslateX(current + (currLabel.minWidth(0)/2));
					chordNum ++;
				}
				
				current += currLabel.minWidth(0) + (this.wholeDistance/currLabel.type);
				this.spacing += this.wholeDistance/currLabel.type;
			}
		}
		
		current += this.labels.get(0).minWidth(0)/2;
		
		for (int i = 0; i < this.barLines.size()-1; i++) {
			this.barLines.get(i).setEndX(current);
		}
		
		Line end = this.barLines.get(this.barLines.size()-1);
		end.setStartX(current);
		end.setEndX(current);
	}

}
