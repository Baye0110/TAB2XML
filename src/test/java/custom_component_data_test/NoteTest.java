package custom_component_data_test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import custom_component_data.Score;

class NoteTest {

	
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
	public void noteTest1() {
		setUp("src/test/resources/system/demoDrumsSimple1.musicxml");
		char actual = score.getParts().get(0).getMeasures().get(0).getNotes().get(0).getStep();	//should be A
		assertEquals('A',actual);
	}
	
	@Test
	public void noteTest2() {
		setUp("src/test/resources/system/demoDrumsSimple1.musicxml");
		char actual = score.getParts().get(0).getMeasures().get(0).getNotes().get(1).getStep();	//The second Note in Measre "1" has F as its step
		assertEquals('F',actual);
	}
}
