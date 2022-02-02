package component_data;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

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
	public Score(String xmlDocument) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(new StringReader(xmlDocument)));
			doc.getDocumentElement().normalize();
			NodeList partsMetaData = doc.getElementsByTagName("score-part");
			NodeList partsMusicData = doc.getElementsByTagName("part");
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
