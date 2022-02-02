package component_data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Part {
	String id;
	String name;
	HashMap<String, Instrument> instruments;
	List<Measure> measures;
	
	public Part(Node instruments, Node music) {
		Element instrumentsEl = null;
		try {
			instrumentsEl = (Element) instruments;
			this.id = instrumentsEl.getAttribute("id");
			this.name = instrumentsEl.getElementsByTagName("part-name").item(0).getTextContent();
			NodeList instrumentLabels = instrumentsEl.getElementsByTagName("score-instrument");
			this.instruments = new HashMap<>();
			for (int i = 0; i < instrumentLabels.getLength(); i++) {
				String id = ((Element) instrumentLabels.item(i)).getAttribute("id");
				String name = ((Element) instrumentLabels.item(i)).getElementsByTagName("instrument-name").item(0).getTextContent();
				this.instruments.put(id, new Instrument(id, name));
			}
			NodeList instrumentData = instrumentsEl.getElementsByTagName("midi-instrument");
			for (int i = 0; i < instrumentData.getLength(); i++) {
				Element current = (Element) instrumentData.item(i);
				String id = current.getAttribute("id");
				
				NodeList midiCh = current.getElementsByTagName("midi-channel");
				NodeList midiPr = current.getElementsByTagName("midi-program");
				NodeList midiUnPitch = current.getElementsByTagName("midi-unpitched");
				NodeList vol = current.getElementsByTagName("volume");
				NodeList pan = current.getElementsByTagName("pan");
				
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
