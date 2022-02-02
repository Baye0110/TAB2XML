package component_data;

import org.w3c.dom.Element;

public class Slide {
	String type;
	int number;
	
	public Slide(Element slide) {
		this.type = slide.getAttribute("type");
		if (!slide.getAttribute("number").isEmpty()) {
			this.number = Integer.valueOf(slide.getAttribute("number"));
		}
	}
}
