package custom_model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import custom_component_data.Note;
import custom_component_data.Score;
import custom_model.notehead.*;
import custom_model.rest.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class MainGraphicsTest extends Application {

	@Override
	public void start(Stage primaryStage) throws IOException {
       File in = new File("src/test/resources/system/demoDrumsSimple1.musicxml");
        String build = "";
        Score score = null;
        Scanner input = null;
        try {
            input = new Scanner(in);
            input.useDelimiter("\n");
            
            while(input.hasNext()) {
                build += input.next() + "\n";
            }
            score = new Score(build);

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        
        int height = 20;
		StaffMeasure fn2 = new StaffMeasure(height, score.getParts().get(0).getMeasures().get(2), true);
		fn2.setSpacing(1.1);
		
		DisplayNote dn = new DisplayNote(height, score.getParts().get(0).getMeasures().get(1).getNotes().get(9), true);
		dn.setTranslateX(height * 3);
		
		Group root = new Group(fn2);
		root.setTranslateX(50);
		root.setTranslateY(50);
		Scene display = new Scene(root, 700, 700);
        
		primaryStage.setScene(display);
		primaryStage.setTitle("Testing custom_model Classes");
		primaryStage.show();
			
	}

	public static void main(String[] args) {
		launch(args);
	}
}