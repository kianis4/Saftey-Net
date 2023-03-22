package src;

import java.util.ArrayList;
import java.util.HashMap;

import src.Map;

public class EdgeWeightedDiGraph {
	private HashMap<String, ArrayList<DirectedEdge>> adj;
	
	/**
	 * Constructor for EdgeWeightedDiGraph
	 */
	public EdgeWeightedDiGraph() {
		if(! Map.isInit()) {
			throw new NullPointerException("Map has not been initialized");
		}
		
		adj = new HashMap<>();
		
		addEdge("MANHATTAN", "STATEN ISLAND");
		addEdge("BROOKLYN", "STATEN ISLAND");
		
		addEdge("BRONX", "MANHATTAN");
		addEdge("QUEENS", "MANHATTAN");
		addEdge("BROOKLYN", "MANHATTAN");
		addEdge("STATEN ISLAND", "MANHATTEN");
		
		addEdge("STATEN ISLAND", "BROOKLYN");
		addEdge("MANHATTAN", "BROOKLYN");
		addEdge("QUEENS", "BROOKLYN");
		
		addEdge("MANHATTAN", "BRONX");
		addEdge("QUEENS", "BRONX");
		
		addEdge("BRONX", "QUEENS");
		addEdge("MANHATTAN", "QUEENS");
		addEdge("BROOKLYN", "QUEENS");
		
	}
	/**
	 * Getter method for instance variable adj
	 * @return HashMap<String, ArrayList<DirectedEdge>> an adjacency list
	 */
	public HashMap<String, ArrayList<DirectedEdge>> adj() {
		return adj;
	}
	/**
	 * Local Method for adding edges to adj
	 * @param to ending vertex
	 * @param from starting vertex
	 */
	private void addEdge(String to, String from) {
		if(adj.get(from) == null) {
			adj.put(from, new ArrayList<>());
			adj.get(from).add(new DirectedEdge(to, from, Map.getChunk(to).getCrimeCount()));
		}
		else {
			adj.get(from).add(new DirectedEdge(to, from, Map.getChunk(to).getCrimeCount()));
		}
	}

}
