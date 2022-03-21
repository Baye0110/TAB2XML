package custom_model;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.HBox;
import javafx.scene.shape.Line;

// FOR ALL INSTRUMENTS
public class ScoreLine extends HBox{
	// Create a Line of TabMeasures or StaffMeasures given a List

	// Stores the
	List<Double> measureDistances;
	List<MusicMeasure> measures;
	// Stores the tallest measure in the List
	double maxHeight;
	
	/** Create a ScoreLine with the given List of MusicMeasures and the width of the pagee
	 * 
	 * @param measures		The measures that have to be in 1 line
	 * @param pageWidth		The width of the page.
	 */
	public ScoreLine(List<? extends MusicMeasure> measures, double pageWidth) {
		this.measureDistances = new ArrayList<>();
		this.measures = new ArrayList<>();
		
		// This variable tracks the length of all the measures combined
		double current = 0;
		// This variable tracks the AMOUNT OF SPACE BETWEEN NOTES ONLY
		double spacing = 0;
		
		this.maxHeight = 0;
		
		// For each MusicMeasure, add it to the ScoreLine, and increment the 2 variables above
		for (MusicMeasure m: measures) {
			this.getChildren().add(m);
			this.measures.add(m);
			this.measureDistances.add(current);
			
			this.maxHeight = m.minHeight(0) > this.maxHeight ? m.minHeight(0) : this.maxHeight;
			current += m.minWidth;
			spacing += m.spacing; 
		}
		
		// Calculates the factor that the spacing between the notes must be increased
		double scale = 1 + (pageWidth - current) / spacing;
		
		// Set the correct spacing for each measure
		for (MusicMeasure m: measures) {
			// Set the correct distance between whole notes (this is the base distance)
			m.setBaseDistance(scale);
			// Corrects the spacing between all the measures based on this new scale.
			m.setSpacing(scale);
		}
	}
}
