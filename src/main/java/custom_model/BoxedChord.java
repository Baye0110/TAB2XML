package custom_model;

import java.util.ArrayList;
import java.util.List;

import custom_component_data.Note;

public class BoxedChord extends BoxedUnit{
	List<BoxedText> frets;
	
	public BoxedChord(double size, List<Note> notes, int measure) {
		
		this.frets = new ArrayList<>();
		
		for (int i = 0; i < notes.size(); i++) {
			Note note = notes.get(i);
			BoxedText fret = new BoxedText("" + note.getNotation().getFret(), size, note.getType() == 0 ? 0.5 : note.getType(), false, true, 0);
			fret.setTranslateY(size * i);
			
			this.frets.add(fret);
			this.getChildren().add(fret);
		}
		
		this.width = frets.get(0).width;
		this.grace = false;
		this.spacingType = notes.get(0).getType();
		this.measure = measure;
		
		BoxedUnit.currMeasureNoteNum ++;
		this.noteNum = currMeasureNoteNum;
		
		this.setOnMouseClicked(e -> {
			if (this == NoteUnit.pressed) {
				NoteUnit.pressed = null;
				this.toggleHighlight();
			}
			else {
				this.toggleHighlight();
				pressed = this;
				System.out.println("Selected Note: \t Measure - " + this.measure + ",  Note - " + this.noteNum);
			}
		});
	}
	
	public void toggleHighlight() {
		NoteUnit originalPressed = pressed;
		pressed = null;
		
		for (BoxedText fret: this.frets) {
			fret.toggleHighlight();
		}
		
		if (this.highlighted) {
			this.highlighted = false;
		}
		else {
			this.highlighted = true;
			if (originalPressed != null) {
				originalPressed.toggleHighlight();
			}
		}		
	}
}
