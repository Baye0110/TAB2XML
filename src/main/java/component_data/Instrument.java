package component_data;

/* (Important for playing the music, not as much for displaying music)
 * Used usually to represent the different parts of a drum
 * 
 * The Part Class stores a list of all Instruments should be in the music
 * The Note Class may have a corresponding Instrument ID stored which represents the Instrument to be played by that Note
 * 
 * Stores information of the Instrument id, name, and data for playing the music 
 */

public class Instrument {
	String id;
	String name;
	int midiChannel;
	int midiProgram;
	int midiUnpitched;
	double volume;
	double pan;
	
	
	public Instrument(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public void setMidiChannel(int channel) {
		this.midiChannel = channel;
	}
	
	public void setMidiProgram(int program) {
		this.midiProgram = program;
	}
	
	public void setMidiUnpitched(int unpitched) {
		this.midiUnpitched = unpitched;
	}
	
	public void setVolume(double vol) {
		this.volume = vol;
	}
	
	public void setPan(double pan) {
		this.pan= pan;
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getMidiChannel() {
		return this.midiChannel;
	}
	
	public int getMidiProgram() {
		return this.midiProgram;
	}
	
	public int getMidiUnpitched() {
		return this.midiUnpitched;
	}
	
	public double getVolume() {
		return this.volume;
	}
	
	public double getPan() {
		return this.pan;
	}
	
	public String toString(){
		String result = "{\n";
		result += "\tid: " + this.id + ",\n";
		result += "\tname: " + this.name + ",\n";
		result += "\tmidiChannel: " + this.midiChannel + ",\n";
		result += "\tmidiProgram: " + this.midiProgram + ",\n";
		result += "\tmidiUnpitched: " + this.midiUnpitched + ",\n";
		result += "\tvol: " + this.volume + ",\n";
		result += "\tpan: " + this.pan + ",\n";
		result += "}";
		return result;
	}

}
