package custom_component_data_test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;

import custom_component_data.Score;
import custom_component_data.Slide;
import custom_component_data.Slur;
import custom_component_data.Tied;

class SlideTest {
	
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
	public void SlideTest1() {
		setUp("src/test/resources/system/demoGuitarSimple1.musicxml");
		int string = score.getParts().get(0).getMeasures().get(0).getNotes().get(0).getNotation().getString(); //<string>3</string>
		assertEquals(3,string);
	}
	
	@Test
	public void SlideTest2() {
		setUp("src/test/resources/system/demoGuitarSimple1.musicxml");
		int string = score.getParts().get(0).getMeasures().get(1).getNotes().get(0).getNotation().getString(); //<string>2</string>
		assertEquals(2,string);
	}
	
	@Test
	public void SlideTest3() {
		setUp("src/test/resources/system/demoGuitarSimple2.musicxml");
		int string = score.getParts().get(0).getMeasures().get(0).getNotes().get(0).getNotation().getString(); //<string>1</string>
		assertEquals(1,string);
		
		int fret = score.getParts().get(0).getMeasures().get(0).getNotes().get(0).getNotation().getFret(); //<fret>5</fret>
		assertEquals(5,fret);
	}
	
	@Test
	public void SlideTest4() {
		setUp("src/test/resources/system/demoGuitarSimple2.musicxml");
		int string = score.getParts().get(0).getMeasures().get(0).getNotes().get(1).getNotation().getString(); //<string>2</string>
		assertEquals(2,string);
		
		int fret = score.getParts().get(0).getMeasures().get(0).getNotes().get(1).getNotation().getFret(); //<fret>7</fret>
		assertEquals(7,fret);
	}
	
	@Test
	public void Slidetest5() {
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
				Slide note1to11 = score.getParts().get(0).getMeasures().get(0).getNotes().get(i).getNotation().getSlides().get(0);
			}
		});
		
		/*
		 *  At index 11, test will start throwing an index out of bound exception when there are 12 indexes in total???
		 *  Note that at this index and index 12, 2 tied notes exist. Does this mean if there 
		 *  already exists another type of list (tied, slide) at a specific index => the slur list for 
		 *  this note will be removed???
		 */
		Assertions.assertThrows(NullPointerException.class, () ->{
			Slide note12test = score.getParts().get(0).getMeasures().get(0).getNotes().get(11).getNotation().getSlides().get(0);
		});
	}
	
	@Test
	public void Slidetest6() {
		setUp("src/test/resources/system/demoGuitarComplex1.musicxml");
		int noteCountM2 = 0;
		for(int i = 0; i < score.getParts().get(0).getMeasures().get(1).getNotes().size(); i++) {
			noteCountM2++;
		}
		// this test confirms there are 12 notes in 2nd measure 
		Assertions.assertTrue(noteCountM2 == 12);
		// this test confirms there exists a slur note at index 0 and 1 of the note list of the 2nd measure
		Slur note1 = score.getParts().get(0).getMeasures().get(1).getNotes().get(0).getNotation().getSlurs().get(0);
		Assertions.assertNotNull(note1);
		Slur note2 = score.getParts().get(0).getMeasures().get(1).getNotes().get(1).getNotation().getSlurs().get(0);
		Assertions.assertNotNull(note2);
		/*
		 *  Since there aren't any slide note in this measure, this test expects to return null for 
		 *  all slur objects at all notes index. However, at index 0 where there is a slur note, the test
		 *  throws an index out of bound exception
		 */
		Assertions.assertThrows(NullPointerException.class, () -> {
			Slide note1test = score.getParts().get(0).getMeasures().get(1).getNotes().get(0).getNotation().getSlides().get(0);
		});
	}
	
	
	@Test
	public void Slidetest7() {
		setUp("src/test/resources/system/demoGuitarComplex1.musicxml");
		Slide note2 = score.getParts().get(0).getMeasures().get(9).getNotes().get(1).getNotation().getSlides().get(0);
		Assertions.assertTrue(note2.getType().equals("start"));
		Assertions.assertTrue(note2.getNumber() == 2);
		
		
		Slide note3 = score.getParts().get(0).getMeasures().get(9).getNotes().get(2).getNotation().getSlides().get(0);
		Assertions.assertTrue(note3.getType().equals("stop"));
		Assertions.assertFalse(note3.getNumber() == 0);
		Assertions.assertTrue(note3.getNumber() == 2);
		
	}

}
