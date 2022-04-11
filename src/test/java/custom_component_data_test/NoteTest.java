package custom_component_data_test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import custom_component_data.Notation;
import custom_component_data.Note;
import custom_component_data.Score;

class NoteTest {
	
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
	void testNoteNumber() {
		setUp("src/test/resources/system/demoDrumsSimple1.musicxml");
		List<Note> noteList1 = score.getParts().get(0).getMeasures().get(0).getNotes();
		Assertions.assertTrue(noteList1.size() == 13);
	}
	
	@Test
	void testGetNotaion() {
		setUp("src/test/resources/system/demoDrumsSimple2.musicxml");
		List<Note> noteList1 = score.getParts().get(0).getMeasures().get(0).getNotes();
		Notation n1 = score.getParts().get(0).getMeasures().get(0).getNotes().get(0).getNotation();
		Assertions.assertTrue(noteList1.get(0).getNotation() == n1);
	}
	
	@Test
	void testGetNoteType() {
		setUp("src/test/resources/system/demoDrumsSimple1.musicxml");
		int notetype1 = score.getParts().get(0).getMeasures().get(0).getNotes().get(0).getType();
		Assertions.assertEquals(notetype1, 8);
		
		int notetype2 = score.getParts().get(0).getMeasures().get(0).getNotes().get(1).getType();
		Assertions.assertEquals(notetype2, 8);
		
		setUp("src/test/resources/system/demoGuitarSimple4.musicxml");
		
		int notetype3 = score.getParts().get(0).getMeasures().get(0).getNotes().get(0).getType();
		Assertions.assertEquals(notetype3, 16);
		
		int notetype4 = score.getParts().get(0).getMeasures().get(0).getNotes().get(1).getType();
		Assertions.assertEquals(notetype4, 8);
		
	}
	
	@Test
	void testGetNotedot() {
		setUp("src/test/resources/system/demoDrumsComplex1.musicxml");
		int dotNum1 = score.getParts().get(0).getMeasures().get(1).getNotes().get(0).getDot();
		Assertions.assertEquals(dotNum1, 0);
		
		int dotNum2 = score.getParts().get(0).getMeasures().get(17).getNotes().get(0).getDot();
		Assertions.assertEquals(dotNum2, 1);
		
		int dotNum3 = score.getParts().get(0).getMeasures().get(64).getNotes().get(2).getDot();
		Assertions.assertEquals(dotNum3, 2);
		
	}
	
	@Test
	void testGetNoteStem() {
		setUp("src/test/resources/system/demoDrumsComplex1.musicxml");
		String s1 = score.getParts().get(0).getMeasures().get(0).getNotes().get(0).getStem();
		Assertions.assertTrue(s1.equals("up"));
		
		setUp("src/test/resources/system/demoGuitarSimple5.musicxml");
		String s2 = score.getParts().get(0).getMeasures().get(0).getNotes().get(0).getStem();
		assertNull(s2);
		
		setUp("src/test/resources/system/demoGuitarSimple4.musicxml");
		String s3 = score.getParts().get(0).getMeasures().get(0).getNotes().get(0).getStem();
		Assertions.assertTrue(s3.equals("none"));
	}
	@Test
	void testGetNoteHead() {
		setUp("src/test/resources/system/demoDrumsComplex1.musicxml");
		String s1 = score.getParts().get(0).getMeasures().get(0).getNotes().get(0).getNotehead();
		Assertions.assertTrue(s1.equals("x"));
		
		setUp("src/test/resources/system/demoDrumsSimple1.musicxml");
		String s2 = score.getParts().get(0).getMeasures().get(0).getNotes().get(3).getNotehead();
		Assertions.assertTrue(s2.equals("x"));
		
		setUp("src/test/resources/system/demoGuitarSimple4.musicxml");
		String s3 = score.getParts().get(0).getMeasures().get(0).getNotes().get(0).getNotehead();
		assertNull(s3);
	}
	
	@Test
	void TestgetParentheses() {
		setUp("src/test/resources/system/demoDrumsComplex1.musicxml");
		assertTrue(score.getParts().get(0).getMeasures().get(7).getNotes().get(11).getParentheses());
		assertFalse(score.getParts().get(0).getMeasures().get(8).getNotes().get(0).getParentheses());
	}
	
