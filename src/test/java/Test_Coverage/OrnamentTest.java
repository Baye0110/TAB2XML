package Test_Coverage;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import custom_component_data.Score;

class OrnamentTest {

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
	void TestgetTremolo() {
		setUp("src/test/resources/system/demoDrumsComplex1.musicxml");
		int actual = score.getParts().get(0).getMeasures().get(43).getNotes().get(14).getNotation().getOrnaments().getTremolo().getNumber();
		int expect = 1;
		assertEquals(actual,expect);
	}
}
