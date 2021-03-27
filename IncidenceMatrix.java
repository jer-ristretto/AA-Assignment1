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

    private int[][] matrix;

	/**
	 * Contructs empty graph.
	 */
    public IncidenceMatrix() {
        matrix = null;
    } // end of IncidenceMatrix()


    public void addVertex(String vertLabel) {
        // Check if vertex already exists
        if (getIndices().containsKey(vertLabel))
            System.err.println("This vertex already exists.");
        else {
            getIndices().put(vertLabel, getIndices().size());
            getSirStates().put(vertLabel, SIRState.S);
        }
    } // end of addVertex()


    public void addEdge(String srcLabel, String tarLabel) {
        // Implement me!
    } // end of addEdge()


    public void toggleVertexState(String vertLabel) {
        // Implement me!
    } // end of toggleVertexState()


    public void deleteEdge(String srcLabel, String tarLabel) {
        // Implement me!
    } // end of deleteEdge()


    public void deleteVertex(String vertLabel) {
        // Check if the key is present
        if (!getIndices().containsKey(vertLabel) || !getSirStates().containsKey(vertLabel))
            System.err.println("Couldn't find the vertex.");
        else {
            // Move the indices of behind vertices forwards
            for (Map.Entry<String, Integer> entry : getIndices().entrySet()) {
                if (entry.getValue() > getIndices().get(vertLabel))
                    entry.setValue(entry.getValue() - 1);
            }
            // Delete the entry in the maps
            getIndices().remove(vertLabel);
            getSirStates().remove(vertLabel);
        }
    } // end of deleteVertex()


    public String[] kHopNeighbours(int k, String vertLabel) {
        // Implement me!

        // please update!
        return null;
    } // end of kHopNeighbours()


    public void printVertices(PrintWriter os) {
        // Implement me!
    } // end of printVertices()


    public void printEdges(PrintWriter os) {
        // Implement me!
    } // end of printEdges()

} // end of class IncidenceMatrix
