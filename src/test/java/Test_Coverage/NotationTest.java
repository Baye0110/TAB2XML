package Test_Coverage;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import custom_component_data.Bend;
import custom_component_data.Notation;
import custom_component_data.Note;
import custom_component_data.Ornament;
import custom_component_data.Score;
import custom_component_data.Slur;
import custom_component_data.Technical;
import custom_component_data.Tremolo;

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
		Notation n1 = score.getParts().get(0).getMeasures().get(0).getNotes().get(0).getNotation();
		Notation n2 = score.getParts().get(0).getMeasures().get(0).getNotes().get(1).getNotation();
		Notation n3 = score.getParts().get(0).getMeasures().get(0).getNotes().get(2).getNotation();
		Notation n4 = score.getParts().get(0).getMeasures().get(0).getNotes().get(3).getNotation();
		assertNull(n1);
		assertNotNull(n2);
		assertNotNull(n3);
		assertNotNull(n4);
	}
	
	@Test
	public void notationTest5() {
		setUp("src/test/resources/system/demoDrumsSimple1.musicxml");
		int numOfNotaion = 0;
		List<Note> noteList1 = score.getParts().get(0).getMeasures().get(1).getNotes();
		for(int i = 0; i < noteList1.size(); i++) {
			if(noteList1.get(i).getNotation() != null) {
				numOfNotaion++;
			}
		}
		Assertions.assertTrue(numOfNotaion == 2);
		numOfNotaion = 0;
		
		List<Note> noteList2 = score.getParts().get(0).getMeasures().get(0).getNotes();
		for(int i = 0; i < noteList2.size(); i++) {
			if(noteList2.get(i).getNotation() != null) {
				numOfNotaion++;
			}
		}
		Assertions.assertTrue(numOfNotaion == 2);
	}
	
	@Test
	public void notationTestGetSlur() {
		setUp("src/test/resources/system/demoGuitarSimple4.musicxml");
		List<Slur> list = score.getParts().get(0).getMeasures().get(0).getNotes().get(0).getNotation().getSlurs();
		Assertions.assertTrue(list.size() == 1);
		
	}
	
	@Test
	public void notationTestGetFret() {
		setUp("src/test/resources/system/demoGuitarSimple4.musicxml");
		int fret = score.getParts().get(0).getMeasures().get(0).getNotes().get(0).getNotation().getFret();
		Assertions.assertTrue(fret == 3);
	}
	
	@Test
	public void notationTestGetString() {
		setUp("src/test/resources/system/demoGuitarSimple4.musicxml");
		int string = score.getParts().get(0).getMeasures().get(0).getNotes().get(0).getNotation().getString();
		Assertions.assertTrue(string == 5);
	}
	
	@Test
	public void notationTestGetTech() {
		setUp("src/test/resources/system/demoGuitarComplex1.musicxml");
		Technical tec1 = score.getParts().get(0).getMeasures().get(0).getNotes().get(0).getNotation().getTechnical();
		Assertions.assertTrue(tec1.getHarmonic().equals("natural"));
		
		Technical tec2 = score.getParts().get(0).getMeasures().get(1).getNotes().get(0).getNotation().getTechnical();
		Assertions.assertEquals(tec2.getPullOff().get(0).getNumber(), 2);
		Assertions.assertTrue(tec2.getPullOff().get(0).getType().equals("start"));
		Assertions.assertTrue(tec2.getPullOff().get(0).getSymbol().equals("P"));
		
		Technical tec3 = score.getParts().get(0).getMeasures().get(17).getNotes().get(11).getNotation().getTechnical();
		Assertions.assertEquals(tec3.getHammerOns().get(0).getNumber(), 2);
		Assertions.assertTrue(tec3.getHammerOns().get(0).symbol().equals("H"));
		Assertions.assertTrue(tec3.getHammerOns().get(0).type().equals("start"));
		
		setUp("src/test/resources/system/demoGuitarSimple1.musicxml");
		double actual = score.getParts().get(0).getMeasures().get(0).getNotes().get(0).getNotation().getTechnical().getBend().getBendAlter();
		double expect = 2.0;
		Assertions.assertEquals(actual,expect);
		
	}
	
	@Test
	public void notationTestgetOrnaments() {
		setUp("src/test/resources/system/demoDrumsComplex1.musicxml");
		Ornament orn = score.getParts().get(0).getMeasures().get(43).getNotes().get(14).getNotation().getOrnaments();
		Assertions.assertEquals(orn.getTremolo().getNumber(),1);
	}
	

}
