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
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class StaffMeasure extends MusicMeasure{
	
	public static final int UPPER_PADDING = 0;
	List<ChordedSlur> slurs;
	
	public StaffMeasure(double height, Measure measure, boolean start) {
		super(height, measure, start);
		
		this.box = new Group();
		this.getChildren().add(this.box);
		
		List<Note> timeModCumulated = new ArrayList<>();
		
		this.numStaffLines = measure.getStaffLines();
		
		if (measure.getNotes().size() == 0) {
			this.generateBarLines(height, measure.getStaffLines());
			this.maxHeight = height * (measure.getStaffLines() + 1.5);
			Rectangle dot = new Rectangle();
			dot.setY(0 - 6 * height);
			dot.setX(0);
			dot.setHeight(2);
			dot.setWidth(2);
			dot.setFill(Color.TRANSPARENT);
			this.getChildren().add(dot);
			
//			this.box.setTranslateX(0);
//			this.box.setTranslateY(0 - height * 0.5);
//			this.box.setHeight(this.maxHeight);
//			this.box.setWidth(this.minWidth);
//			this.box.setFill(Color.TRANSPARENT);
//			this.box.setStroke(Color.DEEPSKYBLUE);
//			this.box.setStrokeWidth(5);
//			this.box.setOpacity(0);
//			this.getChildren().add(box);
			return;
		}
		
		DisplayUnit.currMeasureNoteNum = 0;
		
		List<Note> notes = measure.getNotes();
		this.notes = new ArrayList<NoteUnit>();
		this.slurs = new ArrayList<ChordedSlur>();
		this.setTieds(new ArrayList<ArcLine>());
		this.timeMods = new ArrayList<TimeModificationLabel>();
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
			
			if (notes.get(i).getTimeModification() != null) {
				timeModCumulated.add(notes.get(i));
				if (TimeModificationLabel.isTimeModComplete(timeModCumulated)) {
					TimeModificationLabel tml = new TimeModificationLabel(this.notes.get(this.notes.size() - timeModCumulated.size()), 
							this.notes.get(this.notes.size()-1), 
							notes.get(i).getTimeModification().get("actual"));
					this.timeMods.add(tml);
					this.getChildren().add(tml);
					timeModCumulated = new ArrayList<>();
				}
			}
			
			custom_component_data.Tremolo currTremolo = notes.get(i).getNotation() != null && notes.get(i).getNotation().getOrnaments() != null ? notes.get(i).getNotation().getOrnaments().getTremolo() : null;
			if(currTremolo != null) {
				int numSlants = currTremolo.getNumber();
				currentUnit.addTremolo(numSlants);
			}
		}
		
		/*
		 * TODO: Tremolo Implementation
		 * 
		 * 	1. Check if this note has a tremolo using the ppt. The current note object can be accessed by: notes.get(i)
		 *  2. Once you have a custom_component_data.Tremolo object you can find the number of slants using the "getNumber()" method
		 *  3. Call the "addTremolo(int numOfSlants)" method of the DisplayUnit class. We currently have the variable "currentUnit" to call this method.
		 */
		
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
		
//		this.box.setTranslateX(0);
//		this.box.setTranslateY(0 - 3.5 * height);
//		this.box.setHeight(this.maxHeight);
//		this.box.setWidth(this.minWidth);
//		this.box.setFill(Color.TRANSPARENT);
//		this.box.setStroke(Color.DEEPSKYBLUE);
//		this.box.setStrokeWidth(5);
//		this.box.setOpacity(0);
//		this.getChildren().add(box);
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
		
		for (int i = 0; i < this.timeMods.size(); i++) {
			this.timeMods.get(i).generateLabel(this.numStaffLines, true);
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
		
		double topHeight = 0 - SheetScore.lineSize * 6 - (this.timeMods.size() != 0 ? SheetScore.lineSize * 1.5: 0);
		Line top = new Line(0, topHeight, this.minWidth, topHeight);
		Line left = new Line(0, topHeight, 0, SheetScore.lineSize * this.numStaffLines);
		Line right = new Line(this.minWidth, topHeight, this.minWidth, SheetScore.lineSize * this.numStaffLines);
		Line bottom = new Line(0, SheetScore.lineSize * this.numStaffLines, this.minWidth, SheetScore.lineSize * this.numStaffLines);
		this.box.getChildren().addAll(top, left, right, bottom);
		this.box.setOpacity(0);
		
		top.setStroke(Color.DEEPSKYBLUE); top.setStrokeWidth(5);	bottom.setStroke(Color.DEEPSKYBLUE); bottom.setStrokeWidth(5);
		left.setStroke(Color.DEEPSKYBLUE); left.setStrokeWidth(5);	right.setStroke(Color.DEEPSKYBLUE); right.setStrokeWidth(5);
		
		
	}

}
