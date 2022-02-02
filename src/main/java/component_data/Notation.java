package component_data;

import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Notation {
	List<Slur> slur;
	List<Tied> tied;
	List<Slide> slide;
	Ornament ornament; // to be completed
	int string;
	int fret;
		
	public Notation(Element notation) {
		NodeList slurList = notation.getElementsByTagName("slur");
		NodeList tiedList = notation.getElementsByTagName("tied");
		NodeList slideList = notation.getElementsByTagName("slide");
		
		for (int i = 0; i < slurList.getLength(); i++) {
			this.slur.add(new Slur((Element) slurList.item(i)));
		}
		
		for (int i = 0; i < slideList.getLength(); i++) {
			this.slide.add(new Slide((Element) slideList.item(i)));
		}
		
		for (int i = 0; i < tiedList.getLength(); i++) {
			this.tied.add(new Tied((Element) tiedList.item(i)));
		}
		
		NodeList technical = notation.getElementsByTagName("technical");
		if (technical.getLength() > 0) {
			Element technicalEl = (Element) technical.item(0);
			this.string = Integer.valueOf(technicalEl.getElementsByTagName("string").item(0).getTextContent());
			this.fret = Integer.valueOf(technicalEl.getElementsByTagName("fret").item(0).getTextContent());
			// Add the additional Technical Element data such as PullOff
		}
		
	}

}
