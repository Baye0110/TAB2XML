package custom_model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import custom_component_data.Measure;
import custom_component_data.Note;
import custom_component_data.Tied;
import custom_model.note.NoteUnit;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;

// GUITARS, DRUMS, BASS
abstract public class MusicMeasure extends Pane {
	// The amount of space before any element in a staff
	public static final double START_DISTANCE = 30;
	int numStaffLines;
	
	// Measure Number
	public static int measureCount = 0;
	int measureNum;
	
	// Beam Information/Generator
	BeamInfoProcessor beamProcessor;
	
	// The barLines (the last one is always the barLine for the end of the staff)
	List<Line> barLines;
	// The notes
	List<NoteUnit> notes;
	// The connectors of notes: curved lines, slides, etc
	List<NoteLinker> links;
	// The tieds
	private List<ArcLine> tieds;
	
	//font
	public static String customizefont = "Calibri" ;
	
	// The dimensions of the measure
	double maxHeight;
	double minWidth;
	
	// How much space is between the notes (doesn't include the note width itself
	double spacing;
	// Used for calculating the X position of the next Note
	double currentDistance;
	
	public static double scale = 400;
	// The amount of distance between whole notes (smaller for short notes, longer spacing distance for long notes)
	public double wholeNoteSpacing = scale;
	
	List<Node> endRepeat;
	
	// Does this measure end with a Tied that is starting??
	boolean runOffTied;
	
	// Grouped Highlight Box
	Group box;
	
	/**
	 * 
	 * @param size    The spacing between the lines of the staff
	 * @param m		  The Measure XML parsing object which has all the data
	 * @param start   Does this measure start on a new line on the sheet music?
	 */
	public MusicMeasure(double size, Measure m, boolean start) {
		this.runOffTied = false;
		
		if (m.getNotes().size() == 0) {
			this.currentDistance = 200;
			this.minWidth = 200;
			this.spacing = 200;
			this.notes = new ArrayList<>();
		}
		
		// Set the runOffTieds boolean flag
		if (m.getNotes().size() != 0) {
			Note last = m.getNotes().get(m.getNotes().size()-1);
			boolean hasEndTied = last.getNotation() != null && last.getNotation().getTieds().size() != 0;
			if (hasEndTied) {
				for (Tied tied: last.getNotation().getTieds()) {
					if (tied.getType().equals("start"))
						this.runOffTied = true;
				}
			}
		}		
		
		// Set the initial distance to the start_distance constant.
		this.currentDistance = START_DISTANCE;
		
		MusicMeasure.measureCount += 1;
		this.measureNum = MusicMeasure.measureCount;
		
		Text measureNum = new Text(Integer.toString(this.measureNum));
		measureNum.setFont(Font.font(customizefont, FontPosture.ITALIC, size*1.5));
		measureNum.setTranslateY(0 - size * 0.1);
		measureNum.setTranslateX(size * 0.2);
		this.getChildren().add(measureNum);
		
		if (m.getIsRepeatStart()) {
			RepeatBarLine repeat = new RepeatBarLine(size, m.getStaffLines());
			repeat.setTranslateX(repeat.getFirstLineWidth()/2);
			this.getChildren().add(repeat);
			this.currentDistance += repeat.minWidth(0);
		}
		
		// If this measure is the 1st in its line, then add the clef symbol
		if (start) {
			Line begin = new Line();
			begin.setStartX(0);
			begin.setStartY(0);
			begin.setEndX(0);
			begin.setEndY(size * (m.getStaffLines()-1));
			begin.setStrokeWidth(2);
			this.getChildren().add(begin);
			
			if (m.getTab())  // Clef for guitar, bass
				currentDistance += this.generateTabClef(size * (m.getStaffLines() - 1), currentDistance, m.getStaffLines());
			if (m.getPercussion())  // Clef for drums
				currentDistance += this.generatePercussionClef(size, currentDistance, (m.getStaffLines()-3) * size);
		}
		
		// Set the time signature (Ex: 4/4, 3/4, 5/8, ...)
		if (m.getTimeDisplay()) {
				// Create the numerator (top number) of the time signature
			Text beat = new Text();
			beat.setText(Integer.toString(m.getTimeSignature()[0]));
			//changed1:
			beat.setFont(Font.font(customizefont, size * (m.getStaffLines()-1) * 0.675));
			beat.setX(currentDistance);
				// how many lines of space should be padded above the measure
			int upperPadding = m.getTab() ? 0: StaffMeasure.UPPER_PADDING;
				// set the Y position of the numerator to the middle of the measure
			beat.setY(size * (upperPadding + (m.getStaffLines()-1)/2.0));
			this.getChildren().add(beat);
			
				// Create the denominator (bottom number) of the time signature
			Text beatType = new Text();
			beatType.setText(Integer.toString(m.getTimeSignature()[1]));
			//change2
			beatType.setFont(Font.font(customizefont, size * (m.getStaffLines()-1) * 0.675));
			beatType.setX(currentDistance);
				// Set the Y position of the denominator to the bottom of the meeasure 
			beatType.setY(size * (upperPadding + m.getStaffLines()-1));
			this.getChildren().add(beatType);
			
			// Create a List of 2 Text objects (the top number, the bottom number)
			List<Text> timeVertical = new ArrayList<>();
			timeVertical.add(beat);
			timeVertical.add(beatType);
			// Call the static function to 'center' align the 2 textboxes vertically
			MusicMeasure.alignVerticalText(timeVertical);
			
			// Set the X position of the next element in the measure (the width of this element + 50 pixels spacing)
			currentDistance += beat.minWidth(0) + 50;
		}
	}
	
