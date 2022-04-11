package GUI;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class GuiTest  extends ApplicationTest {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("GUI/mainView.fxml"));
        Scene scene = new Scene(root);


        scene.getStylesheets().add(getClass().getClassLoader().getResource("GUI/styles.css").toExternalForm());

        stage.setTitle("TAB 2 XML");
        stage.setMinWidth(700);
        stage.setMinHeight(500);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testEmptyInput(FxRobot robot) {
        robot.clickOn("#previewButton");
        FxAssert.verifyThat("#previewButton", NodeMatchers.isDisabled());
        
        robot.clickOn("#showMXLButton");
        FxAssert.verifyThat("#showMXLButton", NodeMatchers.isDisabled());
        
        robot.clickOn("#saveMXLButton");
        FxAssert.verifyThat("#saveMXLButton", NodeMatchers.isDisabled());
        
        robot.clickOn("#saveTabButton");
        FxAssert.verifyThat("#saveTabButton", NodeMatchers.isDisabled());
    }

    @Test
    public void invalidInput(FxRobot robot) {
        robot.clickOn("#mainText");
        robot.write("this text is not a valid measure", 0);
        FxAssert.verifyThat("#previewButton", NodeMatchers.isDisabled());
    }
    
    @Test
    public void validInput1(FxRobot robot) {
        robot.clickOn("#mainText");
        robot.write("  3/8\n"
        		+ "|5-----|\n"
        		+ "|--7---|\n"
        		+ "|----6-|\n"
        		+ "|------|\n"
        		+ "|------|\n"
        		+ "|------|\n"
        		+ "", 0);
        
        robot.clickOn("#previewButton");
        FxAssert.verifyThat("#previewButton", NodeMatchers.isEnabled());    
    }
    
    @Test
    public void validInput2(FxRobot robot) {
    	 robot.clickOn("#mainText");
         robot.write("  3/8\n"
         		+ "|5-----|\n"
         		+ "|--7---|\n"
         		+ "|----6-|\n"
         		+ "|------|\n"
         		+ "|------|\n"
         		+ "|------|\n"
         		+ "", 0);
      
         robot.clickOn("#showMXLButton");
         FxAssert.verifyThat("#showMXLButton", NodeMatchers.isEnabled());
        
    }
    
    @Test
    public void validInput3(FxRobot robot) {
    	 robot.clickOn("#mainText");
         robot.write("  3/8\n"
         		+ "|5-----|\n"
         		+ "|--7---|\n"
         		+ "|----6-|\n"
         		+ "|------|\n"
         		+ "|------|\n"
         		+ "|------|\n"
         		+ "", 0);
      
       
         robot.clickOn("#saveMXLButton");
         FxAssert.verifyThat("#saveMXLButton", NodeMatchers.isEnabled());
        
    }
    
    @Test
    public void validInput4(FxRobot robot) {
    	 robot.clickOn("#mainText");
         robot.write("  3/8\n"
         		+ "|5-----|\n"
         		+ "|--7---|\n"
         		+ "|----6-|\n"
         		+ "|------|\n"
         		+ "|------|\n"
         		+ "|------|\n"
         		+ "", 0);
      
         robot.clickOn("#saveTabButton");
         FxAssert.verifyThat("#saveTabButton", NodeMatchers.isDisabled());
        
    }
    
    
	 @Test
    public void TestPlay(FxRobot robot) {
        robot.clickOn("#mainText");
        robot.write("  3/8\n"
        		+ "|5-----|\n"
        		+ "|--7---|\n"
        		+ "|----6-|\n"
        		+ "|------|\n"
        		+ "|------|\n"
        		+ "|------|\n"
        		+ "", 0);
        
        robot.clickOn("#previewButton"); 
        robot.clickOn("#playButton");
        FxAssert.verifyThat("#playButton", NodeMatchers.isEnabled());
        robot.sleep(500);
    }
 
 @Test
    public void TestPause(FxRobot robot) {
        robot.clickOn("#mainText");
        robot.write("  3/8\n"
        		+ "|5-----|\n"
        		+ "|--7---|\n"
        		+ "|----6-|\n"
        		+ "|------|\n"
        		+ "|------|\n"
        		+ "|------|\n"
        		+ "", 0);
        
        robot.clickOn("#previewButton"); 
        robot.clickOn("#playButton");
        robot.clickOn("#pauseButton");
        FxAssert.verifyThat("#pauseButton", NodeMatchers.isEnabled());
        robot.sleep(500);
    }
 
 @Test
    public void TestPlayAndPause(FxRobot robot) {
        robot.clickOn("#mainText");
        robot.write("  3/8\n"
        		+ "|5-----|\n"
        		+ "|--7---|\n"
        		+ "|----6-|\n"
        		+ "|------|\n"
        		+ "|------|\n"
        		+ "|------|\n"
        		+ "", 0);
        
        robot.clickOn("#previewButton"); 
        robot.clickOn("#playButton");
        robot.clickOn("#pauseButton");
        robot.clickOn("#playButton");
        robot.clickOn("#pauseButton");
        robot.clickOn("#playButton");
        robot.clickOn("#pauseButton");
        robot.clickOn("#playButton");
        robot.clickOn("#pauseButton");
        FxAssert.verifyThat("#pauseButton", NodeMatchers.isEnabled());
        robot.sleep(500);
    }
 @Test
    public void TestRepeat(FxRobot robot) {
        robot.clickOn("#mainText");
        robot.write("   7/4\r\n"
        		+ "  REPEAT 8x\r\n"
        		+ "G---4-----------|\r\n"
        		+ "D----4----------|\r\n"
        		+ "A-2---2-----2-5-|\r\n"
        		+ "E-------2-5-----|\r\n"
        		+ "\r\n"
        		+ "G---------------|-----------------------------|--4-----------|\r\n"
        		+ "D-4-4-4-------4-|-3---2-----------------------|---4----------|\r\n"
        		+ "A------4----4---|-------2---------0---2---5---|2---2-----2-5-|\r\n"
        		+ "E-------2-5-----|---------0---3---------------|------2-5-----|", 0);
        robot.clickOn("#previewButton"); 
        robot.sleep(1000);
 }
 
 @Test
    public void TestSetTempo(FxRobot robot) {
        robot.clickOn("#mainText");
        robot.write("   7/4\r\n"
        		+ "  REPEAT 8x\r\n"
        		+ "G---4-----------|\r\n"
        		+ "D----4----------|\r\n"
        		+ "A-2---2-----2-5-|\r\n"
        		+ "E-------2-5-----|\r\n"
        		+ "\r\n"
        		+ "G---------------|-----------------------------|--4-----------|\r\n"
        		+ "D-4-4-4-------4-|-3---2-----------------------|---4----------|\r\n"
        		+ "A------4----4---|-------2---------0---2---5---|2---2-----2-5-|\r\n"
        		+ "E-------2-5-----|---------0---3---------------|------2-5-----|", 0);
        robot.clickOn("#previewButton");
        robot.doubleClickOn("#tempoField");
        robot.write("240",0);
        robot.clickOn("#playButton");
        robot.sleep(10000);
        
 }
  @Test
  public void TestGoToMeasure(FxRobot robot) {
	  robot.clickOn("#mainText");
	  robot.write("  3/8\n"
    		+ "|5-----|\n"
    		+ "|--7---|\n"
    		+ "|----6-|\n"
    		+ "|------|\n"
    		+ "|------|\n"
    		+ "|------|\n"
    		+ "", 0);
    
    robot.clickOn("#previewButton"); 
    robot.doubleClickOn("#gotoMeasureField");
    robot.write("1",0);
    robot.clickOn("#goButton");
    FxAssert.verifyThat("#goButton", NodeMatchers.isEnabled());
  }
    @Test
    public void TestCustomizeDisplay(FxRobot robot) {
  	  robot.clickOn("#mainText");
  	  robot.write("  3/8\n"
      		+ "|5-----|\n"
      		+ "|--7---|\n"
      		+ "|----6-|\n"
      		+ "|------|\n"
      		+ "|------|\n"
      		+ "|------|\n"
      		+ "", 0);
      
      robot.clickOn("#previewButton"); 
      robot.clickOn("#displayButton");
      
      robot.clickOn("#fontValue");
      robot.sleep(200);
      robot.clickOn("#noteSpaceValue");
      robot.sleep(200);
      robot.clickOn("#lineSpaceValue");
      robot.sleep(200);
      robot.clickOn("#taleWidthValue");
      robot.sleep(200);
      robot.clickOn("#applyButton");
      robot.sleep(200);
      robot.clickOn("#resetButton");
      robot.sleep(200);
      robot.clickOn("#applyAndExitButton");
    }
    @Test
    public void TestExportPDF(FxRobot robot) {
  	  robot.clickOn("#mainText");
  	  robot.write("  3/8\n"
      		+ "|5-----|\n"
      		+ "|--7---|\n"
      		+ "|----6-|\n"
      		+ "|------|\n"
      		+ "|------|\n"
      		+ "|------|\n"
      		+ "", 0);
      
      robot.clickOn("#previewButton"); 
      robot.clickOn("#exportButton");
      FxAssert.verifyThat("#exportButton", NodeMatchers.isEnabled());
    }

    
}

