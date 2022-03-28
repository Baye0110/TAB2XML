package GUI;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.fxmisc.richtext.CodeArea;

import custom_component_data.Score;
import custom_model.SheetScore;
import customer_player.musicPlayer;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

public class PreviewController extends Application{
	
	private MainViewController mvc;
	private musicPlayer player;
	private Score score;
	private SheetScore sheet;
	public Window displayWindow;
	 
	@FXML public CodeArea mxlText;
	@FXML ScrollPane sp;
	@FXML TextField tempoField;
	@FXML TextField gotoMeasureField;
	@FXML Button playButton;
	@FXML Button pauseButton;
	@FXML Button goButton;
	@FXML Button exitButton;
	@FXML Button displayButton;
	@FXML Button exportButton;
	
	public PreviewController() {}
	
	 public void setMainViewController(MainViewController mvcInput) {
	    	mvc = mvcInput;
	    }
	 
	 public void update() throws ValidityException, ParserConfigurationException, ParsingException, IOException {
		 tempoField.setText("60");
		 score = new Score(mvc.converter.getMusicXML());
		 sheet = new SheetScore(score, 10, 1050);
		 sp.setContent(sheet);
		 player = new musicPlayer(score, sheet, mvc.converter.getMusicXML());
	 }
	
	public void playHandler() {
		System.out.println("Play Button Clicked!");
		System.out.println("String1: " + player.toString());
		player.play(tempoField.getText());
		System.out.println("String2: " + player.toString());
		System.out.println("The tempoSpeed is: " + player.getTempo());
	}
	public void pauseHandler(){
		System.out.println("Pause Button Clicked!");
		player.pause();
	}
	public void exitHandler(){
		System.out.println("Exit Button Clicked!");
		player.exit();
		mvc.convertWindow.hide();
		System.out.println("preview windows exited");
	}
	public void goHandler(){
		System.out.println("Go Button Clicked!");
		double valToSet = sheet.getMeasurePosition(Integer.parseInt(gotoMeasureField.getText())) / sheet.getHeight();
		sp.setVvalue(valToSet);
	}
	
	public void displayHandler(){
		System.out.println("Customize Display Button Clicked!");
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("GUI/DisplaySettingGUI.fxml"));
			root = loader.load();
			DisplaySettingController controller = loader.getController();
			controller.setPreviewController(this);
			controller.update();
			displayWindow = this.openNewWindow(root, "Display Setting");
			displayWindow.setOnHidden(e->{
				System.out.println("Customize Display Window Closed!");
			});
		} catch (Exception e) {
			Logger logger = Logger.getLogger(getClass().getName());
			logger.log(Level.SEVERE, "Failed to create new Window.", e);
		}
	}
	
	public void expotHandler(){
		System.out.println("Export PDF Button Clicked!");
		//To-Be-Continue!
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
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
