package hr.fer.zemris.apr.utilities;


public class F7 implements Function {
	private String functionString = "(sum(x_i^2))^2 * (1.0 + sin^2(50 * (sum(x_i^2))^0.1))";

	@Override
	public double getValue(double[] x) {
		double sumaKvadrata = 0.0;
		for (double d : x) {
			sumaKvadrata += d * d;
		}
		double value = 1.0 + (Math.sin(50 * Math.pow(sumaKvadrata, 0.1)) * Math
				.sin(50 * Math.pow(sumaKvadrata, 0.1)));
		return Math.pow(sumaKvadrata, 0.25) * value;
	}

	@Override
	public String toString() {
		return functionString;
	}
}
