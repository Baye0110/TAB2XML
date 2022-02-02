package component_data;

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
	
	public Measure(Element measure) {
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
			
			NodeList timeList = attr.getElementsByTagName("time");
			if (timeList.getLength() > 0) {
				this.timeSignature = new int[2];
				this.timeSignature[0] = Integer.valueOf(timeList.item(0).getFirstChild().getTextContent());
				this.timeSignature[1] = Integer.valueOf(timeList.item(0).getLastChild().getTextContent());
				this.timeDisplay = true;
			}
			
			NodeList clefList = attr.getElementsByTagName("clef");
			if (clefList.getLength() > 0) {
				Element clefEl = (Element) clefList.item(0);
				char symbol = (clefEl.getFirstChild().getTextContent()).charAt(0);
				int line = -1;
				if (clefEl.getElementsByTagName("line").getLength() > 0) {
					line = Integer.valueOf(clefEl.getElementsByTagName("line").item(0).getTextContent());
				}
				this.clef = new Clef(symbol, line);
			}
			
			
			this.staffLines = 5;
			NodeList staffList = attr.getElementsByTagName("staff-details");
			if (staffList.getLength() > 0) {
				Element staffEl = (Element) staffList.item(0);
				this.staffLines = Integer.valueOf(staffEl.getElementsByTagName("staff-lines").item(0).getTextContent());
				NodeList tuningList = staffEl.getElementsByTagName("staff-tuning");
				for (int i = 0; i < tuningList.getLength(); i++) {
					this.tunedMeasure = true;
					Character step = tuningList.item(i).getFirstChild().getTextContent().charAt(0);
					Integer octave = Integer.valueOf(tuningList.item(i).getLastChild().getTextContent());
					this.tunings.put(Integer.valueOf(((Element)tuningList.item(i)).getAttribute("line")), new StaffTuning(step, octave));
				}
			}
			
			NodeList noteList = measure.getElementsByTagName("note");
			for (int i = 0; i < noteList.getLength(); i++) {
				this.notes.add(new Note((Element) noteList.item(i)));
			}
			
		} catch (ClassCastException e) {
			System.out.println("Node passed is not a valid measure");
		}
	}

	public Measure(Element measure, Measure previous) {
		this(measure);
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
