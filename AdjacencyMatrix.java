import java.io.PrintWriter;
import java.util.Map;

/**
 * Adjacency matrix implementation for the GraphInterface interface.
 *
 * Your task is to complete the implementation of this class. You may add
 * methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2021.
 */
public class AdjacencyMatrix extends AbstractGraph {

	/**
	 * Contructs empty graph.
	 */
	protected int a_matrix[][];

	public AdjacencyMatrix() {
		// Implement me!
		a_matrix = null;
	} // end of AdjacencyMatrix()

	public void addVertex(String vertLabel) {

		// Check if label already exists
		if (getIndices().containsKey(vertLabel))
			System.err.println("This vertex already exists.");
		else {
			SIRState s1 = SIRState.S;
			// Checking if it is the first value
			if (getIndices().isEmpty()) {
				getIndices().put(vertLabel, 0);
				getSirStates().put(vertLabel, s1);
				// adding the new vertex to the matrix
				a_matrix = new int[1][1];
				// self loop is not considered.
				a_matrix[0][0] = 0;
			} else {
				getIndices().put(vertLabel, getIndices().size());
				getSirStates().put(vertLabel, s1);
				int temp[][] = new int[a_matrix.length + 1][a_matrix[0].length + 1];

				for (int i = 0; i < a_matrix.length; i++) {
					for (int j = 0; j < a_matrix[0].length; j++) {
						temp[i][j] = a_matrix[i][j];
					}
				}
				a_matrix = temp;
			}
		}

	}
	// end of addVertex()

	public void addEdge(String srcLabel, String tarLabel) {

		// Checking if label exists
		if ((getIndices().containsKey(srcLabel)) && (getIndices().containsKey(tarLabel))) {
			if (a_matrix[getIndices().get(srcLabel)][getIndices().get(tarLabel)] == 1
					|| a_matrix[getIndices().get(tarLabel)][getIndices().get(srcLabel)] == 1)
				System.err.println("Edge already exists.");
			else {
				a_matrix[getIndices().get(srcLabel)][getIndices().get(tarLabel)] = 1;
				a_matrix[getIndices().get(tarLabel)][getIndices().get(srcLabel)] = 1;
			}
		} else if (!(getIndices().containsKey(srcLabel)))
			System.err.println("Source Vertex dosen't exist. Please enter an existing vertex.");
		else if (!(getIndices().containsKey(tarLabel)))
			System.err.println("Target Vertex dosen't exist. Please enter an existing vertex.");

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

		if ((getIndices().containsKey(srcLabel)) && (getIndices().containsKey(tarLabel))) {
			if (a_matrix[getIndices().get(srcLabel)][getIndices().get(tarLabel)] == 0
					&& a_matrix[getIndices().get(tarLabel)][getIndices().get(srcLabel)] == 0)
				System.err.println("Target Edge doesn't exist.");
			else {
				a_matrix[getIndices().get(srcLabel)][getIndices().get(tarLabel)] = 0;
				a_matrix[getIndices().get(tarLabel)][getIndices().get(srcLabel)] = 0;
			}
		} else if (!(getIndices().containsKey(srcLabel)))
			System.err.println("Source Vertex doesn't exist. Please enter an existing vertex.");
		else if (!(getIndices().containsKey(tarLabel)))
			System.err.println("Target Vertex doesn't exist. Please enter an existing vertex.");

	} // end of deleteEdge()

	public void deleteVertex(String vertLabel) {
		// deleting from the maps
		if (getIndices().containsKey(vertLabel)) {
			int position = getIndices().get(vertLabel);
			getIndices().remove(vertLabel);
			getSirStates().remove(vertLabel);
			// Deleting from the matrix
			int temp[][] = new int[getIndices().size()][getIndices().size()];
			for (int i = 0; i < a_matrix.length - 1; i++) {
				if (i >= position) {
					for (int j = 0; j < a_matrix[0].length - 1; j++) {
						if (j >= position)
							temp[i][j] = a_matrix[i + 1][j + 1];
						else
							temp[i][j] = a_matrix[i + 1][j];
					}
				} else {
					for (int j = 0; j < a_matrix[0].length - 1; j++) {
						if (j >= position)
							temp[i][j] = a_matrix[i][j + 1];
						else
							temp[i][j] = a_matrix[i][j];
					}
				}
			}
			a_matrix = temp;

			// re-adjusting the values of all the keys
			for (Map.Entry<String, Integer> entry : getIndices().entrySet()) {
				if (entry.getValue() > position) {
					getIndices().put(entry.getKey(), entry.getValue() - 1);
				}
			}

		} else {
			System.err.println("Vertex not found. Please enter an existing vertex.");
		}

	} // end of deleteVertex()

	public String[] kHopNeighbours(int k, String vertLabel) {
		// Implement me!
		int pos = getIndices().get(vertLabel);
		int counter = 0;
		// queue for storing the neighbors
		int arr_queue[] = new int[a_matrix[0].length];
		for (int i = 0; i < arr_queue.length; i++) {
			arr_queue[i] = -1;
		}
		// storing the first value
		arr_queue[0] = pos;
		// position of checkpoint in the queue
		int flag = 0;
		// total size of the queue filled currently. max will be all the vertices.
		int size = 1;
		// new added elements
		do {
			int newly_added = 0;
			do {
				for (int i = 0; i < a_matrix[0].length; i++) {
					if (a_matrix[arr_queue[flag]][i] == 1) {
						// storing neighbors into the queue.
						boolean flag1 = false;
						for (int j = 0; j < arr_queue.length; j++) {
							if (arr_queue[j] == i)
								flag1 = true;
						}
						if (flag1 == false) {
							newly_added++;
							arr_queue[newly_added + size - 1] = i;
						}
					}
				}
				flag++;
			} while (flag < size);
			size = size + (newly_added);
			counter++;

		} while (counter < k);
		if (arr_queue[1] == -1)
			return new String[0];
		else {
			// creating a new array without the first element

			String arr_neighbours[] = new String[size - 1];
			for (int i = 1; i < size; i++) {
				for (Map.Entry<String, Integer> entry : getIndices().entrySet()) {
					if (arr_queue[i] == entry.getValue()) 
						arr_neighbours[i - 1] = entry.getKey();
				}
			}
			return arr_neighbours;
		}
	} // end of kHopNeighbours()

	public void printVertices(PrintWriter os) {
		// is the output format fine??
		System.out.println(getIndices().keySet());
		for (Map.Entry<String, SIRState> entry : getSirStates().entrySet())
			System.out.print("(" + entry.getKey() + "," + entry.getValue() + ")	");
			System.out.println();

	} // end of printVertices()

	public void printEdges(PrintWriter os) {

		for (int i = 0; i < a_matrix.length; i++) {
			for (int j = 0; j < a_matrix[0].length; j++) {
				if (a_matrix[i][j] == 1) {
					String src = null, tar = null;
					for (Map.Entry<String, Integer> entry : getIndices().entrySet()) {
						if (entry.getValue() == i)
							src = entry.getKey();
						if (entry.getValue() == j)
							tar = entry.getKey();
					}
					System.out.println(src + " " + tar);
				}
			}
		}
	} // end of printEdges()

} // end of class AdjacencyMatrix
