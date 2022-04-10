package custom_model;

import java.util.ArrayList;
import java.util.List;

import custom_component_data.Measure;
import custom_component_data.Note;
import custom_component_data.Slur;
import custom_component_data.Tied;
import custom_model.note.BoxedChord;
import custom_model.note.BoxedText;
import custom_model.note.DisplayChord;
import custom_model.note.DisplayNote;
import custom_model.note.DisplayUnit;
import custom_model.note.NoteUnit;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class StaffMeasure extends MusicMeasure{
	
	public static final int UPPER_PADDING = 0;
	List<ChordedSlur> slurs;
	
	public StaffMeasure(double height, Measure measure, boolean start) {
		super(height, measure, start);
		
		this.numStaffLines = measure.getStaffLines();
		
		if (measure.getNotes().size() == 0) {
			this.generateBarLines(height, measure.getStaffLines());
			this.maxHeight = height * (measure.getStaffLines() + 1.5);
			this.box.setTranslateX(0);
			this.box.setTranslateY(0 - height * 0.5);
			this.box.setHeight(this.maxHeight);
			this.box.setWidth(this.minWidth);
			this.box.setFill(Color.TRANSPARENT);
			this.box.setStroke(Color.DEEPSKYBLUE);
			this.box.setStrokeWidth(5);
			this.box.setOpacity(0);
			this.getChildren().add(box);
			return;
		}
		
		DisplayUnit.currMeasureNoteNum = 0;
		
		List<Note> notes = measure.getNotes();
		this.notes = new ArrayList<NoteUnit>();
		this.slurs = new ArrayList<ChordedSlur>();
		this.setTieds(new ArrayList<ArcLine>());
		measure.generatePositions();
		
		for (int i = 0; i < notes.size(); i++) {
			boolean isChord = i + 1 < notes.size() && notes.get(i+1).getChord();
			
			DisplayUnit currentUnit = null;
			Note isSlurInChord = null;
			
			if (isChord) {
				List<Note> chordNotes = new ArrayList<>();
				chordNotes.add(notes.get(i));
				boolean currIsSlur = isSlurInChord == null && (notes.get(i).getNotation() != null && notes.get(i).getNotation().getSlurs().size() != 0);
				isSlurInChord = currIsSlur ? notes.get(i) : isSlurInChord;
				
				int noteNum = 1;
				while (isChord) {
					chordNotes.add(notes.get(i + noteNum));
					currIsSlur = isSlurInChord == null && (notes.get(i + noteNum).getNotation() != null && notes.get(i + noteNum).getNotation().getSlurs().size() != 0);
					isSlurInChord = currIsSlur ? notes.get(i + noteNum) : isSlurInChord;
					noteNum += 1;
					isChord = i + noteNum < notes.size() && notes.get(i + noteNum).getChord();	
					
				}
				
				i += chordNotes.size() - 1;
				
				currentUnit = new DisplayChord(height, chordNotes);
			}
			else {
				double noteHeight = notes.get(i).getGrace() ? height * 0.8 : height;
				currentUnit = new DisplayNote(noteHeight, notes.get(i), false, false, false);	
				currentUnit.setTranslateY(notes.get(i).getGrace() ? height * 0.2 : 0);
//				currentUnit.addTails(height, notes.get(i).getStem() != null && notes.get(i).getStem().equals("down"));
			}
			
			currentUnit.extendStaff((measure.getStaffLines()-1)*2, height);
			
			double pos_y = height*(measure.getStaffLines() + UPPER_PADDING-1.5) - (height/2)*currentUnit.getPosition();
			if (notes.get(i).getRest())
				currentUnit.setTranslateY(height*UPPER_PADDING + (height*(measure.getStaffLines()-1) - currentUnit.getHeight())/2);
			else 
				currentUnit.setTranslateY(currentUnit.getTranslateY() + pos_y);
			
			Note slurNote = isSlurInChord != null ? isSlurInChord : notes.get(i);
			if (slurNote.getNotation() != null && slurNote.getNotation().getSlurs().size() != 0) {
				List<Slur> slurs =  slurNote.getNotation().getSlurs();
				boolean init = false;    
				boolean end = false;
				for (Slur slur: slurs) {
					if (slur.getType().equals("start"))
						init = true;
					if (slur.getType().equals("stop"))
						end = true;
				}
				if (end) {
					this.slurs.get(this.slurs.size()-1).setPositionY(currentUnit);
				}
				if (init) {
					ChordedSlur slur = new ChordedSlur(currentUnit); 
					this.slurs.add(slur);
					this.getChildren().add(slur);
				}
			}
			
			if (!notes.get(i).getRest() && notes.get(i).getNotation() != null && notes.get(i).getNotation().getTieds().size() != 0) {
				List<Tied> tieds = notes.get(i).getNotation().getTieds();
				boolean init = false;
				boolean end = false;
				for (Tied tied: tieds) {
					if (tied.getType().equals("start"))
						init = i < notes.size() - 1;
					if (tied.getType().equals("stop")) 
						end = this.notes.size() != 0;
				}
				
				if (end) 
					currentUnit.setTiedEnd(this);
								
				if (init) 
					currentUnit.addTied(this, true);
			}
			
			this.notes.add(currentUnit);
		}
		
		
		
		MeasureBeamData mbd = new MeasureBeamData(this.notes, measure.getTimeSignature()[1]);
		this.beamProcessor = new BeamInfoProcessor(mbd.getBeamNumbers() , mbd.beamInfos);
		for (int i = 0; i < this.notes.size(); i++) {
			List<Integer> beamNums = mbd.getBeamNumbers();
			DisplayUnit thisNote = (DisplayUnit) this.notes.get(i);
			if (!thisNote.getRest() && beamNums.get(i) == 0) {
				thisNote.addTails(height, false);
			}
		}
		
		for (NoteUnit note: this.notes) {
			note.setTranslateX(currentDistance);
			double spaceAdded = wholeNoteSpacing/note.getSpacingType();
			currentDistance += note.getWidth() +  spaceAdded;
			this.spacing += spaceAdded;
		}
		
		if (measure.getIsRepeatStop()) {
			this.generateEndRepeat(height, measure.getStaffLines(), measure.getBarLineRight().getRepeatNum());
			this.minWidth = this.currentDistance;
		}
		else {
			try {
				this.minWidth = currentDistance + this.notes.get(0).getWidth()/2;
			}catch(Exception e) {
				System.out.println("Invalid note!");
			}
		}
		
		for (NoteUnit noteUnit: this.notes) {
			DisplayUnit note = (DisplayUnit) noteUnit;
			note.generateBox();
			note.getChildren().add(note.getBox());
			this.maxHeight = note.getTranslateY() + note.getTop() > this.maxHeight ? note.getTranslateY() + note.getTop() : this.maxHeight;
			this.getChildren().add(note);
		}
		
		this.generateBarLines(height, measure.getStaffLines());
		
		this.maxHeight = height * (measure.getStaffLines() + 4.5);
		
		this.box.setTranslateX(0);
		this.box.setTranslateY(0 - 3.5 * height);
		this.box.setHeight(this.maxHeight);
		this.box.setWidth(this.minWidth);
		this.box.setFill(Color.TRANSPARENT);
		this.box.setStroke(Color.DEEPSKYBLUE);
		this.box.setStrokeWidth(5);
		this.box.setOpacity(0);
		this.getChildren().add(box);
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
			current += currNote.getWidth() + (this.wholeNoteSpacing/currNote.getSpacingType());
			this.spacing += this.wholeNoteSpacing/currNote.getSpacingType();
		}
		
		for (int i = 0; i < this.slurs.size(); i++) {
			this.slurs.get(i).setPositionX();
		}
		
		for (int i = 0; i < this.getTieds().size(); i++) {
			this.getTieds().get(i).setPositionX(false);
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
		this.minWidth = current;
		
		this.beamProcessor.generateDrumsBeams(this, this.numStaffLines);
	}

}
