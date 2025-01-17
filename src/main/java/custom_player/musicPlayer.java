package custom_player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;

import org.jfugue.integration.MusicXmlParser;
import org.jfugue.pattern.Pattern;
import org.jfugue.pattern.Token;
import org.jfugue.pattern.Token.TokenType;
import org.jfugue.player.Player;
import org.staccato.StaccatoParserListener;

import custom_component_data.Measure;
import custom_component_data.Note;
import custom_component_data.Score;
import custom_component_data.Tied;
import custom_model.SheetScore;
import custom_model.note.NoteUnit;
import javafx.scene.control.Alert;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

public class musicPlayer {

	List<Note> noteList = new ArrayList<Note>();
	Score score;
	SheetScore sheet;
	StaccatoParserListener listner = new StaccatoParserListener();
	MusicXmlParser parser = new MusicXmlParser();
	Player player = new Player();
	org.jfugue.pattern.Pattern musicXMLParttern;
	String stringInstrument ="";
	String drumSet = "V9 ";
	String[] stepToNoteMap = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
	int instrument_type = -1;
	int tempoSpeed = 60;
	
	public musicPlayer(Score score, SheetScore sheet, String musicXml) throws ParserConfigurationException, ValidityException, ParsingException, IOException {
		this.score = score;
		this.sheet = sheet;
		parser.addParserListener(listner); 
		parser.parse(musicXml);
		setNoteList();
		SetInstrumentType();
		setInstrument();
		getRepeat();
		this.sheet.generateBasePlayTimings(score);
		resetMusicToBeginning();
	}
	
	
	public void setNoteList() {
		for(Measure measures: score.getParts().get(0).getMeasures()) {
			for(Note notes: measures.getNotes()) {
				noteList.add(notes);
			}
		}
	}
	public int getTempo() {
		return this.tempoSpeed;
	}
	
