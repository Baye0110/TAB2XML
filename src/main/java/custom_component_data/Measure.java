package custom_component_data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/*
 * Each Measure Object belongs to a Part Object
 * Note: Has 2 Constructor - one if used to create the 1st measure, and the other is for all subsequent measures.
 * 
 * Defines the rules regarding the Notes for this section of the music and
 * contains a list of the Notes to display
 */

public class Measure {
	int divisions; // the number of divisions of beat-type which determines what "1 unit of duration" means
	int fifths; // Represents the number of flats and sharps in the measure
	int[] timeSignature; // Contains the fraction that you see in the beginning of sheet music: timeSignature[0]/timeSignature[1]
	boolean timeDisplay; // Stores if this measure should explicitly state its timeSignature(fraction) on the sheet music
	Clef clef; // Stores the Clef data for this measure (Treble Clef, Bass Clef, TAB, percussion, etc.)
	boolean tunedMeasure; // Is this a tuned measure? (Is it a guitar/string instrument?)
	int staffLines; // How many lines should the Measure have - Default is 5 (changes for stringed instruments depending on number of strings)
	HashMap<Integer, StaffTuning> tunings; // Stores "staffLine" number of mappings 
									//Integer (which string) -->  StaffTuning (tuning details for this string)
	List<Note> notes; // List of Notes to be displayed during the Measure
	boolean percussion; // Is this a percussion instrument?
	boolean tab; // Is this a TAB clef (for guitar/string instruments)?
	
	public int getDivisions() {
		return this.divisions;
	}
	
	public int getFifths() {
		return this.fifths;
	}
	
	public int[] getTimeSignature() {
		return this.timeSignature;
	}
	
	public boolean getTimeDisplay() {
		return this.timeDisplay;
	}
	
	public Clef getClef() {
		return this.clef;
	}
	
	public boolean getTunedMeasure() {
		return this.tunedMeasure;
	}
	
	public int getStaffLines() {
		return this.staffLines;
	}
	
	public HashMap<Integer, StaffTuning> getTunings() {
		return this.tunings;
	}
	
	public List<Note> getNotes() {
		return this.notes;
	}
	
	public boolean getPercussion() {
		return this.percussion;
	}
	
	public boolean getTab() {
		return this.tab;
	}
	
	/*
	 * Constructor 1: Takes Element which represents data of <measure> element from musicXML and whether or not this is the first measure
	 */
	public Measure(Element measure, boolean firstMeasure) {
		try {
			// set up the non-Note related data first
			
			// Set up the divisions for this Measure
			Element attr = (Element) measure.getElementsByTagName("attributes").item(0);
			NodeList divisionsList = attr.getElementsByTagName("divisions");
			this.divisions = -1;
			if (divisionsList.getLength() > 0) {
				this.divisions = Integer.valueOf(divisionsList.item(0).getTextContent());
			}
			
			// Set up the fifths for this Measure
			NodeList keyList = attr.getElementsByTagName("key");
			this.fifths = -1;
			if (keyList.getLength() > 0) {
				this.fifths = Integer.valueOf( ((Element) keyList.item(0)).getElementsByTagName("fifths").item(0).getTextContent());
			}
			
			// Set up the time signature for this Measure: If there is none specified, default to 4/4
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
			
			// Set up the clef of this measure based on if the Clef is "Tab", "percussion" or some other note-based clef such as Treble
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
			
			
			// Set up the staffLines and tuning for each staff line which represent the strings, if this is a string instrument like a guitar
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
			
			// Create the Note objects for this Measure
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

	/*
	 * Constructor 2: Is used for all measures beyond the first
	 * Main purpose is to use the value of the previous measure if they are not specified for the current one explicitly.
	 */
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
	
	public void generatePositions() {
		for (Note note: this.notes) {
			if (this.clef.symbol == 'G') {
				int measureBottom = (this.clef.symbol - 'C') % 7 + 4*7 - (this.clef.line-1)*2;
				int notePosition = (note.step - 'C') % 7 + note.octave*7;
				if (note.step - 'C' < 0) 
					notePosition += 7;
				note.position = notePosition - measureBottom;
			}
		}
			
	}
	
}
