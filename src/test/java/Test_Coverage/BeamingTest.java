package Test_Coverage;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import custom_component_data.Score;
import custom_model.BeamInfoProcessor;
import custom_model.MeasureBeamData;
import custom_model.SheetScore;

class BeamingTest {

	private Score score = null;
	private SheetScore sheet = null;
	
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
           this.sheet = new SheetScore(score);

       } 
       catch (FileNotFoundException e1) {
           e1.printStackTrace();
       }
       
	}
	
	
	@Test
	public void testCorrectBeamingGroups1() {
		this.setUp("src/test/resources/system/demoGuitarComplex1.musicxml");
		MeasureBeamData mbd = new MeasureBeamData(this.sheet.getMeasureList().get(1).getNotes(), this.score.getParts().get(0).getMeasures().get(1).getTimeSignature()[1]);
		List<Integer> expectedBeamGroupings = mbd.getBeamNumbers();
		
		List<Integer> actualBeamGroupings = new ArrayList<>();
		actualBeamGroupings.add(1);	actualBeamGroupings.add(1);	actualBeamGroupings.add(1);	actualBeamGroupings.add(1);	
		actualBeamGroupings.add(2);	actualBeamGroupings.add(2);	actualBeamGroupings.add(2);	actualBeamGroupings.add(2);
		actualBeamGroupings.add(3);	actualBeamGroupings.add(3);	actualBeamGroupings.add(3);	actualBeamGroupings.add(3);	
		assertTrue(expectedBeamGroupings.toString().equals(actualBeamGroupings.toString()));
		
	}
	
	@Test
	public void testCorrectBeamingGroups2() {
		this.setUp("src/test/resources/system/demoGuitarComplex1.musicxml");
		MeasureBeamData mbd = new MeasureBeamData(this.sheet.getMeasureList().get(9).getNotes(), this.score.getParts().get(0).getMeasures().get(9).getTimeSignature()[1]);
		List<Integer> expectedBeamGroupings = mbd.getBeamNumbers();
		
		List<Integer> actualBeamGroupings = new ArrayList<>();
		actualBeamGroupings.add(1);	actualBeamGroupings.add(1);	actualBeamGroupings.add(0);	actualBeamGroupings.add(0);	
		assertTrue(expectedBeamGroupings.toString().equals(actualBeamGroupings.toString()));
	}
	
	@Test
	public void testCorrectBeamingGroups3() {
		this.setUp("src/test/resources/system/demoDrumsComplex1.musicxml");
		MeasureBeamData mbd = new MeasureBeamData(this.sheet.getMeasureList().get(23).getNotes(), this.score.getParts().get(0).getMeasures().get(23).getTimeSignature()[1]);
		List<Integer> expectedBeamGroupings = mbd.getBeamNumbers();
		
		List<Integer> actualBeamGroupings = new ArrayList<>();
		actualBeamGroupings.add(1);	actualBeamGroupings.add(0);	actualBeamGroupings.add(1);	actualBeamGroupings.add(1);	
		actualBeamGroupings.add(0);	actualBeamGroupings.add(2);	actualBeamGroupings.add(2);	
		actualBeamGroupings.add(0);	actualBeamGroupings.add(3);	actualBeamGroupings.add(3);	actualBeamGroupings.add(0); actualBeamGroupings.add(3);
		actualBeamGroupings.add(4);	actualBeamGroupings.add(0);	actualBeamGroupings.add(4);	actualBeamGroupings.add(4);
		assertTrue(expectedBeamGroupings.toString().equals(actualBeamGroupings.toString()));
	}
	
	class Pair {
		int numFull;
		int numHalf;
		
		Pair(int a, int b) {
			this.numFull = a;
			this.numHalf = b;
		}
		
		public String toString() {
			return "full: " + this.numFull + ", half: " + this.numHalf + ".";
		}
	}
	
	
	@Test
	public void testCorrectBeamDrawing1() {
		this.setUp("src/test/resources/system/demoGuitarComplex1.musicxml");
		MeasureBeamData mbd = new MeasureBeamData(this.sheet.getMeasureList().get(19).getNotes(), this.score.getParts().get(0).getMeasures().get(19).getTimeSignature()[1]);
		BeamInfoProcessor bip = new BeamInfoProcessor(mbd.getBeamNumbers(), mbd.getBeamInfos());
		List<Pair> actual = new ArrayList<>();
		
		for (int i = 0; i < bip.getBeamData().size(); i++) {
			actual.add(new Pair(bip.getBeamData().get(i).getNumFull(), bip.getBeamData().get(i).getNumHalf()));
		}
		
		List<Pair> expected = new ArrayList<>();
		expected.add(new Pair(1, 0));	expected.add(new Pair(1, 0));	expected.add(new Pair(1, 0));	expected.add(new Pair(1, 0));
		expected.add(new Pair(2, 0));	expected.add(new Pair(2, 0));	expected.add(new Pair(2, 0));	expected.add(new Pair(2, 0));
		expected.add(new Pair(2, 0));	expected.add(new Pair(2, 0));	expected.add(new Pair(2, 0));	expected.add(new Pair(2, 1));
		
		assertTrue(expected.toString().equals(actual.toString()));
	}
	
	@Test
	public void testCorrectBeamDrawing2() {
		this.setUp("src/test/resources/system/demoDrumsComplex1.musicxml");
		MeasureBeamData mbd = new MeasureBeamData(this.sheet.getMeasureList().get(67).getNotes(), this.score.getParts().get(0).getMeasures().get(67).getTimeSignature()[1]);
		BeamInfoProcessor bip = new BeamInfoProcessor(mbd.getBeamNumbers(), mbd.getBeamInfos());
		List<Pair> actual = new ArrayList<>();
		
		for (int i = 0; i < bip.getBeamData().size(); i++) {
			actual.add(new Pair(bip.getBeamData().get(i).getNumFull(), bip.getBeamData().get(i).getNumHalf()));
		}
		
		List<Pair> expected = new ArrayList<>();
		expected.add(new Pair(1, 0));	expected.add(new Pair(1, 0));	
		expected.add(new Pair(2, 0));	expected.add(new Pair(2, 0));	expected.add(new Pair(2, 0));	expected.add(new Pair(2, 0));
		expected.add(new Pair(2, 0));	expected.add(new Pair(2, 0));
		
		assertTrue(expected.toString().equals(actual.toString()));
		
		List<Integer> expectedBeamGroupings = mbd.getBeamNumbers();
		
		List<Integer> actualBeamGroupings = new ArrayList<>();
		actualBeamGroupings.add(1);	actualBeamGroupings.add(1);	actualBeamGroupings.add(0);	
		actualBeamGroupings.add(2);	actualBeamGroupings.add(2);	actualBeamGroupings.add(2);	actualBeamGroupings.add(2);	
		actualBeamGroupings.add(3);	actualBeamGroupings.add(3);	
		assertTrue(expectedBeamGroupings.toString().equals(actualBeamGroupings.toString()));
	}
	

}
