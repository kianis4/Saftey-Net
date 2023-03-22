package src;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.time.LocalDate;

public class TestChunkT {
	ChunkT chunk;
	CrimeT crime;
	
	@Before
	public void setup() throws Exception {
		chunk = new ChunkT("chunk 1", 5, 30, 30, 10);
		crime = new CrimeT(LocalDate.now(), "Robbery", new CoordinateT(15.21, 25.85));
		chunk.addCrime(crime);
	}
	
	@Test
	public void testIsInBounds() {
		CrimeT crimeOutOfBounds = new CrimeT(LocalDate.now(), "Robbery", new CoordinateT(20.21, 35.85));
		assertTrue(chunk.isInBounds(crime));
		assertFalse(chunk.isInBounds(crimeOutOfBounds));
	}
	
	@Test 
	public void testGetName() {
		assertEquals(chunk.getName(), "chunk 1");
	}
	
	@Test 
	public void testGetCrimes() {
		assertEquals(chunk.getCrimes().get(0), crime);
		assertEquals(chunk.getCrimes().size(), 1);
	}
	
	@Test 
	public void testGetCrimeCount() {
		assertEquals(chunk.getCrimeCount(), 1);
	}
}
