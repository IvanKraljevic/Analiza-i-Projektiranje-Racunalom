package hr.fer.zemris.apr.lab3;

import hr.fer.zemris.apr.lab2.F3;
import hr.fer.zemris.apr.lab2.F4;
import hr.fer.zemris.apr.lab2.Function;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
	private BufferedReader reader;
	private Function function;
	private ChromosomeDecoder decoder;

	public void go() throws Exception {
		reader = new BufferedReader(new InputStreamReader(
				System.in));
		System.out.println("Odaberi funckiju koju želiš optimirati(3/4/6/7):");
		function = getFunction(reader.readLine());

		System.out
				.println("Unesi broj varijabli, broj bitova po kromosomu, te min i max:");
		decoder = getDecoder(reader.readLine());

		System.out
				.println("Unesi broj iteracija, veličinu populacije i vjerojatnost mutacije:");
		runGA(reader.readLine());
	}
	
	private ChromosomeDecoder getDecoder(String line) {
		if (line.equals("")) {
			return new ChromosomeDecoder(3, 18, -100, 100);
		} else {
			String[] tmp = line.split(" +|\t+");
			int var = Integer.parseInt(tmp[0]);
			int bits = Integer.parseInt(tmp[1]);
			int min = Integer.parseInt(tmp[2]);
			int max = Integer.parseInt(tmp[3]);
			return new ChromosomeDecoder(var, bits, min, max);
		}
	}

	private void runGA(String line) {
		if (line.equals("")) {
			new GeneticAlgorithm(function, decoder).run(400000, 500);
		} else {
			String[] tmp = line.split(" +|\t+");
			GeneticAlgorithm.VJER_MUT = Double.parseDouble(tmp[2]);
			new GeneticAlgorithm(function, decoder).run(
					Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]));
		}
	}

	public Function getFunction(String line) throws IOException {
		switch (line) {
		case "6":
			return new F6();
		case "7":
			return new F7();
		case "4":
			return new F4();
		case "3":
			System.out.println("Unesi parametre funkcije:");
			String params = reader.readLine();
			return new F3(params);
		default:
			System.out.println("Nije zadan dobar broj funkcije.");
			System.exit(0);
		}
		return null;
	}

	public static void main(String[] args) {
		try {
			new Main().go();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
}