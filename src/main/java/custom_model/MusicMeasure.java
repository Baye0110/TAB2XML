package custom_model;

import java.util.List;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

abstract public class MusicMeasure extends Pane {
	List<Line> barLines;
	
	double minWidth;
	double spacing;
	public double wholeDistance = 200;
	
	
	public void setBaseDistance(double scale) {
		this.wholeDistance *= scale;
	}
	
	abstract public void setSpacing(double scale);

}
