package custom_model;

import java.util.ArrayList;
import java.util.List;

import custom_component_data.Measure;
import custom_component_data.Score;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class SheetScore extends VBox{

		public SheetScore(Score score, double lineSize, double pageWidth) {
			List<MusicMeasure> cumulated = new ArrayList<>();
			double length = 0;
			
//			if (!score.getParts().get(0).getMeasures().get(0).getTab()) {
//				Text inValid = new Text("This type of tablature is currently unsupported.\n"
//						+ "Support for drum tablature is anticipated to arrive soon.\n"
//						+ "Your patience is greatly appreciated.");
//				inValid.setFont(Font.font(40));
//				inValid.setTextAlignment(TextAlignment.CENTER);
//				inValid.setFill(Color.RED);
//				inValid.setTranslateX(50);
//				this.getChildren().add(inValid);
//				return;
//			}
			
			for (Measure m: score.getParts().get(0).getMeasures()) {
				MusicMeasure mGUI = null;
				if (score.getParts().get(0).getMeasures().get(0).getTab()) {
					mGUI = new TabMeasure(lineSize, m, false);
				}
				else {
					mGUI = new StaffMeasure(lineSize, m, false);
				}
		        
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
