package component_data;

/*
 * Each measure should specify a Clef
 * 
 * Clef Class contains information about the clef of this measure
 * 
 * (The combination of symbol and line represents a musical clef, 'G' and 2 represents the treble clef.
 * 1. symbol = Specifies a note from 'A' to 'G'
 * 2. line = Specifies the number of lines from the bottom which should represent the "symbol"
 * 
 * Ex:
 *  symbol = G; 
 *  line = 2;
 *  This means the 2nd line from the bottom is a G note.
 */

public class Clef {
	char symbol;
	int line;
	
	public Clef(char symbol, int line) {
		this.symbol = symbol;
		this.line = line;
	}
	
	public char getSymbol() {
		return this.symbol;
	}
	
	public int getLine() {
		return this.line;
	}
}
