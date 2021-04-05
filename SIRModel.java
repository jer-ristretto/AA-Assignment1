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
		boolean cont = true;
		String[] infected = null;
		DynamicArray<String> recovered = new DynamicArray<String>();

		while (cont) {
			String[] newInfected = null;
			DynamicArray<String> newRecovered = new DynamicArray<String>();
			newInfected = updatedInfected(infectionProb, seedVertices);
			// infected = infected+ newInfected; copy the elements of newInfected to
			// infected array

		}

	} // end of runSimulation()


	private String[] updatedInfected(float infectionProb, String[] seedVertices) {
		// TODO Auto-generated method stub
		String[] newseedVertices = null;

		return newseedVertices;
	}


	private DynamicArray<String> updateRecovered(ContactsGraph graph, DynamicArray<String> infected, float recoverProb) {
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
