package custom_component_data_test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import custom_component_data.Clef;

//Testing Clef.java 

class ClefTest {
	@Test
	public void cleftTest1() {
		Clef c1 = new Clef('G',1);
		assertNotNull(c1);
	}
	
	
	}


