package custom_component_data;

import org.w3c.dom.Element;

public class BarLine {
	
	String location;
	String style;
	String repeatType;
	Integer repeatNum;
	
	public BarLine(Element barLine) {
		this.location = barLine.getAttribute("location").equals("") ? null : barLine.getAttribute("loaction");
		this.style = barLine.getElementsByTagName("bar-style").getLength() > 0 ? ((Element) barLine.getElementsByTagName("bar-style").item(0)).getTextContent() : null;
		if (barLine.getElementsByTagName("repeat").getLength() > 0) {
			this.repeatType = ((Element)barLine.getElementsByTagName("repeat").item(0)).getAttribute("direction");
			String repeatNum = ((Element)barLine.getElementsByTagName("repeat").item(0)).getAttribute("times");
			this.repeatNum = repeatNum.equals("") ? null : Integer.parseInt(repeatNum);
		}
	}
	
	public String getStyle() {
		return this.style;
	}
	
	public String getLocation() {
		return this.location;
	}
	
	public String getRepeatType() {
		return this.repeatType;
	}
	
	public Integer getRepeatNum() {
		return this.repeatNum;
	}

}
