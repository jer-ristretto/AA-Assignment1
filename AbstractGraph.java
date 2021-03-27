import java.io.PrintWriter;
import java.util.HashMap;

/**
 * Abstract class to allow you to implement common functionality or definitions.
 * All three graph implement classes extend this.
 *
 * Note, you should make sure to test after changes.  Note it is optional to
 * use this file.
 *
 * @author Jeffrey Chan, 2021.
 */
public abstract class AbstractGraph implements ContactsGraph
{
    // key-value
    private HashMap<String, Integer> indices;
    private HashMap<String, SIRState> sirStates;

    public AbstractGraph() {
        this.indices = new HashMap<String, Integer>();
        this.sirStates = new HashMap<String, SIRState>();
    }

    public HashMap<String, Integer> getIndices() {
        return indices;
    }

    public HashMap<String, SIRState> getSirStates() {
        return sirStates;
    }
} // end of abstract class AbstractGraph
