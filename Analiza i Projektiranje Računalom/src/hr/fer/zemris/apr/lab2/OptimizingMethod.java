package hr.fer.zemris.apr.lab2;


public abstract class OptimizingMethod {

	public abstract double[] optimize(double[] x0);

	protected abstract void parseFile(String parameters);
}
