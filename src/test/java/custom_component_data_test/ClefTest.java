package custom_component_data_test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.jupiter.api.Assertions;
//import org.junit.Before;
import org.junit.jupiter.api.Test;

import custom_component_data.Clef;
import custom_component_data.Score;

//Testing Clef.java 

class ClefTest {
	
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
	public void cleftConstructorTest1() {
		Clef c1 = new Clef('G',1);
		assertNotNull(c1);
	}
	

	
	@Test
	public void clefTest1() {
		setUp("src/test/resources/system/demoDrumsSimple1.musicxml");
		Clef expected = new Clef('G', 2);
		Clef actual = score.getParts().get(0).getMeasures().get(0).getClef();
		assertNotNull(actual);
		Assertions.assertTrue(expected.getSymbol() == actual.getSymbol(), "The expected symbol of clef is " + expected.getLine() + "while the actual symbol is " + actual.getLine());
		Assertions.assertTrue(expected.getLine() == actual.getLine(), "The expected line of clef is " + expected.getLine() + "while the actual line is " + actual.getLine()); 
	}
	
	
	@Test
	public void clefTest2() {
		
	}
	
}


