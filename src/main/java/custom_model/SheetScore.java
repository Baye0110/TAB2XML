package custom_model;

import java.util.ArrayList;
import java.util.List;

import custom_component_data.Measure;
import custom_component_data.Score;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

// For ALL INSTRUMENTS
public class SheetScore extends VBox{

	// Puts together all the ScoreLine Objects (ScoreLine = All the measures belonging to 1 line)

	
	/** Creates the SheetScore object
	 * 
	 * @param score			The Score XML parsing object which has all the data for the entire music piece.
	 * @param lineSize		The spacing between the staffLines (the height of the noteheads)
	 * @param pageWidth		The width of the page.
	 */
	public SheetScore(Score score, double lineSize, double pageWidth) {
		
		// Creates an invisible rectangle to add empty space to the top.
		Rectangle topBuffer = new Rectangle(pageWidth, lineSize * 2);
		topBuffer.setStroke(Color.WHITE);
		topBuffer.setOpacity(0);
		this.getChildren().add(topBuffer);
		
		// This stores all the measures which will eventually be put into 1 line
		List<MusicMeasure> cumulated = new ArrayList<>();
		
		// The length of all the measures in the above ArrayList.
		double length = 0;
		
		// Create each ScoreLine of the music by iterating through each Measure XML parsed object.
		for (Measure m: score.getParts().get(0).getMeasures()) {
			// The GUI Measure is stored in "mGUI" and is created based on the instrument of this score. (TAB OR PERCUSSION)
			MusicMeasure mGUI = null;
			if (score.getParts().get(0).getMeasures().get(0).getTab()) {
				mGUI = new TabMeasure(lineSize, m, cumulated.isEmpty());
			}
			else {
				mGUI = new StaffMeasure(lineSize, m, cumulated.isEmpty());
			}
			
			/* If the length of the MusicMeasure (GUI Measures) in the cumulated array surpasses the page width
			 * then create a horizontal line of measures (Create a ScoreLine with the MusicMeasures in 'cumulated' array
			 */
	        if (length >= pageWidth) {
	        	ScoreLine sl1 = new ScoreLine(cumulated, pageWidth);
	        	// Reset the cumulated ArrayList with the newly 'mGUI' as its first element and initialize the length
	        	cumulated = new ArrayList<>();
	        	cumulated.add(mGUI);
	        	length = mGUI.minWidth;
	        	this.getChildren().add(sl1);
	        }
	        /* If the length of the MusicMeasure in the 'cumulated' array will surpass the page width if we also add on
	         * this new measure, then we create a horizontal line of measures (Create a ScoreLine with the MusicMeasures in
	         * in 'cumulated' + mGUI)
	         */
	        else if (length + mGUI.minWidth >= pageWidth) {
	        	cumulated.add(mGUI);
	        	ScoreLine sl1 = new ScoreLine(cumulated, pageWidth);
	        	// reset the cumulated array, and set the length to 0
	        	cumulated = new ArrayList<>();
	        	length = 0;
	        	this.getChildren().add(sl1);
	        }
	        /*
	         * Otherwise, add the mGUI to the 'cumulated' ArrayList, and add its width.
	         */
	        else { 
	        	cumulated.add(mGUI);
	        	length += mGUI.minWidth;
	        }
		}
		
		/*
		 * If at the end the ArrayList is not empty, then simply put all the remaining measures on one line
		 * which stretches the entire page width.
		 */
		if (!cumulated.isEmpty()) {
			ScoreLine sl1 = new ScoreLine(cumulated, pageWidth);
			this.getChildren().add(sl1);
		}
		
		// Add an invisible rectangle at the bottom of the Score as a buffer.
		Rectangle bottomBuffer = new Rectangle(pageWidth, lineSize * 5);
		bottomBuffer.setStroke(Color.WHITE);
		bottomBuffer.setOpacity(0);
		this.getChildren().add(bottomBuffer);
		
		// Set the spacing between each line in the music.
		this.setSpacing(lineSize * 2.5);
	}
}
