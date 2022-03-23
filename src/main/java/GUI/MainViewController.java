package GUI;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.jfugue.integration.MusicXmlParser;
import org.jfugue.pattern.Pattern;
import org.jfugue.pattern.Token;
import org.jfugue.pattern.Token.TokenType;
import org.jfugue.player.Player;
import org.staccato.StaccatoParserListener;

import converter.Converter;
import converter.measure.TabMeasure;
import custom_component_data.Measure;
import custom_component_data.Note;
import custom_component_data.Score;
import custom_component_data.Tied;
import custom_model.SheetScore;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.IndexRange;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import models.measure.note.notations.Slide;
import utility.Range;
import utility.Settings;

public class MainViewController extends Application {
	
	private Preferences prefs;
	public static ExecutorService executor = Executors.newSingleThreadExecutor();
	public File saveFile;
	private static boolean isEditingSavedFile;
	
	public Window convertWindow;
	public Window settingsWindow;

	public Highlighter highlighter;
	public Converter converter;
	org.jfugue.pattern.Pattern musicXMLParttern;
	int instrument_type = 0;
	TextField tempoInput;
	int tempoSpeed = 60;
	String drumString;
	Player player = new Player();

	@FXML  Label mainViewState;
	@FXML  TextField instrumentMode;
	
	@FXML public CodeArea mainText;

	@FXML  TextField gotoMeasureField;
	@FXML  BorderPane borderPane;
	@FXML  Button saveTabButton;
	@FXML  Button saveMXLButton;
	@FXML  Button showMXLButton;
	@FXML  Button previewButton;
	@FXML  Button goToline;
	@FXML  ComboBox<String> cmbScoreType;


	public MainViewController() {
		Settings s = Settings.getInstance();
		prefs = Preferences.userRoot();
		s.inputFolder = prefs.get("inputFolder", System.getProperty("user.home"));
		s.outputFolder = prefs.get("outputFolder", System.getProperty("user.home"));
		s.tsNum = Integer.parseInt(prefs.get("tsNum", "4"));
		s.tsDen = Integer.parseInt(prefs.get("tsDen", "4"));
		s.errorSensitivity = Integer.parseInt(prefs.get("errorSensitivity", "4"));
	}

	@FXML 
	public void initialize() {
		mainText.setParagraphGraphicFactory(LineNumberFactory.get(mainText));
		converter = new Converter(this);
		highlighter = new Highlighter(this, converter);
    	listenforTextAreaChanges();
	}

