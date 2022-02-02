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

}
