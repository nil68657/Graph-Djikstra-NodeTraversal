//Vertex
//Author : Nilanjan Chatterjee  800960960

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

class Vertex implements Comparator<Vertex> {
    public String     name;   // Vertex name
    public List<Vertex> adj;  // Adjacent vertices
    public Vertex     prev;   // Previous vertex on shortest path
    public float      dist;   // Distance of path
    public String	  status; // Status of the vertex: up or down

    public Vertex() {
    }
    
    public Vertex(String nm) {
    	name = nm;
    	adj = new LinkedList<Vertex>();
    	status = Graph.UP;
    	reset();
	}
    
    public void reset() {
    	dist = Graph.INFINITY;
    	prev = null;
	}
    
    // Marks the vertex up
    public void vertexUp() {
    	if(status.equals(Graph.DOWN))
    		status = Graph.UP;
    }
    
    // Marks the vertex down
    public void vertexDown() {
    	if(status.equals(Graph.UP))
    		status = Graph.DOWN;
    }

	@Override
	public int compare(Vertex o1, Vertex o2) {
		return o1.name.compareTo(o2.name);
	}
}