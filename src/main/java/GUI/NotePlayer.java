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
		 System.out.println("Music is playing");
	 }
	 
	 @FXML
	    private void pauseButtonClicked() {
		 System.out.println("Music Paused");
	 }
	 
	 @FXML
	    private void exitButtonClicked() {
		 mvc.convertWindow.hide();
	 }

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
