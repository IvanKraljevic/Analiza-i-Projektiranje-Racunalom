package hr.fer.zemris.apr.utilities;

public class F3 implements Function {
	public double[] parameters;
	private String functionString = "f4 = ";

	public F3(double[] parameters) {
		this.parameters = parameters;
	}

	public F3(String parameters) {
		String[] tmp = parameters.split(" +|\t+");
		this.parameters = new double[tmp.length];
		for (int i = 0; i < this.parameters.length; i++) {
			this.parameters[i] = Double.parseDouble(tmp[i]);
		}
	}

	@Override
	public double getValue(double[] x) {
		if (x.length != parameters.length) {
			System.out.println("ne valja");
		}
		double sum = 0.0;
		for (int i = 0; i < x.length; i++) {
			sum += (x[i] - parameters[i]) * (x[i] - parameters[i]);
		}
		return sum;
	}

	@Override
	public String toString() {
		String tmp = functionString + "";
		for (double p : parameters) {
			tmp += "(x-(" + p + "))^2 + ";
		}
		return tmp;
	}
}
