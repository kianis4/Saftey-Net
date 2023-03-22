package src;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.time.LocalDate;

public class TestCrimeT {
	CrimeT crime;

	@Before
	public void setup() throws Exception {
		crime = new CrimeT(LocalDate.of(2012, 1, 2), "Murder", new CoordinateT(10.91,55.65));
	}
	
	@Test
	public void testDate() {
		assertEquals(crime.date().compareTo(LocalDate.of(2012, 1, 2)), 0);
	}
	
	@Test
	public void testDescription() {
		assertEquals(crime.description(), "Murder");
	}
	
	@Test
	public void testCoordinates() {
		CoordinateT c = crime.coordinates();
		assertEquals(c.latitude(), 10.91, 0.01);
		assertEquals(c.longitude(), 55.65, 0.01);
	}
	
}
