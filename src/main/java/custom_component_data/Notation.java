package custom_component_data;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/*
 * A Note can have a Notation object representing some extra things to display about the note
 * 
 *  May have a List of Slur, Slide, Tied which are musical elements relating multiple notes together
 *  To learn more about them check their seperate classes or google them
 *  
 *  Guitar/String Instrument Important Fields:
 *  1. string: Which string is the corresponding Note played on.
 *  2. fret: Which fret (position on the neck of guitar) the Note should be played on.
 *  
 *  Ornament Class (not yet implemented) is literally just describing how to decorate the Note (adding extra lines on the note, etc..)
 */

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
		
		slur = new ArrayList<>();
		tied = new ArrayList<>();
		slide = new ArrayList<>();
		for (int i = 0; i < slurList.getLength(); i++) {
			this.slur.add(new Slur((Element) slurList.item(i)));
		}
		
		for (int i = 0; i < slideList.getLength(); i++) {
			this.slide.add(new Slide((Element) slideList.item(i)));
		}
		
		for (int i = 0; i < tiedList.getLength(); i++) {
			this.tied.add(new Tied((Element) tiedList.item(i)));
		}
		
		// Add data under the <technical> element such as "fret" or "string"
		NodeList technical = notation.getElementsByTagName("technical");
		if (technical.getLength() > 0) {
			Element technicalEl = (Element) technical.item(0);
			this.string = Integer.valueOf(technicalEl.getElementsByTagName("string").item(0).getTextContent());
			this.fret = Integer.valueOf(technicalEl.getElementsByTagName("fret").item(0).getTextContent());
			// Add the additional Technical Element data such as PullOff
		}
		
	}
	
	public List<Slur> getSlurs() {
		return this.slur;
	}
	
	public List<Slide> getSlides() {
		return this.slide;
	}
	
	public List<Tied> getTieds() {
		return this.tied;
	}
	
	public int getString() {
		return this.string;
	}
	
	public int getFret() {
		return this.fret;
	}
	
	public Ornament getOrnament() {
		return this.ornament;
	}

}
