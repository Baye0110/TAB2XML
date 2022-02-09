package converter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import GUI.MainViewController;
import utility.MusicXMLCreator;
import utility.ValidationError;

public class Converter {

	// Develop branch added
	private Score score;
	private MusicXMLCreator mxlc;
	private MainViewController mvc;
	// MY COMMENT IS BETTER: CONFLICTING COMMENT
	public Converter(MainViewController mvc) {
		this.mvc = mvc;
	}
	
	public void update() {
		
		score = new Score(mvc.mainText.getText());
		mxlc = new MusicXMLCreator(score);
	}
	//FINAL VERSION OF TEST 2
	public String getMusicXML() {
		
		return mxlc.generateMusicXML();
	}
	
	public Score getScore() {
		
		return score;
	}
	
	public List<ValidationError> validate() {
		return score.validate();
	}
    public void saveMusicXMLFile(File file) {
    	
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(mxlc.generateMusicXML());
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}
