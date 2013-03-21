package hr.fer.zemris.apr.lab2;

import hr.fer.zemris.apr.utilities.Function;

import java.util.Arrays;

public class HookeJeves extends OptimizingMethod {
	private double[] e;
	private double[] dx;
	private double[] xn;
	private double[] xp;
	private double[] xb;
	private Function function;
	public long sleep = 0L;

	public HookeJeves(Function function) {
		setFunction(function);
	}

	public HookeJeves(String parameters, Function function) {
		this.function = function;
		parseFile(parameters);
	}

	public void sleep() {
		if (sleep != 0L) {
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public double[] optimize(double[] x0) {
		if (e == null) { // defaultne vrijednosti
			e = new double[x0.length];
			dx = new double[x0.length];
			Arrays.fill(e, 10e-10);
			Arrays.fill(dx, 0.5);
		}

		System.out.println("--- HOOKE-JEEVES POSTUPAK---");
		System.out.println("Pocetna tocka: " + Arrays.toString(x0));
		System.out.println("Parametri programa:\nPreciznost"
				+ Arrays.toString(e) + "\nVektor pomaka: "
				+ Arrays.toString(dx));
		System.out.println("Funkcija: " + function.toString());
		System.out.println("Postavljam xp i xb na pocetnu tocku!");

		xp = x0.clone();
		xb = x0.clone();

		for (int i = 0; stopCondition(); i++) {
			System.out.println("--------------------------\nNOVA ITERACIJA: "
					+ i + ".");
			xn = explore();
			System.out.println("Novi xb: " + Arrays.toString(xb));
			System.out.println("Novi xp: " + Arrays.toString(xp));
			System.out.println("Novi xn: " + Arrays.toString(xn));

			if (function.getValue(xn) < function.getValue(xb)) {
				System.out.println("Nova bazna tocka je prihvacena!");
				xp = calculateNewPoint(xn, xb);
				xb = xn.clone();
			} else {
				System.out
						.println("Nova bazna tocka nije prihvacena.\nSmanjujemo preciznost za pola.");
				lowerThePrecision();
				xp = xb.clone();
			}
			sleep();
		}
		System.out
				.println("----------Postupak zavrsen----------\nKonacno rjesenje: "
						+ Arrays.toString(xb));
		return xb;
	}

	public boolean stopCondition() {
		for (int i = 0; i < dx.length; i++) {
			if (dx[i] <= e[i]) {
				return false;
			}
		}
		return true;
	}

	public double[] explore() {
		double[] x = xp.clone();
		for (int i = 0; i < x.length; i++) {
			double P = function.getValue(x);
			x[i] += dx[i];
			double N = function.getValue(x);

			if (N > P) {
				x[i] -= 2 * dx[i];
				N = function.getValue(x);
				if (N > P) {
					x[i] += dx[i];
				}
			}
		}
		return x;
	}

	public void lowerThePrecision() {
		for (int i = 0; i < dx.length; i++) {
			dx[i] /= 2;
		}
	}

	public double[] calculateNewPoint(double[] x1, double[] x2) {
		double[] x = x1.clone();
		for (int i = 0; i < x.length; i++) {
			x[i] *= 2;
			x[i] -= x2[i];
		}
		return x;
	}

	@Override
	protected void parseFile(String parameters) {
		System.out.println(parameters);
		String[] tmp = parameters.split("\n");
		String[] eps = tmp[0].split(" +|\t+");
		String[] d = tmp[1].split(" +|\t+");
		e = new double[eps.length];
		dx = new double[d.length];
		for (int i = 0; i < eps.length; i++) {
			e[i] = Double.parseDouble(eps[i]);
			dx[i] = Double.parseDouble(d[i]);
		}
	}

	public double[] getE() {
		return e;
	}

	public void setE(double[] e) {
		this.e = e;
	}

	public double[] getDx() {
		return dx;
	}

	public void setDx(double[] dx) {
		this.dx = dx;
	}

	public double[] getXn() {
		return xn;
	}

	public void setXn(double[] xn) {
		this.xn = xn;
	}

	public double[] getXp() {
		return xp;
	}

	public void setXp(double[] xp) {
		this.xp = xp;
	}

	public double[] getXb() {
		return xb;
	}

	public void setXb(double[] xb) {
		this.xb = xb;
	}

	public Function getFunction() {
		return function;
	}

	public void setFunction(Function function) {
		this.function = function;
	}

}
