import java.io.PrintWriter;

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
	public void runSimulation(ContactsGraph graph, String[] seedVertices, float infectionProb, float recoverProb,
			PrintWriter sirModelOutWriter) {
		// IMPLEMENT ME!
		int t = 1;
		int monitor = 0;
		boolean cont = true;
		DynamicArray<String> infected = new DynamicArray<String>();
		DynamicArray<String> recovered = new DynamicArray<String>();

		while (cont) {
			DynamicArray<String> newInfected = new DynamicArray<String>();
			DynamicArray<String> newRecovered = new DynamicArray<String>();

			newInfected = updatedInfected(graph, infectionProb, seedVertices);
			// infected = infected+ newInfected; copy the elements of newInfected to
			// infected array
			for (int i = 0; i < newInfected.getSize(); i++) {
				infected.add(newInfected.get(i));
			}
			newRecovered = updateRecovered(graph, infected, recoverProb);
			for (int i = 0; i < newRecovered.getSize(); i++) {
				recovered.add(newRecovered.get(i));
			}
			if (newRecovered.getSize() == 0 && newInfected.getSize() == 0) {
				monitor++;
			} else
				monitor = 0;

			if (monitor == 10)
				cont = false;
			else {
				t++;
				seedVertices = new String[infected.getSize()];
				for (int i = 0; i < infected.getSize(); i++)
					seedVertices[i] = infected.get(i);
			}
			sirModelOutWriter.print(t + ": [");
			for (int i = 0; i < newInfected.getSize(); i++) {
				sirModelOutWriter.print(newInfected.get(i) + " ");
			}
			sirModelOutWriter.print("] : [");
			for (int i = 0; i < newRecovered.getSize(); i++) {
				sirModelOutWriter.print(newRecovered.get(i) + " ");
			}
			sirModelOutWriter.print("]");
		}

	} // end of runSimulation()

	private DynamicArray<String> updatedInfected(ContactsGraph graph, float infectionProb, String[] seedVertices) {
		// TODO Auto-generated method stub
		AbstractGraph abstractGraph = (AbstractGraph) graph;
		DynamicArray<String> newInfected = new DynamicArray<String>();
		String[] temp = null;
		for (int i = 0; i < seedVertices.length; i++) {
			temp = graph.kHopNeighbours(1, seedVertices[i]);
			for (int j = 0; j < temp.length; j++) {
				if (abstractGraph.getSirStates().get(temp[i]) == SIRState.S) {
					float random = (float) Math.random();
					if (random <= infectionProb) {
						graph.toggleVertexState(temp[i]);
						newInfected.add(temp[i]);
					}
				}
			}
		}
		return newInfected;
	}

	private DynamicArray<String> updateRecovered(ContactsGraph graph, DynamicArray<String> infected,
			float recoverProb) {
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
