package custom_component_data;

import org.w3c.dom.Element;

public class HammerOn {
	Integer number;
	String type;
	String symbol;
	
	public HammerOn(Element hammerOn) {
		this.type = hammerOn.getAttribute("type");
		this.number = !hammerOn.getAttribute("number").equals("") ? Integer.parseInt(hammerOn.getAttribute("number")) : null;
		this.symbol = !hammerOn.getTextContent().equals("") ? hammerOn.getTextContent() : null;
		
	}
	
	public Integer getNumber() {
		return this.number;
	}
	
	public String type() {
		return this.type;
	}
	
	public String symbol() {
		return this.symbol;
	}
}
