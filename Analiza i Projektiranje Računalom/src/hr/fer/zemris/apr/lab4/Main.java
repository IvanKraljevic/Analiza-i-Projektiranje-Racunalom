package hr.fer.zemris.apr.lab4;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class Main {
	public static String pathA = "A.txt";
	public static String pathB = "B.txt";
	public static String pathX0 = "X0.txt";
	public static String pathT = "T.txt";
	public static String pathX_RK = "X-RK.txt";
	public static String pathX_TR = "X-TR.txt";

	public static void go() throws Exception {
		BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
		NumericalIntegration rungeKutta, trapezodialRule;

		System.out.println("Putanja do matrice A:");
		String line = r.readLine();
		if (line.length() != 0)
			pathA = line;
		System.out.println("Putanja do matrice B:");
		line = r.readLine();
		if (line.length() != 0)
			pathB = line;
		System.out.println("Putanja do matrice x0:");
		line = r.readLine();
		if (line.length() != 0)
			pathX0 = line;

		rungeKutta = new RungeKutta(pathA, pathB, pathX0);
		trapezodialRule = new TrapezodialRule(pathA, pathB, pathX0);

		System.out
				.println("Unesi korak integracije, interval i nakon koliko koraka se vrši ispis:");
		String info = r.readLine();
		String[] tmp = info.split(" +|\t+");

		rungeKutta.compute(Double.parseDouble(tmp[0]),
				Double.parseDouble(tmp[1]), Integer.parseInt(tmp[2]));
		trapezodialRule.compute(Double.parseDouble(tmp[0]),
				Double.parseDouble(tmp[1]),
				Integer.parseInt(tmp[2]));

		rungeKutta.plot("Runge-Kutta");
		trapezodialRule.plot("Trapezni");

		// rungeKutta.matlabMatrixOutput(pathX_RK, pathT);
		// trapezodialRule.matlabMatrixOutput(pathX_TR, pathT);
	}

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		// String pathA = "A.txt";
		// String pathB = "B.txt";
		// String pathX0 = "X0.txt";
		// RungeKutta rk = new RungeKutta(pathA, pathB, pathX0);
		// NumericalIntegration tr = new TrapezodialRule(pathA, pathB, pathX0);
		// rk.compute(0.0001, 10, 1);
		// // tr.compute(1, 2, 10);
		// rk.plot("Runge-Kutta");
		// // tr.plot("Trapezni");

		try {
			go();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
