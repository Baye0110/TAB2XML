package custom_model.note;

import java.util.ArrayList;
import java.util.List;

import custom_component_data.Note;
import custom_model.ArcLine;
import custom_model.MusicMeasure;
import custom_model.SheetScore;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class DisplayChord extends DisplayUnit{
	boolean hasFlip;
	int max_position;
	int min_position;
	List<DisplayNote> displayNotes;

	public DisplayChord(double height, List<Note> notes) {
		sortNotes(notes);
		this.hasFlip = isANoteFlipped(notes);
		this.min_position = notes.get(0).getPosition();
		this.max_position = notes.get(notes.size()-1).getPosition();
		this.setRest(false);
		
		this.data = notes.get(notes.size()-1);
		
		this.displayNotes = new ArrayList<>();
		double max_preceding = 0;
		double max_trailing = 0;
		
		boolean flipDone = false;
		for (int i = 0; i < notes.size(); i++) {
			DisplayNote note = null;
			if (flipDone) {
				note = new DisplayNote(height, notes.get(i), hasFlip, false, true);
				flipDone = false;
			}
			else {
				note = new DisplayNote(height, notes.get(i), hasFlip, i>0 && notes.get(i).getPosition() - notes.get(i-1).getPosition() == 1, true);
				flipDone = i>0 && notes.get(i).getPosition() - notes.get(i-1).getPosition() == 1;
			}
						
			max_preceding = note.preceding > max_preceding ? note.preceding : max_preceding;
			max_trailing = note.trailing > max_trailing ? note.trailing : max_trailing;
			displayNotes.add(note);
		}
		
				
		for (int i = 0; i < displayNotes.size(); i++) {
			DisplayNote current = displayNotes.get(i);
			current.setTranslateX(max_preceding - current.preceding);
			current.setTranslateY(0 - (height*0.5)*(notes.get(i).getPosition() - min_position));
			this.getChildren().add(current);
		}
		
		boolean stemDown = notes.get(0).getStem() != null && notes.get(0).getStem().equals("down");

		double noteheadWidth = displayNotes.get(0).noteHeadWidth;
		this.setWidth(max_preceding + noteheadWidth + max_trailing);
		
		if (notes.get(0).getType() <= 1) {
			this.setSpacingType(notes.get(0).getType() != 0 ? notes.get(0).getType() : 0.5);
			this.setPosition(min_position);
			this.setHeight(this.minHeight(0));
			this.setTop(0 - (height*0.5)*(max_position - min_position));
			return;
		}
		
		if (this.hasFlip) {
			if (stemDown) {
				int last = notes.size()-1;
				
				double pos_x = max_preceding + noteheadWidth;
				double start_y = displayNotes.get(last).parenthesesDisplacement;
				start_y += notes.get(last).getNotehead() != null && notes.get(last).getNotehead().equals("x") ? height : height/2;
				double end_y = start_y + (3 + 0.5*(max_position - min_position))*height;
				
				this.stem = new Line(pos_x, start_y, pos_x, end_y);
				this.stem.setStrokeWidth(height/15);
				this.stem.setTranslateX(0 - this.stem.minWidth(0));
				this.getChildren().add(this.stem);
			}
			else {
				int last = notes.size()-1;
				
				double pos_x = max_preceding + noteheadWidth;
				double start_y = displayNotes.get(0).parenthesesDisplacement - height*(3 + 0.5*(max_position-min_position));
				double end_y = displayNotes.get(0).parenthesesDisplacement;
				end_y += notes.get(0).getNotehead() == null || !notes.get(0).getNotehead().equals("x") ? height/2 : 0;
				
				this.stem = new Line(pos_x, start_y, pos_x, end_y);
				this.stem.setStrokeWidth(height/15);
				this.stem.setTranslateX(0 - this.stem.minWidth(0));
				this.getChildren().add(this.stem);
			}
		}
		else {
			double pos_x = stemDown ? max_preceding : max_preceding + noteheadWidth;
			double end_y = stemDown ? height : 0;
			double start_y  = end_y - (max_position - min_position)*0.5*height;
			if (!stemDown) {
				this.setTop(0 - ((max_position - min_position)*0.5 + 3)*height);
			}
			
			this.stem = new Line(pos_x, start_y, pos_x, end_y);
			this.stem.setStrokeWidth(height/15);
			this.stem.setTranslateX(0 - this.stem.minWidth(0));
			this.getChildren().add(this.stem);
			
		}
		
		this.setSpacingType(notes.get(0).getType() == 0 ? 0.5 : (notes.get(0).getGrace() ? 24 : notes.get(0).getType()));
		this.setPosition(min_position);
		this.setHeight(this.minHeight(0));		
		
		
		DisplayUnit.currMeasureNoteNum ++;
		this.noteNum = DisplayUnit.currMeasureNoteNum;
		this.measure = MusicMeasure.measureCount;
		
		this.setOnMouseClicked(e -> {
			if (this == NoteUnit.pressed) {
				NoteUnit.pressed = null;
				this.toggleHighlight();
			}
			else {
				this.toggleHighlight();
				NoteUnit.pressed = this;
			}
		});
		
		this.generateBox();
		this.getChildren().add(box);
		this.box.setOpacity(0.0);
	}
	
	public static void sortNotes(List<Note> notes) {
		for (int i = 0; i < notes.size()-1; i++) {
			int minPosition = i;
			for (int j = i + 1; j < notes.size(); j++) {
				if (notes.get(j).getPosition() < notes.get(minPosition).getPosition()) {
					minPosition = j;
				}
			}
			Note temp = notes.get(minPosition);
			notes.set(minPosition, notes.get(i));
			notes.set(i, temp);
		}
	}
	
	public static boolean isANoteFlipped(List<Note> notes) {
		for (int i = 1; i < notes.size(); i++) {
			if (notes.get(i).getPosition() - notes.get(i-1).getPosition() == 1) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void extendStaff(int positions, double height) {
		for (DisplayNote note: this.displayNotes) {
			note.extendStaff(positions, height);
		}
	}
	
	public void toggleHighlight() {
		if (!this.isHighlighted) {
			this.isHighlighted = true;
			this.box.setOpacity(1.0);
			
			if (pressed != null) {
				pressed.toggleHighlight();
			}	
			if (this.noteNum > -1) {
				System.out.println("Selected Note: \t Measure - " + this.measure + ",  Note - " + this.noteNum);
			}
		}
		else {
			this.box.setOpacity(0.0);
			this.isHighlighted = false;
		}
		
	}
	
	public void generateBox() {
		double topHeight = 0.0;
		topHeight -= this.getTranslateY() > 0 ? this.getTranslateY(): 0;
		topHeight -= SheetScore.lineSize * 4;
		
		Line left = new Line(0 - 2, topHeight, 0 - 2, SheetScore.lineSize);
		Line top = new Line(0 - 2, topHeight, this.minWidth(0) + 2, topHeight);
		Line bottom = new Line(0 - 2, SheetScore.lineSize, this.minWidth(0) + 2, SheetScore.lineSize);
		Line right = new Line(this.minWidth(0) + 2, topHeight, this.minWidth(0) + 2, SheetScore.lineSize);
		left.setStroke(Color.DEEPSKYBLUE);
		top.setStroke(Color.DEEPSKYBLUE);
		bottom.setStroke(Color.DEEPSKYBLUE);
		right.setStroke(Color.DEEPSKYBLUE);
		
		this.box = new Group(left, top, bottom, right);
		this.box.setOpacity(0);
	}
	
	public void addTails(double height, boolean stemDown) {
		this.displayNotes.get(this.displayNotes.size()-1).addTails(height, stemDown);
	}
	
	public void extendStemForBeam() {
		for (DisplayNote note: this.displayNotes) {
			if (note.stem != null)
				note.stem.setStartY(0 - note.getTranslateY() - SheetScore.lineSize * 4);
		}
		this.stem.setStartY(0 - this.getTranslateY() - SheetScore.lineSize * 4);
	}
	
	public List<DisplayNote> getNotes() {
		return this.displayNotes;
	}

	@Override
	public List<ArcLine> addTied(MusicMeasure m, boolean withinMeasure) {
		List<ArcLine> arcs = new ArrayList<>();
		List<DisplayNote> chordNotes = this.getNotes();
		for (int j = 0; j < chordNotes.size(); j++) {
			ArcLine arc = new ArcLine(SheetScore.lineSize * 0.9, 5, j == chordNotes.size() - 1);
			arc.setTranslateY(this.getTranslateY() + chordNotes.get(j).getTranslateY() + (j != chordNotes.size() - 1 ? SheetScore.lineSize  : 0));
			arc.setStartNote(this);
			if (withinMeasure)
				m.getTieds().add(arc);
			arcs.add(arc);
			m.getChildren().add(arc);
		}
		return arcs;
	}
	
	public void setTiedEnd(MusicMeasure m) {
		List<DisplayNote> chordNotes = this.getNotes();
		for (int j = 0; j < chordNotes.size(); j++) {
			m.getTieds().get(m.getTieds().size() - chordNotes.size() + j).setEndNote(this);
		}
	}
	
	@Override
	public void setInterTiedEnd(List<ArcLine> arcs) {
		for (int i = this.getNotes().size(); i > 0; i--) {
			arcs.get(arcs.size() - i).setEndNote(this);
		}	
	}
}
