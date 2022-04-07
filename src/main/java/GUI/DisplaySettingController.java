package GUI;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import custom_model.SheetScore;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

public class DisplaySettingController{
	
	private PreviewController pc;
	ObservableList<String> fontValues = FXCollections.observableArrayList();
	ObservableList<String> noteSpaceValues = FXCollections.observableArrayList();
	ObservableList<String> lineSpaceValues = FXCollections.observableArrayList();
	ObservableList<String> noteSizeValues = FXCollections.observableArrayList();
	
	@FXML Button applyButton;
	@FXML Button resetButton;
	@FXML Button applyAndExitButton;
	@FXML 
	private ChoiceBox<String> fontValue;
	@FXML 
	private ChoiceBox<String> noteSpaceValue;
	@FXML 
	private ChoiceBox<String> lineSpaceValue;
	@FXML 
	private ChoiceBox<String> noteSizeValue;
	
	
	 public void setPreviewController(PreviewController pcInput) {
		 this.pc = pcInput;
	 }
	 
	 public void update() throws ValidityException, ParserConfigurationException, ParsingException, IOException {
		 loadFonts();
		 loadNoteSize();
		 loadLineSpace();
		 loadNoteSpace();
	 }
	
	public void ApplyHandler() throws ValidityException, ParserConfigurationException, ParsingException, IOException{
		System.out.println("Apply Button Clicked!");
		
		if(!lineSpaceValue.getValue().equals("--Choose--")) {
			SheetScore.lineSize = Double.parseDouble(lineSpaceValue.getValue());
			System.out.println("Loading...");
			pc.update();
			System.out.println("Now the line space has been changed to: " + SheetScore.lineSize);
		}
	}
	
	public void resetHandler() throws ValidityException, ParserConfigurationException, ParsingException, IOException{
		System.out.println("Rest Button Clicked!");
		SheetScore.lineSize = 10;
		pc.update();
	}

	public void applyandexitHandler() throws ValidityException, ParserConfigurationException, ParsingException, IOException{
		ApplyHandler();
		pc.displayWindow.hide();
		System.out.println("Apply&Reset Button Clicked!");
	}
	
	private void loadFonts() {
		fontValues.removeAll(fontValues);
		String a = "font1";
		String b = "font2";
		String c = "font3";
		String d = "font4";
		fontValues.addAll(a,b,c,d);
		fontValue.getItems().addAll(fontValues);
		
		
	}
	private void loadNoteSize() {
		noteSizeValues.removeAll(noteSizeValues);
		String a = "1";
		String b = "2";
		String c = "3";
		String d = "4";
		noteSizeValues.addAll(a,b,c,d);
		noteSizeValue.getItems().addAll(noteSizeValues);
		
		
	}
	private void loadNoteSpace() {
		noteSpaceValues.removeAll(noteSpaceValues);
		String a = "1";
		String b = "2";
		String c = "3";
		String d = "4";
		noteSpaceValues.addAll(a,b,c,d);
		noteSpaceValue.getItems().addAll(noteSpaceValues);
		
		
	}
	private void loadLineSpace() {
		//5 - 30
		lineSpaceValues.removeAll(lineSpaceValues);
		for(int i = 5; i <= 30; i+=5) {
			lineSpaceValues.add(i+"");
		}
		lineSpaceValue.getItems().addAll(lineSpaceValues);
		
		
	}

}
