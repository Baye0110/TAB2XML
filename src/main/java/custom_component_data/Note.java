package custom_component_data;

import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/*
 * Note Objects are contained in a Measure Object
 * 
 * Each Note has a bunch of values such as "pitch", "duration", "instrument", "octave", etc..
 * which are stored within this class. Further explanation of these elements are given next to the fields.
 */

public class Note {
	boolean pitched; // Is the instument pitched (Guitar - pitched, Drums - unpitched)
	char step; // Represents the Note value from 'A' to 'G'
	int octave; // Represents the octave this note belongs to 
	int alter; // Number representing Sum of Flat (-1) and Sharp (+1) keys for this note
	int duration; // Stores how long the note should be played (relative to the "beat-type" and "divisions" fields of the Measure)
	String instrumentID; // Stores the ID of the instrument to be played for this note (The instrument data can be recieved from Part Class)
	int voice; //I'll be honest, no clue what this does but it's in the MusicXML code
	int type; // 0 = breve note, 1 = whole note, 2 = half note, 4 = quarter note, 8 = eighth note, 16 = 16th note, ...
	int dot; // The number of "dots" associated with this note which extend its duration
	String stem; // Should the stem of the note (the verticle line) point up, down, or should it have no stem
 	String notehead; // The shape of the note: solid circle, white circle, cross
	boolean grace; // Is this a grace note? (A note which supports the one that comes after)
	boolean chord; // Does this note belong to a chord with previous notes
	boolean rest; // Is this a rest?
	HashMap<String, Integer> timeModification; // Sometimes a group of notes appear to be played for a certain duration, but actually are played differently
												// Has 2 mappings:
												//   1. "actual" --> integer representing actual time for note to be played
												//   2. "normal" --> integer representing the displayed time (on the sheet music) for note to be played
	Notation notation;  // Records extra details about the appearance of the note, the fret/string, any Slur/Tied/Slide
	String beamType; // Go to this link to understand: https://www.w3.org/2021/06/musicxml40/musicxml-reference/elements/beam/
	int beamNumber;	// Above link ^
	int position;  // Position of the Note above the bottom-most line of the staff
	
