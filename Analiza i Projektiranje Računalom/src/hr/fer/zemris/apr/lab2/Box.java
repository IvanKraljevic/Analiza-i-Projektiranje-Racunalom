package hr.fer.zemris.apr.lab2;

import java.util.Arrays;
import java.util.Random;

public class Box extends OptimizingMethod {
	public double EPSILON;
	public double reflectionCoefficient;
	private Random random;
	private double[] explicitBounds;
	private Function function;

	public Box(Function function) {
		EPSILON = 10e-10;
		reflectionCoefficient = 1.3;
		random = new Random();
		explicitBounds = new double[] { -100, 100 };
		this.function = function;
	}

	public Box(double epsilon, double reflectionCoeff, Function function) {
		this.EPSILON = epsilon;
		this.reflectionCoefficient = reflectionCoeff;
		random = new Random();
		explicitBounds = new double[] { -100, 100 };
		this.function = function;
	}

	public Box(String parameters, Function function) {
		parseFile(parameters);
		random = new Random();
		explicitBounds = new double[] { -100, 100 };
		this.function = function;
	}

	@Override
	public double[] optimize(double[] x0) {
		if (!satisfiesExplicit(x0) || !satisfiesImplicit(x0)) {
			throw new RuntimeException();// nismo zadali dobru točku
		}

		System.out.println("Generiram 2n točaka centroida! n=" + x0.length);
		double[] xc = x0.clone();
		double[][] x = new double[2 * xc.length][xc.length];
		for (int t = 0; t < 2 * xc.length; t++) { // generiranje 2n točaka
			for (int i = 0; i < xc.length; i++) {
				x[t][i] = explicitBounds[0] + random.nextDouble()
						* (explicitBounds[1] - explicitBounds[0]);
			}
			while (!satisfiesImplicit(x[t])) {
				x[t] = moveTowardsCentroid(x[t], xc);
			}
			xc = calculateCentroid(x, t); // jer nam nije cijelo polje ispunjeno
											// još
		}
		System.out.println("Generirane tocke centroida:\n"
				+ Arrays.deepToString(x));

		boolean stop = false;
		int br = 0;
		do {
			br++;
			int h = getWorstIndex(x);
			int h2 = get2ndWorstIndex(x, h);
			xc = calculateCentroidWithoutWorst(x, h);
			double[] xr = calculateXr(xc, x[h]);


			System.out.println("----------------");
			System.out.println(br + ". Iteracija.\nNajgora tocka: "
					+ Arrays.toString(x[h]));
			System.out.println("Centroid: " + Arrays.toString(xc));
			System.out.println("Reflektirana tocka: " + Arrays.toString(xr));

			for (int i = 0; i < xr.length; i++) {
				if (xr[i] < explicitBounds[0]) {
					xr[i] = explicitBounds[0];
				} else if (xr[i] > explicitBounds[1]) {
					xr[i] = explicitBounds[1];
				}
			}
			while (!satisfiesImplicit(xr)) {
				xr = moveTowardsCentroid(xr, xc);
			}
			if (function.getValue(xr) > function.getValue(x[h2])) {
				xr = moveTowardsCentroid(xr, xc);
			}
			x[h] = xr;

			/*
			 * stop = true; for (int i = 0; i < xc.length; i++) { if
			 * (Math.abs(xc[i] - x[h][i]) > EPSILON) stop = false; }
			 */
			stop = kriterij(x, xc);
		} while (!stop);

		System.out
				.println("---------PROGRAM ZAVRŠEN---------\nPronađeno rješenje: "
						+ Arrays.toString(xc));
		return xc;
	}

	public boolean kriterij(double[][] x, double[] xc) {
		double value = 0.0;
		for (int i = 0; i < x.length; i++) {
			value += (function.getValue(x[i]) - function.getValue(xc))
					* (function.getValue(x[i]) - function.getValue(xc));
		}
		value /= x.length;
		value = Math.sqrt(value);
		if (value <= EPSILON) {
			return true;
		}
		return false;
	}

	public int getWorstIndex(double[][] x) {
		int worst = 0;
		for (int i = 0; i < x.length; i++) {
			if (function.getValue(x[i]) > function.getValue(x[worst])) {
				worst = i;
			}
		}
		return worst;
	}

	public int get2ndWorstIndex(double[][] x, int worstIndex) {
		int nextWorst = 0;
		if (worstIndex == 0)
			nextWorst = 1;
		for (int i = 0; i < x.length; i++) {
			if (i == worstIndex)
				continue;
			if (function.getValue(x[i]) > function.getValue(x[nextWorst])) {
				nextWorst = i;
			}
		}
		return nextWorst;
	}

	public double[] calculateXr(double[] xc, double[] xh) {
		double[] xr = new double[xc.length];
		for (int i = 0; i < xc.length; i++) {
			xr[i] = (1 + reflectionCoefficient) * xc[i] - reflectionCoefficient
					* xh[i];
		}
		return xr;
	}

	public double[] moveTowardsCentroid(double[] x, double[] cent) {
		double[] moved = new double[x.length];
		for (int i = 0; i < x.length; i++) {
			moved[i] = 0.5 * (x[i] + cent[i]);
		}
		return moved;
	}

	public double[] calculateCentroidWithoutWorst(double[][] x, int worstIndex) {
		double[] centroid = new double[x[0].length];
		Arrays.fill(centroid, 0);
		for (int j = 0; j < x.length; j++) {
			if (j == worstIndex)
				continue;
			for (int i = 0; i < x[j].length; i++) {
				centroid[i] += x[j][i];
			}
		}
		for (int i = 0; i < centroid.length; i++) {
			centroid[i] /= (x.length - 1);
		}
		return centroid;
	}

	private double[] calculateCentroid(double[][] x, int t) {
		double[] centroid = new double[x[0].length];
		Arrays.fill(centroid, 0.0);
		for (int j = 0; j < t + 1; j++) {
			for (int i = 0; i < x[j].length; i++) {
				centroid[i] += x[j][i];
			}
		}
		for (int i = 0; i < centroid.length; i++) {
			centroid[i] /= (t + 1);
		}
		return centroid;
	}

	public boolean satisfiesImplicit(double[] x) {
		if ((x[0] - x[1] <= 0) && (x[0] - 2 <= 0)) {
			return true;
		}
		return false;
	}

	public boolean satisfiesExplicit(double[] x) {
		for (int i = 0; i < x.length; i++) {
			if (x[i] > explicitBounds[1] || x[i] < explicitBounds[0]) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected void parseFile(String parameters) {
		String[] tmp = parameters.split(" +|\t+");
		EPSILON = Double.parseDouble(tmp[0]);
		reflectionCoefficient = Double.parseDouble(tmp[1]);

	}

}
