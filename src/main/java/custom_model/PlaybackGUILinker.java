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
		for (int i = measureOfNote; i < measures.size() && sheet.playing; i++) {
			MusicMeasure measure = measures.get(i);
			diff = measureSum - (System.currentTimeMillis() - current);
			sheet.timings.set(timingsNumber, sheet.timings.get(timingsNumber) + diff);
			
			measureSum = 0;
			current = System.currentTimeMillis();
			diff = 0;
			
			int j = (i == measureOfNote) ? notePressed : 0;
			for (; j < measure.notes.size() && sheet.playing; j++) {
				measure.notes.get(j).toggleHighlight();
//				if (j == ((i == measureOfNote) ? notePressed : 0)) {
//					sheet.timings.set(timingsNumber, sheet.timings.get(timingsNumber) - diff);
//					diff = 0;
//				}
				try {
					Thread.sleep((long) ((double) sheet.timings.get(timingsNumber)));
					measureSum += sheet.timings.get(timingsNumber);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				NoteUnit.pressed = measure.notes.get(j);
				timingsNumber ++;
			}
		}
		
		
	}	
	
}