	@Test
	void TestetGrace() {
		setUp("src/test/resources/system/demoDrumsComplex1.musicxml");
		assertTrue(score.getParts().get(0).getMeasures().get(7).getNotes().get(17).getGrace());
		assertFalse(score.getParts().get(0).getMeasures().get(7).getNotes().get(16).getGrace());
	}
	
	@Test
	void TestgetChord() {
		setUp("src/test/resources/system/demoDrumsComplex1.musicxml");
		assertTrue(score.getParts().get(0).getMeasures().get(8).getNotes().get(1).getChord());
		assertFalse(score.getParts().get(0).getMeasures().get(8).getNotes().get(0).getChord());
	}
	
	@Test
	void TestgetRest() {
		setUp("src/test/resources/system/demoDrumsComplex1.musicxml");
		assertTrue(score.getParts().get(0).getMeasures().get(64).getNotes().get(1).getRest());
		assertFalse(score.getParts().get(0).getMeasures().get(65).getNotes().get(0).getRest());
	}
	
	@Test
	void TestgetTimeModification() {
	setUp("src/test/resources/system/demoDrumsComplex1.musicxml");
	HashMap<String, Integer> actual = score.getParts().get(0).getMeasures().get(84).getNotes().get(0).getTimeModification();
	int expect = 2;
	assertEquals(actual.size(),expect);
	assertEquals(actual.get("actual"),3);
	assertEquals(actual.get("normal"),2);
	}
	
	
	@Test
	void TestgetPitched() {
		setUp("src/test/resources/system/demoDrumsComplex1.musicxml");
		assertFalse(score.getParts().get(0).getMeasures().get(1).getNotes().get(0).getPitched());
	}
	
	@Test
	void TestgetStep() {
		setUp("src/test/resources/system/demoDrumsComplex1.musicxml");
		char actual = score.getParts().get(0).getMeasures().get(0).getNotes().get(8).getStep();
		char expected = 'G';
		assertEquals(actual,expected);
	}
	
	@Test
	void TestgetOctave() {
		setUp("src/test/resources/system/demoDrumsComplex1.musicxml");
		int actual = score.getParts().get(0).getMeasures().get(0).getNotes().get(0).getOctave();
		int expected = 5;
		assertEquals(actual,expected);
	}
	
	@Test
	void TestgetgetAlter() {
		setUp("src/test/resources/system/demoGuitarSimple1.musicxml");
		int actual = score.getParts().get(0).getMeasures().get(0).getNotes().get(0).getAlter();
		int expected = 1;
		assertEquals(actual,expected);
	}
	
	@Test
	void TestgetDuration() {
		setUp("src/test/resources/system/demoGuitarSimple1.musicxml");
		int actual = score.getParts().get(0).getMeasures().get(0).getNotes().get(0).getDuration();
		int expected = 16;
		assertEquals(actual,expected);
	}
	
	@Test
	void TestgetInstrumentID() {
		setUp("src/test/resources/system/demoDrumsComplex1.musicxml");
		String actual = score.getParts().get(0).getMeasures().get(0).getNotes().get(0).getInstrumentID();
		String expected = "P1-I50";
		assertEquals(actual,expected);
	}
	
	@Test
	void TesttoString() {
		setUp("src/test/resources/system/demoGuitarSimple1.musicxml");
		String expect = "{\n";
		expect += "step: " + score.getParts().get(0).getMeasures().get(0).getNotes().get(0).getStep() + "\n";
		expect += "octave: " + score.getParts().get(0).getMeasures().get(0).getNotes().get(0).getOctave() + "\n";
		expect += "duration: " + score.getParts().get(0).getMeasures().get(0).getNotes().get(0).getDuration() + "\n";	
		expect += "type: " + score.getParts().get(0).getMeasures().get(0).getNotes().get(0).getType() + "\n";
		expect += "}";
	
		String actual = score.getParts().get(0).getMeasures().get(0).getNotes().get(0).toString();
		assertEquals(actual,expect);
		
	}
	
}
