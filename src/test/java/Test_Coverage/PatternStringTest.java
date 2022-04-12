package Test_Coverage;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import custom_component_data.Measure;
import custom_component_data.Score;
import custom_model.SheetScore;
import custom_model.note.NoteUnit;
import custom_player.musicPlayer;

class PatternStringTest {


	private Score score = null;
	private SheetScore sheet = null;
	private musicPlayer player = null;
	
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
           this.player = new musicPlayer(score, sheet, build);

       } 
       catch (Exception e1) {
           e1.printStackTrace();
       }   
	}
	
	@Test
	void checkChording() {
		setUp("src/test/resources/system/demoGuitarComplex1.musicxml");
		NoteUnit.pressed = this.sheet.getMeasureList().get(0).getNotes().get(0);
		String patternStr = this.player.generateSpecificPattern();
		Scanner scan = new Scanner(patternStr);
		scan.next(); scan.next(); scan.next(); scan.next();
		
		int[] chordsExpected = {2, 0, 2, 3, 2, 0, 2, 3, 0, 0, 0, 0, 6, 4, 5, 5, 5, 6, 4, 3, 5, 2, 5, 5, 5};
		int[] chordsActual = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		int measures = 0;
		while(scan.hasNext() && measures < 25) {
			String curr = scan.next();
			
			if (curr.equals("|")) {
				measures += 1;
				continue;
			}
			
			for (int i = 0; i < curr.length(); i++) {
				if (curr.charAt(i) == '+')
					chordsActual[measures] += 1;
			}
		}
		
		for (int i = 0; i < 25; i++) {
			assertEquals(chordsExpected[i], chordsActual[i], i);
		}
	}
	
	
	@Test
	void checkGracers() {
		setUp("src/test/resources/system/demoGuitarComplex1.musicxml");
		NoteUnit.pressed = this.sheet.getMeasureList().get(0).getNotes().get(0);
		String patternStr = this.player.generateSpecificPattern();
		Scanner scan = new Scanner(patternStr);
		scan.next(); scan.next(); scan.next(); scan.next();
		
		int[] graceExpected = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 2, 1, 0, 0, 1, 2, 0, 0, 0, 3, 2, 1};
		int[] graceActual = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		int measures = 0;
		while(scan.hasNext() && measures < 27) {
			String curr = scan.next();
			
			if (curr.equals("|")) {
				measures += 1;
				continue;
			}
			
			for (int i = 0; i < curr.length() - 1; i++) {
				if (curr.substring(i, i+2).equals("X."))
					graceActual[measures] += 1;
			}
		}
		
		for (int i = 0; i < 27; i++) {
			assertEquals(graceExpected[i], graceActual[i], i);
		}
	}
	
	
	@Test
	void checkDotage() {
		setUp("src/test/resources/system/demoGuitarComplex1.musicxml");
		NoteUnit.pressed = this.sheet.getMeasureList().get(0).getNotes().get(0);
		String patternStr = this.player.generateSpecificPattern();
		Scanner scan = new Scanner(patternStr);
		scan.next(); scan.next(); scan.next(); scan.next();
		
		int[] dotExpected = {3, 0, 3, 4, 3, 0, 3, 4, 1, 8, 0, 0, 0, 0};
		int[] dotActual = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		int measures = 0;
		while(scan.hasNext() && measures < 14) {
			String curr = scan.next();
			
			if (curr.equals("|")) {
				measures += 1;
				continue;
			}
			
			for (int i = 0; i < curr.length() - 1; i++) {
				if (curr.charAt(i) == '.')
					dotActual[measures] += 1;
			}
		}
		
		for (int i = 0; i < 14; i++) {
			assertEquals(dotExpected[i], dotActual[i], "" + i);
		}
	}

}