	public void shift(double shiftAmount) {
		for (Node n : this.getChildren()) {
			n.setTranslateY(n.getTranslateY() + shiftAmount);
		}
	}
	
	
	public void setBaseDistance(double scale) {
		// Change the base spacing (the spacing between whole notes)
		this.wholeNoteSpacing *= scale;
	}
	
	// The abstract method so that StaffMeasure and TabMeasure can set the spacing between notes, but the implementation is done in the StaffMeasure/TabMeasure Classes
	abstract public void setSpacing(double scale);
	
	/** Creates the Clef for the Guitar/Bass (The clef is the letters 'T', 'A', 'B' written vertically)
	 * 
	 * @param size				The height of the measure
	 * @param currentDistance	The X position in the measure to place the clef.
	 * @param staffLines		The number of staffLines (aka strings) for this instrument
	 * 
	 * @return	double 			The width of the clef object 
	 */
	public double generateTabClef(double size, double currentDistance, double staffLines) {
		// Create a List to store the 3 Text Objects
		List<Text> tabVertical = new ArrayList<>();
		
		// Create Text object for "T"
		//change3
		Text t = new Text();
		t.setText("T");
		t.setFont(Font.font(customizefont, size * 0.45));
		t.setX(currentDistance);
		// Set Y position a third of the way down the 
		t.setY(size/3.0);
		this.getChildren().add(t);
		tabVertical.add(t);
		
		Text a = new Text();
		a.setText("A");
		a.setFont(Font.font(customizefont,size * 0.45));
		a.setX(currentDistance);
		a.setY(size*2.0/3.0);
		this.getChildren().add(a);
		tabVertical.add(a);
		
		Text b = new Text();
		b.setText("B");
		b.setFont(Font.font(customizefont,size * 0.45));
		b.setX(currentDistance);
		b.setY(size);
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
	
	public int getMeasureNume() {
		return this.measureNum;
	}
	
	public void generateBarLines(double size, int staffLines) {
		this.barLines = new ArrayList<Line>();
		for (int i = 0; i < staffLines; i++) {
			Line barLine = new Line();
			barLine.setStartX(0);
			barLine.setStartY(size * i);
			barLine.setEndX(this.minWidth);
			barLine.setEndY(size * i);
			barLine.setStroke(Paint.valueOf("0x777"));
			barLine.setStrokeWidth(0.6);
			
			this.barLines.add(barLine);
			this.getChildren().add(barLine);
		}
		
		// Create the line at the end of the measure (to show that the measure has ended
		Line end = new Line();
		end.setStartX(this.minWidth);
		end.setStartY(0);
		end.setEndX(this.minWidth);
		end.setEndY((staffLines - 1) * size);
		end.setStrokeWidth(2);
		this.barLines.add(end);
		this.getChildren().add(end);
	}

	public void generateEndRepeat(double size, int staffLines, int repeatNumber) {
		endRepeat = new ArrayList<>();
		
		Text times = new Text();
		times.setText("x" + repeatNumber);
		times.setFont(Font.font(customizefont, size));
		times.setTranslateX(this.currentDistance);
		this.getChildren().add(times);
		
		RepeatBarLine repeat = new RepeatBarLine(size, staffLines);
		repeat.setScaleX(-1);
		repeat.setTranslateX(this.currentDistance);
		this.currentDistance += repeat.minWidth(0) + repeat.getFirstLineWidth()/2;
		this.getChildren().add(repeat);
		
		endRepeat.add(times);
		endRepeat.add(repeat);
		
	}
	
	public List<NoteUnit> getNotes() {
		return this.notes;
	}
	
	public double getMeasureWidth() {
		return this.minWidth;
	}
	
	public double getMeasureHeight() {
		return this.maxHeight;
	}
	
	public double getWholeNoteSpacing() {
		return this.wholeNoteSpacing;
	}
	
	public boolean getRunOffTied() {
		return this.runOffTied;
	}

	public List<ArcLine> getTieds() {
		return tieds;
	}

	public void setTieds(List<ArcLine> tieds) {
		this.tieds = tieds;
	}
	
	public void goToMeasureHighlightBox() {
		Group box = this.box;
		Thread thread = new Thread() {
			public void run() {
				box.setOpacity(1);
				double time = 2500;
				for (int i = 0; i < time; i+=100) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					box.setOpacity(1 - i/time);
				}
				box.setOpacity(0);
			}
		};
		
		thread.start();
		
	}
}
