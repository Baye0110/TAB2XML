package custom_model;

import java.util.List;

import custom_component_data.Note;
import custom_model.note.NoteUnit;

import java.util.ArrayList;

public class MeasureBeamData {
	
	public class BeamInfo {
		int beamsRight;
		int beamsLeft;
		NoteUnit note;
		boolean isTail;
		
		BeamInfo(NoteUnit curr, NoteUnit next) {
			this.note = curr;
			this.beamsLeft = this.calculateNumOfBeams(curr);
			this.beamsRight = this.calculateNumOfBeams(next);
			this.isTail = false;
		}
		
		BeamInfo(NoteUnit tail) {
			this.note = tail;
			this.isTail = true;
		}
		
		private int calculateNumOfBeams(NoteUnit note) {
			int beamsCalculation = ((int)note.getSpacingType())/4;
			int beams_log = 0;
			
			while (beamsCalculation > 1) {
				beams_log ++;
				beamsCalculation /= 2;
			}
			
			return beams_log;
		}
		
		public int getBeamsRight() {
			return this.beamsRight;
		}
		
		public int getBeamsLeft() {
			return this.beamsLeft;
		}
		
		public NoteUnit getNote() {
			return this.note;
		}
		
		public boolean getTail() {
			return this.isTail;
		}
		
		private void setToTail() {
			this.isTail = true;
		}
	}
	
	
    List<Integer> beamNumbers;
    List<BeamInfo> beamInfos;
    String beamStyle;
    
    public static void main(String[] args) {

    }

    public MeasureBeamData(List<NoteUnit> notes, int timeDenominator) {
        this.beamNumbers = new ArrayList<>();
        this.beamInfos = new ArrayList<>();
        double duration = 0.0;
        boolean beaming = false;
        int beamCount = 1;
        double baseBeatDuration = generateBeatDuration(timeDenominator);
        double cumulativeBeatDuration = generateBeatDuration(timeDenominator);

        for (int i = 0; i < notes.size(); i++) {
            NoteUnit currNote = notes.get(i);
            Note data = currNote.getData();

            if (!data.getGrace()) {
            	duration += this.calculateNoteDuration(currNote);
            }
            boolean notEligible = currNote.getSpacingType() < 8 || data.getRest() || data.getGrace();
            if (notEligible) {
                this.beamNumbers.add(0);
                this.beamInfos.add(null);
            }
            else if (i + 1 < notes.size() && (notes.get(i+1).getData().getRest() || (notes.get(i+1).getSpacingType() < 8 && !notes.get(i+1).getData().getGrace()))){
                if (beaming) {
                    this.beamNumbers.add(beamCount);
                    this.beamInfos.add(new BeamInfo(notes.get(i)));
                    beamCount++;
                    beaming = false;
                }
                else {
                    this.beamNumbers.add(0);
                    this.beamInfos.add(null);
                }
            }
            else if (i + 1 < notes.size() && notes.get(i+1).getData().getGrace()) {
                int temp = i + 2;
                while (notes.get(temp).getData().getGrace()) { temp++; }
                if (notes.get(temp).getSpacingType() < 8) {
                    if (beaming) {
                        this.beamNumbers.add(beamCount);
                        this.beamInfos.add(new BeamInfo(notes.get(i)));
                        beamCount++;
                        beaming = false;
                    }
                    else {
                        this.beamNumbers.add(0);
                        this.beamInfos.add(null);
                    } 
                }
                else if (duration >= cumulativeBeatDuration) {
                    while (duration >= cumulativeBeatDuration) {
                        cumulativeBeatDuration += baseBeatDuration;
                    }
                    if (beaming) {
                        this.beamNumbers.add(beamCount);
                        this.beamInfos.add(new BeamInfo(notes.get(i)));
                        beamCount++;
                        beaming = false;
                    }
                    else {
                        this.beamNumbers.add(0);
                        this.beamInfos.add(null);
                    }
                }
                else {
                    this.beamNumbers.add(beamCount);
                    this.beamInfos.add(new BeamInfo(currNote, notes.get(temp)));
                    beaming = true;
                }
            }
            else if (duration >= cumulativeBeatDuration) {
                while (duration >= cumulativeBeatDuration) {
                    cumulativeBeatDuration += baseBeatDuration;
                }
                if (beaming) {
                    this.beamNumbers.add(beamCount);
                    this.beamInfos.add(new BeamInfo(notes.get(i)));
                    beamCount++;
                    beaming = false;
                }
                else {
                    this.beamNumbers.add(0);
                    this.beamInfos.add(null);
                }
            }
            else if (i == notes.size()-1) {
                if (beaming) {
                    this.beamNumbers.add(beamCount);
                    this.beamInfos.add(new BeamInfo(notes.get(i)));
                }
                else {
                    this.beamNumbers.add(0);
                    this.beamInfos.add(null);
                }
            }
            else {
                this.beamNumbers.add(beamCount);
                this.beamInfos.add(new BeamInfo(currNote, notes.get(i+1)));
                beaming = true;
            }
            
            while (duration >= cumulativeBeatDuration) {
                cumulativeBeatDuration += baseBeatDuration;
            }

        }

        double firstPosFromTop = notes.get(0).getTranslateY();
        double lastPosFromTop = notes.get(notes.size()-1).getTranslateY();
        
        if (lastPosFromTop == firstPosFromTop) {
        	this.beamStyle = "flat";
        }
        else if (lastPosFromTop > firstPosFromTop) {
        	this.beamStyle = "up";
        }
        else {
        	this.beamStyle = "down";
        }

    }

    public List<Integer> getBeamNumbers() {
        return this.beamNumbers;
    }
    
    public List<BeamInfo> getBeamInfos() {
    	return this.beamInfos;
    }

    public String getBeamStyle() {
        return this.beamStyle;
    }


    public static double generateBeatDuration(int timeDenominator) {
        if (timeDenominator == 4)
            return 1.0/4.0;
        else if (timeDenominator == 8) 
            return 3.0/8.0;

        return 1.0/4.0;
    }

    public double calculateNoteDuration(NoteUnit note) {
        double duration = 1/note.getSpacingType();

        Note data = note.getData();

        double dotDuration = duration/2;
        for (int i = 0; i < data.getDot(); i++) {
            duration += dotDuration;
            dotDuration /= 2;
        }
        if (data.getTimeModification() != null) {
            duration *= ((double) data.getTimeModification().get("normal") / data.getTimeModification().get("actual"));
            System.out.println((double) data.getTimeModification().get("normal") / data.getTimeModification().get("actual"));
            duration += 0.0005;
        }
        return duration;
    }

    public void nullifyStyle(boolean isGuitarBass) {
        this.beamStyle = "flat";
    }
    
    public String toString() {
    	StringBuilder str = new StringBuilder();
    	
    	for (int i = 0; i < this.beamNumbers.size(); i++) {
    		str.append(this.beamNumbers.get(i) + ": \n");
    		if (this.beamInfos.get(i) != null) {
    			str.append("\tleft: " + this.beamInfos.get(i).getBeamsLeft() + ", \t");
        		str.append("right: " + this.beamInfos.get(i).getBeamsRight() + " !");
    		}
    		str.append("\n\n");
    	}
    	
    	return str.toString();
    }
}
