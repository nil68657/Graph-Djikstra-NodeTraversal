//Heap
//Author : Nilanjan Chatterjee   800960960
import java.util.List;
import java.util.ArrayList;

public class Heap {

	private List<Vertex> heap;

	public Heap() {
		heap = new ArrayList<Vertex>();
		heap.add(null); 
	}

	// Check if the heap is empty
	public boolean isEmpty() {
		return heap.size()==0;
	}

	// Get the parent of an indexed-vertex
	private Vertex getParent(int index) {
		return heap.get(index / 2);
	}

	// Get the left child of an indexed-vertex
	private Vertex getLeftChild(int index) {
		return heap.get(2 * index);
	}

	// Get the right child of an indexed-vertex
	private Vertex getRightChild(int index) {
		return heap.get(2 * index + 1);
	}

	// Add a new vertex into heap
	public void addVertex(Vertex vertex) {
		heap.add(null);
		int index = heap.size()-1;
		while(index > 1 && getParent(index).dist > vertex.dist) {
			heap.set(index, getParent(index));
			index /= 2;
		}
		heap.set(index, vertex);
	}

	// Remove the closest vertex
	public Vertex removeVertex() {
		Vertex removedVertex = heap.get(1);
		int index = heap.size() - 1;
		Vertex lastVertex = heap.remove(index);
		if(index > 1) {
			heap.set(1, lastVertex);
			reorganiseHeap();     
		}
		return removedVertex;
	}

	// Reorganise heap after vertex removal
	private void reorganiseHeap() {
		Vertex root = heap.get(1);
		int index = 1, last = heap.size()-1;
		while(true) {
			int childIndex = index * 2;
			if(childIndex > last) break;
			Vertex child = getLeftChild(index);
			if(((index * 2) + 1)<=last && 
					getRightChild(index).dist < child.dist) {
				childIndex = (index * 2) + 1;
				child = getRightChild(index);
			}
			if (child.dist > root.dist) break;
			heap.set(index, child);
			index = childIndex;
		}
		heap.set(index, root);
	}
}