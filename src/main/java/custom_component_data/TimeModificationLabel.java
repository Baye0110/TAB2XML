package custom_component_data;

import java.util.List;

import custom_model.MusicMeasure;
import custom_model.SheetScore;
import custom_model.note.NoteUnit;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TimeModificationLabel extends Group {
	int actualNumber;
	NoteUnit start;
	NoteUnit end;

	public TimeModificationLabel(NoteUnit start, NoteUnit end, Integer actual) {
		this.actualNumber = actual;
		this.start = start;
		this.end = end;		
	}


	public static boolean isTimeModComplete(List<Note> notes) {
		if (notes.size() >= 2 && ((notes.get(0).getType() == 8 && notes.get(1).getType() == 4) || 
				(notes.get(0).getType() == 4 && notes.get(1).getType() == 8))
				&& notes.get(0).getTimeModification().get("actual").equals(Integer.valueOf(3))) {
			
			return true;
		}
		else if (notes.get(0).getTimeModification().get("actual").equals(Integer.valueOf(notes.size()))) {
			return true;
		}
		
		return false;
	}
	
	public void generateLabel(int staffLines, boolean isDrums) {		
		double width = this.end.getTranslateX() + this.end.getWidth() - this.start.getTranslateX();
		Line startLine = new Line(0, 0, 0, 0 - SheetScore.lineSize);
		Line endLine = new Line(width, 0, width, 0 - SheetScore.lineSize);
		Line connect = null;
		if (isDrums) 
			connect = new Line(0, 0 - SheetScore.lineSize, width, 0 - SheetScore.lineSize);
		else 
			connect = new Line(0, 0, width, 0);
		
		Text text = new Text(Integer.toString(this.actualNumber));
		text.setFont(Font.font(MusicMeasure.customizefont, SheetScore.lineSize * 1.5));
		text.setTranslateX(width/2 - text.minWidth(0)/2);
		
		this.getChildren().addAll(startLine, endLine, connect);
		this.setTranslateX(this.start.getTranslateX());
		if (isDrums) {
			this.setTranslateY(0 - SheetScore.lineSize * 4.5);
			text.setTranslateY(0 - SheetScore.lineSize/2);
		}
		else {
			this.setTranslateY(SheetScore.lineSize * (staffLines + 3.5));
			text.setTranslateY(SheetScore.lineSize/2);
			startLine.setStartY(-1); endLine.setStartY(-1);
		}
		
		Rectangle r = new Rectangle();
		r.setFill(Color.WHITE);
		r.setX(text.getTranslateX());
		r.setY(text.getTranslateY() - text.minHeight(0));
		r.setWidth(text.minWidth(0));
		r.setHeight(text.minHeight(0));
		this.getChildren().add(r);
		this.getChildren().add(text);
		
	}

}
