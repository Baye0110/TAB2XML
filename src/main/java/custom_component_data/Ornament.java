package custom_component_data;

import org.w3c.dom.Element;

/* !! Incomplete Class !!
 * 
 * This Class should include data regarding the <ornament> musicXML:
 * 
 * The one must have element is the "tremelo", others may also be added though
 * 
 */
public class Ornament {
	Tremolo tremolo;
	
	public Ornament(Element ornaments) {
		if (ornaments.getElementsByTagName("tremolo").getLength() > 0) {
			this.tremolo = new Tremolo((Element) ornaments.getElementsByTagName("tremolo").item(0));
		}
	}
	
	public Tremolo getTremolo() {
		return this.tremolo;
	}
}
