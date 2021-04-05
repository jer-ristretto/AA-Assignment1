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
    private HashMap<String, Integer> edgeIndices;

	/**
	 * Construct empty graph.
	 */
    public IncidenceMatrix() {
        matrix = null;
        edgeIndices = new HashMap<String, Integer>();
    } // end of IncidenceMatrix()


    public void addVertex(String vertLabel) {
        // Check duplicate vertex
        if (getIndices().containsKey(vertLabel))
            System.err.println("Found duplicate vertex");
        else {
            // Map the new vertex to its index and SIR state
            getIndices().put(vertLabel, getIndices().size());
            getSirStates().put(vertLabel, SIRState.S);

            // Insert the row of the new vertex into the matrix
            if (matrix != null) {
                boolean[][] tempMatrix = new boolean[matrix.length + 1][matrix[0].length];
                for (int i = 0; i < matrix.length; i++) {
                    for (int j = 0; j < matrix[0].length; j++) {
                        tempMatrix[i][j] = matrix[i][j];
                    }
                }
                matrix = tempMatrix;
            }
        }
    } // end of addVertex()


    public void addEdge(String srcLabel, String tarLabel) {
        // Check duplicate edge
        if (edgeIndices.containsKey(srcLabel + tarLabel)
                || edgeIndices.containsKey(tarLabel + srcLabel)) {
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
        edgeIndices.put(srcLabel + tarLabel, edgeIndices.size());

        // Instantiate the matrix if this is the first edge added into the graph
        if (matrix == null) {
            matrix = new boolean[getIndices().size()][1];
        }
        else {
            // Insert the column of the new edge into the matrix
            boolean[][] tempMatrix = new boolean[matrix.length][matrix[0].length + 1];
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    tempMatrix[i][j] = matrix[i][j];
                }
            }
            matrix = tempMatrix;
        }

        // Reflect presence of the edge
        int srcIndex = getIndices().get(srcLabel);
        matrix[srcIndex][matrix[0].length - 1] = true;
        int tarIndex = getIndices().get(tarLabel);
        matrix[tarIndex][matrix[0].length - 1] = true;
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
        // Check if the edge is present
        // Remove the edge from the map if yes
        int edgeIndex = -1;
        if (edgeIndices.containsKey(srcLabel + tarLabel)) {
            edgeIndex = edgeIndices.get(srcLabel + tarLabel);
            edgeIndices.remove(srcLabel + tarLabel);
        }
        else if (edgeIndices.containsKey(tarLabel + srcLabel)) {
            edgeIndex = edgeIndices.get(tarLabel + srcLabel);
            edgeIndices.remove(tarLabel + srcLabel);
        }
        else {
            System.err.println("The edge is not present in the graph");
            return;
        }

        // Remove the column of the edge from the matrix
        if (matrix[0].length == 1)
            matrix = null;
        else {
            boolean[][] tempMatrix = new boolean[matrix.length][matrix[0].length - 1];
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    // Copy the columns as is until reach the column to be deleted
                    if (j < edgeIndex)
                        tempMatrix[i][j] = matrix[i][j];
                    //Move the following columns to the left
                    else
                        tempMatrix[i][j] = matrix[i][j + 1];
                }
            }
            matrix = tempMatrix;
        }

        // Move the indices of behind edges forward
        for (Map.Entry<String, Integer> entry : edgeIndices.entrySet()) {
            if (entry.getValue() > edgeIndex)
                entry.setValue(entry.getValue() - 1);
        }
    } // end of deleteEdge()


    public void deleteVertex(String vertLabel) {
        // Check if the vertex is present
        if (!getIndices().containsKey(vertLabel)
                || !getSirStates().containsKey(vertLabel)) {
            System.err.println("The vertex is not present in the graph");
            return;
        }

        // Remove all edges of the vertex
        for (String edgeLabel : edgeIndices.keySet())
            if (edgeLabel.contains(vertLabel))
                deleteEdge(edgeLabel.substring(0, 1), edgeLabel.substring(1));

        // Remove the row of the vertex from the matrix
        if (matrix.length == 1)
            matrix = null;
        else {
            boolean[][] tempMatrix = new boolean[matrix.length - 1][matrix[0].length];
            for (int i = 0; i < matrix.length; i++) {
                // Copy the rows as is until reach the row to be deleted
                if (i < getIndices().get(vertLabel)) {
                    for (int j = 0; j < matrix[0].length; j++) {
                        tempMatrix[i][j] = matrix[i][j];
                    }
                }
                // Skip the row to be deleted
                // Move up the following rows
                else {
                    for (int j = 0; j < matrix[0].length; j++) {
                        tempMatrix[i][j] = matrix[i + 1][j];
                    }
                }
            }
            matrix = tempMatrix;
        }

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
            return null;

        String[] neighbours = null;

        // BFS
        // Define "depth" as the number of hops away from the initial vertex
        int depth = 0;
        // Store the vertices visited in the last depth
        String[] lastVisited = {vertLabel};
        while (depth < k) {
            // Terminate BFS when no more vertex is visited in the last depth
            if (lastVisited == null)
                return neighbours;

            String[] currVisited = null;

            // Locate the vertices visited in the last depth
            for (int m = 0; m < lastVisited.length; m++) {
                String srcVertex = lastVisited[m];
                int rowIndex = getIndices().get(srcVertex);
                // Find the edges associated with the vertices
                for (int j = 0; j < matrix[0].length; j++) {
                    if (matrix[rowIndex][j] == true) {
                        // Use the edge to find the index of the target vertex
                        for (int i = 0; i < matrix.length; i++) {
                            if (matrix[i][j] == true) {
                                // Retrieve the label of the target vertex with its index
                                String tarVertex = null;
                                boolean isSrc = false;
                                for (Map.Entry<String, Integer> entry : getIndices().entrySet()) {
                                    // Check if the vertex is the source vertex itself
                                    if (entry.getValue() == i && entry.getKey().equals(srcVertex)) {
                                        isSrc = true;
                                        break;
                                    }
                                    if (entry.getValue() == i && !entry.getKey().equals(srcVertex)) {
                                        tarVertex = entry.getKey();
                                        break;
                                    }
                                }
                                // Move to the next row if the vertex is the source vertex itself
                                if (isSrc)
                                    continue;
                                // Check if this vertex has already been visited
                                boolean isVisited = false;
                                for (String visitedVertex : neighbours) {
                                    if (tarVertex.equals(visitedVertex)) {
                                        isVisited = true;
                                        break;
                                    }
                                }
                                if (!isVisited) {
                                    // Add the target vertex into the array of neighbours
                                    if (neighbours == null)
                                        neighbours = new String[]{tarVertex};
                                    else {
                                        String[] temp = new String[neighbours.length + 1];
                                        for (int n = 0; n < neighbours.length; n++)
                                            temp[n] = neighbours[n];
                                        temp[temp.length - 1] = tarVertex;
                                        neighbours = temp;
                                    }
                                    // Record the target vertex visited in the current depth
                                    if (currVisited == null)
                                        currVisited = new String[]{tarVertex};
                                    else {
                                        String[] temp = new String[neighbours.length + 1];
                                        for (int n = 0; n < temp.length; n++)
                                            temp[n] = currVisited[n];
                                        temp[temp.length - 1] = tarVertex;
                                        currVisited = temp;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            depth++;
            lastVisited = currVisited;
        }

        return neighbours;
    } // end of kHopNeighbours()


    public void printVertices(PrintWriter os) {
        for (Map.Entry<String, SIRState> entry : getSirStates().entrySet()) {
            os.print("(" + entry.getKey() + ", " + entry.getValue() + ") ");
        }
        os.println();
    } // end of printVertices()


    public void printEdges(PrintWriter os) {
        for (Map.Entry<String, Integer> entry : edgeIndices.entrySet()) {
            os.println(entry.getKey().charAt(0) + " " + entry.getKey().charAt(1));
            os.println(entry.getKey().charAt(1) + " " + entry.getKey().charAt(0));
        }
    } // end of printEdges()

} // end of class IncidenceMatrix
