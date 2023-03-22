package src;

import java.util.ArrayList;

public class ChunkT {
	private final String neighbourhoodName;
	private final double[] boundaries;
	private ArrayList<CrimeT> crimesCommitedInArea;
	private int crimeCount;
	
	/* Coordinates are split into leftmost, rightmost as well as upper and lower bounds.
	 * (left/right) are longitudes, (up/down) are latitudes. */
	public ChunkT(String name, double left, double right, double up, double down) {
		neighbourhoodName = name;
		
		crimesCommitedInArea = new ArrayList<>();
		boundaries = new double[4]; /* Rectangular boundaries, we have four coordinates. */
		crimeCount = 0;
		
		boundaries[0] = left;
		boundaries[1] = right;
		boundaries[2] = up;
		boundaries[3] = down;
	}
	/**
	 * Method for adding crime to a Chunk
	 * @param crime a CrimeT object
	 */
	public void addCrime(CrimeT crime) {
		crimesCommitedInArea.add(crime);
		crimeCount++;
	}
	/**
	 * Getter method to retrieve instance variable crimesCommitedInArea
	 * @return ArrayList<CrimeT> crimesCommitedInArea, a list of crimes committed in the area
	 */
	public ArrayList<CrimeT> getCrimes() {
		return this.crimesCommitedInArea;
	}
	/**
	 * Getter method to retrieve instance variable crimeCount
	 * @return int crimeCount, the number of crimes committed in the area
	 */
	public int getCrimeCount() {
		return crimeCount;
	}
	/**
	 * Getter method to retrieve instance variable neighbourhoodName
	 * @return String neighbourhoodName, the name of the bureau
	 */
	public String getName() {
		return neighbourhoodName;
	}
	/**
	 * Getter method to retrieve instance variable boundaries
	 * @return double[] boundaries, an array of the boundaries
	 */
	public double[] boundaries() {
		return boundaries;
	}
	/**
	 * Method to check if a crime occurred in a chunk
	 * @param crime a CrimeT object
	 * @return boolean true if in bounds, false if not in bounds
	 */
	public boolean isInBounds(CrimeT crime) {
		double latitude = crime.coordinates().latitude();
		double longitude = crime.coordinates().longitude();
		
		return (longitude >= boundaries[0] && longitude <= boundaries[1]
				&& latitude <= boundaries[2] && latitude >= boundaries[3]);
	}
}
