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
	public void slideTest1() {
		setUp("src/test/resources/system/demoGuitarSimple1.musicxml");
		int string = score.getParts().get(0).getMeasures().get(0).getNotes().get(0).getNotation().getString(); //<string>3</string>
		assertEquals(3,string);
	}
	
	public void slideTest2() {
		setUp("src/test/resources/system/demoGuitarSimple1.musicxml");
		int string = score.getParts().get(0).getMeasures().get(1).getNotes().get(0).getNotation().getString(); //<string>2</string>
		assertEquals(2,string);
	}
	
	public void slideTest3() {
		setUp("src/test/resources/system/demoGuitarSimple2.musicxml");
		int string = score.getParts().get(0).getMeasures().get(0).getNotes().get(0).getNotation().getString(); //<string>1</string>
		assertEquals(1,string);
		
		int fret = score.getParts().get(0).getMeasures().get(0).getNotes().get(0).getNotation().getFret(); //<fret>5</fret>
		assertEquals(5,fret);
	}
	
	public void slideTest4() {
		setUp("src/test/resources/system/demoGuitarSimple2.musicxml");
		int string = score.getParts().get(0).getMeasures().get(0).getNotes().get(1).getNotation().getString(); //<string>2</string>
		assertEquals(2,string);
		
		int fret = score.getParts().get(0).getMeasures().get(0).getNotes().get(1).getNotation().getFret(); //<fret>7</fret>
		assertEquals(7,fret);
	}

}
