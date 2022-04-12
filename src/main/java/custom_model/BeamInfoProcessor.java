package custom_model;

import java.util.ArrayList;
import java.util.List;

import custom_model.MeasureBeamData.BeamInfo;
import custom_model.note.DisplayUnit;
import custom_model.note.NoteUnit;
import javafx.scene.Group;
import custom_model.MusicMeasure;

public class BeamInfoProcessor {
	public static final double beamWidthFactor = 0.4;
	public static final double beamSpaceFactor = 1.0/6.0;
	List<BeamCreationData> beamData;
	
	
	public class BeamCreationData {
		boolean isTail;
		int numFull;
		int numHalf;
		NoteUnit current;
		NoteUnit other;
		
		BeamCreationData(boolean isTail, int numFull, int numHalf) {
			this.isTail = isTail;
			this.numFull = numFull;
			this.numHalf = numHalf;			
		}
		
		private void setNote(NoteUnit note) {
			this.current = note;
		}
		
		private void setOther(NoteUnit other) {
			this.other = other;
		}
		
		public int getNumFull() {
			return this.numFull;
		}
		
		public int getNumHalf() {
			return this.numHalf;
		}
	}
	
	
	
	public BeamInfoProcessor(List<Integer> beamGroupings, List<BeamInfo> beamInfo) {
		this.beamData = new ArrayList<>();
		
		for (int i = 0; i < beamGroupings.size(); i++) {
//			// There is no beaming for this note
			if (beamGroupings.get(i) == 0) {
				//this.beamData.add(null);
			}
			// This note/chord is at the last of a beamed group
			else if (beamInfo.get(i).getTail()) {
				int prev = i - 1;
				while (beamInfo.get(prev) == null) {  prev --; }
				int beamsOnTail = beamInfo.get(prev).getBeamsRight() - beamInfo.get(prev).getBeamsLeft();
				
				BeamCreationData bcd = new BeamCreationData(true, beamInfo.get(prev).getBeamsLeft(), beamsOnTail > 0 ? beamsOnTail : 0);
				bcd.setNote(beamInfo.get(i).getNote());
				bcd.setOther(beamInfo.get(prev).getNote());
				this.beamData.add(bcd);
			}
			// This note/chord is at the beginning/middle of a beamed group
			else {
				// The beams on next note are at least as many as this note, then don't add any half-slants
				if (beamInfo.get(i).getBeamsLeft() <= beamInfo.get(i).getBeamsRight()) {
					BeamCreationData bcd = new BeamCreationData(false, beamInfo.get(i).getBeamsLeft(), 0);
					bcd.setNote(beamInfo.get(i).note);
					bcd.setOther(this.getNext(beamInfo, i));
					this.beamData.add(bcd);
				}
				// The beams on this note are more than the next
				else {
					int prev = i - 1;
					while (prev >= 0 && beamInfo.get(prev) == null) {  prev --; }
					
					// If the previous note was part of this beaming group with the same num of beams and the previous beam had at least as many beams as the current note
					if (prev >= 0 && beamGroupings.get(prev) == beamGroupings.get(i) && beamInfo.get(prev).getBeamsLeft() >= beamInfo.get(prev).getBeamsRight()) {
						BeamCreationData bcd = new BeamCreationData(false, beamInfo.get(i).getBeamsRight(), 0);
						bcd.setNote(beamInfo.get(i).note);
						bcd.setOther(this.getNext(beamInfo, i));
						this.beamData.add(bcd);
					}
					// This is the first note in a grouping, or its previous note had fewer beams
					else {
						BeamCreationData bcd = new BeamCreationData(false, beamInfo.get(i).getBeamsRight(), beamInfo.get(i).getBeamsLeft() - beamInfo.get(i).getBeamsRight());
						bcd.setNote(beamInfo.get(i).note);
						bcd.setOther(this.getNext(beamInfo, i));
						this.beamData.add(bcd);
					}
				}
			}
		}
		
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		
		for (int i = 0; i < this.beamData.size(); i++) {
			str.append(i + "\n");
//			if (this.beamData.get(i) == null) {
//				str.append("\tno beam!\n");
//			}
			if (this.beamData.get(i).isTail) {
				str.append("\tend: " + this.beamData.get(i).numHalf + "\n");
			}
			else {
				str.append("\tfull: " + this.beamData.get(i).numFull + ", \thalf: " + this.beamData.get(i).numHalf + "\n");
			}
		}
		
		str.append("\n");
		
		return str.toString();
	}
	
