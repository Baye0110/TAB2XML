package custom_component_data_test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import custom_component_data.Note;
import custom_component_data.Score;
import custom_component_data.Tied;

class TiedTest {
	
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
	public void tiedTest1() {
		setUp("src/test/resources/system/demoDrumsSimple1.musicxml");
		Tied note1 = score.getParts().get(0).getMeasures().get(0).getNotes().get(11).getNotation().getTieds().get(0);
		Assertions.assertNotNull(note1);
		Assertions.assertTrue(note1.getType().equals("start"));
		Assertions.assertTrue(note1.getNumber() == 0);
		Assertions.assertTrue(note1.getPlacement().equals(""));
		
		Tied note2 = score.getParts().get(0).getMeasures().get(0).getNotes().get(12).getNotation().getTieds().get(0);
		Assertions.assertTrue(note2.getType().equals("start"));
		Assertions.assertTrue(note2.getNumber() == 0);
		Assertions.assertTrue(note2.getPlacement().equals(""));
			
	}
	
	@Test
	public void tiedTest2() {
		setUp("src/test/resources/system/demoDrumsSimple1.musicxml");
		Tied note1 = score.getParts().get(0).getMeasures().get(1).getNotes().get(0).getNotation().getTieds().get(0);
		Assertions.assertTrue(note1.getType().equals("stop"));
		Assertions.assertTrue(note1.getNumber() == 0);
		Assertions.assertTrue(note1.getPlacement().equals(""));
		
		Tied note2 = score.getParts().get(0).getMeasures().get(1).getNotes().get(1).getNotation().getTieds().get(0);
		Assertions.assertTrue(note2.getType().equals("stop"));
		Assertions.assertTrue(note2.getNumber() == 0);
		Assertions.assertTrue(note2.getPlacement().equals(""));
	}
	
	
	

}
