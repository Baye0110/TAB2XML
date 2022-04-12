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
		setUp("src/test/resources/system/demoDrumsComplex1.musicxml");
		List<List<Integer>> tiedStarts = new ArrayList<>();
		List<List<Integer>> tiedEnds = new ArrayList<>();
		tiedStarts.add(generateList(35, 7));  	tiedEnds.add(generateList(35, 9));
		tiedStarts.add(generateList(35, 8));	tiedEnds.add(generateList(35, 10));
		tiedStarts.add(generateList(63, 7));   tiedEnds.add(generateList(63, 9));
		tiedStarts.add(generateList(63, 8));   tiedEnds.add(generateList(63, 10));
		tiedStarts.add(generateList(64, 0));   tiedEnds.add(generateList(64, 1));
		tiedStarts.add(generateList(64, 1));   tiedEnds.add(generateList(64, 2));
		tiedStarts.add(generateList(65, 0));   tiedEnds.add(generateList(65, 2));
		tiedStarts.add(generateList(65, 1));   tiedEnds.add(generateList(65, 3));
		tiedStarts.add(generateList(65, 2));   tiedEnds.add(generateList(65, 4));
		tiedStarts.add(generateList(65, 3));   tiedEnds.add(generateList(65, 5));
		tiedStarts.add(generateList(66, 0));   tiedEnds.add(generateList(66, 2));
		tiedStarts.add(generateList(66, 1));   tiedEnds.add(generateList(66, 3));
		tiedStarts.add(generateList(66, 2));   tiedEnds.add(generateList(66, 4));
		tiedStarts.add(generateList(66, 3));   tiedEnds.add(generateList(66, 5));
		tiedStarts.add(generateList(67, 0));   tiedEnds.add(generateList(67, 2));
		tiedStarts.add(generateList(67, 1));   tiedEnds.add(generateList(67, 3));
		tiedStarts.add(generateList(67, 2));   tiedEnds.add(generateList(67, 4));
		tiedStarts.add(generateList(67, 3));   tiedEnds.add(generateList(67, 5));
		tiedStarts.add(generateList(121, 0));   tiedEnds.add(generateList(121, 2));
		tiedStarts.add(generateList(121, 1));   tiedEnds.add(generateList(121, 3));
		tiedStarts.add(generateList(123, 0));   tiedEnds.add(generateList(123, 2));
		tiedStarts.add(generateList(123, 1));   tiedEnds.add(generateList(123, 3));
		tiedStarts.add(generateList(125, 0));   tiedEnds.add(generateList(125, 2));
		tiedStarts.add(generateList(125, 1));   tiedEnds.add(generateList(125, 3));
		
		int start = 0;
		int end = 0;
		for (int i = 0; i < score.getParts().get(0).getMeasures().size(); i++) {
			Measure m = score.getParts().get(0).getMeasures().get(i);
			for (int j = 0; j < m.getNotes().size(); j++) {
				Note n = m.getNotes().get(j);
				if (n.getNotation() != null && n.getNotation().getTieds().size() != 0) {
					for (Tied t: n.getNotation().getTieds()) {
						if (t.getType().equals("start")) {
							List<Integer> startLocation = tiedStarts.get(start);
							assertTrue(startLocation.get(0) == i && startLocation.get(1) == j, "i: " + i + ", j:" + j);
							start++;
						}
						else if (t.getType().equals("stop")) {
							List<Integer> endLocation = tiedEnds.get(end);
							assertTrue(endLocation.get(0) == i && endLocation.get(1) == j);
							end++;
						}
						else {
							fail("Tied is neither of type start nor of type stop.");
						}
					}
				}
			}
		}
		
		assertTrue(start == end && start == tiedStarts.size() && end == tiedEnds.size());
	}
	
	private List<Integer> generateList(int measure, int note) { 
		List<Integer> arr = new ArrayList<>();
		arr.add(measure);
		arr.add(note);
		return arr;
	}

	

}
