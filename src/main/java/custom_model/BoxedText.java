package custom_model;

import javafx.scene.Group;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class BoxedText extends Group{
	static final int PADDING_LEFT = 8; 
	static final int FONT_SIZE = 12;
	double size;
	double type;
	boolean chord;
	
	BoxedText(String text, double size, double type, boolean chord) {
		
		Text label = new Text(text);
		label.setFont(Font.font(FONT_SIZE));
		label.setX(PADDING_LEFT/2.00);
		if (text.length() == 1) {
			label.setX(PADDING_LEFT/2.00 + FONT_SIZE*1.25/4);
		}
		label.setY(label.minHeight(0));
		
		Rectangle container = new Rectangle();
		container.setWidth(FONT_SIZE*1.25 + PADDING_LEFT);
		container.setHeight(size);
		container.setFill(Paint.valueOf("0xfff"));
		container.setStroke(Paint.valueOf("0xccc"));
		container.setStrokeWidth(2);		
		
		this.getChildren().add(container);
		this.getChildren().add(label);	
		this.size = container.getWidth();
		this.type = type;
		this.chord = chord;
	}
}