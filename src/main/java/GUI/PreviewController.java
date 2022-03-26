package GUI;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.fxmisc.richtext.CodeArea;

import custom_component_data.Score;
import custom_model.SheetScore;
import customer_player.musicPlayer;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

public class PreviewController extends Application{
	
	private MainViewController mvc;
	private musicPlayer player;
	private Score score;
	private SheetScore sheet;
	 
	@FXML public CodeArea mxlText;
	@FXML ScrollPane sp;
	@FXML TextField tempoField;
	@FXML TextField gotoMeasureField;
	@FXML Button playButton;
	@FXML Button pauseButton;
	@FXML Button goButton;
	@FXML Button exitButton;
	
	public PreviewController() {}
	
	 public void setMainViewController(MainViewController mvcInput) {
	    	mvc = mvcInput;
	    }
	 
	 public void update() throws ValidityException, ParserConfigurationException, ParsingException, IOException {
		 tempoField.setText("60");
		 score = new Score(mvc.converter.getMusicXML());
		 sheet = new SheetScore(score, 10, 1050);
		 sp.setContent(sheet);
		 player = new musicPlayer(mvc.converter.getMusicXML());
	 }
	
	public void playHandler() {
		System.out.println("String1: " + player.toString());
		player.play(tempoField.getText());
		System.out.println("String2: " + player.toString());
		System.out.println("The tempoSpeed is: " + player.getTempo());
	}
	public void pauseHandler(){
		player.pause();
	}
	public void exitHandler(){
		player.exit();
		mvc.convertWindow.hide();
		System.out.println("preview windows exited");
	}
	public void goHandler(){
		double valToSet = sheet.getMeasurePosition(Integer.parseInt(gotoMeasureField.getText())) / sheet.getHeight();
		sp.setVvalue(valToSet);
	}
	
	public void apply(){
		
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
