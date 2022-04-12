package Test_Coverage;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import custom_component_data.Score;
import custom_model.SheetScore;
import custom_player.musicPlayer;

class HighlightTimingTest {

	private Score score = null;
	private SheetScore sheet = null;
	private List<Double> timings = null;
	
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
           this.sheet = new SheetScore(score);
           sheet.generateBasePlayTimings(score);
           this.timings = sheet.getTimings();

       } 
       catch (Exception e1) {
           e1.printStackTrace();
       }   
	}
	
	@Test
	public void testTimings() {
		this.setUp("src/test/resources/system/demoGuitarSimple5.musicxml");
		
		int length = 8;
		double[] expectedTimings = {0.5, 0.5, 0.25, 1/16.0, 0.25 + 0.125 + 0.0625, 0.25, 0.375, 0.375};
		for (int i = 0; i < length; i++) {
			double timing = timings.get(i);
			
			assertEquals(expectedTimings[i] * 4000, timing, "" + i);
		}
	}
	

}
