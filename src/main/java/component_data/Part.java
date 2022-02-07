package component_data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/*
 * Part Class which is initialized as an object by the Score Class
 * Note: Because we're only dealing with guitars, drums tablature it seems all Scores may have only 1 Part
 * 
 * Contains 4 elements:
 * 	id - a unique ID for this part
 *  name - a name for this part (usually "Drumset" or "Guitar" )
 *  instruments - used mainly for drums and contains a mapping of Instrument ID -->  Instrument object containing its data
 *  measures - A list of measures (essentially these are sections of music containing some number of beats worth of music
 */

public class Part {
	String id;
	String name;
	HashMap<String, Instrument> instruments;
	List<Measure> measures;
	
	public String getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public HashMap<String, Instrument> getInstruments() {
		return this.instruments;
	}
	
	public List<Measure> getMeasures() {
		return this.measures;
	}
	
	public Part(Node instruments, Node music) {
		Element instrumentsEl = null;
		try {
			// initialize the id and partname
			instrumentsEl = (Element) instruments;
			this.id = instrumentsEl.getAttribute("id");
			this.name = instrumentsEl.getElementsByTagName("part-name").item(0).getTextContent();
			
			// initialize the mapping of instruments as given by the <score-instrument> elements in musicXML input.
			NodeList instrumentLabels = instrumentsEl.getElementsByTagName("score-instrument");
			this.instruments = new HashMap<>();
			for (int i = 0; i < instrumentLabels.getLength(); i++) {
				String id = ((Element) instrumentLabels.item(i)).getAttribute("id");
				String name = ((Element) instrumentLabels.item(i)).getElementsByTagName("instrument-name").item(0).getTextContent();
				this.instruments.put(id, new Instrument(id, name));
			}
			
			// For each above initialized instrument extract more of its data from the <midi-instrument> elements
			NodeList instrumentData = instrumentsEl.getElementsByTagName("midi-instrument");
			for (int i = 0; i < instrumentData.getLength(); i++) {
				Element current = (Element) instrumentData.item(i);
				String id = current.getAttribute("id");
				
				NodeList midiCh = current.getElementsByTagName("midi-channel");
				NodeList midiPr = current.getElementsByTagName("midi-program");
				NodeList midiUnPitch = current.getElementsByTagName("midi-unpitched");
				NodeList vol = current.getElementsByTagName("volume");
				NodeList pan = current.getElementsByTagName("pan");
				
				// only initialize a certain data value of the instrument if its actually provided (prevents null assignment and errors)
				if (midiCh.getLength() > 0)
					this.instruments.get(id).setMidiChannel(Integer.valueOf(midiCh.item(0).getTextContent()));
				if (midiPr.getLength() > 0)
					this.instruments.get(id).setMidiProgram(Integer.valueOf(midiPr.item(0).getTextContent()));
				if (midiUnPitch.getLength() > 0)
					this.instruments.get(id).setMidiUnpitched(Integer.valueOf(midiUnPitch.item(0).getTextContent()));
				if (vol.getLength() > 0) 
					this.instruments.get(id).setVolume(Double.valueOf(vol.item(0).getTextContent()));
				if (pan.getLength() > 0)
					this.instruments.get(id).setPan(Double.valueOf(pan.item(0).getTextContent()));	
			}
		} catch (ClassCastException e) {
			System.out.println("Initializing Part Metadata: Attempting to Initialize Part with non DOM elements");
		}
		
		// Initialize the list of measures: Initialize the 1st measure with Constructor 1 and all subsequent measures with Constructor 2
		// because sometimes a measure can borrow data from previous constructors.
		Element musicEl = null;
		try {
			this.measures = new ArrayList<Measure>();
			musicEl = (Element) music;
			NodeList measures = musicEl.getElementsByTagName("measure");
			for (int i = 0; i < measures.getLength(); i++) {
				if (i == 0) {
					this.measures.add(new Measure((Element) measures.item(i), true));
				} else {
					this.measures.add(new Measure((Element) measures.item(i), this.measures.get(this.measures.size()-1)));				
				}
			}
		} catch (ClassCastException e) {
			System.out.println("Initializing Part Music: Attempting to Initialize Part with non DOM elements");
		}
	}
	

}
