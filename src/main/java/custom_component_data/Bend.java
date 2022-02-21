package custom_component_data;

import org.w3c.dom.Element;

public class Bend {
	double bendAlter;
	String shape;
	
	public Bend(Element bend) {
		this.bendAlter = Double.valueOf(bend.getTextContent());
		this.shape = !bend.getAttribute("shape").equals("") ? bend.getAttribute("shape") : null;
	}
	
	public double getBendAlter() {
		return this.bendAlter;
	}
	
	public String getShape() {
		return this.shape;
	}
}
