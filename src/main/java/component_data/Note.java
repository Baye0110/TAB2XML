package component_data;

import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Note {
	boolean pitched; //
	char step; //
	int octave; //
	int alter; //
	int duration; //
	String instrumentID; //
	int voice; //
	int type; //
	int dot; //
	String stem; //
 	String notehead; //
	boolean grace; //
	boolean chord; //
	boolean rest; //
	HashMap<String, Integer> timeModification; // 
	Notation notation;
	String beam; //
	
	public String toString() {
		String result = "{\n";
		result += "step: " + this.step + "\n";
		result += "octave: " + this.octave + "\n";
		result += "duration: " + this.duration + "\n";	
		result += "type: " + this.type + "\n";
		result += "}";
		return result;
	}
	
	
	public Note(Element noteData) {
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
		
		
		NodeList duration = noteData.getElementsByTagName("duration");
		if (duration.getLength() > 0) {
			this.duration = Integer.valueOf(duration.item(0).getTextContent());
		}
		
		NodeList instrumentID = noteData.getElementsByTagName("instrument");
		if (instrumentID.getLength() > 0) {
			this.instrumentID = ((Element)instrumentID.item(0)).getAttribute("id");
		}
		
		NodeList voiceList = noteData.getElementsByTagName("voice");
		if (voiceList.getLength() > 0) {
			this.voice = Integer.valueOf(voiceList.item(0).getTextContent());
		}
		
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
		
		NodeList stemList = noteData.getElementsByTagName("stem");
		if (stemList.getLength() > 0) {
			this.stem = stemList.item(0).getTextContent();
		}
		
		NodeList noteheadList = noteData.getElementsByTagName("notehead");
		if (noteheadList.getLength() > 0) {
			this.notehead = noteheadList.item(0).getTextContent();
		}
		
		this.chord = noteData.getElementsByTagName("chord").getLength() > 0 ? true : false;
		
		this.grace = noteData.getElementsByTagName("grace").getLength() > 0 ? true : false;
		
		this.rest = noteData.getElementsByTagName("rest").getLength() > 0 ? true : false;
		
		this.dot = noteData.getElementsByTagName("dot").getLength();
		
		NodeList timeModList = noteData.getElementsByTagName("time-modification");
		if (timeModList.getLength() > 0) {
			this.timeModification = new HashMap<String, Integer>();
			Element timeModEl = (Element) timeModList.item(0);
			this.timeModification.put("actual", Integer.valueOf(timeModEl.getElementsByTagName("actual-notes").item(0).getTextContent()));
			this.timeModification.put("normal", Integer.valueOf(timeModEl.getElementsByTagName("normal-notes").item(0).getTextContent()));
		}
		
		NodeList notationList = noteData.getElementsByTagName("notation");
		if (notationList.getLength() > 0) {
			this.notation = new Notation((Element) notationList.item(0));
		}
		
		this.beam = noteData.getElementsByTagName("beam").getLength() > 0 ? noteData.getElementsByTagName("beam").item(0).getTextContent() : null;
	}
}
