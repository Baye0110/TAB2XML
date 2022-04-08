package GUI;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import custom_model.MusicMeasure;
import custom_model.SheetScore;
import custom_model.note.BoxedText;
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
	ObservableList<String> taleWidthValues = FXCollections.observableArrayList();
	
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
	private ChoiceBox<String> taleWidthValue;
	
	 public void setPreviewController(PreviewController pcInput) {
		 this.pc = pcInput;
	 }
	 
	 public void update() throws ValidityException, ParserConfigurationException, ParsingException, IOException {
		fontValue.setBackground(null);
		lineSpaceValue.setBackground(null);
		noteSpaceValue.setBackground(null);
		taleWidthValue.setBackground(null);
		
		loadFonts();
		loadNoteSize();
		loadLineSpace();
		loadNoteSpace();
		 
		lineSpaceValue.setValue("10");
		taleWidthValue.setValue("1045");
		fontValue.setValue("Calibri");
		noteSpaceValue.setValue("400");
	 }
	
	public void ApplyHandler() throws ValidityException, ParserConfigurationException, ParsingException, IOException{
		System.out.println("Apply Button Clicked!");
		boolean change = false;
		String lsv = lineSpaceValue.getValue();
		String fv = fontValue.getValue();
		String nsv = noteSpaceValue.getValue();
		String tw = taleWidthValue.getValue();
		
		if(!lsv.equals("--Choose--")) {
			if(SheetScore.lineSize != Double.parseDouble(lsv)) {
				SheetScore.lineSize = Double.parseDouble(lsv);
				change = true;
			}
		}
		
		if(!fv.equals("--Choose--")) {
			if(!MusicMeasure.customizefont.equalsIgnoreCase(fv)) {
				MusicMeasure.customizefont = fv;
				BoxedText.customizefont = fv;
				change = true;
			}

		}
		
		if(!nsv.equals("--Choose--")) {
			if(MusicMeasure.scale != Double.parseDouble(nsv)) {
				MusicMeasure.scale = Double.parseDouble(nsv);
				change = true;
			}
		}
		
		if(!tw.equals("--Choose--")) {
			if(SheetScore.pageWidth != Double.parseDouble(tw)) {
				SheetScore.pageWidth = Double.parseDouble(tw);
				change = true;
			}
		}
		if(change) {
			pc.update();
		}
	}
	
	public void resetHandler() throws ValidityException, ParserConfigurationException, ParsingException, IOException{
		System.out.println("Rest Button Clicked!");
		lineSpaceValue.setValue("10");
		taleWidthValue.setValue("1045");
		fontValue.setValue("Calibri");
		noteSpaceValue.setValue("400");
		ApplyHandler();
		System.out.println("Rest Complete!");
	}

	public void applyandexitHandler() throws ValidityException, ParserConfigurationException, ParsingException, IOException{
		ApplyHandler();
		pc.displayWindow.hide();
		System.out.println("Apply&Reset Button Clicked!");
	}
	
	private void loadFonts() {
		fontValues.removeAll(fontValues);
		String start = "--Choose--";
		String a = "Calibri";
		String b = "Serif";
		String c = "SansSerif";
		String d = "Monospaced";
		String e = "Dialog";
		fontValues.addAll(start,a,b,c,d,e);
		fontValue.getItems().addAll(fontValues);
		
		
	}
	private void loadNoteSpace() {
		noteSpaceValues.removeAll(noteSpaceValues);
		noteSpaceValues.add("--Choose--");
		for(int i = 100; i <= 800; i+=100) {
			noteSpaceValues.add(i+"");
		}
		noteSpaceValue.getItems().addAll(noteSpaceValues);
		
		
	}
	private void loadLineSpace() {
		//5 - 30
		lineSpaceValues.removeAll(lineSpaceValues);
		lineSpaceValues.add("--Choose--");
		for(int i = 5; i <= 30; i+=5) {
			lineSpaceValues.add(i+"");
		}
		lineSpaceValue.getItems().addAll(lineSpaceValues);
	}
	
	private void loadNoteSize() {
		taleWidthValues.removeAll(taleWidthValues);
		taleWidthValues.add("--Choose--");
		for(int i = 645; i <= 1045; i+=100) {
			taleWidthValues.add(i+"");
		}
		taleWidthValue.getItems().addAll(taleWidthValues);
		
		
		
	}

}
