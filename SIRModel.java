import java.io.PrintWriter;
import java.util.Map;

/**
 * SIR model.
 *
 * @author Jeffrey Chan, 2021.
 */
public class SIRModel {

	/**
	 * Default constructor, modify as needed.
	 */
	public SIRModel() {

	} // end of SIRModel()

	/**
	 * Run the SIR epidemic model to completion, i.e., until no more changes to the
	 * states of the vertices for a whole iteration.
	 *
	 * @param graph             Input contracts graph.
	 * @param seedVertices      Set of seed, infected vertices.
	 * @param infectionProb     Probability of infection.
	 * @param recoverProb       Probability that a vertex can become recovered.
	 * @param sirModelOutWriter PrintWriter to output the necessary information per
	 *                          iteration (see specs for details).
	 */
	public void runSimulation(ContactsGraph graph, String[] seedVertices,
							  float infectionProb, float recoverProb, PrintWriter sirModelOutWriter) {

		AbstractGraph abstractGraph = (AbstractGraph) graph;

		DynamicArray<String> infected = new DynamicArray<String>();

		// Add existing infected vertices into the infected array
		for (Map.Entry<String, SIRState> entry : abstractGraph.getSirStates().entrySet()) {
			if (entry.getValue() == SIRState.I)
				infected.add(entry.getKey());
		}

		for (String seed : seedVertices) {
			// Check if seed vertices are present
			if (abstractGraph.getIndices().get(seed) == null) {
				System.err.println("At least one seed vertex is not present.");
				return;
			}
			// Check if the seed vertices are susceptible
			if (abstractGraph.getSirStates().get(seed) == SIRState.S) {
				// Change state for seed vertices and add into the infected array
				graph.toggleVertexState(seed);
				infected.add(seed);
			}
		}

		int t = 1;
		int monitor = 0;
		boolean cont = true;

		while (cont) {
			// Simulate infection and recovering in the network
			DynamicArray<String> newInfected = updatedInfected(graph, infectionProb, seedVertices);
			DynamicArray<String> newRecovered = updateRecovered(graph, infected, recoverProb);

			// Add new infected vertices from last iteration to total infected vertices
			for (int i = 0; i < newInfected.getSize(); i++) {
				infected.add(newInfected.get(i));
			}

			// Increment the monitor if no state change
			if (newRecovered.getSize() == 0 && newInfected.getSize() == 0) {
				monitor++;
			}
			// Reset monitor if any state change
			else
				monitor = 0;

			// Print the simulation result
			sirModelOutWriter.print(t + ": [");
			for (int i = 0; i < newInfected.getSize(); i++) {
				sirModelOutWriter.print(newInfected.get(i) + " ");
			}
			sirModelOutWriter.print("] : [");
			for (int i = 0; i < newRecovered.getSize(); i++) {
				sirModelOutWriter.print(newRecovered.get(i) + " ");
			}
			sirModelOutWriter.print("]");
			sirModelOutWriter.println();

			if (monitor == 10)
				cont = false;
			else {
				t++;
				seedVertices = new String[infected.getSize()];
				for (int i = 0; i < infected.getSize(); i++)
					seedVertices[i] = infected.get(i);
			}
		}

	} // end of runSimulation()


	// Simulate infection in the network
	private DynamicArray<String> updatedInfected(ContactsGraph graph, float infectionProb, String[] seedVertices) {

		AbstractGraph abstractGraph = (AbstractGraph) graph;

		DynamicArray<String> newInfected = new DynamicArray<String>();

		for (int i = 0; i < seedVertices.length; i++) {
			// Find the vertices with close contact
			String[] neighbours = graph.kHopNeighbours(1, seedVertices[i]);

			for (int j = 0; j < neighbours.length; j++) {
				// Infect vertex only if it's in the susceptible state
				if (abstractGraph.getSirStates().get(neighbours[j]) == SIRState.S) {
					float random = (float) Math.random();
					if (random <= infectionProb) {
						graph.toggleVertexState(neighbours[j]);
						newInfected.add(neighbours[j]);
					}
				}
			}
		}
		return newInfected;
	}


	// Simulate recovering in the network
	private DynamicArray<String> updateRecovered(ContactsGraph graph,
												 DynamicArray<String> infected, float recoverProb) {

		DynamicArray<String> newRecovered = new DynamicArray<String>();
		for (int i = 0; i < infected.getSize(); i++) {
			float random = (float) Math.random();
			if (random < recoverProb) {
				String recoveredVert = infected.get(i);
				graph.toggleVertexState(recoveredVert);
				newRecovered.add(recoveredVert);
				infected.remove(i);
			}
		}

		return newRecovered;
	}
} // end of class SIRModel
