package custom_component_data;

/*(Important for Playing Music - Not as much for displaying music)
 * 
 * StaffTuning Class stores the tuning for stringed instruments:
 * 
 * For a string instrument (like guitar) each line on the music represents a string and each string must be tuned:
 * 
 * Stores 2 elements to indicate the tuning of a string:
 * 1. tuningOctave = the octave
 * 2. tuningStep = a letter from 'A' to 'G' representing the exact pitch on the specified octave
 */

public class StaffTuning {
	char tuningStep;
	int tuningOctave;

	public StaffTuning(Character step, Integer octave) {
		this.tuningStep = step;
		this.tuningOctave = octave;
	}
	
	public char getTuningStep() {
		return this.tuningStep;
	}
	
	public int getTuningOctave() {
		return this.tuningOctave;
	}

}
