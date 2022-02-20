package custom_model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import custom_component_data.Score;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainGraphicsTest extends Application {

	@Override
	public void start(Stage primaryStage) {
        File in = new File("src/test/resources/system/demoGuitarComplex1.musicxml");
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
		
        int size = 25;
        TabMeasure m1 = new TabMeasure(score.getParts().get(0).getMeasures().get(0), size, false);
        TabMeasure m2 = new TabMeasure(score.getParts().get(0).getMeasures().get(17), size, false);
		TabMeasure m3 = new TabMeasure(score.getParts().get(0).getMeasures().get(2), size, false);
		List<TabMeasure> measureList = new ArrayList<>(); measureList.add(m1); measureList.add(m3); measureList.add(m2);
		ScoreLine sl1 = new ScoreLine(measureList, 1200);
		//BoxedText ting = new BoxedText("Hi! It's Moi...", 50);
		m1.setTranslateX(50); m2.setTranslateX(50); m3.setTranslateX(50);
		VBox measures = new VBox(sl1);
		measures.setSpacing(50);
		Scene display = new Scene(measures, m2.minWidth + 100, 3 * (m1.minHeight(0) + size*4));
		primaryStage.setScene(display);
		primaryStage.setTitle("Testing custom_model Classes");
		primaryStage.show();
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}