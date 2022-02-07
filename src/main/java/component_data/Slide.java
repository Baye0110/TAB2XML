package component_data;

import org.w3c.dom.Element;

/*
 *	Slide is a musical element, which along with Tied and Slur is related to multiple notes
 *
 *  Has 3 fields which are extracted as the XML attributes of <slide> element
 *  1. type: does the note this object correspond to "start" or "end" the tie
 *  2. number: Tracks the number of Slide elements
 */

public class Slide {
	String type;
	int number;
	
	public Slide(Element slide) {
		this.type = slide.getAttribute("type");
		if (!slide.getAttribute("number").isEmpty()) {
			this.number = Integer.valueOf(slide.getAttribute("number"));
		}
	}
	
	public String getType() {
		return this.type;
	}
	
	public int getNumber() {
		return this.number;
	}
}
