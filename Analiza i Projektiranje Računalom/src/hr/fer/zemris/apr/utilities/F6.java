package hr.fer.zemris.apr.utilities;


public class F6 implements Function {
	@Override
	public double getValue(double[] x) {
		double sumaKvadrata = 0.0;
		for (double d : x) {
			sumaKvadrata += d * d;
		}
		double brojnik = Math.sin(Math.sqrt(sumaKvadrata)) *  Math.sin(Math.sqrt(sumaKvadrata)) - 0.5;
		double nazivnik = (1 + 0.001 * sumaKvadrata)
				* (1 + 0.001 * sumaKvadrata);
		return 0.5 + (brojnik / nazivnik);
	}
}
