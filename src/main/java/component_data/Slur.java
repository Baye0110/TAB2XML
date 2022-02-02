package component_data;

import org.w3c.dom.Element;

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
