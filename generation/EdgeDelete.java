import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class EdgeDelete {
	// no.of edges to delete
	private static final int LENGTH = 10;
	// Path for input graph
	static String ifilename = "D:\\Git-Repo\\RMIT\\AnA\\Assignment1\\AA-Assignment1\\TestCases\\EdgeDelete\\Inputs\\1.net";
	// Path for output script
	static String ofilename = "D:\\Git-Repo\\RMIT\\AnA\\Assignment1\\AA-Assignment1\\TestCases\\EdgeAdd\\Outputs\\graph11.out";

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		if (ifilename != null) {

			try {
				BufferedReader reader = new BufferedReader(new FileReader(ifilename));

				String line;
				String delimiterRegex = "[ \t]+";
				String[] tokens;
				String srcLabel, tarLabel;
				int a_matrix[][] = null;

				boolean bVertexPhrase = true;

				while ((line = reader.readLine()) != null) {
					// check if switch to edge phrase, which means line is *Edges
					if (line.compareTo("*Edges") == 0) {
						bVertexPhrase = false;
						continue;
					}

					if (!bVertexPhrase) {
						tokens = line.trim().split(delimiterRegex);
						srcLabel = tokens[0];
						tarLabel = tokens[1];
						// add edge

						if (a_matrix == null) {
							a_matrix = new int[1][2];
							a_matrix[0][0] = Integer.parseInt(srcLabel);
							a_matrix[0][1] = Integer.parseInt(tarLabel);

						} else {
							int temp[][] = new int[a_matrix.length + 1][2];
							for (int i = 0; i < a_matrix.length; i++) {
								temp[i][0] = a_matrix[i][0];
								temp[i][1] = a_matrix[i][1];

							}
							temp[a_matrix.length][0] = Integer.parseInt(srcLabel);
							temp[a_matrix.length][1] = Integer.parseInt(tarLabel);

							a_matrix = temp;

						}

					}
				} // after
				try {
					PrintWriter writer = new PrintWriter(new FileWriter(ofilename));
					int counter = 0;
					int arr[][] = new int[LENGTH][2];
					do {

						int random_int1 = (int) Math.floor(Math.random() * (a_matrix.length));

						boolean monitor = false;
						for (int i = 0; i < counter; i++) {
							if (a_matrix[random_int1][0] == arr[i][0] && a_matrix[random_int1][1] == arr[i][1])
								monitor = true;
						}
						if (!monitor) {
							arr[counter][0] = a_matrix[random_int1][0];
							arr[counter][1] = a_matrix[random_int1][1];

							counter++;
						}
						// }

					} while (counter < LENGTH);
					for (int i = 0; i < arr.length; i++)
						writer.println("DE " + arr[i][0] + " " + arr[i][1]);

					writer.close();
					System.out.println("Successfully wrote to the file.");
				} catch (IOException e) {
					System.out.println("An error occurred.");
					e.printStackTrace();

				}
				reader.close();
			} catch (FileNotFoundException ex) {
				System.out.println("File " + args[1] + " not found.");
			} catch (IOException ex) {
				System.out.println("Cannot open file " + args[1]);
			}
		}

	}

}
