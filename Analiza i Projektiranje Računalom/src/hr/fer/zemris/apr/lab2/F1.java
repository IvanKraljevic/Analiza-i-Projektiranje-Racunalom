package hr.fer.zemris.apr.lab2;

public class F1 implements Function {
	String functionString = "f1(x,y) = 10*(x^2-y)^2+(1-x)^2";
	@Override
	public double getValue(double[] x) {

		return (10 * (x[0] * x[0] - x[1]) * (x[0] * x[0] - x[1]))
				+ ((1 - x[0]) * (1 - x[0]));
	}

	@Override
	public String toString() {
		return functionString;
	}

}
