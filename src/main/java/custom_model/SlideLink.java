package custom_model;

import javafx.scene.shape.Line;

public class SlideLink extends NoteLinker {

	private Line slideLine;
	int startVal;
	int endVal;
	private double height;
	
	public SlideLink(double length, double height) {
		Line slide = new Line();
		slide.setStartX(0);
		slide.setStartY(0);
		slide.setEndX(length);
		slide.setEndY(height);
		this.getChildren().add(slide);
		
		this.length = length;
		this.height = height;
		this.slideLine = slide;
	}
	
	@Override
	public void setLength(double newLength) {
		this.slideLine.setEndX(newLength);

	}

	public void setHeight(double newHeight) {
		this.slideLine.setEndY(newHeight);
	}
	
	public double getLength() {
		return this.length;
	}
	
	public double getHeight() {
		return this.height;
	}
	
	public void setStartVal(int startVal) {
		this.startVal = startVal;
	}
	
	public void setStartX(int start) {
		this.slideLine.setStartX(start);
	}
}
