package custom_model;

import custom_model.note.NoteUnit;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;

public class ArcLine extends Group {
	NoteUnit start;
	NoteUnit end;
	Arc arc;
	
	ArcLine(double height, double length, boolean upFacing){
        Arc arc1 = new Arc();
        arc1.setRadiusX(length/2);	
        //length/2 = radius x
        
        arc1.setRadiusY(height);
        //height = radius y
        
        arc1.setCenterX(length/2);
        //center x = length / 2 
        
        arc1.setCenterY(height);
        //center y = height
        
        arc1.setStartAngle(25);	//tested
        arc1.setLength(130);
        
        //if upFacing is false then we rotate the shape 180 degrees
        if(upFacing == false)
        {
        	arc1.setScaleY(-1);
        }
        
        arc1.setType(ArcType.OPEN);		//This is necessary to make curved line
        arc1.setFill(Color.TRANSPARENT);	//Make inside of Ellipse as invisible
        arc1.setStroke(Color.BLACK);		//Make the outline (curve) black
        
        this.arc = arc1;
        
        this.getChildren().add(arc1);
	}
	
	public void setStartNote(NoteUnit start) {
		this.start = start;
	}
	
	public void setEndNote(NoteUnit end) {
		this.end = end;
	}
	
	public NoteUnit getStartNote() {
		return this.start;
	}
	
	public NoteUnit getEndNote() {
		return this.end;
	}
	
	public void setPositionX() {
		this.setTranslateX(this.start.getTranslateX() + this.start.getWidth()/2);
		
		double halfLength = (this.end.getTranslateX() - this.start.getTranslateX()) / 2;
		this.arc.setRadiusX(halfLength);
		this.arc.setCenterX(halfLength);
		
		for (int i = 1; i <= this.arc.getCenterY()/4; i++) {
			ArcLine supporting = new ArcLine(this.arc.getCenterY() + i, 2 * halfLength, true);
			supporting.setTranslateY(-i);
			this.getChildren().add(supporting);
		}
	}
}
