// Edge
//Author : Nilanjan Chatterjee  800960960

class Edge {
	public Vertex 	source;		// Source vertex of the edge
	public Vertex 	dest;		// Destination vertex of the edge
	public float 	weight;		// Weight of the edge
	public String	status;		// Edge up or down
	
	Edge(Vertex source, Vertex dest, float weight) {
		this.source = source;
		this.dest = dest;
		this.weight = weight;
		status = Graph.UP;
	}
	
	// Marks the edge up
	public void edgeUp() {
		if(status.equals(Graph.DOWN))
			status = Graph.UP;
	}
	
	// Marks the edge down
	public void edgeDown() {
		if(status.equals(Graph.UP))
			status = Graph.DOWN;
	}
}