	/*
	 * Create a Note object by passing an Element which represents the <note> element in musicXML:
	 * Will parse the data and create a Note object with all the relevant instance variables initialized
	 */
	public Note(Element noteData) {
		
		// Initialize the Note's "step" and "octave" based on if it is unpitched or pitched
		NodeList unpitchedList = noteData.getElementsByTagName("unpitched");
		NodeList pitchList = noteData.getElementsByTagName("pitch");
		if (unpitchedList.getLength() > 0) {
			Element unpitchedEl = (Element) unpitchedList.item(0);
			this.pitched = false;
			this.step = unpitchedEl.getElementsByTagName("display-step").item(0).getTextContent().charAt(0);
			this.octave = Integer.valueOf(unpitchedEl.getElementsByTagName("display-octave").item(0).getTextContent());
		}
		else if (pitchList.getLength() > 0) {
			this.pitched = true;
			Element pitchEl = (Element) pitchList.item(0);
			this.step = pitchEl.getElementsByTagName("step").item(0).getTextContent().charAt(0);
			this.octave = Integer.valueOf(pitchEl.getElementsByTagName("octave").item(0).getTextContent());
			NodeList alterList  = pitchEl.getElementsByTagName("alter");
			this.alter = alterList.getLength() > 0 ? Integer.valueOf(alterList.item(0).getTextContent()) : 0;
		}
		
		// Set duration of the Note
		NodeList duration = noteData.getElementsByTagName("duration");
		if (duration.getLength() > 0) {
			this.duration = Integer.valueOf(duration.item(0).getTextContent());
		}
		
		// Set the instrument for the Note
		NodeList instrumentID = noteData.getElementsByTagName("instrument");
		if (instrumentID.getLength() > 0) {
			this.instrumentID = ((Element)instrumentID.item(0)).getAttribute("id");
		}
		
		// Set the voice for the Note
		NodeList voiceList = noteData.getElementsByTagName("voice");
		if (voiceList.getLength() > 0) {
			this.voice = Integer.valueOf(voiceList.item(0).getTextContent());
		}
		
		// Set the type (whole, half, quarter, 8th, 16th, ...) for the Note 
		NodeList typeList = noteData.getElementsByTagName("type");
		if (typeList.getLength() > 0) {
			String type = typeList.item(0).getTextContent();
			HashMap<String, Integer> typeNameMappings = new HashMap<>();
			typeNameMappings.put("eighth", 8);
			typeNameMappings.put("quarter", 4);
			typeNameMappings.put("half", 2);
			typeNameMappings.put("whole", 1);
			typeNameMappings.put("breve", 0);
			if (typeNameMappings.containsKey(type)) {
				this.type = typeNameMappings.get(type);
			}
			else {
				this.type = Integer.valueOf(type.substring(0, type.length()-2));
			}
		}
		
		
		// Initialize the stem value for the Note
		NodeList stemList = noteData.getElementsByTagName("stem");
		if (stemList.getLength() > 0) {
			this.stem = stemList.item(0).getTextContent();
		}
		
		// Initialize the notehead value for the Note
		NodeList noteheadList = noteData.getElementsByTagName("notehead");
		if (noteheadList.getLength() > 0) {
			this.notehead = noteheadList.item(0).getTextContent();
		}
		
		// Initialize 3 boolean values: Is this part of a chord? Is this a grace note? Is this a rest?
		this.chord = noteData.getElementsByTagName("chord").getLength() > 0 ? true : false;
		
		this.grace = noteData.getElementsByTagName("grace").getLength() > 0 ? true : false;
		
		this.rest = noteData.getElementsByTagName("rest").getLength() > 0 ? true : false;
		
		// Initialize number of dots associated with this Note
		this.dot = noteData.getElementsByTagName("dot").getLength();
		
		// Initalizes the timeModification of this Note
		NodeList timeModList = noteData.getElementsByTagName("time-modification");
		if (timeModList.getLength() > 0) {
			this.timeModification = new HashMap<String, Integer>();
			Element timeModEl = (Element) timeModList.item(0);
			this.timeModification.put("actual", Integer.valueOf(timeModEl.getElementsByTagName("actual-notes").item(0).getTextContent()));
			this.timeModification.put("normal", Integer.valueOf(timeModEl.getElementsByTagName("normal-notes").item(0).getTextContent()));
		}
		
		// Initializes any Notation for this Note
		NodeList notationList = noteData.getElementsByTagName("notations");
		if (notationList.getLength() > 0) {
			this.notation = new Notation((Element) notationList.item(0));
		}
		
		// Initializes the beam data for this note
		this.beamType = noteData.getElementsByTagName("beam").getLength() > 0 ? ((Element)noteData.getElementsByTagName("beam").item(0)).getTextContent() : null;
		this.beamNumber = this.beamType != null ? Integer.valueOf(((Element)noteData.getElementsByTagName("beam").item(0)).getAttribute("number")): 0;
	}
	
	
	public Notation getNotation() {
		return this.notation;
	}
	
	public String getBeamType() {
		return this.beamType;
	}
	
	public int getBeamNumber() {
		return this.beamNumber;
	}
	public int getVoice() {
		return this.voice;
	}
	
	public int getType() {
		return this.type;
	}
	
	public int getDot() {
		return this.dot;
	}
	
	public String getStem() {
		return this.stem;
	}
	
	public String getNotehead() {
		return this.notehead;
	}
	
	public boolean getGrace() {
		return this.grace;
	}
	
	public boolean getChord() {
		return this.chord;
	}
	
	public boolean getRest() {
		return this.rest;
	}
	
	public HashMap<String, Integer> getTimeModification() {
		return this.timeModification;
	}
	
	public boolean getPitched() {
		return this.pitched;
	}
	
	public char getStep() {
		return this.step;
	}
	
	public int getOctave() {
		return this.octave;
	}
	
	public int getAlter() {
		return this.alter;
	}
	
	public int getDuration() {
		return this.duration;
	}
	
	public String getInstrumentID() {
		return this.instrumentID;
	}
	
	public int getPosition() {
		return this.position;
	}
	/*
	 * toString() method for debugging purposes:
	 */
	public String toString() {
		String result = "{\n";
		result += "step: " + this.step + "\n";
		result += "octave: " + this.octave + "\n";
		result += "duration: " + this.duration + "\n";	
		result += "type: " + this.type + "\n";
		result += "}";
		return result;
	}
	
}
