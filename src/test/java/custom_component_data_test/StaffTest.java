package custom_component_data_test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import custom_component_data.Score;
import custom_component_data.StaffTuning;

class StaffTest {

	Score score = null;
	
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
	void staffTest1() {
		setUp("src/test/resources/system/demoGuitarSimple1.musicxml");
		int staffLines = score.getParts().get(0).getMeasures().get(1).getStaffLines();
		
		assertTrue(staffLines == 6);
	}
	
	@Test
	void staffTest2() {
		setUp("src/test/resources/system/demoGuitarSimple1.musicxml");
		
		HashMap<Integer, StaffTuning> map = score.getParts().get(0).getMeasures().get(1).getTunings();
		
		char[] steps = {'D', 'A', 'D', 'G', 'B', 'E'};
		int[] octave = {2, 2, 3, 3, 3, 4};
		
		for (int i = 0; i < 6; i++) {
			assertTrue(steps[i] == map.get(Integer.valueOf(i+1)).getTuningStep(), (i+1)+"th step is incorrectly parsed.");
			assertTrue(octave[i] == map.get(Integer.valueOf(i+1)).getTuningOctave(), (i+1)+"th octave is incorrectly parsed.");
		}
	}

}
