package custom_model;

import java.util.ArrayList;
import java.util.List;

import custom_component_data.Measure;
import custom_component_data.Score;
import javafx.scene.layout.VBox;

public class SheetScore extends VBox{

		public SheetScore(Score score, double lineSize, double pageWidth) {
			List<TabMeasure> cumulated = new ArrayList<>();
			double length = 0;
			for (Measure m: score.getParts().get(0).getMeasures()) {
		        TabMeasure mGUI = new TabMeasure(m, lineSize, false);
		        if (length >= pageWidth) {
		        	ScoreLine sl1 = new ScoreLine(cumulated, pageWidth);
		        	cumulated = new ArrayList<>();
		        	cumulated.add(mGUI);
		        	length = mGUI.minWidth;
		        	sl1.setTranslateX(50);
		        	this.getChildren().add(sl1);
		        }
		        else if (length + mGUI.minWidth >= pageWidth) {
		        	cumulated.add(mGUI);
		        	ScoreLine sl1 = new ScoreLine(cumulated, pageWidth);
		        	cumulated = new ArrayList<>();
		        	length = 0;
		        	this.getChildren().add(sl1);
		        	sl1.setTranslateX(50);
		        }
		        else {
		        	cumulated.add(mGUI);
		        	length += mGUI.minWidth;
		        }
			}
			
			if (!cumulated.isEmpty()) {
				ScoreLine sl1 = new ScoreLine(cumulated, pageWidth);
				sl1.setTranslateX(50);
				this.getChildren().add(sl1);
			}
			
			this.setSpacing(25);			
		}
}
