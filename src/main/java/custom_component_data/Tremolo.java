package custom_component_data;

import org.w3c.dom.Element;

public class Tremolo {
	int number;
	String type;
	
	public Tremolo(Element tremolo) {
		this.number = Integer.parseInt(tremolo.getTextContent());
		this.type = !tremolo.getAttribute("type").equals("") ? tremolo.getAttribute("type") : null;
	}
	
	public int getNumber() {
		return this.number;
	}
	
	public String getType() {
		return this.type;
	}

}
