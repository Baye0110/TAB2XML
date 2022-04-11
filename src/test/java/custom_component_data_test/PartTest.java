package custom_component_data_test;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import custom_component_data.Score;

class PartTest {
	
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

	@Test
	public void partTest1() {
		setUp("src/test/resources/system/demoDrumsSimple1.musicxml");
		int partCount = 0;
		for(int i = 0; i < score.getParts().size(); i++) {
			partCount++;
		}
		Assertions.assertEquals(1, partCount);
		Assertions.assertTrue(score.getParts().get(0).getId().equals("P1"));
		Assertions.assertTrue(score.getParts().get(0).getName().equals("Drumset"));
		
		int measureCount = 0;
		for(int i = 0; i < score.getParts().get(0).getMeasures().size(); i++) {
			measureCount++;
		}
		Assertions.assertEquals(3, measureCount);
		
		int instrumentCount = 0;
		for(int i = 0; i < score.getParts().get(0).getInstruments().size(); i++) {
			instrumentCount++;
		}
		Assertions.assertEquals(13, instrumentCount);
	}
	
	@Test
	public void partTest2() {
		setUp("src/test/resources/system/demoBassSimple1.musicxml");
		int partCount = 0;
		for(int i = 0; i < score.getParts().size(); i++) {
			partCount++;
		}
		Assertions.assertEquals(1, partCount);
		Assertions.assertTrue(score.getParts().get(0).getId().equals("P1"));
		Assertions.assertFalse(score.getParts().get(0).getName().equals("Drumset"));
		Assertions.assertFalse(score.getParts().get(0).getName().equals("Lololol"));
		Assertions.assertTrue(score.getParts().get(0).getName().equals("Bass"));
		
		int measureCount = 0;
		for(int i = 0; i < score.getParts().get(0).getMeasures().size(); i++) {
			measureCount++;
		}
		Assertions.assertEquals(4, measureCount);
		
		int instrumentCount = 0;
		for(int i = 0; i < score.getParts().get(0).getInstruments().size(); i++) {
			instrumentCount++;
		}
		Assertions.assertEquals(0, instrumentCount);
	}
	
	
	@Test
	public void partTest3()
	{
		setUp("src/test/resources/system/demoDrumsSimple2.musicxml");
		assertNotNull(score.getParts());
		assertEquals("P1",score.getParts().get(0).getId());
		assertEquals("Drumset",score.getParts().get(0).getName());
	}
	
	@Test
	public void partTest4()
	{
		setUp("src/test/resources/system/demoDrumsSimple3.musicxml");
		assertNotNull(score.getParts());
		assertEquals("Drumset",score.getParts().get(0).getName());
		assertEquals("P1",score.getParts().get(0).getId());
	}
	
	@Test
	void TestgetInstruments() {
		setUp("src/test/resources/system/demoDrumsComplex1.musicxml");
		int actualNumOfInstrument =score.getParts().get(0).getInstruments().size();
		int expectNumberOfInstrument = 13;
		assertEquals(actualNumOfInstrument,expectNumberOfInstrument);
	}
	
	@Test
	void TestgetMeasures() {
		setUp("src/test/resources/system/demoDrumsComplex1.musicxml");
		int actualNumOfMeasures =score.getParts().get(0).getMeasures().size();
		int expectNumberOfMeasures = 126;
		assertEquals(actualNumOfMeasures,expectNumberOfMeasures);
	}

}
