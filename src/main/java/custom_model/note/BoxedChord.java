package custom_model.note;

import java.util.ArrayList;
import java.util.List;

import custom_component_data.Note;
import custom_model.ArcLine;
import custom_model.MusicMeasure;
import custom_model.SheetScore;

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
			BoxedText fret = new BoxedText("" + note.getNotation().getFret(), isGrace ? size * 0.65 : size, note.getType() == 0 ? 0.5 : (isGrace? 32: note.getType()), isGrace, true, 0, notes.get(i));
			fret.setTranslateY(size * (notes.get(i).getNotation().getString() - min));
			fret.setIsChord(true);
			
			this.frets.add(fret);
			this.getChildren().add(fret);
		}
		
		this.setWidth(frets.get(0).getWidth());
		this.setGrace(isGrace);
		this.setSpacingType(notes.get(0).getType());
		this.setMeasure(measure);
		this.sortFrets();
		
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
	
	public List<BoxedText> getFrets() {
		return this.frets;
	}
	
	public void sortFrets() {
		// lowest on the tabulature to the highest
		for (int i = 0; i < this.frets.size() - 1; i++) {
			int lowest = i;
			for (int j = i + 1; j < this.frets.size(); j++) {
				if (this.frets.get(j).getTranslateY() > this.frets.get(lowest).getTranslateY()) {
					lowest = j;
				}
			}
			BoxedText temp = this.frets.get(i);
			this.frets.set(i, this.frets.get(lowest));
			this.frets.set(lowest, temp);
		}
	}

	@Override
	public List<ArcLine> addTied(MusicMeasure m, boolean withinMeasure) {
		List<ArcLine> arcs = new ArrayList<>();
		List<BoxedText> frets = this.getFrets();
		for (int j = 0; j < frets.size(); j++) {
			ArcLine arc = new ArcLine(SheetScore.lineSize * 0.9, 5, j == frets.size() - 1);
			arc.setTranslateY(this.getTranslateY() + frets.get(j).getTranslateY() - (j == frets.size() - 1 ? SheetScore.lineSize : 0 - SheetScore.lineSize));
			arc.setStartNote(this);
			if (withinMeasure)
				m.getTieds().add(arc);
			m.getChildren().add(arc);
			arcs.add(arc);
		}
		return arcs;
	}

	@Override
	public void setTiedEnd(MusicMeasure m) {
		List<BoxedText> frets = this.getFrets();
		for (int j = 0; j < frets.size(); j++) {
			m.getTieds().get(m.getTieds().size() - frets.size() + j).setEndNote(this);
		}
	}
	
	@Override
	public void setInterTiedEnd(List<ArcLine> arcs) {
		for (int i = this.getFrets().size(); i > 0; i--) {
			arcs.get(arcs.size() - i).setEndNote(this);
		}	
	}
}
