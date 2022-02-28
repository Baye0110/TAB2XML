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
import custom_component_data.Slur;
import custom_component_data.Tied;

class SlurTest {
	
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
	public void Slurtest1() {
		setUp("src/test/resources/system/demoDrumsSimple1.musicxml");
		int noteCountM1 = 0;
		for(int i = 0; i < score.getParts().get(0).getMeasures().get(0).getNotes().size(); i++) {
			noteCountM1++;
		}
		// this test confirms there are 13 notes in 1st measure 
		Assertions.assertTrue(noteCountM1 == 13);
		// this test confirms there exists a tied note at index 11 and 12 of the note list of the 1st measure
		Tied note12 = score.getParts().get(0).getMeasures().get(0).getNotes().get(11).getNotation().getTieds().get(0);
		Assertions.assertNotNull(note12);
		Tied note13 = score.getParts().get(0).getMeasures().get(0).getNotes().get(12).getNotation().getTieds().get(0);
		Assertions.assertNotNull(note13);
		/*
		 *  Since there aren't any slur note in this xml, this test expects to return null for 
		 *  all slur objects at all notes index. However, only index 0 -> 10 returns the desired result
		 */
		Assertions.assertThrows(NullPointerException.class, () -> {
			for(int i = 0; i < score.getParts().get(0).getMeasures().get(0).getNotes().size()-2; i++) {
				Slur note1to11 = score.getParts().get(0).getMeasures().get(0).getNotes().get(i).getNotation().getSlurs().get(0);
			}
		});
		
		/*
		 *  At index 11, test will start throwing an index out of bound exception when there are 12 indexes in total???
		 *  Note that at this index and index 12, 2 tied notes exist. Does this mean if there 
		 *  already exists another type of list (tied, slide) at a specific index => the slur list for 
		 *  this note will be removed???
		 */
		Assertions.assertThrows(NullPointerException.class, () ->{
			Slur note12test = score.getParts().get(0).getMeasures().get(0).getNotes().get(11).getNotation().getSlurs().get(0);
		});
	}
	
	
	@Test
	public void SlurTest2() {
		setUp("src/test/resources/system/demoGuitarComplex1.musicxml");
		Slur note1 = score.getParts().get(0).getMeasures().get(1).getNotes().get(0).getNotation().getSlurs().get(0);
		Assertions.assertTrue(note1.getType().equals("start"));
		Assertions.assertTrue(note1.getPlacement().equals("above"));
		Assertions.assertTrue(note1.getNumber() == 2);
		
		
		Slur note2 = score.getParts().get(0).getMeasures().get(1).getNotes().get(1).getNotation().getSlurs().get(0);
		Assertions.assertTrue(note2.getType().equals("stop"));
		Assertions.assertFalse(note2.getNumber() == 0);
		Assertions.assertTrue(note2.getNumber() == 2);
		Assertions.assertTrue(note2.getPlacement().equals(""));
	}
	
	

}
