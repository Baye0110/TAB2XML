package custom_model;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;

public class ArcLine extends Group {
	
	ArcLine(double height, double length, boolean upFacing){
        Arc arc1 = new Arc();
        arc1.setRadiusX(length/2);	
        //length/2 = radius x
        
        arc1.setRadiusY(height);
        //height = radius y
        
        arc1.setCenterX(length/2);
        //center x = length / 2 
        
        arc1.setCenterY(height + 0.466*length/2);
        //tested and calculated : center y = height + tan(25) * length/2
        
        arc1.setStartAngle(25);	//tested
        arc1.setLength(130);
        
        if(upFacing == false)
        {
        	arc1.setScaleY(-1);
        }
        
        arc1.setType(ArcType.OPEN);		//This is necessary to make curved line
        arc1.setFill(Color.TRANSPARENT);	//Make inside of Ellipse as invisible
        arc1.setStroke(Color.BLACK);		//Make the outline (curve) black
        
        this.getChildren().add(arc1);
	}
}
