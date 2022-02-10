package custom_component_data_test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import custom_component_data.Instrument;


class InstrumentTest {

	@Test
	public void toStringTest()
	{
		String expected = "{\n"+"\tid: " + "ABC123" + ",\n"
		+ "\tname: " + "music1" + ",\n"
		+ "\tmidiChannel: " + 1 + ",\n"
		+ "\tmidiProgram: " + 2 + ",\n"
		+ "\tmidiUnpitched: " + 3 + ",\n"
		+ "\tvol: " + 3.5 + ",\n"
		+ "\tpan: " + 2.5 + ",\n"
		+ "}";
		
		Instrument in = new Instrument("ABC123","music1");
		in.setMidiChannel(1);
		in.setMidiProgram(2);
		in.setMidiUnpitched(3);
		in.setVolume(3.5);
		in.setPan(2.5);
		
		String actual = in.toString();
		
		assertEquals(expected, actual);
		
	}
	

}
