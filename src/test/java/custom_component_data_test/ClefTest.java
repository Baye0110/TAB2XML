package custom_component_data_test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//import org.junit.Before;
import org.junit.jupiter.api.Test;

import custom_component_data.Clef;
import custom_component_data.Score;

//Testing Clef.java 

class ClefTest {
	@Test
	public void cleftTest1() {
		Clef c1 = new Clef('G',1);
		assertNotNull(c1);
	}
	
	Score score = null;
	
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
	public void cleftTest2() {
		Clef expected = new Clef('G', 2);
		Clef actual = score.getParts().get(0).getMeasures().get(0).getClef();
		assertEquals(expected, actual);
	}
	
	@Test
	public void clefTest3() {
		
	}
	
	
}


