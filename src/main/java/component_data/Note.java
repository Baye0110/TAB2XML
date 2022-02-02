package component_data;

import java.util.HashMap;

import org.w3c.dom.Element;

public class Note {
	boolean unpitched;
	char step;
	int octave;
	int alter;
	int position;
	int duration;
	Instrument instrument;
	int voice;
	int type;
	int dot;
	String stem;
	String notehead;
	boolean grace;
	boolean chord;
	boolean rest;
	HashMap<String, Integer> timeModification;
	int tuplet;
	
	public Note(Element item) {
		
	}
}
