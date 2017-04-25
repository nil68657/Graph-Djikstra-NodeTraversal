//Graph
//Author : Nilanjan Chatterjee    800960960

import java.io.FileReader;
import java.util.StringTokenizer;
import java.io.IOException;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Graph extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public static final int INFINITY = Integer.MAX_VALUE;
    public static final String UP = "up", DOWN = "down";
    public static final Vertex sortVertices = new Vertex();
    
    private Map<String,Vertex> vertexMap = new HashMap<String,Vertex>();
    private Map<String, Map<String, Edge>> edgeMap = new HashMap<String, Map<String, Edge>>();
    
    public Graph() {
    }
    
    public Graph(int status) {
		switch(status) {
			case 0:
				System.err.println("Unrecognised command/ Insufficient arguments. ");
				break;
			case 1:
				System.err.println("Edge does not exist. ");
				break;
			case 2:
				System.err.println("Vertex does not exist. ");
				break;
			case 3:
				System.err.println("Source vertex does not exist. ");
				break;
			case 4:
				System.err.println("Destination vertex does not exist. ");
				break;
			default:
				System.err.println("Graph error. ");
				break;
		}
    }
    
    // Checks if vertices exist
    public boolean verticesExist(String... vertices) {
    	for(String v: vertices)
    		if(vertexMap.get(v) == null)
    			return false;
    	return true;
    }
    
    // Checks if an edge between vertices exists
    public boolean edgeExists(String sourceName, String destName) {
    	return edgeMap.containsKey(sourceName) && 
			edgeMap.get(sourceName).containsKey(destName);
    }
    
    // Adds a new edge to the graph.
    public void addEdge(String sourceName, String destName, float weight, boolean bidirectional) {
        Vertex v = getVertex(sourceName);
        Vertex w = getVertex(destName);
        if(v.adj.indexOf(w) == -1)
        	v.adj.add(w);
        if(!sourceName.equals(destName) && bidirectional && w.adj.indexOf(v) == -1)
        	w.adj.add(v);

        int direction = bidirectional ? 2 : 1;
        Vertex vetrices[] = {v, w};
        for(int i = 0; i < direction; i++) {
        	Edge edge = new Edge(vetrices[i], vetrices[~i+2], weight);
            if(!edgeMap.containsKey(vetrices[i].name)) {
            	Map<String, Edge> edgeWeight = new HashMap<String, Edge>();
            	edgeWeight.put(vetrices[~i+2].name, edge);
            	edgeMap.put(vetrices[i].name, edgeWeight);
            }
            else edgeMap.get(vetrices[i].name).put(vetrices[~i+2].name, edge);
        }
    }
    
    // Deletes an existing edge from the graph
    public void deleteEdge(String sourceName, String destName, boolean bidirectional) {
    	String nodes[] = {sourceName, destName};
    	
    	int direction = bidirectional ? 2 : 1;
        for(int i = 0; i < direction; i++)
            if(edgeExists(nodes[i], nodes[~i+2]) && verticesExist(nodes[i], nodes[~i+2])) {
            	edgeMap.get(nodes[i]).remove(nodes[~i+2]);
            	vertexMap.get(nodes[i]).adj.remove(vertexMap.get(nodes[~i+2]));
            }
            else if(!sourceName.equals(destName))
            	throw new Graph(1);
    }
    
    // Marks an edge (in the direction of source to destination) as up
    public void edgeUp(String sourceName, String destName) {
    	if(edgeExists(sourceName, destName))
    		edgeMap.get(sourceName).get(destName).edgeUp();
    	else throw new Graph(1);
    }
    
    // Marks an edge (in the direction of source to destination) as down
    public void edgeDown(String sourceName, String destName) {
    	if(edgeExists(sourceName, destName))
    		edgeMap.get(sourceName).get(destName).edgeDown();
    	else throw new Graph(1);
    }
    
    // Marks an existing vertex down
    public void vertexUp(String vertexName) {
    	Vertex v = null;
    	if((v = vertexMap.get(vertexName)) != null)
    		v.vertexUp();
    	else throw new Graph(2);
    }
    
    // Marks an existing vertex down
    public void vertexDown(String vertexName) {
    	Vertex v = null;
    	if((v = vertexMap.get(vertexName)) != null)
    		v.vertexDown();
    	else throw new Graph(2);
    }
    
    // Gets shortest path (Dijkstra's algorithm)
    public void getShortestPath(String sourceName, String destName) {
    	if(!vertexMap.containsKey(sourceName)) throw new Graph(3);
    	if(!vertexMap.containsKey(destName)) throw new Graph(4);
    	dijkstra(sourceName, destName);
    }
    
    // Prints the graph
    public void print() {
    	System.out.println("");
    	List<Vertex> vertices = new ArrayList<Vertex>(vertexMap.values());
    	vertices.sort(sortVertices);
    	for(Vertex v: vertices) {
    		System.out.print(v.name);
    		if(v.status.equals(DOWN))
    			System.out.print("  " + " DOWN");
    		System.out.println("");
    		List<Vertex> adjacentVertices = v.adj;
    		adjacentVertices.sort(sortVertices);
    		Iterator<Vertex> i = adjacentVertices.iterator();
    		while(i.hasNext()) {
    			Vertex adjVertex = (Vertex) i.next();
    			if(edgeMap.containsKey(v.name) &&
    					edgeExists(v.name, adjVertex.name)) {
    				System.out.print(" " + adjVertex.name + "  " + 
	    					edgeMap.get(v.name).get(adjVertex.name).weight);
	    			if(edgeMap.get(v.name).get(adjVertex.name).status.equals(DOWN))
	    				System.out.print("  " + "DOWN");
	    			System.out.println("");
    			}
    		}
        }
    }
    
    // Get all reachable vertices from one
    private ArrayList<Vertex> adjantReachableVertices(Vertex v, ArrayList<Vertex> visited, ArrayList<Vertex> reachable) {
    	List<Vertex> adjacentVertices = v.adj;
		adjacentVertices.sort(sortVertices);
		Iterator<Vertex> i = adjacentVertices.iterator();
		while(i.hasNext()) {
			Vertex adjVertex = (Vertex) i.next();
			if(visited.indexOf(adjVertex) == -1) {
				if(	adjVertex.status.equals(UP) && edgeMap.containsKey(v.name) &&
					edgeExists(v.name, adjVertex.name) && 
					edgeMap.get(v.name).get(adjVertex.name).status.equals(UP)) {
					visited.add(adjVertex);
					reachable.add(adjVertex);
					adjantReachableVertices(adjVertex, visited, reachable);
				}
			}
		}
		return reachable;
    }
    
    /**
     * Prints all vertices with status up and other reachable vertices from them
     * This algorithm mimics Depth/Breadth First Search (more details in README).
     * Its time complexity is O(|E| + |V|).
     * @throws IOException 
     */
    public void reachable() {
    	System.out.println("");
    	List<Vertex> vertices = (new ArrayList<Vertex>(vertexMap.values()));
    	vertices.sort(sortVertices);
    	for(Vertex v: vertices) {
    		if(v.status.equals(UP)) {
    			System.out.println(v.name);
    			ArrayList<Vertex> visited = new ArrayList<Vertex>();
    			visited.add(v);
    			ArrayList<Vertex> reachable = adjantReachableVertices(v, visited, new ArrayList<Vertex>());
    			reachable.sort(new Vertex());
    			for(Vertex r: reachable)
    				System.out.println("  " + r.name);
    		}
    	}
    }
    
    // Gets distance/weight between two vertices
    public float distance(Vertex s, Vertex d) {
    	if(edgeExists(s.name, d.name))
    		return edgeMap.get(s.name).get(d.name).weight;
    	else throw new Graph(1);
    }
    
    // Gets unidirectional edge, if exists, between source and destination
    public Edge getEdge(String source, String destination) {
    	if(edgeExists(source, destination))
    		return edgeMap.get(source).get(destination);
		return null;
    }
    
    // Gets unidirectional edge, if exists, between source and destination
    public Edge getEdge(Vertex source, Vertex destination) {
    	if(edgeExists(source.name, destination.name))
    		return edgeMap.get(source.name).get(destination.name);
		return null;
    }
    
    /**
     * Driver routine to print total distance.
     * It calls recursive routine to print shortest path to
     * destNode after a shortest path algorithm has run.
     * @throws IOException 
     */
    public void printPath(String destName) {
        Vertex w = vertexMap.get(destName);
        if(w == null)
            throw new NoSuchElementException("Destination vertex not found");
        else if(w.dist == INFINITY)
        	System.err.println(destName + " is unreachable");
        else {
            printPath(w);
            System.out.println(" " + String.format("%.2f", w.dist));
        }
    }
    
    /**
     * Recursive routine to print shortest path to destination
     * after running shortest path algorithm. The path
     * is known to exist.
     */
    private void printPath(Vertex dest) {
        if(dest.prev != null) {
            printPath(dest.prev);
            System.out.print(" ");
        }
        System.out.print(dest.name);
    }

    /**
     * If vertexName is not present, add it to vertexMap.
     * In either case, return the Vertex.
     */
    private Vertex getVertex(String vertexName) {
        Vertex v = vertexMap.get(vertexName);
        if(v == null) {
            v = new Vertex(vertexName);
            vertexMap.put(vertexName, v);
        }
        return v;
    }
    
    /**
     * Initialises the vertex output info prior to running
     * any shortest path algorithm.
     */
    private void clearAll() {
        for(Vertex v : vertexMap.values())
            v.reset();
    }

    /**
     * Single-source shortest-path algorithm for weighted graph.
     * @throws IOException 
     */
    public void dijkstra(String startName, String finishName) {
        clearAll();
        Vertex start = vertexMap.get(startName);
        if(start == null)
            throw new NoSuchElementException("Start vertex not found");
        if(start.status.equals(DOWN)) {
        	System.out.println("The source vertex is down.");
        	return;
        }
        
        
        Heap Q = new Heap();
        Q.addVertex(start);
        
        start.dist = 0;
        System.out.println("");
        while(!Q.isEmpty()) {
            Vertex u = Q.removeVertex();
            if(u.name.equals(finishName)) break;
            for(Vertex v : u.adj) {
                if(v.status.equals(UP) && v.dist > u.dist + distance(u, v) &&
                		getEdge(u, v).status.equals(UP)) {
                    v.dist = u.dist + distance(u, v);
                    v.prev = u;
                    Q.addVertex(v);
                }
            }
        }
        printPath(finishName);
    }

    /**
     * Process a request; return false if end of file.
     * @throws IOException 
     */
    public static void processRequest(String input, Graph g) {
    	String inputQuery[] = input.split(" "); 
    	int inputLength = inputQuery.length;
    	switch(inputLength) {
    		case 1:
    			switch(input.toLowerCase()) {
            		case "print":
            			g.print();
            			break;
            		case "reachable":
            			g.reachable();
            			break;
    	        	case "quit":
    	        		return;
	        		default:
	        			throw new Graph(0);
            	}
    			break;
    		case 2:
    			switch(inputQuery[0].toLowerCase()) {
    				case "vertexdown":
    					g.vertexDown(inputQuery[1]);
    					break;
    				case "vertexup":
    					g.vertexUp(inputQuery[1]);
    					break;
					default:
						throw new Graph(0);
    			}
    			break;
    		case 3:
    			switch(inputQuery[0].toLowerCase()) {
    				case "path":
    					g.getShortestPath(inputQuery[1], inputQuery[2]);
    					break;
    				case "deleteedge":
    					g.deleteEdge(inputQuery[1], inputQuery[2], false);
    					break;
    				case "addedge":
    					g.addEdge(inputQuery[1], inputQuery[2], 1, false);
    					break;
    				case "edgedown":
    					g.edgeDown(inputQuery[1], inputQuery[2]);
    					break;
    				case "edgeup":
    					g.edgeUp(inputQuery[1], inputQuery[2]);
    					break;
					default:
						throw new Graph(0);
    			}
    			break;
    		case 4:
    			if(inputQuery[0].toLowerCase().equals("addedge"))
    				g.addEdge(inputQuery[1], inputQuery[2], Float.parseFloat(inputQuery[3]), false);
    			break;
			default:
				break;
    	}
    }
    
    // Builds a graph
    private static Graph buildGraph(String filename) {
    	Graph g = new Graph();
        try {
            FileReader fin = new FileReader(filename);
            Scanner graphFile = new Scanner(fin);

            // Read the edges and insert
            String line;
            while(graphFile.hasNextLine()) {
                line = graphFile.nextLine();
                StringTokenizer st = new StringTokenizer(line);
                try {
                    if(st.countTokens() < 2) {
                        System.err.println("Skipping ill-formatted line " + line);
                        continue;
                    }
                    float weight = 1;
                    String source = st.nextToken();
                    String dest = st.nextToken();
                    if(st.hasMoreTokens())
                    	weight = Float.parseFloat(st.nextToken());
                    g.addEdge(source, dest, weight, true);
                }
                catch(NumberFormatException e) {
                	System.err.println("Skipping ill-formatted line " + line); 
            	}
            }
            graphFile.close();
         }
         catch(IOException e) {
        	 System.err.println(e);
    	 }
        return g;
    }
    
    private static void readQueries(Scanner s, Graph g) {
        while(s.hasNextLine())
			processRequest(s.nextLine(), g);
		s.close();
    }

    /**
     * A main routine that:
     * 1. Reads a file containing edges (supplied as a command-line parameter);
     * 2. Forms the graph;
     * 3. Repeatedly prompts for two vertices and
     *    runs the shortest path algorithm.
     * The data file is a sequence of lines of the format
     *    source destination 
     */
    public static void main(String[] args) {
         Graph g = buildGraph(args[0]);
         Scanner s = new Scanner(System.in);
		 readQueries(s, g);
		 System.out.println("");
    }
}