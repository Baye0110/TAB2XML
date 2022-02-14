package custom_component_data;

import org.w3c.dom.Element;

/*
 *	Tied is a musical element, which along with Slur and Slide is related to multiple notes
 *
 *  Has 3 fields which are extracted as the XML attributes of <tied> element
 *  1. type: does the note this object correspond to "start" or "end" the tie
 *  2. number: Tracks the number of Tied elements
 *  3. placement: Will the <tied> element be displayed above/below the note
 */

public class Tied {
	String type;
	Integer number;
	String placement;
	
	public Tied(Element tied) {
		this.type = tied.getAttribute("type");
		this.number = !tied.getAttribute("number").equals("") ? Integer.valueOf(tied.getAttribute("number")): 0;
		this.placement = tied.getAttribute("placement");
	}
	
	public int getNumber() {
		if (this.number == null)
			return 0;
		else 
			return this.number;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String getPlacement() {
		return this.placement;
	}
}
