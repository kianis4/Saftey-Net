package src;

import java.util.HashMap;
import java.util.Stack;
import java.util.TreeMap;

public class ShortestPath {
	
	private HashMap<String, Double> distTo;
	private HashMap<String, DirectedEdge> edgeTo;
	private TreeMap<Double, String> PQ;
	/**
	 * Constructor for ShortestPath
	 * @param G EdgeWeightedDiGraph to find shortest path within
	 * @param source String, the source vertex
	 */
	public ShortestPath(EdgeWeightedDiGraph G, String source) {
		distTo = new HashMap<>();
		edgeTo = new HashMap<>();
		PQ = new TreeMap<>();
		
		//Set weight of all to infinity except source.
		for(String vertex : G.adj().keySet()) {
			distTo.put(vertex, Double.POSITIVE_INFINITY);
		}
		distTo.put(source, 0.0);
		PQ.put(0.0, source);
		
		while(! PQ.isEmpty()) {
			relax(G, PQ.pollFirstEntry().getValue());
		}
	}
	/**
	 * Helper method for finding shortest path
	 * @param G EdgeWeightedDiGraph to find shortest path within
	 * @param vertex 
	 */
	public void relax(EdgeWeightedDiGraph G, String vertex) {
		for(DirectedEdge e : G.adj().get(vertex)) {
			if(distTo.get(e.to()) > distTo.get(vertex) + e.weight()) {
				distTo.put(e.to(), distTo.get(vertex) + e.weight());
				edgeTo.put(e.to(), e);
				PQ.put(distTo.get(e.to()), e.to());
			}
		}
	}
	/**
	 * Local method to check if there is a path to a vertex
	 * @param String v the vertex to check
	 * @return boolean true if there is a path to the vertex, false if there is not 
	 */
	private boolean hasPathTo(String v)
	{
		return distTo.get(v) < Double.POSITIVE_INFINITY;
	}
	/**
	 * Method for returning the path to a vertex
	 * @param v String the vertex to find path to
	 * @return Stack<DirectedEdge> the path to the vertex v
	 */
	public Stack<DirectedEdge> pathTo(String v)
	{
		if(! hasPathTo(v)) return null; 
		Stack<DirectedEdge> path = new Stack<>();
		for(DirectedEdge e = edgeTo.get(v); e!= null; e = edgeTo.get(e.from()))
			path.push(e); 
		return path;
	}

}
