package hr.fer.zemris.apr.lab3;

import hr.fer.zemris.apr.lab2.Function;

public class F7 implements Function {

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

}
