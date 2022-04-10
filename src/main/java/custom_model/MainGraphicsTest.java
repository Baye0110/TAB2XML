package custom_model;

import java.io.File;

import java.io.FileInputStream;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;



public class MainGraphicsTest extends Application {
	/**
	 * DON'T COMMIT YOUR CHANGES TO THIS CLASS!	
	 * 
	 * USE THIS CLASS TO TEST YOUR CODE WITHOUT RUNNING THE WHOLE PROGRAM!!
	 * MANUAL GUI TESTING CLASS!
	 * 
	 */

	@Override
	public void start(Stage primaryStage) throws IOException {
//        File in = new File("src/test/resources/system/demoDrumsSimple1.musicxml");
//        String build = "";
//        Score score = null;
//        Scanner input = null;
//        try {
//            input = new Scanner(in);
//            input.useDelimiter("\n");
//            
//            while(input.hasNext()) {
//                build += input.next() + "\n";
//            }
//            score = new Score(build);
//
//        } catch (FileNotFoundException e1) {
//            e1.printStackTrace();
//        }    
		
		
		/**
		IMPORTANT JAVAFX Classes:
		
			Line
			Ellipse - Oval Shape
			Text
			Rectangle
			TextField
			
			Image 
			ImageView
			
			Group  - Add all your elements (from the aforementioned classes) to an object of type Group
			Pane
			VBox - Stacking objects vertically
			HBox - Stacking objects side-by-side / horizontally
			
			Color.valueOf("0x89f");
			Color.WHITE;
		*/
		
		
		/*
		 * 1. Start a JavaFX Main File with a Stage which is essentially a WINDOW
		 */
		
		Stage stage = new Stage(); // WINDOW
        stage.setTitle("Learnign JavaFX"); // Title for the window
        stage.show(); // Displays the window
        
        
        /*
         * 2. Create an empty Group which stores all the elements.
         */
        Group g1 = new Group();
        
        
        /*
         * 3. Create a Scene using a GUI element, and attach it to the Stage (window)
         */
        
        Scene scene = new Scene(g1, 1200, 700); // CONTENTS: 
        			//new Scene(GUI element,  width,  height)
        scene.setFill(Color.WHITE);  // Background Color
        stage.setScene(scene); // attaches the contents
        
        
        /*
         * 4. Create Your GUI Elements and Add them to the Group
         */
        
        
        // Creating a text element
        /*Text msg = new Text("Hi!");  // The message is set in the constructor or can be set by: msg.setText("Hello! ...");
        msg.setFont(Font.font(70));  // Set the fontsize of the text
        msg.setTranslateX(100);		 // Set the X co-ordinate
        msg.setTranslateY(400);      // Set the Y co-ordinate 
        	// NOTE: By default the Y co-ordinate is the bottom of the text, not the top (Set the Y=35 instead of 400 to better understand this
        g1.getChildren().add(msg);  // Add the text GUI element to the Group
        
        
        // Creating a Line element
        Line l1 = new Line();
        l1.setStartX(50);  l1.setStartY(200);  // The (x,y) coordinate for the beginning of the line
        l1.setEndX(200);  l1.setEndY(50);	   // The (x,y) coordinate for the end of the line
        l1.setStroke(Color.RED);			   // Setting the color of a Line (Color.SELECT_THE_COLOR_FROM_THE_OPTIONS)
        l1.setStrokeWidth(20);				   // The width of the Line in pixels
        g1.getChildren().add(l1);			   // Add the line GUI element to the Group 
        
        
        // Creating an Ellipse (which is an Oval/Circle shape)
        Ellipse e1 = new Ellipse();
        e1.setRadiusX(300);			// The horizontal-radius  (equal to half its width)
        e1.setRadiusY(20);			// The vertical-radius  (equal to half its height)
        e1.setCenterX(300);			// The X co-ordinate of the Center of the Ellipse
        e1.setCenterY(20 + 450);    // The Y co-ordinate of the Center of the Ellipse
        	// NOTE: Set radiusX = radiusY, to create a circle
        	// NOTE2: The center of the circle is by-default on the top-left corner of the group
        g1.getChildren().add(e1);   // Add the Ellipse GUI element
        
        
        // Creating an Imagee
        Image image = new Image(new FileInputStream("src/main/resources/image_assets/note_Tail.png"));  // Provide a path to the image file, starting from 'src'
        ImageView imageDisplay = new ImageView(image);  // Create the GUI element for the specified Image object (the one create in the previous line)
        imageDisplay.setFitHeight(500);			// Set the height of the image
        imageDisplay.setFitWidth(250);			// Set the width of the image
        g1.getChildren().add(imageDisplay);		// Add the image GUI element to the Group
        
*/
        // Testing your own Class without running the TAB2XML Gradle Project (Simply run this file like a normal Java Class)
        /*QuarterRest qr = new QuarterRest(100);    // Create an object instance of your class
        g1.getChildren().add(qr); 				  // Add it to the group
        qr.setTranslateX(250);	qr.setTranslateX(50);			  // Translate the object (Set its (x,y) coordinates)     
        
        
        // ExampleClass which will display a Face (refer to ExampleClass.java)
        ExampleClass ex = new ExampleClass(Color.RED, Color.ORANGE);
        ex.setTranslateX(700);
        ex.setTranslateY(25);
        g1.getChildren().add(ex);*/
        
        
        /*
         * Getting the height and width of the element:  (Always give the argument '0')
         */
        /*double heightOfEllipse = e1.minHeight(0);
        double widthOfEllipse = e1.minWidth(0);
        System.out.println("Height: " + heightOfEllipse + "\nWidth: " + widthOfEllipse);*/
        
       /* SlantLine sl = new SlantLine(100, 14, 20.0, false);
        sl.setTranslateX(400);
        sl.setTranslateY(90);
        g1.getChildren().add(sl);*/
        
//        Tremolo t = new Tremolo(100, 14, 30.0, false, 3);
//        t.setTranslateX(400);
//        t.setTranslateY(19);
//        g1.getChildren().add(t);
//       
//        Bend b = new Bend(200, 200, "full");
//        b.setTranslateX(700);
//        b.setTranslateY(50);
//        g1.getChildren().add(b);
        
  	}

	/*public static void main(String[] args) {
		launch();
	}*/
}

