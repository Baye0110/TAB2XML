package custom_model;

import javafx.scene.Group;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class BoxedText extends Group{
	// Restore this Class

	static final int PADDING_LEFT = 8; 
	static final int FONT_SIZE = 12;
	double size;
	double type;
	boolean chord;
	
	BoxedText(String text, double size, double type, boolean chord) {
		
		Text label = new Text(text);
		label.setFont(Font.font(size*0.675));
		label.setX((size + PADDING_LEFT - label.minWidth(0))/2.00);
//		if (text.length() == 1) {
//			label.setX(PADDING_LEFT/2.00 + FONT_SIZE*1.25/4);
//		}
		label.setY((size*0.93 - label.minHeight(0)/2)/2 + label.minHeight(0)/2);
		
		Rectangle container = new Rectangle();
		container.setWidth(size + PADDING_LEFT);
		container.setHeight(size*0.93);
		container.setFill(Paint.valueOf("0xfff"));
//		container.setStroke(Paint.valueOf("0xccc"));
//		container.setStrokeWidth(2);		
		
		this.getChildren().add(container);
		this.getChildren().add(label);	
		this.size = container.getWidth();
		this.type = type;
		this.chord = chord;
	}
}