package custom_component_data;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Technical {
	Bend bend;
	List<HammerOn> hammerOns;
	List<PullOff> pullOffs;
	String harmonic;
	
	public Technical(Element technical) {
		if (technical.getElementsByTagName("bend").getLength() > 0)
			this.bend = new Bend((Element) technical.getElementsByTagName("bend").item(0));
		
		NodeList hammerOnList = technical.getElementsByTagName("hammer-on");
		if (hammerOnList.getLength() > 0) {
			this.hammerOns = new ArrayList<>();
			for (int i = 0; i < hammerOnList.getLength(); i++) {
				this.hammerOns.add(new HammerOn((Element) hammerOnList.item(i)));
			}
		}
		
		NodeList pullOffList = technical.getElementsByTagName("pull-off");
		if (pullOffList.getLength() > 0) {
			this.pullOffs = new ArrayList<>();
			for (int i = 0; i < pullOffList.getLength(); i++) {
				this.pullOffs.add(new PullOff((Element) pullOffList.item(i)));
			}
		}
		
		if (technical.getElementsByTagName("harmonic").getLength() > 0) {
			Element harmonic = (Element) technical.getElementsByTagName("harmonic").item(0);

			if (harmonic.getElementsByTagName("natural").getLength() > 0)
				this.harmonic = "natural";
			else if (harmonic.getElementsByTagName("artificial").getLength() > 0)
				this.harmonic = "artificial";
			else
				this.harmonic = "none";
		}
	}
	
	public Bend getBend() {
		return this.bend;
	}
	
	public List<HammerOn> getHammerOns() { 
		return this.hammerOns;
	}
	
	public List<PullOff> getPullOff() { 
		return this.pullOffs;
	}

	public String getHarmonic() {
		return this.harmonic;
	}
}
