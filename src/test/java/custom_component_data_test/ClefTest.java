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
	
	//Score score = null;
	
	/*@Before
	public void setUp() {
		 // Get the file
        File in = new File("src/test/resources/system/demoGuitarComplex1.musicxml");
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
        

        // Test if the File was valid and that Score was built
        assertEquals(score==null, false);
        
	}*/
	
	@Test
	public void cleftTest1() {
		Clef c1 = new Clef('G',1);
		assertNotNull(c1);
	}
	
	
	@Test
	public void cleftTest2() {
		Score scoreSheet = new Score("capricho.txt");
		Clef expected = new Clef('G', 2);
		Clef actual = scoreSheet.getParts().get(0).getMeasures().get(0).getClef();
		assertNotNull(actual);
		assertEquals(expected, actual);
		
		
	}
	
	@Test
	public void clefTest3() {
		Score scoreSheet = new Score("capricho.txt");
		Clef expected = new Clef('G', 2);
		Clef actual1 = scoreSheet.getParts().get(0).getMeasures().get(0).getClef();
		Clef actual2 = scoreSheet.getParts().get(0).getMeasures().get(3).getClef();
		assertNotNull(actual1);
		assertNotNull(actual2);
		Assertions.assertTrue(actual1.getSymbol() == actual2.getSymbol(), "Measure 1 has Clef " + actual1.getSymbol() + "while Measure 2 has Clef " + actual2.getSymbol());
	}
	
	
	@Test
	public void clefTest4() {
		Score scoreSheet = new Score("ex38.txt");
		Clef expected = new Clef('G', 2);
		Clef actual = scoreSheet.getParts().get(0).getMeasures().get(0).getClef();
		assertNotNull(actual);
		assertEquals(expected, actual); 
	}
	
	@Test
	public void clefTest5() {
		Score scoreSheet = new Score("ex38.txt");
		Clef expected = new Clef('G', 2);
		Clef actual = scoreSheet.getParts().get(0).getMeasures().get(0).getClef();
		Assertions.assertTrue(expected.getLine() == actual.getLine(), "The expected line of clef is " + expected.getLine() + "while the actual line is " + actual.getLine()); 
	}
	
}


