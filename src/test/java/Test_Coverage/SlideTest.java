package Test_Coverage;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;

import custom_component_data.Measure;
import custom_component_data.Note;
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
	public void slideTest5() {
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
		Assertions.assertThrows(IndexOutOfBoundsException.class, () ->{
			Slide note12test = score.getParts().get(0).getMeasures().get(0).getNotes().get(11).getNotation().getSlides().get(0);
		});
	}
	
	@Test
	public void slideTest6() {
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
		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
			Slide note1test = score.getParts().get(0).getMeasures().get(1).getNotes().get(0).getNotation().getSlides().get(0);
		});
	}
	
	@Test
	public void slideTest7() {
		setUp("src/test/resources/system/demoGuitarComplex1.musicxml");
		List<List<Integer>> slideStarts = new ArrayList<>();
		List<List<Integer>> slideEnds = new ArrayList<>();
		slideStarts.add(generateList(9, 1));  	slideEnds.add(generateList(9, 2));
		slideStarts.add(generateList(40, 13));	slideEnds.add(generateList(40, 15));
		slideStarts.add(generateList(41, 3));   slideEnds.add(generateList(41, 5));
		slideStarts.add(generateList(41, 11));   slideEnds.add(generateList(41, 12));
		slideStarts.add(generateList(42, 3));   slideEnds.add(generateList(42, 5));
		slideStarts.add(generateList(43, 8));   slideEnds.add(generateList(43, 9));
		slideStarts.add(generateList(47, 8));   slideEnds.add(generateList(47, 9));
		slideStarts.add(generateList(50, 8));   slideEnds.add(generateList(50, 9));
		slideStarts.add(generateList(51, 3));   slideEnds.add(generateList(51, 4));
		slideStarts.add(generateList(51, 7));   slideEnds.add(generateList(51, 8));
		slideStarts.add(generateList(55, 8));   slideEnds.add(generateList(55, 9));
		slideStarts.add(generateList(57, 14));   slideEnds.add(generateList(57, 15));
		slideStarts.add(generateList(58, 12));   slideEnds.add(generateList(58, 13));
		
		int start = 0;
		int end = 0;
		for (int i = 0; i < score.getParts().get(0).getMeasures().size(); i++) {
			Measure m = score.getParts().get(0).getMeasures().get(i);
			for (int j = 0; j < m.getNotes().size(); j++) {
				Note n = m.getNotes().get(j);
				if (n.getNotation() != null && n.getNotation().getSlides().size() != 0) {
					for (Slide s: n.getNotation().getSlides()) {
						if (s.getType().equals("start")) {
							List<Integer> startLocation = slideStarts.get(start);
							assertTrue(startLocation.get(0) == i && startLocation.get(1) == j, "i: " + i + ", j:" + j);
							start++;
						}
						else if (s.getType().equals("stop")) {
							List<Integer> endLocation = slideEnds.get(end);
							assertTrue(endLocation.get(0) == i && endLocation.get(1) == j);
							end++;
						}
						else {
							fail("Slide is neither of type start nor of type stop.");
						}
					}
				}
			}
		}
		
		assertTrue(start == end && start == slideStarts.size() && end == slideEnds.size());
	}
	
	private List<Integer> generateList(int measure, int note) { 
		List<Integer> arr = new ArrayList<>();
		arr.add(measure);
		arr.add(note);
		return arr;
	}

}
