import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Incidence matrix implementation for the GraphInterface interface.
 *
 * Your task is to complete the implementation of this class.  You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2021.
 */
public class IncidenceMatrix extends AbstractGraph
{

    private boolean[][] matrix;
    private int rowSize;
    private int colSize;
    private static final int iniSize = 2;
    private HashMap<String, Integer> edgeIndices;
    private HashMap<Integer, String> edgeLabels;

	/**
	 * Construct empty graph.
	 */
    public IncidenceMatrix() {
        matrix = new boolean[iniSize][iniSize];
        rowSize = 0;
        colSize = 0;
        edgeIndices = new HashMap<String, Integer>();
        edgeLabels = new HashMap<Integer, String>();
    } // end of IncidenceMatrix()


    public void addVertex(String vertLabel) {
        // Check duplicate vertex
        if (getIndices().containsKey(vertLabel))
            System.err.println("Found duplicate vertex");
        else {
            // Map the new vertex to its index and SIR state
            getIndices().put(vertLabel, getIndices().size());
            getSirStates().put(vertLabel, SIRState.S);

            // Add the row of the new vertex into the matrix
            if (rowSize >= matrix.length) {
                boolean[][] tempMatrix = new boolean[matrix.length * 2][matrix[0].length];
                for (int i = 0; i < matrix.length; i++) {
                    for (int j = 0; j < matrix[0].length; j++) {
                        tempMatrix[i][j] = matrix[i][j];
                    }
                }

                matrix = tempMatrix;
            }

            rowSize++;
        }
    } // end of addVertex()


    public void addEdge(String srcLabel, String tarLabel) {
        // Check duplicate edge
        if (edgeIndices.containsKey(srcLabel + " " + tarLabel)
                || edgeIndices.containsKey(tarLabel + " " + srcLabel)) {
            System.err.println("Found duplicate edge");

            return;
        }

        // Check if both vertices are present
        if (!getIndices().containsKey(srcLabel)
                || !getIndices().containsKey(tarLabel)) {
            System.err.println("At least one vertex is not present");
            return;
        }

        // Map the edge to its index
        edgeIndices.put(srcLabel + " " + tarLabel, edgeIndices.size());
        edgeLabels.put(edgeLabels.size(), srcLabel + " " + tarLabel);

        // Add the column of the new edge into the matrix
        if (colSize >= matrix[0].length) {
            boolean[][] tempMatrix = new boolean[matrix.length][matrix[0].length * 2];
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    tempMatrix[i][j] = matrix[i][j];
                }
            }

