package custom_model;

import java.util.ArrayList;
import java.util.List;

import custom_model.MeasureBeamData.BeamInfo;
import custom_model.note.NoteUnit;

public class BeamInfoProcessor {
	class BeamCreationData {
		boolean isTail;
		int numFull;
		int numHalf;
		NoteUnit note;
		
		BeamCreationData(boolean isTail, int numFull, int numHalf) {
			this.isTail = isTail;
			this.numFull = numFull;
			this.numHalf = numHalf;			
		}
		
		private void setNote(NoteUnit note) {
			this.note = note;
		}
	}
	
	List<BeamCreationData> beamData;
	
	public BeamInfoProcessor(List<Integer> beamGroupings, List<BeamInfo> beamInfo) {
		this.beamData = new ArrayList<>();
		
		for (int i = 0; i < beamGroupings.size(); i++) {
			// There is no beaming for this note
			if (beamGroupings.get(i) == 0) {
				this.beamData.add(null);
			}
			// This note/chord is at the last of a beamed group
			else if (beamInfo.get(i).getTail()) {
				int prev = i - 1;
				while (beamInfo.get(prev) == null) {  prev --; }
				int beamsOnTail = beamInfo.get(prev).getBeamsRight() - beamInfo.get(prev).getBeamsLeft();
				
				BeamCreationData bcd = new BeamCreationData(true, beamInfo.get(prev).getBeamsLeft(), beamsOnTail > 0 ? beamsOnTail : 0);
				bcd.note = beamInfo.get(i).getNote();
				this.beamData.add(bcd);
			}
			// This note/chord is at the beginning/middle of a beamed group
			else {
				// The beams on next note are at least as many as this note, then don't add any half-slants
				if (beamInfo.get(i).getBeamsLeft() <= beamInfo.get(i).getBeamsRight()) {
					BeamCreationData bcd = new BeamCreationData(false, beamInfo.get(i).getBeamsLeft(), 0);
					bcd.setNote(beamInfo.get(i).note);
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
						this.beamData.add(bcd);
					}
					// This is the first note in a grouping, or its previous note had fewer beams
					else {
						BeamCreationData bcd = new BeamCreationData(false, beamInfo.get(i).getBeamsRight(), beamInfo.get(i).getBeamsLeft() - beamInfo.get(i).getBeamsRight());
						bcd.setNote(beamInfo.get(i).note);
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
			if (this.beamData.get(i) == null) {
				str.append("\tno beam!\n");
			}
			else if (this.beamData.get(i).isTail) {
				str.append("\tend: " + this.beamData.get(i).numHalf + "\n");
			}
			else {
				str.append("\tfull: " + this.beamData.get(i).numFull + ", \thalf: " + this.beamData.get(i).numHalf + "\n");
			}
		}
		
		str.append("\n");
		
		return str.toString();
	}
}
