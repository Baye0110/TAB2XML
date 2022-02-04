package component_data;

import org.w3c.dom.Element;

/*
 *	Slur is a musical element, which along with Tied and Slide is related to multiple notes
 *
 *  Has 3 fields which are extracted as the XML attributes of <slur> element
 *  1. type: does the note this object correspond to "start" or "end" the tie
 *  2. number: Tracks the number of Slur elements
 *  3. placement: Will the <slur> element be displayed above/below the note
 */

public class Slur {
	int number;
	String type;
	String placement;
	
	public Slur(Element slur) {
		this.type = slur.getAttribute("type");
		this.number = Integer.valueOf(slur.getAttribute("number"));
		this.placement = slur.getAttribute("placement");		
	}

}
