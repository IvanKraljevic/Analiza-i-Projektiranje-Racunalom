package hr.fer.zemris.apr.lab2;

import hr.fer.zemris.apr.utilities.F1;
import hr.fer.zemris.apr.utilities.F2;
import hr.fer.zemris.apr.utilities.F3;
import hr.fer.zemris.apr.utilities.F4;
import hr.fer.zemris.apr.utilities.Function;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
	BufferedReader reader;
	Function function;
	OptimizingMethod optAlgorithm;

	private void go() {
		function = null;
		reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(System.in));
			System.out
					.println("Odaberi algoritam:\nHooke-Jeves: 1\nBoxov algoritam: 2");
			String algorithm = reader.readLine();

			System.out.println("Putanja do datoteke sa parametrima?");
			String filePath = reader.readLine();

			System.out.println("Odaberi funkciju: 1,2,3 ili 4:");
			String functionString = reader.readLine();
			function = getFunction(functionString);

			optAlgorithm = getOptimizingMethodAlgorithm(algorithm, filePath,
					function);
			System.out.println("Unesi pocetnu tocku pretrazivanja:");
			String[] stringPocetna = reader.readLine().split(" +|\t+");
			double[] x0 = new double[stringPocetna.length];
			for (int i = 0; i < x0.length; i++) {
				x0[i] = Double.parseDouble(stringPocetna[i]);
			}

			try {
				optAlgorithm.optimize(x0);
			} catch (RuntimeException e) {
				System.out.println("Pocetna tocka nije dobro zadana!");
				System.exit(0);
			}
		} catch (IOException e) {
			
		}
	}

	private Function getFunction(String functionString) throws IOException {
		switch (functionString) {
		case "1":
			return new F1();
		case "2":
			return new F2();
		case "4":
			return new F4();
		case "3":
			System.out.println("Unesi parametre funkcije:");
			String params = reader.readLine();
			return new F3(params);
		default:
			System.out.println("Krivi unos!");
			System.exit(0);
		}
		return null;
	}

	private OptimizingMethod getOptimizingMethodAlgorithm(String algorithm,
			String filePath, Function function) {
		BufferedReader fileReader = null;
		OptimizingMethod method = null;

		if (algorithm.trim().equalsIgnoreCase("1")) {
			if (filePath.trim().equals("")) {
				return new HookeJeves(function);
			} else {
				try {
					fileReader = new BufferedReader(new FileReader(filePath));
					String params = fileReader.readLine();
					params += "\n" + fileReader.readLine();
					fileReader.close();
					return new HookeJeves(params, function);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			if (filePath.trim().equals("")) {
				return new Box(function);
			} else {
				try {
					fileReader = new BufferedReader(new FileReader(filePath));
					String params = fileReader.readLine();
					fileReader.close();
					return new Box(params, function);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return method;
	}

	public static void main(String[] args) {
		new Main().go();
	}
}
