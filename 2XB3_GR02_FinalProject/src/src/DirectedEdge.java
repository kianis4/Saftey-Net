package src;

public class DirectedEdge {
	private int weight;
	private String to;
	private String from;
	/**
	 * Constructor for DirectedEdge
	 * @param to where the edge goes to
	 * @param from where the edge starts from
	 * @param weight the weight of the edge
	 */
	public DirectedEdge(String to, String from, int weight) {
		this.to = to;
		this.from = from;
		this.weight = weight;
	}
	/**
	 * Getter method for instance variable to
	 * @return String the vertex the edge goes to
	 */
	public String to() {
		return to;
	}
	/**
	 * Getter method for instance variable from
	 * @return String the vertex the edge starts from
	 */
	public String from() {
		return from;
	}
	/**
	 * Getter method for instance variable weight
	 * @return int the weight of the edge
	 */
	public int weight() {
		return weight;
	}
	/**
	 * Method for checking whether an edge is equal to another edge
	 * @param other DirectedEdge, another edge to compare to
	 * @return boolean, true if they are equal, false if they are not equal
	 */
	public boolean equals(DirectedEdge other) {
		return this.to().equals(other.to()) && this.from().equals(other.from());
	}

}
