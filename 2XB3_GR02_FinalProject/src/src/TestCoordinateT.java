package src;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class TestCoordinateT {
	CoordinateT coordinate;

	@Before
	public void setup() throws Exception {
		coordinate = new CoordinateT(88.62, 11.00);
	}

	@Test
	public void testLatitude() {
		assertEquals(coordinate.latitude(), 88.62, 0.01);
	}

	@Test
	public void testLongitude() {
		assertEquals(coordinate.longitude(), 11.00, 0.01);
	}


}
