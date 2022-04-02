package custom_model.note;

import java.util.ArrayList;
import java.util.List;

import custom_component_data.Note;

public class BoxedChord extends BoxedUnit{
	List<BoxedText> frets;
	
	public BoxedChord(double size, List<Note> notes, int measure, boolean isGrace) {
		
		this.frets = new ArrayList<>();
		
		int min = notes.get(0).getNotation().getString();
		this.data = notes.get(0);
		for (int i = 1; i < notes.size(); i++) {
			if (notes.get(i).getNotation().getString() < min) {
				min = notes.get(i).getNotation().getString();
				this.data = notes.get(i);
			}
		}
		
		for (int i = 0; i < notes.size(); i++) {
			Note note = notes.get(i);
			BoxedText fret = new BoxedText("" + note.getNotation().getFret(), isGrace ? size * 0.65 : size, note.getType() == 0 ? 0.5 : note.getType(), false, true, 0, notes.get(i));
			fret.setTranslateY(size * (notes.get(i).getNotation().getString() - min));
			
			this.frets.add(fret);
			this.getChildren().add(fret);
		}
		
		this.setWidth(frets.get(0).getWidth());
		this.setGrace(isGrace);
		this.setSpacingType(notes.get(0).getType());
		this.setMeasure(measure);
		
		BoxedUnit.noteCounter ++;
		this.noteNum = noteCounter;
		
		this.setOnMouseClicked(e -> {
			if (this == NoteUnit.pressed) {
				NoteUnit.pressed = null;
				this.toggleHighlight();
			}
			else {
				this.toggleHighlight();
				pressed = this;
			}
		});
	}
	
	public void toggleHighlight() {
		NoteUnit originalPressed = pressed;
		pressed = null;
		
		for (BoxedText fret: this.frets) {
			fret.toggleHighlight();
		}
		
		if (this.isHighlighted) {
			this.isHighlighted = false;
		}
		else {
			this.isHighlighted = true;
			if (originalPressed != null) {
				originalPressed.toggleHighlight();
			}
			System.out.println("Selected Note: \t Measure - " + this.getMeasure() + ",  Note - " + this.getNoteNum());
		}		
	}
}
