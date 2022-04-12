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
	public void slurTest1() {
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
		Assertions.assertThrows(IndexOutOfBoundsException.class, () ->{
			Slur note12test = score.getParts().get(0).getMeasures().get(0).getNotes().get(11).getNotation().getSlurs().get(0);
		});
	}
	
	@Test
	public void slurTest2() {
		setUp("src/test/resources/system/demoDrumsComplex1.musicxml");
		List<List<Integer>> slurStarts = new ArrayList<>();
		List<List<Integer>> slurEnds = new ArrayList<>();
		slurStarts.add(generateList(3, 17));  	slurEnds.add(generateList(3, 18));
		slurStarts.add(generateList(7, 17));	slurEnds.add(generateList(7, 18));
		slurStarts.add(generateList(23, 2));   slurEnds.add(generateList(23, 3));
		slurStarts.add(generateList(23, 6));   slurEnds.add(generateList(23, 7));
		slurStarts.add(generateList(23, 10));   slurEnds.add(generateList(23, 11));
		slurStarts.add(generateList(23, 14));   slurEnds.add(generateList(23, 15));
		slurStarts.add(generateList(23, 18));   slurEnds.add(generateList(23, 19));
		slurStarts.add(generateList(31, 0));   slurEnds.add(generateList(31, 1));
		slurStarts.add(generateList(31, 9));   slurEnds.add(generateList(31, 10));
		slurStarts.add(generateList(45, 6));   slurEnds.add(generateList(45, 7));
		slurStarts.add(generateList(45, 18));   slurEnds.add(generateList(45, 19));
		slurStarts.add(generateList(46, 6));   slurEnds.add(generateList(46, 7));
		slurStarts.add(generateList(47, 6));   slurEnds.add(generateList(47, 7));
		slurStarts.add(generateList(47, 18));   slurEnds.add(generateList(47, 19));
		slurStarts.add(generateList(48, 6));   slurEnds.add(generateList(48, 7));
		slurStarts.add(generateList(48, 16));   slurEnds.add(generateList(48, 17));
		
		int start = 0;
		int end = 0;
		for (int i = 0; i < 49; i++) {
			Measure m = score.getParts().get(0).getMeasures().get(i);
			for (int j = 0; j < m.getNotes().size(); j++) {
				Note n = m.getNotes().get(j);
				if (n.getNotation() != null && n.getNotation().getSlurs().size() != 0) {
					for (Slur t: n.getNotation().getSlurs()) {
						if (t.getType().equals("start")) {
							List<Integer> startLocation = slurStarts.get(start);
							assertTrue(startLocation.get(0) == i && startLocation.get(1) == j, "i: " + i + ", j:" + j);
							start++;
						}
						else if (t.getType().equals("stop")) {
							List<Integer> endLocation = slurEnds.get(end);
							assertTrue(endLocation.get(0) == i && endLocation.get(1) == j);
							end++;
						}
						else {
							fail("Slur is neither of type start nor of type stop.");
						}
					}
				}
			}
		}
		
		assertTrue(start == end && start == slurStarts.size() && end == slurEnds.size());
	}
	
	private List<Integer> generateList(int measure, int note) { 
		List<Integer> arr = new ArrayList<>();
		arr.add(measure);
		arr.add(note);
		return arr;
	}
	

}
