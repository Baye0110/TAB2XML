package custom_component_data_test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import custom_component_data.Score;

class StaffTest {
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
	public void staffTest1() {
		setUp("\"src/test/resources/system/demoGuitarSimple1.musicxml\"");
		int sl = score.getParts().get(0).getMeasures().get(0).getStaffLines();
		assertEquals(6,sl);
	}
	
	@Test
	public void staffTest2() {
		setUp("\"src/test/resources/system/demoGuitarSimple1.musicxml\"");
		assertNotNull(score.getParts().get(0).getMeasures().get(0).getTunings());
	}

}
