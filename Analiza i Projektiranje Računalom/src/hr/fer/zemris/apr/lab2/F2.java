package hr.fer.zemris.apr.lab2;

public class F2 implements Function {
	private String functionString = "f2(x,y) = (x-4)^2 + 4(y-2)^2";
	@Override
	public double getValue(double[] x) {
		return (x[0] - 4) * (x[0] - 4) + 4 * (x[1] - 2) * (x[1] - 2);
	}

	@Override
	public String toString() {
		return functionString;
	}
}
