package custom_component_data_test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import custom_component_data.Notation;
import custom_component_data.Note;
import custom_component_data.Score;

class NotationTest {
	
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
	public void notationTest1() {
		setUp("src/test/resources/system/demoDrumsSimple1.musicxml");
		boolean existNotation = false;
		List<Note> noteList = score.getParts().get(0).getMeasures().get(0).getNotes();
		for(int i = 0; i < noteList.size(); i++) {
			if(noteList.get(i).getNotation() != null) {
				existNotation = true;
			}
		}
		Assertions.assertTrue(existNotation == true);
	}
	
	@Test
	public void notationTest2() {
		setUp("src/test/resources/system/demoDrumsSimple1.musicxml");
		boolean existNotation = false;
		List<Note> noteList = score.getParts().get(0).getMeasures().get(1).getNotes();
		for(int i = 0; i < noteList.size(); i++) {
			if(noteList.get(i).getNotation() != null) {
				existNotation = true;
			}
		}
		Assertions.assertTrue(existNotation == true);
	}

	@Test
	public void notationTest3() {
		setUp("src/test/resources/system/demoDrumsSimple1.musicxml");
		boolean existNotation = false;
		List<Note> noteList = score.getParts().get(0).getMeasures().get(2).getNotes();
		for(int i = 0; i < noteList.size(); i++) {
			if(noteList.get(i).getNotation() != null) {
				existNotation = true;
			}
		}
		Assertions.assertTrue(existNotation == false);
	}
	
	@Test
	public void notationTest4() {
		setUp("src/test/resources/system/demoDrumsSimple2.musicxml");
		Notation n = score.getParts().get(0).getMeasures().get(0).getNotes().get(0).getNotation();
		assertNotNull(n);
	}

}
