package generation;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class KNeighboursScript {

	// no. of vertices
	private static final int LENGTH = 10;
	// no of Hops to take
	private static final int HOP = 400;
	// output file name
	static String filename = "D:\\Git-Repo\\RMIT\\AnA\\Assignment1\\AA-Assignment1\\TestCases\\Khop\\Inputs\\a-Med.in";
	// no.of KHop commands
	static final int COUNT = 5;

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
			PrintWriter writer = new PrintWriter(new FileWriter(filename));
			for (int i = 0; i < arr.length; i++) {
				writer.println("KN " + HOP + " " + arr[i]);

			}
			writer.close();
			System.out.println("Successfully wrote to the file.");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// KN <<khop>> <<vertex>>.. for 100 times

	}

}
