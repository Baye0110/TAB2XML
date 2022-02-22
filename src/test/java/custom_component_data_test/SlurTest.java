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
		
	}
	
	

}