            matrix = tempMatrix;
        }

        // Reflect presence of the edge in the matrix
        int srcIndex = getIndices().get(srcLabel);
        matrix[srcIndex][colSize] = true;
        int tarIndex = getIndices().get(tarLabel);
        matrix[tarIndex][colSize] = true;

        colSize++;
    } // end of addEdge()


    public void toggleVertexState(String vertLabel) {
        // Check if the vertex is present
        if (!getIndices().containsKey(vertLabel)
                || !getSirStates().containsKey(vertLabel)) {
            System.err.println("The vertex is not present in the graph");
            return;
        }

        if (getSirStates().get(vertLabel) == SIRState.S)
            getSirStates().replace(vertLabel, SIRState.I);
        else
            getSirStates().replace(vertLabel, SIRState.R);
    } // end of toggleVertexState()


    public void deleteEdge(String srcLabel, String tarLabel) {
        // Check if both vertices are present
        if (!getIndices().containsKey(srcLabel)
                || !getIndices().containsKey(tarLabel)) {
            System.err.println("At least one vertex is not present");
            return;
        }

        // Check if the edge is present
        if (!edgeIndices.containsKey(srcLabel + " " + tarLabel)
                && !edgeIndices.containsKey(tarLabel + " " + srcLabel)) {
            System.err.println("The edge is not present in the graph");
            return;
        }

        // Remove the edge from the map
        int edgeIndex = -1;
        if (edgeIndices.containsKey(srcLabel + " " + tarLabel)) {
            edgeIndex = edgeIndices.get(srcLabel + " " + tarLabel);
            edgeIndices.remove(srcLabel + " " + tarLabel);
        }
        else {
            edgeIndex = edgeIndices.get(tarLabel + " " + srcLabel);
            edgeIndices.remove(tarLabel + " " + srcLabel);
        }
        edgeLabels.remove(edgeIndex);

        // Move the indices of behind edges forward
        for (int i = edgeIndex; i < edgeLabels.size(); i++) {
            edgeLabels.put(i, edgeLabels.get(i + 1));
            edgeIndices.replace(edgeLabels.get(i), i);
        }
        if (edgeIndex != edgeLabels.size())
            edgeLabels.remove(edgeLabels.size() - 1);

        // Remove the column of the edge from the matrix
        for (int i = 0; i < rowSize; i++) {
            for (int j = edgeIndex; j < colSize - 1; j++) {
                matrix[i][j] = matrix[i][j + 1];
            }

            matrix[i][colSize - 1] = false;
        }

        colSize--;
    } // end of deleteEdge()


    public void deleteVertex(String vertLabel) {
        // Check if the vertex is present
        if (!getIndices().containsKey(vertLabel)) {
            System.err.println("The vertex is not present in the graph");
            return;
        }

        // Remove all edges of the vertex
        for (int i = 0; i < edgeLabels.size(); i++) {
            String edgeLabel = edgeLabels.get(i);
            String[] srcAndTar = edgeLabel.split(" ", 2);
            if (srcAndTar[0].equals(vertLabel) || srcAndTar[1].equals(vertLabel)) {
                deleteEdge(srcAndTar[0], srcAndTar[1]);
                i--;
            }
        }

        // Remove the row of the vertex from the matrix
        int vertIndex = getIndices().get(vertLabel);
        for (int i = vertIndex; i < rowSize - 1; i++) {
            for (int j = 0; j < colSize; j++) {
                matrix[i][j] = matrix[i + 1][j];
            }
        }

        for (int j = 0; j < colSize; j++) {
            matrix[rowSize - 1][j] = false;
        }

        rowSize--;

        // Move the indices of behind vertices forward
        for (Map.Entry<String, Integer> entry : getIndices().entrySet()) {
            if (entry.getValue() > getIndices().get(vertLabel))
                entry.setValue(entry.getValue() - 1);
        }

        // Delete the vertex in the maps
        getIndices().remove(vertLabel);
        getSirStates().remove(vertLabel);
    } // end of deleteVertex()


    public String[] kHopNeighbours(int k, String vertLabel) {
        if (k == 0)
            return new String[0];

        // Check if the vertex exists
        if (!getIndices().containsKey(vertLabel)) {
            System.err.println("The vertex is not present in the graph");
            return new String[0];
        }

        DynamicArray<String> neighbours = new DynamicArray<String>();

        // BFS
        // Define "depth" as the number of hops away from the initial vertex
        int depth = 0;
        // Store the vertices visited in the last depth
        DynamicArray<String> lastVisited = new DynamicArray<String>();
        lastVisited.add(vertLabel);
        while (depth < k) {
            // Terminate BFS when no more vertex is visited in the last depth
            if (lastVisited.getSize() == 0) {
                String[] neighbours2 = new String[neighbours.getSize()];
                return neighbours.toArray(neighbours2);
            }

            DynamicArray<String> currVisited = new DynamicArray<String>();

            // Locate the vertex visited in the last depth (source vertex)
            for (int m = 0; m < lastVisited.getSize(); m++) {
                String srcVertex = lastVisited.get(m);
                int rowIndex = getIndices().get(srcVertex);

                // Find the edge associated with the source vertex
                for (Map.Entry<String, Integer> entry : edgeIndices.entrySet()) {
                    String[] srcAndTar = entry.getKey().split(" ", 2);
                    if (!srcAndTar[0].equals(srcVertex) && !srcAndTar[1].equals(srcVertex))
                        continue;

                    // Find the target vertex
                    String tarVert = null;
                    if (srcAndTar[0].equals(srcVertex))
                        tarVert = srcAndTar[1];
                    else
                        tarVert = srcAndTar[0];

                    // Skip the edge if the target vertex is the initial source vertex
                    if (vertLabel.equals(tarVert))
                        continue;

                    // Check if the target vertex has already been visited
                    boolean isVisited = false;
                    if (neighbours.getSize() != 0) {
                        for (int n = 0; n < neighbours.getSize(); n++) {
                            if (neighbours.get(n).equals(tarVert)) {
                                isVisited = true;
                                break;
                            }
                        }
                    }

                    if (!isVisited) {
                        // Add the target vertex to the array of neighbours
                        neighbours.add(tarVert);
                        // Record the target vertex visited in the current depth
                        currVisited.add(tarVert);
                    }
                }
            }
            depth++;
            lastVisited = currVisited;
        }

        String[] neighbours2 = new String[neighbours.getSize()];
        return neighbours.toArray(neighbours2);
    } // end of kHopNeighbours()


    public void printVertices(PrintWriter os) {
        for (Map.Entry<String, SIRState> entry : getSirStates().entrySet()) {
            os.print("(" + entry.getKey() + ", " + entry.getValue() + ") ");
        }
        os.println();
    } // end of printVertices()


    public void printEdges(PrintWriter os) {
        for (Map.Entry<String, Integer> entry : edgeIndices.entrySet()) {
            String[] srcAndTar = entry.getKey().split(" ", 2);
            os.println(srcAndTar[0] + " " + srcAndTar[1]);
            os.println(srcAndTar[1] + " " + srcAndTar[0]);
        }
    } // end of printEdges()
} // end of class IncidenceMatrix
