package custom_component_data_test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import custom_component_data.Measure;
import custom_component_data.Score;

//Testing Measure.java in component_data
class MeasureTest {
	
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
	public void testMeasure1() {
		setUp("src/test/resources/system/demoDrumsSimple1.musicxml");
		Measure m = score.getParts().get(0).getMeasures().get(0);	//measure number 1
		assertNotNull(m);
		
		int d = m.getDivisions(); //should be 16
		assertEquals(16,d);
		
		int f = m.getFifths();	//should be zero
		assertEquals(0,f);
	}
	
	@Test
	public void testMeasure2() {
		setUp("src/test/resources/system/demoDrumsSimple1.musicxml");
		Measure m = score.getParts().get(0).getMeasures().get(1);	//measure number 2
		assertNotNull(m);
		
		int d = m.getDivisions(); //should be 16
		assertEquals(16,d);
	}
}
