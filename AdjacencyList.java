import java.io.PrintWriter;
import java.util.Map;

/**
 * Adjacency list implementation for the AssociationGraph interface.
 *
 * Your task is to complete the implementation of this class. You may add
 * methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2019.
 */
public class AdjacencyList extends AbstractGraph {

	private EdgeList[] edgeLists;

	/**
	 * Constructs empty graph.
	 */
	public AdjacencyList() {
		edgeLists = null;
	} // end of AdjacencyList()

	public void addVertex(String vertLabel) {
		// Implement me!
		if (getIndices().containsKey(vertLabel))
			System.err.println("This vertex already exists.");
		else {
			SIRState s1 = SIRState.S;
			getIndices().put(vertLabel, getIndices().size());
			getSirStates().put(vertLabel, s1);
			if (edgeLists == null) {
				edgeLists = new EdgeList[1];
				edgeLists[0] = null;
			} else {
				EdgeList[] temp = new EdgeList[edgeLists.length + 1];
				for (int i = 0; i < edgeLists.length; i++) {
					temp[i] = edgeLists[i];
				}
				// do i need to do this here or should it be done in addEdge
				temp[edgeLists.length] = null;
				edgeLists = temp;

			}
		}
	} // end of addVertex()

	public void addEdge(String srcLabel, String tarLabel) {
		// Check if both vertices are present
		if (!getIndices().containsKey(srcLabel) || !getIndices().containsKey(tarLabel)) {
			System.err.println("At least one vertex is not present");
			return;
		}

		// Check duplicate edge
		EdgeList srcList = edgeLists[getIndices().get(srcLabel)];
		if (srcList != null && srcList.contain(tarLabel)) {
			System.err.println("Found duplicate edge");
			return;
		}

		// Add the edges to the adjacency lists of both source and target vertex
		if (srcList == null)
			srcList = new EdgeList();
		EdgeList tarList = edgeLists[getIndices().get(tarLabel)];
		if (tarList == null)
			tarList = new EdgeList();
		srcList.add(tarLabel);
		tarList.add(srcLabel);
	} // end of addEdge()

	public void toggleVertexState(String vertLabel) {
		// Check if the vertex is present
		if (!getIndices().containsKey(vertLabel) || !getSirStates().containsKey(vertLabel)) {
			System.err.println("The vertex is not present in the graph");
			return;
		}

		if (getSirStates().get(vertLabel) == SIRState.S)
			getSirStates().replace(vertLabel, SIRState.I);
		else
			getSirStates().replace(vertLabel, SIRState.R);
	} // end of toggleVertexState()

	public void deleteEdge(String srcLabel, String tarLabel) {
		// Check if the edge is present
		Integer srcIndex = getIndices().get(srcLabel);
		if (srcIndex == null) {
			System.err.println("The edge is not present");
			return;
		}
		EdgeList srcList = edgeLists[srcIndex];
		if (!srcList.contain(tarLabel)) {
			System.err.println("The edge is not present");
			return;
		}

		// Remove the edges from the adjacency lists of both source and target vertex
		Integer tarIndex = getIndices().get(tarLabel);
		EdgeList tarList = edgeLists[tarIndex];
		srcList.remove(tarLabel);
		tarList.remove(srcLabel);
	} // end of deleteEdge()

	public void deleteVertex(String vertLabel) {
		// deleting from all the linkedlist
		if (getIndices().containsKey(vertLabel)) {
			for (int i = 0; i < edgeLists.length; i++) {
				// should be change the return type to null
				edgeLists[i].remove(vertLabel);
			}
			int pos = getIndices().get(vertLabel);
			EdgeList[] temp = new EdgeList[edgeLists.length - 1];
			for (int i = 0; i < edgeLists.length - 1; i++) {
				if (i >= pos)
					temp[i] = edgeLists[i + 1];
				else
					temp[i] = edgeLists[i];
			}
			// deleting from HashMaps
			getIndices().remove(vertLabel);
			getSirStates().remove(vertLabel);
			// readjusting the Hashmaps
			for (Map.Entry<String, Integer> entry : getIndices().entrySet()) {
				if (entry.getValue() > pos) {
					getIndices().put(entry.getKey(), entry.getValue() - 1);
				}
			}
		} else {
			System.err.println("Vertex not found. Please enter an existing vertex.");
		}

	} // end of deleteVertex()

