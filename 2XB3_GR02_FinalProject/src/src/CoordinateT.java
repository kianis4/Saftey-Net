package src;

public class CoordinateT {
	
	private final double latitude;
	private final double longitude;
	
	/**
	 * Constructor for CoordinateT
	 * @param latitude the latitude of a coordinate
	 * @param longitude the longitude of a coordinate
	 */
	public CoordinateT(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	/**
	 * Getter method for instance variable latitude
	 * @return double, the latitude
	 */
	public double latitude() {
		return this.latitude;
	}
	/**
	 * Getter method for instance variable longitude
	 * @return double, the longitude
	 */
	public double longitude() {
		return this.longitude;
	}
	/**
	 * Method for finding the distance between 2 coordinates
	 * @param coordinate a CoordinateT object to compare with
	 * @return double, the distance between 2 coordinates
	 */
	public double distanceTo(CoordinateT coordinate) {
		double dLat = Math.toRadians(coordinate.latitude - this.latitude);
		double dLon = Math.toRadians(coordinate.longitude - this.longitude);
		double lat1 = Math.toRadians(this.latitude);
		double lat2 = Math.toRadians(coordinate.latitude);

		double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
		double c = 2 * Math.asin(Math.sqrt(a));
		return 6372.8 * c;
	}
	
}
