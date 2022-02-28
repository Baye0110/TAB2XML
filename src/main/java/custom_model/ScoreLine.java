package custom_model;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.HBox;
import javafx.scene.shape.Line;

public class ScoreLine extends HBox{
	List<Double> measureDistances;
	
	public ScoreLine(List<? extends MusicMeasure> measures, double pageWidth) {
		this.measureDistances = new ArrayList<>();
		
		Line start = new Line();
		start.setStartX(0);
		start.setStartY(measures.get(0).barLines.get(0).getStartY());
		start.setEndX(0);
		start.setEndY(measures.get(0).barLines.get(measures.get(0).barLines.size()-2).getEndY());
		start.setStrokeWidth(2);
		measures.get(0).getChildren().add(start);
		
		
		double current = 0;
		double spacing = 0;
		for (MusicMeasure m: measures) {
			this.getChildren().add(m);
			this.measureDistances.add(current);
			current += m.minWidth;
			spacing += m.spacing; 
		}
		
		double scale = 1 + (pageWidth - current) / spacing;
		System.out.println(scale);
		for (MusicMeasure m: measures) {
			m.setBaseDistance(scale);
			m.setSpacing(scale);
		}
	}
}