	public String[] kHopNeighbours(int k, String vertLabel) {
		if (k == 0) {
			return null;
		}

		String[] neighbours = null;

		// BFS
		// Define "depth" as the number of hops away from the initial vertex
		int depth = 0;
		// Store the vertices visited in the last depth
		String[] lastVisited = { vertLabel };
		while (depth < k) {
			// Terminate BFS when no more vertex is visited in the last depth
			if (lastVisited == null)
				return neighbours;

			String[] currVisited = null;

			// Locate the vertices visited in the last depth
			for (int m = 0; m < lastVisited.length; m++) {
				String srcVertex = lastVisited[m];
				EdgeList list = edgeLists[getIndices().get(srcVertex)];
				// Add the target vertices into neighbours and vertices visited in the current
				// depth
				neighbours = list.addToArray(neighbours);
				currVisited = list.addToArray(currVisited);
			}
			depth++;
			lastVisited = currVisited;
		}

		return neighbours;
	} // end of kHopNeighbours()

	public void printVertices(PrintWriter os) {
		System.out.println(getIndices().keySet());
		for (Map.Entry<String, SIRState> entry : getSirStates().entrySet())
			System.out.print("(" + entry.getKey() + ", " + entry.getValue() + ")");

	} // end of printVertices()

	public void printEdges(PrintWriter os) {
		for (Map.Entry<String, Integer> entry : getIndices().entrySet()) {
			EdgeList list = edgeLists[entry.getValue()];
			list.printEdges(entry.getKey(), os);
		}
	} // end of printEdges()

	private class EdgeList {

		private Node head;
		private int length;

		public void add(String vertLabel) {
			Node newNode = new Node(vertLabel);

			if (head != null)
				newNode.setNext(head);

			head = newNode;

			length++;
		}

		public boolean remove(String vertLabel) {
			if (length == 0) {
				return false;
			}

			Node currNode = head;
			Node prevNode = null;

			// check if value is head node
			if (currNode.getVertex().equals(vertLabel)) {
				head = currNode.getNext();
				length--;
				return true;
			}

			prevNode = currNode;
			currNode = currNode.getNext();

			// scan through list to find node to delete
			while (currNode != null) {
				if (currNode.getVertex().equals(vertLabel)) {
					prevNode.setNext(currNode.getNext());
					currNode = null;
					length--;
					return true;
				}
				prevNode = currNode;
				currNode = currNode.getNext();
			}

			return false;
		}

		public String get(int index) throws IndexOutOfBoundsException {
			if (index >= length || index < 0) {
				throw new IndexOutOfBoundsException("Supplied index is invalid.");
			}

			Node currNode = head;
			for (int i = 0; i < index; ++i) {
				currNode = currNode.getNext();
			}

			return currNode.getVertex();
		}

		public boolean contain(String vertex) {
			Node currNode = head;
			for (int i = 0; i < length; ++i) {
				if (currNode.getVertex().equals(vertex)) {
					return true;
				}
				currNode = currNode.getNext();
			}

			return false;
		}

		public void printEdges(String srcLabel, PrintWriter os) {
			Node currNode = head;
			for (int i = 0; i < length; i++) {
				os.println(srcLabel + currNode.getVertex());
				currNode = currNode.getNext();
			}
		}

		public String[] addToArray(String[] arr) {
			Node currNode = head;
			for (int i = 0; i < length; i++) {
				if (arr == null) {
					arr = new String[1];
					arr[0] = currNode.getVertex();
				} else {
					String vertex = currNode.getVertex();

					// Skip the vertex if it has already been visited
					boolean isVisited = false;
					for (String vertLabel : arr) {
						if (vertLabel.equals(vertex)) {
							isVisited = true;
							break;
						}
					}
					if (isVisited)
						break;

					String[] temp = new String[arr.length + 1];
					for (int j = 0; j < arr.length; j++)
						temp[j] = arr[j];
					temp[temp.length - 1] = vertex;
					arr = temp;
				}
				currNode = currNode.getNext();
			}

			return arr;
		}

		private class Node {

			protected String vertLabel;
			protected Node nextNode;

			public Node(String vertLabel) {
				this.vertLabel = vertLabel;
				nextNode = null;
			}

			public String getVertex() {
				return vertLabel;
			}

			public Node getNext() {
				return nextNode;
			}

			public void setNext(Node next) {
				this.nextNode = next;
			}
		}
	}
} // end of class AdjacencyList
