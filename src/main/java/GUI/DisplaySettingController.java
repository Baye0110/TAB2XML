package GUI;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

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
	ObservableList<Integer> noteSpaceValues = FXCollections.observableArrayList();
	ObservableList<Integer> lineSpaceValues = FXCollections.observableArrayList();
	ObservableList<Integer> noteSizeValues = FXCollections.observableArrayList();
	
	@FXML Button applyButton;
	@FXML Button resetButton;
	@FXML Button applyAndExitButton;
	@FXML 
	private ChoiceBox<String> fontValue;
	@FXML 
	private ChoiceBox<Integer> noteSpaceValue;
	@FXML 
	private ChoiceBox<Integer> lineSpaceValue;
	@FXML 
	private ChoiceBox<Integer> noteSizeValue;
	
	
	 public void setPreviewController(PreviewController pcInput) {
		 this.pc = pcInput;
	 }
	 
	 public void update() throws ValidityException, ParserConfigurationException, ParsingException, IOException {
		 loadFonts();
		 loadNoteSize();
		 loadLineSpace();
		 loadNoteSpace();
	 }
	
	public void ApplyHandler(){
		System.out.println("Apply Button Clicked!");
	}
	
	public void resetHandler(){
		System.out.println("Rest Button Clicked!");
	}

	public void applyandexitHandler(){
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
		int a = 1;
		int b = 2;
		int c = 3;
		int d = 4;
		noteSizeValues.addAll(a,b,c,d);
		noteSizeValue.getItems().addAll(noteSizeValues);
		
		
	}
	private void loadNoteSpace() {
		noteSpaceValues.removeAll(noteSpaceValues);
		int a = 1;
		int b = 2;
		int c = 3;
		int d = 4;
		noteSpaceValues.addAll(a,b,c,d);
		noteSpaceValue.getItems().addAll(noteSpaceValues);
		
		
	}
	private void loadLineSpace() {
		lineSpaceValues.removeAll(lineSpaceValues);
		int a = 1;
		int b = 2;
		int c = 3;
		int d = 4;
		lineSpaceValues.addAll(a,b,c,d);
		lineSpaceValue.getItems().addAll(lineSpaceValues);
		
		
	}

}
