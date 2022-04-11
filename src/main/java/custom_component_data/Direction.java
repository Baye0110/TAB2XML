package custom_component_data;

import org.w3c.dom.Element;

public class Direction {
	
	String placement;
	Double[] position;
	String words;
	
	public String getPlacement() {
		return this.placement;
	}
	
	public Double[] getPosition() {
		return this.position;
	}
	
	public String getWords() {
		return this.words;
	}
	
	public Direction(Element direction) {
		this.placement = direction.getAttribute("placement").equals("") ? null : direction.getAttribute("placement");
		
		this.position = new Double[2];
		this.position[0] = direction.getElementsByTagName("words").getLength() > 0 ? Double.parseDouble(((Element)direction.getElementsByTagName("words").item(0)).getAttribute("relative-x")) : null;
		this.position[1] = this.position[0] != null ? Double.parseDouble(((Element)direction.getElementsByTagName("words").item(0)).getAttribute("relative-y")) : null;
		
		this.words = this.position[0] != null ?  ((Element)direction.getElementsByTagName("words").item(0)).getTextContent() : null;
	}

}
