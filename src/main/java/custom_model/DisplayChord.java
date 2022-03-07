package custom_model;

import java.util.ArrayList;
import java.util.List;

import custom_component_data.Note;
import javafx.scene.shape.Line;

public class DisplayChord extends DisplayUnit{
	// Restore this Class
	
	boolean hasFlip;
	int max_position;
	int min_position;
	List<DisplayNote> displayNotes;

	public DisplayChord(double height, List<Note> notes) {
		
		sortNotes(notes);
		this.hasFlip = isANoteFlipped(notes);
		this.min_position = notes.get(0).getPosition();
		this.max_position = notes.get(notes.size()-1).getPosition();
		
		this.displayNotes = new ArrayList<>();
		double max_preceding = 0;
		double max_trailing = 0;
		
		boolean flipDone = false;
		for (int i = 0; i < notes.size(); i++) {
			DisplayNote note = null;
			if (flipDone) {
				note = new DisplayNote(height, notes.get(i), hasFlip, false);
				flipDone = false;
			}
			else {
				note = new DisplayNote(height, notes.get(i), hasFlip, i>0 && notes.get(i).getPosition() - notes.get(i-1).getPosition() == 1);
				flipDone = i>0 && notes.get(i).getPosition() - notes.get(i-1).getPosition() == 1;
			}
			
			if (i == notes.size()-1)
				note.addTails(height, notes.get(i).getStem() != null && notes.get(i).getStem().equals("down"));
			
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
		this.width = max_preceding + noteheadWidth + max_trailing;
		
		if (notes.get(0).getType() <= 1) {
			this.spacingType = notes.get(0).getType() != 0 ? notes.get(0).getType() : 0.5;
			this.position = min_position;
			this.height = this.minHeight(0);
			this.top = 0 - (height*0.5)*(max_position - min_position);
			return;
		}
		
		if (this.hasFlip) {
			if (stemDown) {
				int last = notes.size()-1;
				
				double pos_x = max_preceding + noteheadWidth;
				double start_y = displayNotes.get(last).parenthesesDisplacement;
				start_y += notes.get(last).getNotehead() != null && notes.get(last).getNotehead().equals("x") ? height : height/2;
				double end_y = start_y + (3 + 0.5*(max_position - min_position))*height;
				
				Line staff = new Line(pos_x, start_y, pos_x, end_y);
				staff.setStrokeWidth(height/15);
				staff.setTranslateX(0 - staff.minWidth(0));
				this.getChildren().add(staff);
			}
			else {
				int last = notes.size()-1;
				
				double pos_x = max_preceding + noteheadWidth;
				double start_y = displayNotes.get(0).parenthesesDisplacement - height*(3 + 0.5*(max_position-min_position));
				double end_y = displayNotes.get(0).parenthesesDisplacement;
				end_y += notes.get(0).getNotehead() == null || !notes.get(0).getNotehead().equals("x") ? height/2 : 0;
				
				Line staff = new Line(pos_x, start_y, pos_x, end_y);
				staff.setStrokeWidth(height/15);
				staff.setTranslateX(0 - staff.minWidth(0));
				this.getChildren().add(staff);
			}
		}
		else {
			double pos_x = stemDown ? max_preceding : max_preceding + noteheadWidth;
			double start_y = stemDown ? height : 0;
			double end_y = start_y - (max_position - min_position)*0.5*height;
			if (!stemDown) {
				this.top = 0 - ((max_position - min_position)*0.5 + 3)*height;
			}
			
			Line staff = new Line(pos_x, start_y, pos_x, end_y);
			staff.setStrokeWidth(height/15);
			staff.setTranslateX(0 - staff.minWidth(0));
			this.getChildren().add(staff);
			
		}
		
		this.spacingType = notes.get(0).getType() != 0 ? notes.get(0).getType() : 0.5;
		this.position = min_position;
		this.height = this.minHeight(0);			
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

	@Override
	public void addTails(double height, boolean stemDown) {
		
		
	}
}
