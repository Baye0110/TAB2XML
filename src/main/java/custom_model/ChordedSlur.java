package custom_model;

import custom_model.note.NoteUnit;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;

public class ChordedSlur extends Group{
	private Arc arc;
	private NoteUnit start;
	private NoteUnit end;
	private String style;
	
	public ChordedSlur(NoteUnit start) {
		this.start = start;
	}
	
	public double getPosYDiffDouble() {
		double graceStart = this.start.getGrace() ? SheetScore.lineSize * 0.2 : 0;
		double graceEnd = this.end.getGrace() ? SheetScore.lineSize * 0.2 : 0;
		double startY = this.start.getTranslateY() - graceStart;
		double endY = this.end.getTranslateY() - graceEnd;
		
		return endY - startY;
	}
	
	public int getPosYDiff() {
		// 1: if start has higher position then end
		// 0: Same y-position
		// -1: The start note is lower position thene end
		
		int comparison = -2;
		
		double diff = this.getPosYDiffDouble();		
		
		if (diff > 0) {
			comparison = 1;
		}
		else if (diff == 0) {
			comparison = 0;
		}
		else {
			comparison = -1;
		}		
		
		return comparison;
	}
	
	public void setPositionY(NoteUnit end) {
		this.end = end;
		
		double heightDiff = this.getPosYDiffDouble();			
		
		this.arc = new Arc();
		if (heightDiff >= 0) {
			this.arc.setStartAngle(180);
			this.arc.setCenterY(0);
		}
		else if (heightDiff < 0) {
			this.arc.setStartAngle(270);
			this.arc.setCenterY(0 - heightDiff);
		}
			
		if (heightDiff == 0) {
			this.arc.setLength(180);
			this.arc.setRadiusY(SheetScore.lineSize * 0.6);
		}
		else {
			this.arc.setLength(90);
			this.arc.setRadiusY(heightDiff);
		}
			
		this.arc.setType(ArcType.OPEN);
		this.arc.setFill(Color.TRANSPARENT);
		this.arc.setStroke(Color.BLACK);
		
		
		this.setTranslateY(start.getTranslateY() + SheetScore.lineSize);
		
		this.getChildren().add(arc);
		
	}
	
	public void setPositionX() {
//		if (this.end == null) {
//			System.out.println(start.toString());
//			return;
//		}
		
		this.setTranslateX(this.start.getTranslateX() + SheetScore.lineSize * 0.56);
		double length = this.end.getTranslateX() - this.start.getTranslateX();
		double heightDiff = this.getPosYDiffDouble();
		
		if (heightDiff > 0) {
			this.arc.setCenterX(length);
			this.arc.setRadiusX(length);
			this.style = "topToDown";
		}
		else if (heightDiff == 0) {
			this.arc.setCenterX(length/2);
			this.arc.setRadiusX(length/2);
			this.style = "flat";
		}
		else {
			this.arc.setCenterX(0);
			this.arc.setRadiusX(length);
			this.style = "downToTop";
		}
		
		double baseRadiusX = this.arc.getRadiusX();
		double baseRadiusY = this.arc.getRadiusY();
		double baseCenterX = this.arc.getCenterX();
		double baseCenterY = this.arc.getCenterY();
		for(int i = 1; i < SheetScore.lineSize/4; i++) {
			Arc arc = new Arc();
			if (this.style.equals("flat")) {
				arc.setStartAngle(180);
				arc.setLength(180);
				arc.setRadiusX(baseRadiusX);
				arc.setRadiusY(baseRadiusY - i);
				arc.setCenterX(baseCenterX);
				arc.setCenterY(baseCenterY);
			}
			else {
				arc.setLength(90 + i*1.5);
				arc.setRadiusX(baseRadiusX - i*1.5);
				arc.setRadiusY(baseRadiusY - i*1.5);
				arc.setCenterY(baseCenterY + i*2);
				if (this.style.equals("topToDown")) {
					arc.setStartAngle(180 - i*1.5);
					arc.setCenterX(baseCenterX - i*2);				
				}
				else {
					arc.setStartAngle(270 - i*1.5);
					arc.setCenterX(baseCenterX + i*2);
				}
			}
			
			arc.setType(ArcType.OPEN);
			arc.setFill(Color.TRANSPARENT);
			arc.setStroke(Color.BLACK);
			arc.setStrokeWidth(1.5);
			this.getChildren().add(arc);
		}
	}
	
	public void setNoteStart(NoteUnit startNote) {
		this.start = startNote;
	}
	
	public void setEndStart(NoteUnit endNote) {
		this.end = endNote;
	}
	
	public NoteUnit getNoteStart() {
		return this.start;
	}
	
	public NoteUnit getNoteEnd() {
		return this.end;
	}
}