	public void setTempo(String tempoInput) {
		if(tempoSpeed != Integer.parseInt(tempoInput)) {
			tempoSpeed = Integer.parseInt(tempoInput);
		}
		if (tempoSpeed <= 0) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("The tempo should bigger than 0");
			alert.setHeaderText(null);
			alert.show();
			System.out.println("please set valid tempo!");
		}
		musicXMLParttern.setTempo(tempoSpeed);
		this.sheet.setTempoOnTimings(tempoSpeed);
	}
	
	public void getPlayType() {
		if(instrument_type == 1) {
			System.out.println("Bass is playing");
		}else if(instrument_type == 2) {
			System.out.println("Guitar is playing");
		}else if(instrument_type == 3) {
			System.out.println("Drum is playing");
		}
	}
	
	public void play(String tempoInput) {
		setTempo(tempoInput);
		getPlayType();
		while (!this.sheet.getThreadKilled() && !this.isPlaying()) {
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if(isPaused()) {
			resume();
		}else if(isFinished()) {			
			this.player = new Player();
			if (NoteUnit.pressed == null) {
				player.delayPlay(0, musicXMLParttern.toString());
			}
			else {
				this.player = new Player();
				player.delayPlay(0, this.generateSpecificPattern());
			}
			this.sheet.startHighlight();
		}else {
			if (NoteUnit.pressed == null) {
				player.delayPlay(0, musicXMLParttern.toString());
			}
			else {
				this.player = new Player();
				player.delayPlay(0, this.generateSpecificPattern());
			}
			this.sheet.startHighlight();
		}
	}
	
	public boolean isPaused() {
		return player.getManagedPlayer().isPaused();
	}
	
	
	public boolean isPlaying() {
		return player.getManagedPlayer().isPlaying();
	}
	
	
	public void resume() {
		this.player = new Player();
		player.delayPlay(0, this.generateSpecificPattern());
		this.sheet.startHighlight();
	}
	
	
	public void pause() {
		if(isPlaying()) {
			player.getManagedPlayer().pause();
			sheet.stopHighLight();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.sheet.removeAllHighlight();
			System.out.println("Music paused");
		}else {
			System.out.println("playing a music first");
		}
	}
	
	public void exit() {
		if(isPlaying()) {
			finish();
			sheet.stopHighLight();
		}
	}
	
	public boolean isFinished() {
		return player.getManagedPlayer().isFinished();
	}
	
	public void finish() {
		player.getManagedPlayer().finish();
		if (this.isPaused())
			player = new Player();		
	}
	
	//1:BASS 2:GUITAR 3:DRUMS
	public void SetInstrumentType() {
		if(score.getParts().get(0).getMeasures().get(0).getTab()) {
			if(score.getParts().get(0).getName().equals("Bass")) {
				this.instrument_type = 1;	
				setStringInstrument();
			}else {
				this.instrument_type = 2;
				setStringInstrument();
			}
		}else {
			this.instrument_type = 3;
			setDrumSet();
		}
	}
	
	public void setInstrument() {
		if(instrument_type == 1) {
			musicXMLParttern = new Pattern(stringInstrument);
			musicXMLParttern.setInstrument("Acoustic_Bass");
		}else if(instrument_type ==2) {
			musicXMLParttern = new Pattern(stringInstrument);
			musicXMLParttern.setInstrument("Guitar");
		}else if(instrument_type == 3) {
			musicXMLParttern = new Pattern(drumSet);
		}
	}
	
	public void setStringInstrument() {
		int count = 0;	
		double graceTime = 0.0;
		for(Token tokens: listner.getPattern().getTokens()) {
			if(tokens.getType() == TokenType.NOTE) {	
				Note current = noteList.get(count);
				while (current.getGrace()) {
					int stepIndex = mapToNote(current.getStep(), current.getAlter());
					int octave = current.getOctave() - (stepIndex < 0 ? 1 : 0);
					if (current.getChord()) {
						stringInstrument += "+";
					}
					else {
						stringInstrument += " ";
						graceTime += (double)1/48.0;
					}
					stringInstrument += stepToNoteMap[stepIndex < 0 ? 11 : stepIndex % 12] + octave + "X.";
					
					current = noteList.get(++count);
				}
					
				String duration = mapToDuration(current.getType());
				if (count > 0 && noteList.get(count - 1).getGrace() || graceTime > 0.0) {
					double durationCalculation = 1.0/current.getType();
					double dotAddition = durationCalculation;
					for (int i = 0; i < current.getDot(); i++) {
						dotAddition /= 2.0;
						durationCalculation += dotAddition;
					}
					durationCalculation -= graceTime;
					duration = "/" + durationCalculation;
				}
				else {
					for (int i = 0; i < current.getDot(); i++) {
						duration += ".";
					}
				}
				
				duration = this.applyTied(current.getNotation().getTieds(), duration);
					
				String slideStart = "";
				String slideStop = "";
				List<custom_component_data.Slide> slides = current.getNotation().getSlides();
				if (slides.size() > 0 && slides.get(0).getType().equals("start")) {
					int countAfterSlide = count + 1;
					while(noteList.get(countAfterSlide).getNotation().getSlides().size() > 0 && !noteList.get(countAfterSlide).getNotation().getSlides().get(0).getType().equals("stop")) {
						countAfterSlide ++;
					}
					stringInstrument += developSlideString(current, noteList.get(countAfterSlide));

					count++;
					continue;
				}
				
				int stepIndex = mapToNote(current.getStep(), current.getAlter());
				int octave = current.getOctave() - (stepIndex < 0 ? 1 : 0);
				String timeMod = this.generateTimeModString(current.getTimeModification());
				
				
				
				if(noteList.get(count).getChord()) {
					stringInstrument += "+" + stepToNoteMap[stepIndex < 0 ? 11 : stepIndex % 12] + octave + duration + timeMod + "A90";
				}else {
					stringInstrument += slideStart + " " + stepToNoteMap[stepIndex < 0 ? 11 : stepIndex % 12] + octave + duration + timeMod + "A90" + slideStop;
				}
				
				if ( !(count + 1 < noteList.size() && noteList.get(count + 1).getChord()) ) {
					graceTime = 0.0;
				}
				
				count++;
			}else {
				stringInstrument += " " + tokens;
			}
		}
	}
	
	public void setDrumSet() {
		for (Measure m: score.getParts().get(0).getMeasures()) {
			double graceTime = 0.0;
			for (int i = 0; i < m.getNotes().size(); i++) {
				Note n = m.getNotes().get(i);
				if (!n.getGrace() && i + 1 < m.getNotes().size() && m.getNotes().get(i+1).getChord()) {
					String instrumentName = Integer.toString(score.getParts().get(0).getInstruments().get(n.getInstrumentID()).getMidiUnpitched() - 1);					
					String duration = mapToDuration(n.getType());
					if (graceTime > 0.0) {
						double durationCalculation = 1.0/n.getType();
						double dotAddition = durationCalculation;
						for (int dot = 0; dot < n.getDot(); dot++) {
							dotAddition /= 2.0;
							durationCalculation += dotAddition;
						}
						durationCalculation -= graceTime;
						duration = "/" + durationCalculation;
					} else {
						for (int dot = 0; dot < n.getDot(); dot++) {
							duration += ".";
						}
					}
					drumSet += instrumentName + duration + "+";
				}
				else {
					while (n.getGrace()) {
						drumSet += Integer.toString(score.getParts().get(0).getInstruments().get(n.getInstrumentID()).getMidiUnpitched() - 1) + "X.";
						if (m.getNotes().get(i+1).getChord()) {
							drumSet += "+";
						}
						else {
							drumSet += "A90 ";
							graceTime += (double)1/48;
						}
						n = m.getNotes().get(++i);
						continue;
					}
					
					String duration = mapToDuration(n.getType());
					if (i > 0 && graceTime > 0.0) {
						double durationCalculation = 1.0/n.getType();
						double dotAddition = durationCalculation;
						for (int dot = 0; dot < n.getDot(); dot++) {
							dotAddition /= 2.0;
							durationCalculation += dotAddition;
						}
						durationCalculation -= graceTime;
						duration = "/" + durationCalculation;
					}
					else {
						for (int dot = 0; dot < n.getDot(); dot++) {
							duration += ".";
						}
					}
					
					if (n.getNotation() != null) {
						duration = this.applyTied(n.getNotation().getTieds(), duration);
					}
					String timeMod = this.generateTimeModString(n.getTimeModification());
					
					if (n.getRest()) {
						drumSet += "R" + duration + " ";
					}
					else {
						String instrumentName = Integer.toString(score.getParts().get(0).getInstruments().get(n.getInstrumentID()).getMidiUnpitched() - 1);
						drumSet += instrumentName + duration + timeMod + "A90 ";
					}
				}
			}
			
			if (m.getNotes().size() == 0) {
				double duration = (double) m.getTimeSignature()[0] / m.getTimeSignature()[1];
				drumSet += " R/" + duration + " "; 
			}
			
			drumSet += "| ";
		}
	}
	
	public int mapToNote(char step, int alter) {
		HashMap<Character, Integer> mapping = new HashMap<>();
		mapping.put('C', 0);
		mapping.put('D', 2);
		mapping.put('E', 4);
		mapping.put('F', 5);
		mapping.put('G', 7);
		mapping.put('A', 9);
		mapping.put('B', 11);
		
		int note = mapping.get(step) + alter;
		return note;
	}
	
	public static String mapToDuration(int type) {
		String s = null;
		
		switch(type) {
		case 1:
			s = "W";
			break;
		case 2:
			s = "H";
			break;
		case 4:
			s = "Q";
			break;
		case 8:
			s = "I";
			break;
		case 16: 
			s = "S";
			break;
		case 32:
			s = "T";
			break;
		case 64:
			s = "X";
			break;
		case 128:
			s = "O";
			break;
		default:
			s = "Q";
		}
			
		return s;
	}
	public String developSlideString(Note start, Note end) {
		String slide = "";
		
		int startNote = (mapToNote(start.getStep(), start.getAlter()) + 12 * start.getOctave());
		int endNote = (mapToNote(end.getStep(), end.getAlter()) + 12 * end.getOctave());
		double durationCalculation = 1.0/start.getType();
		double dotAddition = durationCalculation;
		for (int i = 0; i < start.getDot(); i++) {
			dotAddition /= 2.0;
			durationCalculation += dotAddition;
		}
		double steps = endNote - startNote;
		for (int i = startNote; i < endNote; i++) {
			slide += " " + i + "/" + (durationCalculation/steps);
		}
		
		return slide;
	}
	
	public int getInstrumentType() {
		return this.instrument_type;
	}
	
	public String toString() {
		if(instrument_type == 1 || instrument_type == 2) {
			return stringInstrument + "";
		}else {
			return drumSet + "";
		}
	}
	public List<Note> getNote(){
		return this.noteList;
	}
	
	public String generateTimeModString(HashMap<String, Integer> modification) {
		String timeMod = "";
		if (modification == null)
			return timeMod;
		
		timeMod = "*" + modification.get("actual") + ":" + modification.get("normal");		
		return timeMod;
	}
	

	public String applyTied(List<Tied> tieds, String duration) {
		if (tieds.size() > 0) {
			if (tieds.get(0).getType().equals("start") || (tieds.size() > 1 && tieds.get(1).getType().equals("start")) ) {
				duration += "-";
			}
			if (tieds.get(0).getType().equals("stop") || (tieds.size() > 1 && tieds.get(1).getType().equals("stop")) ) {
				duration = "-" + duration;
			}
		}
		
		return duration;
	}
	

	public String generateSpecificPattern() {
		StringBuilder str = new StringBuilder("T" + this.tempoSpeed + " ");
		if (this.instrument_type == 3) {str.append("V9 ");}
		else if (this.instrument_type == 2) {str.append("I[Guitar] ");}
		else if (this.instrument_type == 1) {str.append("I[Acoustic_Bass] ");}
		
		int measure = 1;
		int note = 1;
		
		int targetMeasure = NoteUnit.pressed.getMeasure();
		int targetNote = NoteUnit.pressed.getNoteNum();
		int repeated = 0;
		boolean normalMeasure = true;
		for (int i = 0; i < NoteUnit.pressed.getMeasure() - 1; i++) {
			Measure m = score.getParts().get(0).getMeasures().get(i);
			if (m.getIsRepeatStart() || !normalMeasure) {
				repeated += 1;
				normalMeasure = false;
			}
			if (m.getIsRepeatStop()) {
				targetMeasure += repeated * m.getBarLineRight().getRepeatNum() - 1;
				repeated = 0;
			}
		}
		
		Scanner tokens = new Scanner(this.musicXMLParttern.toString());
		
		while (tokens.hasNext()) {
			String token = tokens.next();
			if (measure == targetMeasure) {
				if (note == targetNote) {
					str.append(token.toString() + " ");
				}
				else if (!token.equals("|") && token.charAt(0) != 'V' && token.charAt(0) != 'I' && token.charAt(0) != 'T') {
					note ++;
				}
			}
			else if (token.equals("|")) {
				measure ++;
			}
		}
		return str.toString();
	}
	
	public void getRepeat() {
		Boolean sameMeasure = true;
		List<Measure> measures = new ArrayList<>();
		Scanner scan = new Scanner(musicXMLParttern.toString());
		List<String> scan2 = new ArrayList<String>();
		String scan1 = "";
		while(scan.hasNext()) {
			String s = scan.next();
			if(s.equals("|")) {
				scan1 += "| ";
				scan2.add(scan1);
				scan1 = "";
			}else {
				scan1 += (s + " ");
			}
			
		}
		measures = score.getParts().get(0).getMeasures();
		
		StringBuilder repeat = new StringBuilder();
		StringBuilder string = new StringBuilder();
	
		for(int i = 0; i < measures.size(); i++) {
			
			if(measures.get(i).getIsRepeatStart() || !sameMeasure) {
				repeat.append(scan2.get(i));
				sameMeasure = false;
			}
			if(sameMeasure) {
				string.append(scan2.get(i));
			}

			if(measures.get(i).getIsRepeatStop()) {
				sameMeasure = true;
				String copy = repeat.toString();
				for(int j = 1; j < measures.get(i).getBarLineRight().getRepeatNum(); j++) {
					repeat.append(copy);
				}
				string.append(repeat);
			}
		}
		
		String s = string.toString();
		musicXMLParttern = new Pattern(s);
	}
	
	public void resetMusicToBeginning() {
		this.sheet.stopHighLight();
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.sheet.removeAllHighlight();
		NoteUnit.pressed = null;
	}
}