	@FXML
	private void handleCurrentSongSettings() {
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("GUI/currentSongSettingsWindow.fxml"));
			root = loader.load();
			CurrentSongSettingsWindowController controller = loader.getController();
			controller.setMainViewController(this);
			settingsWindow = this.openNewWindow(root, "Current Song Settings");
		} catch (IOException e) {
			Logger logger = Logger.getLogger(getClass().getName());
			logger.log(Level.SEVERE, "Failed to create new Window.", e);
		}
	}
	
	@FXML
	private void handleSystemDefaultSettings() {
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("GUI/systemDefaultSettingsWindow.fxml"));
			root = loader.load();
			SystemDefaultSettingsWindowController controller = loader.getController();
			controller.setMainViewController(this);
			settingsWindow = this.openNewWindow(root, "System Default Settings");
		} catch (IOException e) {
			Logger logger = Logger.getLogger(getClass().getName());
			logger.log(Level.SEVERE, "Failed to create new Window.", e);
		}
	}

	@FXML
	private void handleNew() {
		boolean userOkToGoAhead = promptSave();
		if (!userOkToGoAhead) return;
		this.mainText.clear();
		instrumentMode.setText("None");
		isEditingSavedFile = false;
	}

	@FXML
	private void handleOpen() {
		boolean userOkToGoAhead = promptSave();
		if (!userOkToGoAhead) return;

		String startFolder = prefs.get("inputFolder", System.getProperty("user.home"));
		File openDirectory;
		if (saveFile!=null && saveFile.canRead()) {
			openDirectory = new File(saveFile.getParent());
		}else
			openDirectory = new File(startFolder);

		if(!openDirectory.canRead()) {
			openDirectory = new File("c:/");
		}

		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
		fileChooser.setInitialDirectory(openDirectory);
		File openedFile = fileChooser.showOpenDialog(MainApp.STAGE);
		if (openedFile==null) return;
		if (openedFile.exists()) {
			try {
				String newText = Files.readString(Path.of(openedFile.getAbsolutePath())).replace("\r\n", "\n");
				mainText.replaceText(new IndexRange(0, mainText.getText().length()), newText);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}

		saveFile = openedFile;
		isEditingSavedFile = true;

	}

	@FXML
	private boolean handleSaveAs() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save As");
		fileChooser.setInitialDirectory(new File(Settings.getInstance().outputFolder));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
		if (saveFile!=null) {
			fileChooser.setInitialFileName(saveFile.getName());
			fileChooser.setInitialDirectory(new File(saveFile.getParent()));
		}

		File newSaveFile = fileChooser.showSaveDialog(MainApp.STAGE);
		if (newSaveFile==null) return false;
		try {
			FileWriter myWriter = new FileWriter(newSaveFile.getPath());
			myWriter.write(mainText.getText());
			myWriter.close();

			saveFile = newSaveFile;
			isEditingSavedFile = true;
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	@FXML
	private boolean handleSave() {
		if (!isEditingSavedFile || saveFile==null || !saveFile.exists())
			return this.handleSaveAs();
		try {
			FileWriter myWriter = new FileWriter(saveFile.getPath());
			myWriter.write(mainText.getText());
			myWriter.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	private boolean promptSave() {

		//we don't care about overwriting a blank file. If file is blank, we are ok to go. it doesn't matter if it is saved or not
		if (mainText.getText().isBlank())  return true;

		try {
			if (saveFile!=null && Files.readString(Path.of(saveFile.getAbsolutePath())).replace("\r\n", "\n").equals(mainText.getText()))
				return true;    //if file didn't change, we are ok to go. no need to save anything, no chance of overwriting.
		}catch (Exception e){
			e.printStackTrace();
		}

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Unsaved file");
		alert.setHeaderText("This document is unsaved and will be overwritten. Do you want to save it first?");
		alert.setContentText("Choose your option.");

		ButtonType buttonTypeSave = new ButtonType("Save");
		ButtonType buttonTypeOverwrite = new ButtonType("Overwrite");
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(buttonTypeSave, buttonTypeOverwrite, buttonTypeCancel);

		Optional<ButtonType> result = alert.showAndWait();

		boolean userOkToGoAhead = false;
		if (result.get() == buttonTypeSave){
			boolean saved;
			if (isEditingSavedFile) {
				saved = handleSave();
			}else {
				saved = handleSaveAs();
			}
			if (saved)
				userOkToGoAhead = true;
		} else if (result.get() == buttonTypeOverwrite) {
			// ... user chose "Override". we are good to go ahead
			userOkToGoAhead = true;
		}
		//if user chose "cancel", userOkToGoAhead is still false. we are ok.
		return userOkToGoAhead;
	}

	private Window openNewWindow(Parent root, String windowName) {
		Stage stage = new Stage();
		stage.setTitle(windowName);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(MainApp.STAGE);
		stage.setResizable(false);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		return scene.getWindow();
	}

	@FXML
	private void saveTabButtonHandle() {
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("GUI/convertWindow.fxml"));
			root = loader.load();
			SaveMXLController controller = loader.getController();
			controller.setMainViewController(this);
			convertWindow = this.openNewWindow(root, "ConversionOptions");
		} catch (IOException e) {
			Logger logger = Logger.getLogger(getClass().getName());
			logger.log(Level.SEVERE, "Failed to create new Window.", e);
		}
	}
	
	@FXML
	void saveMXLButtonHandle() {
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("GUI/saveMXLWindow.fxml"));
			root = loader.load();
			SaveMXLController controller = loader.getController();
			controller.setMainViewController(this);
			convertWindow = this.openNewWindow(root, "Save MusicXML");
		} catch (IOException e) {
			Logger logger = Logger.getLogger(getClass().getName());
			logger.log(Level.SEVERE, "Failed to create new Window.", e);
		}
	}
	
	@FXML
	private void showMXLButtonHandle() {
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("GUI/showMXL.fxml"));
			root = loader.load();
			ShowMXLController controller = loader.getController();
			controller.setMainViewController(this);
			controller.update();
			convertWindow = this.openNewWindow(root, "MusicXML output");
		} catch (IOException e) {
			Logger logger = Logger.getLogger(getClass().getName());
			logger.log(Level.SEVERE, "Failed to create new Window.", e);
		}
	}
	
	@FXML
	private void previewButtonHandle() throws Exception {
	
		System.out.println("Preview Button Clicked!");
		try {
	
			Stage window = new Stage();
			window.setTitle("Music sheet");
			this.tempoInput = new TextField("60");
			Label tempoLabel = new Label("Tempo:");
			
			Button play = new Button("Play");
			Button pause = new Button("Pause");
			Button exit = new Button("Exit");
			
			play.setTranslateX(100);
			
			tempoLabel.setTranslateX(150);
			tempoLabel.setFont(new Font(15));
			
			tempoInput.setTranslateX(200);
			
			
			pause.setTranslateX(500);
			
			exit.setTranslateX(1100);
			
			Score score = new Score(converter.getMusicXML());
			SheetScore sheet = new SheetScore(score, 10, 1050);
			sheet.setTranslateX(50);
			
			ScrollPane sp = new ScrollPane();
			sp.setContent(sheet);
			sp.setTranslateX(50);
			sp.setMaxWidth(1150);
			sp.setMaxHeight(600);
			sp.setMinHeight(sheet.getChildren().get(0).minHeight(0)+50);
			
			TextField goToMeasure = new TextField("1");
			Label measureLabel = new Label("Go to Measure: ");
			measureLabel.setTranslateX(700);
			measureLabel.setFont(Font.font(15));
			goToMeasure.setTranslateX(840);
			goToMeasure.setMaxWidth(80);
			Button goButton = new Button("Go!");
			goButton.setTranslateX(930);
			
			Pane PlayPane = new Pane();
			
			PlayPane.getChildren().add(play);
			PlayPane.getChildren().add(tempoLabel);
			PlayPane.getChildren().add(tempoInput);
			PlayPane.getChildren().add(pause);
			PlayPane.getChildren().add(exit);
			PlayPane.getChildren().add(goToMeasure);
			PlayPane.getChildren().add(measureLabel);
			PlayPane.getChildren().add(goButton);
			
			PlayPane.setTranslateY(25);
			VBox root = new VBox(sp, PlayPane);
			Scene scene = new Scene(root, 1250, 700);
			window.setScene(scene);	
			
			StaccatoParserListener listner = new StaccatoParserListener();
			MusicXmlParser parser = new MusicXmlParser();
			parser.addParserListener(listner);
						
			parser.parse(converter.getMusicXML());

			List<Note> noteList = new ArrayList<Note>();
			
			String stringInstrument ="";
			System.out.println("S0: " + listner.getPattern().toString());
			for(Measure measures: score.getParts().get(0).getMeasures()) {
				for(Note notes: measures.getNotes()) {
					noteList.add(notes);
				}
			}
			
			String[] stepToNoteMap = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
			
			if(score.getParts().get(0).getMeasures().get(0).getTab()) {
				int count = 0;
				
				for(Token tokens: listner.getPattern().getTokens()) {
					if(tokens.getType() == TokenType.NOTE) {
								
						double graceTime = 0.0;
						Note current = noteList.get(count);
						while (current.getGrace()) {
							int stepIndex = mapToNote(current.getStep(), current.getAlter());
							int octave = current.getOctave() - (stepIndex < 0 ? 1 : 0);
							stringInstrument += " " + stepToNoteMap[stepIndex < 0 ? 11 : stepIndex % 12] + octave + "O.";
							graceTime += (double)1/96;
							current = noteList.get(++count);
						}
						
						String duration = mapToDuration(current.getType());
						if (count > 0 && noteList.get(count - 1).getGrace()) {
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
						
						List<Tied> tieds = current.getNotation().getTieds();
						if (tieds.size() > 0) {
							if (tieds.get(0).getType().equals("start") || (tieds.size() > 1 && tieds.get(1).getType().equals("start")) ) {
								duration += "-";
								System.out.println("Start the vote!");
							}
							if (tieds.get(0).getType().equals("stop") || (tieds.size() > 1 && tieds.get(1).getType().equals("stop")) ) {
								duration = "-" + duration;
								System.out.println("Stop the vote!");
							}
						}
						
						String slideStart = "";
						String slideStop = "";
						List<custom_component_data.Slide> slides = current.getNotation().getSlides();
						if (slides.size() > 0 && slides.get(0).getType().equals("start")) {
							int countAfterSlide = count + 1;
							while(noteList.get(countAfterSlide).getNotation().getSlides().size() > 0 && !noteList.get(countAfterSlide).getNotation().getSlides().get(0).getType().equals("stop")) {
								countAfterSlide ++;
							}
							stringInstrument += developSlideString(current, noteList.get(countAfterSlide));
							
//							if (slides.get(0).getType().equals("start")) {
//								slideStart += " :CE(65,127) :CE(5,64)";
//							}
//							else if (slides.get(0).getType().equals("stop")) {
//								slideStop += " :CE(65,0)";
//							}
						}
									
						int stepIndex = mapToNote(current.getStep(), current.getAlter());
						int octave = current.getOctave() - (stepIndex < 0 ? 1 : 0);
						
						if(noteList.get(count).getChord()) {
							stringInstrument += "+" + stepToNoteMap[stepIndex < 0 ? 11 : stepIndex % 12] + octave + duration + "A90";
						}else {
							stringInstrument += slideStart + " " + stepToNoteMap[stepIndex < 0 ? 11 : stepIndex % 12] + octave + duration + "A90" + slideStop;
						}
						
						count++;
					}else {
						stringInstrument += " " + tokens;
					}
				}
				musicXMLParttern = new Pattern(stringInstrument);
				if(score.getParts().get(0).getName().equals("Bass")) {
					musicXMLParttern.setInstrument("Acoustic_Bass");
					instrument_type = 1;
				}else {
					musicXMLParttern.setInstrument("Guitar");
					instrument_type = 2;
				}
			}else {
				
				String drumSet = "V9 ";
				int countDrum = 0;
				
				for (Measure m: score.getParts().get(0).getMeasures()) {
					for (int i = 0; i < m.getNotes().size(); i++) {
						Note n = m.getNotes().get(i);
						if (i + 1 < m.getNotes().size() && m.getNotes().get(i+1).getChord()) {
							String instrumentName = Integer.toString(score.getParts().get(0).getInstruments().get(n.getInstrumentID()).getMidiUnpitched() - 1);					
							String duration = mapToDuration(n.getType());
							for (int dot = 0; dot < n.getDot(); dot++) {
								duration += ".";
							}
							drumSet += instrumentName + duration + "+";
						}
						else {
							double graceTime = 0.0;
							while (n.getGrace()) {
								drumSet += Integer.toString(score.getParts().get(0).getInstruments().get(n.getInstrumentID()).getMidiUnpitched() - 1) + "X.A90 ";
								graceTime += (double)1/48;
								n = m.getNotes().get(++i);
							}
							
							String duration = mapToDuration(n.getType());
							if (i > 0 && noteList.get(i - 1).getGrace()) {
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
							
							if (n.getRest()) {
								drumSet += "R" + duration + " ";
							}
							else {
								String instrumentName = Integer.toString(score.getParts().get(0).getInstruments().get(n.getInstrumentID()).getMidiUnpitched() - 1);
								drumSet += instrumentName + duration + "A90 ";
							}
						}
						System.out.println("left: " + (noteList.size() - i + 1));
					}
					
					drumSet += "| ";
				}
				
//				for(Token tokens: listner.getPattern().getTokens()) {
//					if(tokens.getType() == TokenType.NOTE) {
//						if (noteList.get(countDrum).getRest()) {
//							drumSet += tokens + " ";
//						}
//						else if(countDrum + 1 < noteList.size() && noteList.get(countDrum + 1).getChord()) {
////							while(drumSet.charAt(drumSet.length() - 1) != ']') {
////								drumSet = drumSet.substring(0, drumSet.length() - 1);
////							}
//							
//							String instrumentName = Integer.toString(score.getParts().get(0).getInstruments().get(noteList.get(countDrum).getInstrumentID()).getMidiUnpitched());							
//							drumSet += instrumentName + "+";
//						}
//						else {
//							String duration = mapToDuration(noteList.get(countDrum).getType());
//							if (noteList.get(countDrum).getGrace()) {
//								duration = "O.";
//							} else {
//								for (int dot = 0; dot < noteList.get(countDrum).getDot(); dot++) {
//									duration += ".";
//								}
//							}
//							
//							String instrumentName = Integer.toString(score.getParts().get(0).getInstruments().get(noteList.get(countDrum).getInstrumentID()).getMidiUnpitched());							
//							drumSet += instrumentName + duration + " ";
//						}
//						countDrum++;
//					} else if (!(tokens.getType() == TokenType.INSTRUMENT)){
//						drumSet += tokens + " ";
//					}
//					System.out.println(tokens.toString() + countDrum);
//				}

				
				System.out.println("S1: " + drumSet);
				musicXMLParttern = new Pattern(drumSet);
				instrument_type = 3;
				System.out.println("s2:" + drumSet);

			}
			
			if(tempoSpeed != Integer.parseInt(tempoInput.getText())) {
				tempoSpeed = Integer.parseInt(tempoInput.getText());
			}
			
			
			play.setOnAction(e -> {
				// set tempo
				if(tempoSpeed != Integer.parseInt(tempoInput.getText())) {
					tempoSpeed = Integer.parseInt(tempoInput.getText());
				}
				System.out.println("String1: " + musicXMLParttern);
					if(instrument_type == 1) {
						musicXMLParttern.setTempo(tempoSpeed);
						System.out.println("Bass is playing");
						window.setTitle("Bass is playing");
					}else if(instrument_type == 2){
						musicXMLParttern.setTempo(tempoSpeed);
						System.out.println("Guitar is playing");
						window.setTitle("Guitar is playing");		
					}else if(instrument_type == 3){
						musicXMLParttern.setTempo(tempoSpeed);
						System.out.println("Drum is playing");
						window.setTitle("Drum is playing");	
					}
					
					System.out.println("String2: " + musicXMLParttern);
				if(player.getManagedPlayer().isPaused()) {
					player.getManagedPlayer().resume();
					System.out.println("Music is resumed");
				}else if(player.getManagedPlayer().isPlaying()) {
					System.out.println("Music is Playing");
				}else {
					this.player = new Player();
					player.delayPlay(0,musicXMLParttern.toString());
				}
				System.out.println("String3: " + musicXMLParttern);
				System.out.println("The tempoSpeed is: " + tempoSpeed);
			});
			
			
			pause.setOnAction(e -> {
				if(player.getManagedPlayer().isPlaying()) {
					player.getManagedPlayer().pause();
					window.setTitle("Music Paused");
					System.out.println("Music paused");
				}else if(player.getManagedPlayer().isFinished()){
					window.setTitle("Music sheet");
					System.out.println("playing a music first");
				}else {
					window.setTitle("Music sheet");
					System.out.println("playing a music first");
				}
			});
			

			exit.setOnAction(e -> {
				window.close();
				if(player.getManagedPlayer().isPlaying()) {
					player.getManagedPlayer().finish();
				}
					System.out.println("preview windows exited");
			});
			window.show();
			window.setOnHiding(e -> {
				if(player.getManagedPlayer().isPlaying()) {
					player.getManagedPlayer().finish();
				}
				System.out.println("preview windows exited");
			});
			
			goButton.setOnAction(e -> {
				double valToSet = sheet.getMeasurePosition(Integer.parseInt(goToMeasure.getText())) / sheet.getHeight();
				sp.setVvalue(valToSet);
			});
			
		} catch (Exception e) {
			Logger logger = Logger.getLogger(getClass().getName());
			logger.log(Level.SEVERE, "Failed to create new Window.", e);
		}
		// converter.getMusicXML() returns the MusicXML output as a String
		
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
	
	public static void unImplementedFunctionOnClick(String functionName, String functionDesc) {
		Text lack = new Text(functionDesc + " :) ... \n\n\n\n\n **(Once this feature is developed)");
		lack.setTranslateY(100);
		lack.setFont(Font.font(20));
		lack.setTextAlignment(TextAlignment.CENTER);
		Pane lackPane = new Pane(lack);
		Scene lackScence = new Scene(lackPane, 400, 300);
		Stage inImplemented = new Stage();
		inImplemented.setScene(lackScence);
		inImplemented.show();
		inImplemented.setTitle(functionName);
	}

	public void refresh() {
        mainText.replaceText(new IndexRange(0, mainText.getText().length()), mainText.getText()+" ");
    }

	@FXML
	private void handleGotoMeasure() {
		int measureNumber = Integer.parseInt( gotoMeasureField.getText() );
		if (!goToMeasure(measureNumber)) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Measure " + measureNumber + " could not be found.");
			alert.setHeaderText(null);
			alert.show();
		}
	}
	
    private boolean goToMeasure(int measureCount) {
        TabMeasure measure = converter.getScore().getMeasure(measureCount);
        if (measure == null) return false;
        List<Range> linePositions = measure.getRanges();
        int pos = linePositions.get(0).getStart();
    	mainText.moveTo(pos);
        mainText.requestFollowCaret();
        mainText.requestFocus();
        return true;
    }

    public void listenforTextAreaChanges() {
        //Subscription cleanupWhenDone = 
    	mainText.multiPlainChanges()
                .successionEnds(Duration.ofMillis(350))
                .supplyTask(this::update)
                .awaitLatest(mainText.multiPlainChanges())
                .filterMap(t -> {
                    if(t.isSuccess()) {
                        return Optional.of(t.get());
                    } else {
                        t.getFailure().printStackTrace();
                        return Optional.empty();
                    }
                })
                .subscribe(highlighter::applyHighlighting);
    }
    
    public Task<StyleSpans<Collection<String>>> update() {
    	String text = mainText.getText();

        Task<StyleSpans<Collection<String>>> task = new Task<>() {
            @Override
            protected StyleSpans<Collection<String>> call() {
            	converter.update();
            	
                if (converter.getScore().getTabSectionList().isEmpty()){
                	saveMXLButton.setDisable(true);
                	previewButton.setDisable(true);
                	showMXLButton.setDisable(true);
                }
                else
                {
                	saveMXLButton.setDisable(false);
                	previewButton.setDisable(false);
                	showMXLButton.setDisable(false);
                }
                return highlighter.computeHighlighting(text);
            }
        };
        executor.execute(task);
        task.isDone();
        return task;
    }
    
	@Override
	public void start(Stage primaryStage) throws Exception {

	}
}