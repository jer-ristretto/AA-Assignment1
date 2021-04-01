import java.io.PrintWriter;
// import java.util.*;

/**
 * Adjacency list implementation for the AssociationGraph interface.
 *
 * Your task is to complete the implementation of this class.  You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2019.
 */
public class AdjacencyList extends AbstractGraph
{

    private LinkedList[] linkedLists;


    /**
	 * Contructs empty graph.
	 */
    public AdjacencyList() {
        linkedLists = null;
    } // end of AdjacencyList()


    public void addVertex(String vertLabel) {
        // Implement me!

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
        // Implement me!
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


    private class LinkedList {

        private Node head;
        private int length;


        public void add(int newValue) {
            Node newNode = new Node(newValue);

            // If head is empty, then list is empty and head reference need to be initialised.
            if (head == null) {
                head = newNode;
            }
            // otherwise, add node to the head of list.
            else {
                newNode.setNext(head);
                head = newNode;
            }

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


        private class Node {
            /** Stored value of node. */
            protected String vertLabel;
            /** Reference to next node. */
            protected Node nextNode;


            public Node(int vertLabel) {
                vertLabel = vertLabel;
                nextNode = null;
            }


            public String getVertex() {
                return vertLabel;
            }


            public Node getNext() {
                return nextNode;
            }


            public void setVertex(String vertLabel) {
                this.vertLabel = vertLabel;
            }


            public void setNext(Node next) {
                this.nextNode = next;
            }
        }
    }
} // end of class AdjacencyList
