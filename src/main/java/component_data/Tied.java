package component_data;

import org.w3c.dom.Element;

public class Tied {
	String type;
	int number;
	String placement;
	
	public Tied(Element tied) {
		this.type = tied.getAttribute("type");
	}
}
