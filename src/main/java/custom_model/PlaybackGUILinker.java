package custom_model;

import java.util.List;

import custom_model.note.NoteUnit;

public class PlaybackGUILinker extends Thread {
	SheetScore sheet;
	int measureOfNote;
	int notePressed;
	
	
	public PlaybackGUILinker (SheetScore sheet, int measureOfNote, int notePressed) {
		this.sheet = sheet;
		this.measureOfNote = measureOfNote;
		this.notePressed = notePressed;
	}
	
	@Override
	public void run() {
		System.out.println("Linker is Running!");
		
		List<MusicMeasure> measures = sheet.getMeasureList();
		int timingsNumber = sheet.getTimingOfNote(measureOfNote, notePressed, measures);
		
		double measureSum = 0;
		double diff = 0;
		double current = System.currentTimeMillis();
		for (int i = measureOfNote; i < measures.size() && sheet.isPlaying; i++) {
			this.sheet.sp.setVvalue(sheet.getMeasurePosition(i + 1));
			
			MusicMeasure measure = measures.get(i);
			diff = measureSum - (System.currentTimeMillis() - current);
			current = System.currentTimeMillis();
			if (sheet.noteTimings.get(timingsNumber) + diff <= 0) {
				diff = 0 - sheet.noteTimings.get(timingsNumber);
				sheet.noteTimings.set(timingsNumber, 0.0);
			}
			else {
				sheet.noteTimings.set(timingsNumber, sheet.noteTimings.get(timingsNumber) + diff);
			}
			
			int first = timingsNumber;
			
			measureSum = 0;
			
			int j = (i == measureOfNote) ? notePressed : 0;
			for (; j < measure.notes.size() && sheet.isPlaying; j++) {
				measure.notes.get(j).toggleHighlight();
//				if (j == ((i == measureOfNote) ? notePressed : 0)) {
//					sheet.timings.set(timingsNumber, sheet.timings.get(timingsNumber) - diff);
//					diff = 0;
//				}
				try {
					NoteUnit.pressed = measure.notes.get(j);
					Thread.sleep((long) ((double) sheet.noteTimings.get(timingsNumber)));
					measureSum += sheet.noteTimings.get(timingsNumber);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				timingsNumber ++;
			}
			
			sheet.noteTimings.set(first, sheet.noteTimings.get(first) - diff);
			diff = 0;
		}
		
		if (sheet.isPlaying == true) {
			if (NoteUnit.pressed != null && NoteUnit.pressed.getHighlighted()) {
				NoteUnit.pressed.toggleHighlight();
			}
			NoteUnit.pressed = null;
			sheet.isPlaying = false;
		}
		
		this.sheet.threadKilled = true;		
		
	}	
	
}
