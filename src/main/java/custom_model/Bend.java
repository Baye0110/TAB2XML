package custom_model;

import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

public class Bend extends Group{
	private double height;
	private double length;
	private String text;
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
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public Bend(double height, double length, String text) {
		this.height = height;
		this.length = length;
		this.text = text;
		// create arc part
		Arc arc = new Arc();
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
		this.getChildren().add(half1);
		
		Polygon half2 = new Polygon();
		half2.getPoints().addAll(half1.getPoints());
		half2.setScaleX(-1);
		half2.setTranslateX(-7.0);
		this.getChildren().add(half2);
		
		// add Text
		Text t = new Text(length - 8.0, -20.0, text);
		t.setStyle("-fx-font: 20 arial");
		this.getChildren().add(t);
	}

}
