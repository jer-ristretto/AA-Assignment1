package generation;

import java.io.FileWriter; // Import the FileWriter class
import java.io.IOException; // Import the IOException class to handle errors
import java.io.PrintWriter;

public class AddVertexScript {

	// No.of existing vertices
	final static int LENGTH = 50000;
	// output file name
	final static String FILENAME = "C:\\\\Users\\\\Aaron\\\\Downloads\\\\EpidemicModel\\\\AddVertex-high.in";
	// No.of vertices to be added
	final static int COUNT = 100;

	public static void main(String[] args) {
		try {

			PrintWriter writer = new PrintWriter(new FileWriter(FILENAME));

			for (int i = LENGTH + 1; i <= LENGTH + COUNT; i++) {
				writer.println("AV " + i);

			}
			writer.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
}