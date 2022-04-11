package Test_Coverage;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import custom_component_data.Clef;
import custom_component_data.Measure;
import custom_component_data.Score;

//Testing Measure.java in component_data
class MeasureTest {
	
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
	public void testMeasure1() {
		setUp("src/test/resources/system/demoDrumsSimple1.musicxml");
		Measure m = score.getParts().get(0).getMeasures().get(0);	//measure number 1
		assertNotNull(m);
		
		int d = m.getDivisions(); //should be 16
		assertEquals(16,d);
		
		int f = m.getFifths();	//should be zero
		assertEquals(0,f);
	}
	
	@Test
	public void testMeasure2() {
		setUp("src/test/resources/system/demoDrumsSimple1.musicxml");
		Measure m = score.getParts().get(0).getMeasures().get(1);	//measure number 2
		assertNotNull(m);
		
		int d = m.getDivisions(); //should be 16
		assertEquals(16,d);
	}
	
	@Test
	public void testMeasure3() {
		setUp("src/test/resources/system/demoDrumsSimple2.musicxml");
		Measure m = score.getParts().get(0).getMeasures().get(0);	//measure number 1
		assertNotNull(m);
		
		int d = m.getDivisions(); //<divisions>32</divisions>
		assertEquals(32,d);
		
		int f = m.getFifths(); //<fifths>0</fifths>
		assertEquals(0,f);
	}
	
	@Test
	public void testMeasure4() {
		setUp("src/test/resources/system/demoDrumsSimple3.musicxml");
		Measure m = score.getParts().get(0).getMeasures().get(0);	//measure number 1
		assertNotNull(m);
		
		int size =  score.getParts().get(0).getMeasures().size(); //3
		assertEquals(3,size);
		
		int d = m.getDivisions(); //<divisions>16</divisions>
		assertEquals(16,d);
		
		int f = m.getFifths(); //<fifths>0</fifths>
		assertEquals(0,f);
	}
	
	@Test
	public void TestgetIsRepeatStart() {
		setUp("src/test/resources/system/money.musicxml");
		boolean actual = score.getParts().get(0).getMeasures().get(0).getIsRepeatStart();
		assertTrue(actual);
	}
	@Test
	public void TestgetIsRepeatStop() {
		setUp("src/test/resources/system/money.musicxml");
		boolean actual = score.getParts().get(0).getMeasures().get(0).getIsRepeatStop();
		assertTrue(actual);
	}
	
	@Test
	public void TestgetBarLine() {
		setUp("src/test/resources/system/money.musicxml");
		String acturalStyle1 = score.getParts().get(0).getMeasures().get(0).getBarLineLeft().getStyle();
		String expectStyle1 = "heavy-light";
		assertEquals(acturalStyle1,expectStyle1);
		
		String actualRepeatType1 = score.getParts().get(0).getMeasures().get(0).getBarLineLeft().getRepeatType();
		String expectRepeatType1 = "forward";
		assertEquals(actualRepeatType1,expectRepeatType1);
		
		assertNull(score.getParts().get(0).getMeasures().get(0).getBarLineLeft().getRepeatNum());
		
		String acturalStyle2 = score.getParts().get(0).getMeasures().get(0).getBarLineRight().getStyle();
		String expectStyle2 = "light-heavy";
		assertEquals(acturalStyle2,expectStyle2);
		
		String actualRepeatType2 = score.getParts().get(0).getMeasures().get(0).getBarLineRight().getRepeatType();
		String expectRepeatType2 = "backward";
		assertEquals(actualRepeatType2,expectRepeatType2);
		
		int actualRepeaNumber2 = score.getParts().get(0).getMeasures().get(0).getBarLineRight().getRepeatNum();
		int expectRepeaNumber2 = 8;
		assertEquals(actualRepeaNumber2,expectRepeaNumber2);
	}
	
	@Test
	public void TestgetDirections() {
		setUp("src/test/resources/system/money.musicxml");
		String actual1 = score.getParts().get(0).getMeasures().get(0).getDirections().get(0).getPlacement();
		String expect1 = "above";
		assertEquals(actual1,expect1);
		
		String actual2 = score.getParts().get(0).getMeasures().get(0).getDirections().get(0).getWords();
		String expect2 = "x8";
		assertEquals(actual2,expect2);
		
		Double[] actual3 = score.getParts().get(0).getMeasures().get(0).getDirections().get(0).getPosition();
		Double[] expect3 = {0.0,0.0};
		assertEquals(actual3[0],expect3[0]);
		assertEquals(actual3[1],expect3[1]);
	}
	
	@Test
	public void TestgetTimeSignature() {
		setUp("src/test/resources/system/money.musicxml");
		int[] actual = score.getParts().get(0).getMeasures().get(0).getTimeSignature();
		int[] expect = {7,4};
		assertEquals(actual[0],expect[0]);
		assertEquals(actual[1],expect[1]);
		assertTrue(score.getParts().get(0).getMeasures().get(0).getTimeDisplay());
		
	}
	
	@Test
	public void TestgetClef() {
		setUp("src/test/resources/system/demoDrumsSimple3.musicxml");
		Clef expect = new Clef('G',2);
		Clef actual = score.getParts().get(0).getMeasures().get(0).getClef();
		assertEquals(actual.getLine(),expect.getLine());
		assertEquals(actual.getSymbol(),expect.getSymbol());
		assertFalse(score.getParts().get(0).getMeasures().get(0).getTunedMeasure());
	}
	
	@Test
	public void TestgetStaffLines() {
		setUp("src/test/resources/system/money.musicxml");
		int actual = score.getParts().get(0).getMeasures().get(0).getStaffLines();
		int expect = 4;
		assertEquals(actual,expect);
	}
	
	@Test
	public void TestgetPercussion() {
		setUp("src/test/resources/system/demoDrumsSimple3.musicxml");
		assertTrue(score.getParts().get(0).getMeasures().get(0).getPercussion());
		
		setUp("src/test/resources/system/demoGuitarComplex1.musicxml");
		assertFalse(score.getParts().get(0).getMeasures().get(0).getPercussion());
	}
	
	@Test
	public void TestgetTab() {
		setUp("src/test/resources/system/demoGuitarComplex1.musicxml");
		assertTrue(score.getParts().get(0).getMeasures().get(0).getTab());
		
		setUp("src/test/resources/system/demoDrumsSimple1.musicxml");
		assertFalse(score.getParts().get(0).getMeasures().get(0).getTab());
		
	}
}
