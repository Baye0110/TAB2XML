package custom_component_data_test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import custom_component_data.Instrument;
import custom_component_data.Score;


class InstrumentTest {

	// Test toString method in Intrument.java
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
		
		// assertEquals (expected, actual, epsilon) or without epsilon
		assertEquals(expected, actual);
		
	}
	
	@Test
	public void InstrumentTest1() {
		Score scoreSheet = new Score("ex38.txt");
		Instrument expected = null;
		Instrument actual = scoreSheet.getParts().get(0).getInstruments().get("");
		assertEquals(expected, actual);
	}
	
	
	@Test
	public void InstrumentTest2() {
		Score scoreSheet = new Score("parabola.txt");
		Instrument expected = new Instrument("P1-I45", "Pedal Hi-hat");
		Instrument actual = scoreSheet.getParts().get(0).getInstruments().get("P1-I45");
		Assertions.assertTrue(expected.getName().equals(actual.getName()), "The expected instrument name is " + expected.getName() + "while the actual instruent name is " + actual.getName());
		Assertions.assertTrue(expected.getId().equals(actual.getId()), "The expected instrument id is " + expected.getId() + "while the actual instruent id is " + actual.getId());
	}
	

}
