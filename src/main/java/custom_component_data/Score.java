package custom_component_data;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/*
 * This is the main class which encompasses the entire music sheet (the Score).
 * The XML is parsed by creating an instance of the Score pass with 1 argument: A String representing the XML file to be parsed
 * 
 * It holds 3 Values:
 * 		title - The name of the music piece
 * 		author - The name of the composer of the music
 * 		partList - The list of Parts (which each represent the music for 1 instrument - drums, guitars, bass, etc)
 * 					Note: Since we are using tablature, the partList will usually have only 1 element.
 * 
 * Further documentation is provided within the class below.
 */

public class Score {
	String title;
	String author;
	List<Part> partList;
	
	/*
	 * Main Method: Used for testing the XML Parsing. 
	 * First recieves input from and outputs each line to the console.
	 * Then it outputs some values which can be checked in the Console to see if it is correct.
	 */
//	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
//		// Extracts the data from XML file and simply puts it in a String and outputs
//		// File in = new File("C://Users/pc/Desktop/demoComplex.musicxml");
//		File in = new File("src/test/resources/system/demoGuitarComplex1.musicxml");
//		String build = "";
//		Scanner input = new Scanner(in);
//		input.useDelimiter("\n");
//		while(input.hasNext()) {
//			build += input.next() + "\n";
//		}
//		System.out.println(build);
//		
//		// Perform the parsing by creating Score object and passing the String containing the XML file
//		Score test = new Score(build);
//		
//		// Testing: You can configure it however you like to test for certain values
//		System.out.println(test.partList.get(0).measures.get(0).clef.symbol);
//		for (Instrument e:  test.partList.get(0).instruments.values()) {
//			System.out.println(e);
//		}
//	}
	
	/*
	 * Constructor: Has 1 argument which is a String containing the whole XML file
	 * 
	 * The 1st and last 2 lines are used to output how long the parsing takes
	 * 
	 * The rest parses the document
	 */
	public Score(String xmlDocument) {
		long start = System.currentTimeMillis();
		
		try {
			// Create new Document element which parses the data
			// and stores the data in "doc" variable
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(new StringReader(xmlDocument)));
			doc.getDocumentElement().normalize();
			
			// Get the name of the piece
			NodeList scoreName = doc.getElementsByTagName("movement-title");
			if (scoreName.getLength() != 0) {
				this.title = ((Element) scoreName.item(0)).getTextContent();
			}
			
			// Get the name of the author
			NodeList authorName = doc.getElementsByTagName("creator");
			if (authorName.getLength() != 0) {
				this.author = ((Element) authorName.item(0)).getTextContent().equals("") ? null : ((Element) authorName.item(0)).getTextContent();
			}
			
			// Selects and makes a list of the "score-part" elements and "part" elements
			NodeList partsMetaData = doc.getElementsByTagName("score-part");
			NodeList partsMusicData = doc.getElementsByTagName("part");
			
			// For each Part in the XML file, we create a Part object which has 2 arguments:
			// 1. the partsMetaData (general Information) which contains the name and instruments for this part: "score-part" element in XML
			// 2. the partsMusicData which contains the measures, and notes: represented as "part" element in the XML
			this.partList = new ArrayList<Part>();
			for (int i = 0; i < partsMetaData.getLength(); i++) {
				this.partList.add(new Part(partsMetaData.item(i), partsMusicData.item(i)));
			}
		// The following are exceptions possible to due to the initial Document parse
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
		long end = System.currentTimeMillis();
		System.out.println("Parse Time: " + (end - start));
		
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getAuthor() {
		return this.author;
	}
	
	public List<Part> getParts() {
		return this.partList;
	}
}