	public NoteUnit getNext(List<BeamInfo> list, int index) {
		int curr = index + 1;
		
		while (list.get(curr) == null) {
			curr++;
		}
		
		return list.get(curr).getNote();
	}
	
	public void generateGuitarBeams(MusicMeasure m, int lines) {
		double beamWidth = SheetScore.lineSize * BeamInfoProcessor.beamWidthFactor;
		double beamSpace = SheetScore.lineSize * BeamInfoProcessor.beamSpaceFactor;
		double measureBottom = SheetScore.lineSize * (2 + lines) - beamWidth;
		for (BeamCreationData data : this.beamData) {
			double posY = 0.0;
			if (data.isTail) {
				if (data.numHalf == 0)
					continue;
				
				for (int i = 0; i < data.numFull; i++) { posY += beamWidth + beamSpace; }
				double halfDistance = (data.current.getTranslateX() - data.other.getTranslateX())/2.0;
				Tremolo t = new Tremolo(halfDistance, beamWidth, 0, false, data.numHalf, beamSpace);
				//posY += beamWidth * data.numHalf + beamSpace * (data.numFull - 1);
				t.setTranslateY(measureBottom - posY);
				t.setTranslateX(data.other.getTranslateX() + data.other.getWidth()/2 + halfDistance);
				m.getChildren().add(t);
			}
			else {
				double length = data.other.getTranslateX() - data.current.getTranslateX();
				for (int i = 0; i < data.numFull; i++) {
					SlantLine line = new SlantLine(length, beamWidth, 0, false);
					line.setTranslateX(data.current.getTranslateX() + data.current.getWidth()/2);
					line.setTranslateY(measureBottom - posY);
					m.getChildren().add(line);
					posY += beamWidth + beamSpace;
				}
				for (int i = 0; i < data.numHalf; i++) {
					SlantLine line = new SlantLine(length/2, beamWidth, 0, false);
					line.setTranslateX(data.current.getTranslateX() + data.current.getWidth()/2);
					line.setTranslateY(measureBottom - posY);
					m.getChildren().add(line);
					posY += beamWidth + beamSpace;
				}
			}
		}
	}
	
	public void generateDrumsBeams(MusicMeasure m, int lines) {
		double beamWidth = SheetScore.lineSize * BeamInfoProcessor.beamWidthFactor;
		double beamSpace = SheetScore.lineSize * BeamInfoProcessor.beamSpaceFactor;
		double measureTop = 0 - SheetScore.lineSize * 4;
		for (BeamCreationData data : this.beamData) {
			DisplayUnit current = (DisplayUnit) data.current;
			DisplayUnit other = (DisplayUnit) data.other;
			
			current.extendStemForBeam();
			
			double posY = 0.0;
			if (data.isTail) {
				if (data.numHalf == 0)
					continue;
				
				for (int i = 0; i < data.numFull; i++) { posY += beamWidth + beamSpace; }
				double currStemPos = current.getTranslateX() + current.getStem().getStartX() - current.getStem().minWidth(0);
				double prevStemPos = other.getTranslateX() + other.getStem().getStartX();
				double halfDistance = (currStemPos - prevStemPos)/2.0;
				Tremolo t = new Tremolo(halfDistance, beamWidth, 0, false, data.numHalf, beamSpace);
				//posY += beamWidth * data.numHalf + beamSpace * (data.numFull - 1);
				t.setTranslateY(measureTop + posY);
				t.setTranslateX(prevStemPos + halfDistance);
				m.getChildren().add(t);
			}
			else {
				double currStemPos = current.getTranslateX() + current.getStem().getStartX() - current.getStem().minWidth(0);
				double nextStemPos = other.getTranslateX() + other.getStem().getStartX();
				double length = nextStemPos - currStemPos;
				for (int i = 0; i < data.numFull; i++) {
					SlantLine line = new SlantLine(length, beamWidth, 0, false);
					line.setTranslateX(currStemPos);
					line.setTranslateY(measureTop + posY);
					m.getChildren().add(line);
					posY += beamWidth + beamSpace;
				}
				for (int i = 0; i < data.numHalf; i++) {
					SlantLine line = new SlantLine(length/2, beamWidth, 0, false);
					line.setTranslateX(currStemPos);
					line.setTranslateY(measureTop + posY);
					m.getChildren().add(line);
					posY += beamWidth + beamSpace;
				}
			}
		}
		
	}
	
	public List<BeamCreationData> getBeamData() {
		return this.beamData;
	}
}
