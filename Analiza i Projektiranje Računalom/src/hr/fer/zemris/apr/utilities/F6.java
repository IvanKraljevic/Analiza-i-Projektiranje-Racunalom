package hr.fer.zemris.apr.utilities;


public class F6 implements Function {
	private String functionString = "f6 = 0.5 + (sin^2(sqrt(sum(x_i^2))) - 0.5) / (1 + 0.001 * sum(x_i^2))^2";

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

	@Override
	public String toString() {
		return functionString;
	}
}
