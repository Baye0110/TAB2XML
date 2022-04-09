package GUI;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;

import org.fxmisc.richtext.CodeArea;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;

import custom_component_data.Score;
import custom_model.MusicMeasure;
import custom_model.ScoreLine;
import custom_model.SheetScore;
import custom_model.note.BoxedText;
import custom_player.musicPlayer;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import nu.xom.ParsingException;
import nu.xom.ValidityException;
import javafx.embed.swing.SwingFXUtils;

public class PreviewController extends Application{
	
	private MainViewController mvc;
	private musicPlayer player;
	private Score score;
	public SheetScore sheet;
	public Window displayWindow;
	public File saveFile;
	@FXML public CodeArea mxlText;
	@FXML ScrollPane sp;
	@FXML TextField tempoField;
	@FXML TextField gotoMeasureField;
	@FXML Button playButton;
	@FXML Button pauseButton;
	@FXML Button goButton;
	@FXML Button stopButton;
	@FXML Button displayButton;
	@FXML Button exportButton;
	List<BufferedImage> bufferedimage = new ArrayList<BufferedImage>();

	
	public PreviewController() {}
	
	 public void setMainViewController(MainViewController mvcInput) {
	    	mvc = mvcInput;
	    }
	 
	 public void update() throws ValidityException, ParserConfigurationException, ParsingException, IOException {
		 initialButton();
		 tempoField.setText("60");
		 score = new Score(mvc.converter.getMusicXML());
		 sheet = new SheetScore(score);
		 sp.setContent(sheet);
		 player = new musicPlayer(score, sheet, mvc.converter.getMusicXML());
	 }
	 
	 private void initialButton() {
		 playButton.setVisible(true);
		 pauseButton.setVisible(false);
	 }

	 private void getBufferimage() {
		SheetScore copy = new SheetScore(score);
		
		for (ScoreLine line: copy.getScoreLines()) {
			double width = 0.0;
			Group lineCopy = new Group();
			
			for (MusicMeasure mm: line.getMeasures()) {
				lineCopy.getChildren().add(mm);
				mm.setTranslateX(width);
				width += mm.getMeasureWidth();
			}
			
			bufferedimage.add(SwingFXUtils.fromFXImage(lineCopy.snapshot(new SnapshotParameters(), null), null));
		}
	}

	public void playHandler() {
		System.out.println("Play Button Clicked!");
		playButton.setVisible(false);
		pauseButton.setVisible(true);
		player.play(tempoField.getText());
		System.out.println("The tempoSpeed is: " + player.getTempo());
		Thread thread = new Thread() {
			public void run() {
				while(!player.isFinished()) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				initialButton();
				System.out.println("Music is Finished");
			}
		};
		thread.start();
	}
	public void pauseHandler(){
		System.out.println("Pause Button Clicked!");
		playButton.setVisible(true);
		pauseButton.setVisible(false);
		player.pause();
	}
	public void stopHandler() throws ValidityException, ParserConfigurationException, ParsingException, IOException{
		System.out.println("stop Button Clicked!");
		playButton.setVisible(true);
		pauseButton.setVisible(false);
		player.resetMusicToBeginning();
		player.finish();
	}
	
	public void exit() {
		player.exit();
		initialValue();
		System.out.println("preview windows exited");
	}
	
	public void goHandler(){
		System.out.println("Go Button Clicked!");
		this.sp.setVmax(this.sheet.minHeight(0) + this.sp.minHeight(0));
		double valToSet = sheet.getMeasurePosition(Integer.parseInt(gotoMeasureField.getText()));
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
			
		FileChooser fileChooser = new FileChooser();
	    fileChooser.setTitle("Save As");
	    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("pdf file", "*.pdf");
	    fileChooser.getExtensionFilters().add(extFilter);
	    File file = fileChooser.showSaveDialog(mvc.convertWindow);
	      if(file != null) {
	    	  try {
	    		  PdfWriter writer = new PdfWriter(file); 
	    		  PdfDocument pdf = new PdfDocument(writer);
	    		  Document doc = new Document(pdf); 
	    		  getBufferimage();
	    		  for(int i = 0; i < bufferedimage.size(); i ++) {
	    			  File outputFile = new File("image" + i + ".png");
	    			  ImageIO.write(bufferedimage.get(i), "png", outputFile);
	    			  ImageData imgD = ImageDataFactory.create(outputFile.getPath());
	  	              Image image = new Image(imgD);
	  	              doc.add(image);
	  	              outputFile.delete();
	    		 }
	    		 doc.close();
	    		 bufferedimage = new ArrayList<BufferedImage>();
	  			
	    		  System.out.println("PDF saved successfully, File path: " + file.getPath());
		        } catch (Exception e) {
		        	System.out.println("PDF saved failed");
		        }
	      }
	}
	
	private void initialValue() {
		SheetScore.lineSize = 10;
		SheetScore.pageWidth = 1045;
		BoxedText.customizefont = MusicMeasure.customizefont = "Calibri";
		MusicMeasure.scale = 400;
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
		
	}

}
