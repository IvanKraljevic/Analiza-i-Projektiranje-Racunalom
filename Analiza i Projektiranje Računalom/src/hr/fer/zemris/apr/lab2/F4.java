package hr.fer.zemris.apr.lab2;

public class F4 implements Function {
	private String functionString = "f4(x,y) = |(x-y)*(x+y)|+(x^2+y^2)^0.5";

	@Override
	public double getValue(double[] x) {
		double l = Math.abs((x[0] - x[1]) * (x[0] + x[1]));
		double r = Math.sqrt(x[0] * x[0] + x[1] * x[1]);
		return r + l;
	}
	
	@Override
	public String toString() {
		return functionString;
	}
}
