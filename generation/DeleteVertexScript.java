package generation;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class DeleteVertexScript {

	// No.of existing vertices
	final static int LENGTH = 50000;
	// output file name
	static String FILENAME = "D:\\Git-Repo\\RMIT\\AnA\\Assignment1\\AA-Assignment1\\TestCases\\EdgeDelete\\Outputs\\graph1.out";
	// No.of vertices to be added
	final static int COUNT = 100;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int arr[] = new int[COUNT];
		Arrays.fill(arr, -1);
		int capacity = 0;
		boolean val = false;
		boolean flag = true;
		do {
			int random_int = (int) Math.floor(Math.random() * (LENGTH));
			for (int k = 0; k < arr.length; k++) {
				if (arr[k] == random_int + 1)
					val = true;
			}
			if (val == false) {
				arr[capacity] = random_int + 1;
				capacity++;
			} else
				val = false;

			if (arr[arr.length - 1] != -1)
				flag = false;
		} while (flag);
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(FILENAME));
			for (int i = 0; i < arr.length; i++) {
				writer.println("DV " + arr[i]);

			}
			writer.close();
			System.out.println("Successfully wrote to the file.");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
