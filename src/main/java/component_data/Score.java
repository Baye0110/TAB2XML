package component_data;

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
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Score {
	String title;
	String author;
	List<Part> partList;
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
//		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//		DocumentBuilder db = dbf.newDocumentBuilder();
//		Document doc = db.parse(new File("C://Users/pc/Desktop/demo1.musicxml"));
//		doc.getDocumentElement().normalize();
//		System.out.println(doc.getElementsByTagName("part-name").item(0).getTextContent());
		File in = new File("C://Users/pc/Desktop/drumsComplex.musicxml");
		String build = "";
		Scanner input = new Scanner(in);
		input.useDelimiter("\n");
		while(input.hasNext()) {
			build += input.next() + "\n";
		}
		System.out.println(build);
		
		Score test = new Score(build);
		System.out.println(test.partList.get(0).measures.get(0).clef.symbol);
		for (Instrument e:  test.partList.get(0).instruments.values()) {
			System.out.println(e);
		}
	}
	
	public Score(String xmlDocument) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(new StringReader(xmlDocument)));
			doc.getDocumentElement().normalize();
			NodeList partsMetaData = doc.getElementsByTagName("score-part");
			NodeList partsMusicData = doc.getElementsByTagName("part");
			this.partList = new ArrayList<Part>();
			for (int i = 0; i < partsMetaData.getLength(); i++) {
				this.partList.add(new Part(partsMetaData.item(i), partsMusicData.item(i)));
			}
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
		
	}
}
