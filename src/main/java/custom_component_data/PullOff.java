package custom_component_data;

import org.w3c.dom.Element;

public class PullOff {
	Integer number;
	String type;
	String symbol;
	
	public PullOff(Element pullOff) {	
		this.type = pullOff.getAttribute("type");
		this.number = !pullOff.getAttribute("number").equals("") ? Integer.valueOf(pullOff.getAttribute("number")): null;
		this.symbol = !pullOff.getTextContent().equals("") ? pullOff.getTextContent(): null;
	}
	
	public Integer getNumber() {
		return this.number;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String getSymbol() {
		return this.symbol;
	}
}
