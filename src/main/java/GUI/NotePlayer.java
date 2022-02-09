package GUI;

import javax.swing.JTextArea;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utility.Settings;


public class NotePlayer extends Application{
	private MainViewController mvc;
	 @FXML private JTextArea TextArea;
	
	 public void setMainViewController(MainViewController mvcInput) {
	    	mvc = mvcInput;
	    }
	
	 
	 @FXML
	    private void playButtonClicked() {
		 
	 }
	 
	 @FXML
	    private void pauseButtonClicked() {
		 
	 }
	 
	 @FXML
	    private void exitButtonClicked() {
		 
	 }

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
