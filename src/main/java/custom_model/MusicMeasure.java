package custom_model;

import java.util.ArrayList;
import java.util.List;

import custom_component_data.Measure;
import custom_component_data.Note;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

abstract public class MusicMeasure extends Pane {
	// restore this class
	
	public static final double START_DISTANCE = 30;
	List<Line> barLines;
	
	double maxHeight;
	double minWidth;
	double spacing;
	public double wholeDistance = 400;
	double currentDistance;
	
	
	public MusicMeasure(double size, Measure m, boolean start) {
		this.currentDistance = START_DISTANCE;
		
		if (start) {
			if (m.getTab())
				currentDistance += this.generateTabClef(size * (m.getStaffLines() - 1), currentDistance, m.getStaffLines());
			if (m.getPercussion())
				currentDistance += this.generatePercussionClef(4 * size, currentDistance, (m.getStaffLines()-3) * size);
		}
		
		if (m.getTimeDisplay()) {
			Text beat = new Text();
			beat.setText(Integer.toString(m.getTimeSignature()[0]));
			beat.setFont(Font.font(size * (m.getStaffLines()-1) * 0.675));
			beat.setX(currentDistance);
			int upperPadding = m.getTab() ? 2: StaffMeasure.UPPER_PADDING;
			beat.setY(size * (upperPadding + (m.getStaffLines()-1)/2.0));
			this.getChildren().add(beat);
			
			Text beatType = new Text();
			beatType.setText(Integer.toString(m.getTimeSignature()[1]));
			beatType.setFont(Font.font(size * (m.getStaffLines()-1) * 0.675));
			beatType.setX(currentDistance);
			beatType.setY(size * (upperPadding + m.getStaffLines()-1));
			this.getChildren().add(beatType);
			
			List<Text> timeVertical = new ArrayList<>();
			timeVertical.add(beat);
			timeVertical.add(beatType);
			MusicMeasure.alignVerticalText(timeVertical);
			
			currentDistance += beat.minWidth(0) + 50;
		}
	}
	
	
	public void setBaseDistance(double scale) {
		this.wholeDistance *= scale;
	}
	
	abstract public void setSpacing(double scale);
	
	// returns the width of the clef
	public double generateTabClef(double size, double currentDistance, double staffLines) {
		List<Text> tabVertical = new ArrayList<>();
		
		Text t = new Text();
		t.setText("T");
		t.setFont(Font.font(size * 0.45));
		t.setX(currentDistance);
		t.setY(size/3.0 + (size/(staffLines-1)*2));
		this.getChildren().add(t);
		tabVertical.add(t);
		
		Text a = new Text();
		a.setText("A");
		a.setFont(Font.font(size * 0.45));
		a.setX(currentDistance);
		a.setY(size*2.0/3.0 + (size/(staffLines-1)*2));
		this.getChildren().add(a);
		tabVertical.add(a);
		
		Text b = new Text();
		b.setText("B");
		b.setFont(Font.font(size * 0.45));
		b.setX(currentDistance);
		b.setY(size + (size/(staffLines-1)*2));
		this.getChildren().add(b);
		tabVertical.add(b);
		
		MusicMeasure.alignVerticalText(tabVertical);
		
		return t.minWidth(0) + 50;
	}
	
	public double generatePercussionClef(double start, double currentDistance, double height) {
		Line l1 = new Line(currentDistance, start, currentDistance, start + height);
		l1.setStrokeWidth(height/5.5);
		
		Line l2 = new Line(currentDistance + l1.minWidth(0)*2.5, start, currentDistance + l1.minWidth(0)*2.5, start + height);
		l2.setStrokeWidth(height/5.5);
		
		this.getChildren().add(l1);
		this.getChildren().add(l2);
		
		return l1.minWidth(0)*4 + 50;
	}
	
	public static void alignVerticalText(List<Text> list) {
		double max_width = 0;
		
		for (int i = 0; i < list.size(); i++) {
			max_width = list.get(i).minWidth(0) > max_width ? list.get(i).minWidth(0) : max_width;
		}
		
		for (Text t: list) {
			t.setX(t.getX() + (max_width-t.minWidth(0))/2);
		}
	}

}
