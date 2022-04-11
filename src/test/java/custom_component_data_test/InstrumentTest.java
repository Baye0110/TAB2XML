package custom_component_data_test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import custom_component_data.Instrument;
import custom_component_data.Score;


class InstrumentTest {
	
	private Score score = null;
	
	private void setUp(String location) {
		 // Get the file
        File in = new File(location);
        String build = "";

        // Set up Scanner to use File Input (If invalid file then no Score object will be created
        Scanner input = null;
        try {
            input = new Scanner(in);
            input.useDelimiter("\n");
            
            // Put the entire file in String "build"
            while(input.hasNext()) {
                build += input.next() + "\n";
            }
            score = new Score(build);

        } 
        catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        
	}

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
		setUp("src/test/resources/system/demoDrumsComplex1.musicxml");
		int numInstrument = 0;
		for(int i = 0; i < score.getParts().get(0).getInstruments().size(); i++) {
			numInstrument++;
		}
		assertEquals(13, numInstrument);
		Instrument expected1 = new Instrument("P1-I45", "Pedal Hi-Hat");
		Instrument actual1 = score.getParts().get(0).getInstruments().get("P1-I45");
		Assertions.assertTrue(expected1.getId().equals(actual1.getId()));
		Assertions.assertTrue(expected1.getName().equals(actual1.getName()));
	}
	
	
	@Test
	public void InstrumentTest2() {
		setUp("src/test/resources/system/demoGuitarSimple2.musicxml");
		int numInstrument = 0;
		for(int i = 0; i < score.getParts().get(0).getInstruments().size(); i++) {
			numInstrument++;
		}
		assertEquals(0, numInstrument);
		Instrument expected1 = null;
		Instrument actual1 = score.getParts().get(0).getInstruments().get("");
		Assertions.assertEquals(expected1, actual1);
	}
	
	@Test
	public void TestGetID() {
		setUp("src/test/resources/system/demoDrumsComplex1.musicxml");
		String actualID1 = score.getParts().get(0).getId();
		String expectID1 = "P1";
		Assertions.assertEquals(expectID1, actualID1);
		
		String actualID2 = score.getParts().get(0).getMeasures().get(0).getNotes().get(0).getInstrumentID();
		String expectID2 = "P1-I50";
		Assertions.assertEquals(expectID2, actualID2);
	}
	
	@Test
	public void TestGetName() {
		setUp("src/test/resources/system/demoDrumsComplex1.musicxml");
		String actualName1 = score.getParts().get(0).getName();
		String expectName1 = "Drumset";
		Assertions.assertEquals(expectName1, actualName1);	
	}
	

	@Test
	public void TestgetMidiChannel() {
		setUp("src/test/resources/system/demoDrumsComplex1.musicxml");
		int actual = score.getParts().get(0).getInstruments().get("P1-I46").getMidiChannel();
		int expect = 10;
		Assertions.assertEquals(expect, actual);	
	}
	
	@Test
	public void TestgetMidiProgram() {
		setUp("src/test/resources/system/demoDrumsComplex1.musicxml");
		int actual = score.getParts().get(0).getInstruments().get("P1-I46").getMidiProgram();
		int expect = 1;
		Assertions.assertEquals(expect, actual);	
	}
	
	@Test
	public void TestgetMidiUnpitched() {
		setUp("src/test/resources/system/demoDrumsComplex1.musicxml");
		int actual = score.getParts().get(0).getInstruments().get("P1-I46").getMidiUnpitched();
		int expect = 46;
		Assertions.assertEquals(expect, actual);	
	}
	
	@Test
	public void TestgetVolume() {
		setUp("src/test/resources/system/demoDrumsComplex1.musicxml");
		double actual = score.getParts().get(0).getInstruments().get("P1-I46").getVolume();
		double expect = 78.7402;
		Assertions.assertEquals(expect, actual);	
	}
	
	@Test
	public void TestgetPan() {
		setUp("src/test/resources/system/demoDrumsComplex1.musicxml");
		double actual = score.getParts().get(0).getInstruments().get("P1-I46").getPan();
		double expect = 0;
		Assertions.assertEquals(expect, actual);
	}
	
	@Test
	public void TestAllSetter() {
		Instrument actual = new Instrument("P1-I45", "Pedal Hi-Hat");
		actual.setMidiChannel(10);
		actual.setMidiProgram(1);
		actual.setMidiUnpitched(44);
		actual.setPan(0);
		actual.setVolume(8.7402);
		
		int expectMidiChanel = 10;
		int expectMidiProgram = 1;
		int expectMidiUnipitched = 44;
		double expectedPan = 0;
		double expectedVolume = 8.7402;
		
		Assertions.assertEquals(expectMidiChanel, actual.getMidiChannel());
		Assertions.assertEquals(expectMidiProgram, actual.getMidiProgram());
		Assertions.assertEquals(expectMidiUnipitched, actual.getMidiUnpitched());
		Assertions.assertEquals(expectedPan, actual.getPan());
		Assertions.assertEquals(expectedVolume, actual.getVolume());
	}

}
