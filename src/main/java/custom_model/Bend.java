package custom_model;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Bend extends Group{
	private double height;
	private double length;
	private Text text;
	private Arc arc;
	private List<Polygon> arrowHead;
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	public double getLength() {
		return length;
	}
	public void setLength(double length) {
		this.length = length;
	}
	public Text getText() {
		return text;
	}
	public void setText(Text text) {
		this.text = text;
	}
	
	public Bend(double height, double length, String text) {
		this.height = height;
		this.length = length;
		this.arrowHead = new ArrayList<Polygon>();
		// create arc part
		this.arc = new Arc();
		arc.setCenterX(0.0);
		arc.setCenterY(0.0);
		arc.setStartAngle(270);
		arc.setLength(90);
		arc.setRadiusX(length);
		arc.setRadiusY(height);
		arc.setFill(Color.TRANSPARENT);
		arc.setStroke(Color.BLACK);
		this.getChildren().add(arc);
		
		// create arrowhead
		Polygon half1 = new Polygon();
		half1.getPoints().addAll(new Double[] {
			length, 0.0,
			length + 7.0, 0.0,
			length, -12.0,
		});
		this.arrowHead.add(half1);
		this.getChildren().add(half1);
		
		Polygon half2 = new Polygon();
		half2.getPoints().addAll(half1.getPoints());
		half2.setScaleX(-1);
		half2.setTranslateX(-7.0);
		this.arrowHead.add(half2);
		this.getChildren().add(half2);
		
		// add Text
		Text t = new Text(length - 8.0, -20.0, text);
		t.setFont(Font.font(MusicMeasure.customizefont, SheetScore.lineSize));
		this.text = t;
		this.getChildren().add(t);
	}
	
	public void adjustLength(double length) {
		double addition = length - this.length;
		
		for (Polygon half: this.arrowHead) {
			for (int i = 0; i < half.getPoints().size(); i += 2) {
				half.getPoints().set(i, half.getPoints().get(i) + addition);
			}
		}
		
		this.text.setX(this.text.getX() + addition);
		
		this.arc.setRadiusX(this.arc.getRadiusX() + addition);
		
	}

}
