package component_data;

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
