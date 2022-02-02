package component_data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Measure {
	int divisions;
	int fifths;
	int[] timeSignature;
	boolean timeDisplay;
	Clef clef;
	boolean tunedMeasure;
	int staffLines;
	HashMap<Integer, StaffTuning> tunings;
	List<Note> notes;
	boolean percussion;
	boolean tab;
	
	public Measure(Element measure, boolean firstMeasure) {
		try {
			Element attr = (Element) measure.getElementsByTagName("attributes").item(0);
			NodeList divisionsList = attr.getElementsByTagName("divisions");
			this.divisions = -1;
			if (divisionsList.getLength() > 0) {
				this.divisions = Integer.valueOf(divisionsList.item(0).getTextContent());
			}
			
			NodeList keyList = attr.getElementsByTagName("key");
			this.fifths = -1;
			if (keyList.getLength() > 0) {
				this.fifths = Integer.valueOf( ((Element) keyList.item(0)).getElementsByTagName("fifths").item(0).getTextContent());
			}
			
			if (firstMeasure) {
				this.timeSignature = new int[2];
				this.timeSignature[0] = 4;
				this.timeSignature[1] = 4;
			}
			NodeList timeList = attr.getElementsByTagName("time");
			if (timeList.getLength() > 0) {
				this.timeSignature = new int[2];
				this.timeSignature[0] = Integer.valueOf(((Element)timeList.item(0)).getElementsByTagName("beats").item(0).getTextContent());
				this.timeSignature[1] = Integer.valueOf(((Element)timeList.item(0)).getElementsByTagName("beat-type").item(0).getTextContent());
				this.timeDisplay = true;
			}
			
			NodeList clefList = attr.getElementsByTagName("clef");
			if (clefList.getLength() > 0) {
				Element clefEl = (Element) clefList.item(0);
				String clefSign = clefEl.getElementsByTagName("sign").item(0).getTextContent();
				if (clefSign.equals("percussion")) {
					this.percussion = true;
					this.tab = false;
					this.clef = new Clef('G', 2);
				}
				else if (clefSign.equals("TAB")){
					this.tab = true;
					this.percussion = false;
					this.clef = new Clef('G', 2);
				}
				else {
					this.percussion = false;
					this.tab = false;
					char symbol = clefSign.charAt(0);
					int line = -1;
					if (clefEl.getElementsByTagName("line").getLength() > 0) {
						line = Integer.valueOf(clefEl.getElementsByTagName("line").item(0).getTextContent());
					}
					this.clef = new Clef(symbol, line);
				}
			}
			
			
			this.staffLines = 5;
			NodeList staffList = attr.getElementsByTagName("staff-details");
			if (staffList.getLength() > 0) {
				Element staffEl = (Element) staffList.item(0);
				this.staffLines = Integer.valueOf(staffEl.getElementsByTagName("staff-lines").item(0).getTextContent());
				NodeList tuningList = staffEl.getElementsByTagName("staff-tuning");
				this.tunings = new HashMap<Integer, StaffTuning>();
				for (int i = 0; i < tuningList.getLength(); i++) {
					this.tunedMeasure = true;
					Element tuningEl = (Element) tuningList.item(0);
					Character step = tuningEl.getElementsByTagName("tuning-step").item(0).getTextContent().charAt(0);
					Integer octave = Integer.valueOf(tuningEl.getElementsByTagName("tuning-octave").item(0).getTextContent());
					this.tunings.put(Integer.valueOf(tuningEl.getAttribute("line")), new StaffTuning(step, octave));
				}
			}
			
			NodeList noteList = measure.getElementsByTagName("note");
			this.notes = new ArrayList<Note>();
			for (int i = 0; i < noteList.getLength(); i++) {
				Note toAdd = new Note((Element) noteList.item(i));
				this.notes.add(toAdd);
			}
			
		} catch (ClassCastException e) {
			System.out.println("Node passed is not a valid measure");
		}
	}

	public Measure(Element measure, Measure previous) {
		this(measure, false);
		if (this.divisions == -1) {
			this.divisions = previous.divisions;
		}
		if (this.fifths == -1) {
			this.fifths = previous.fifths;
		}
		if (this.timeSignature == null) {
			this.timeSignature = previous.timeSignature;
		}
		if (this.clef == null) {
			this.clef = previous.clef;
		}
		if (this.tunedMeasure == false) {
			this.tunedMeasure = previous.tunedMeasure;
		}
		
		this.staffLines = previous.staffLines;
		if (this.tunings == null) {
			this.tunings = previous.tunings;
		}
		
	}
}
