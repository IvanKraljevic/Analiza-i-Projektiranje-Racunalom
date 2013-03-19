package hr.fer.zemris.apr.lab4;

import hr.fer.zemris.apr.lab1.Matrix;

import java.util.ArrayList;

public class TrapezodialRule extends NumericalIntegration {

	public TrapezodialRule(String pathA, String pathB, String pathX0) {
		this.a = new Matrix(pathA);
		this.b = new Matrix(pathB);
		this.x0 = new Matrix(pathX0);
		x = new ArrayList<Matrix>();
	}

	public TrapezodialRule(Matrix a, Matrix b, Matrix x0) {
		super();
		this.a = a;
		this.b = b;
		this.x0 = x0;
		x = new ArrayList<Matrix>();
	}

	@Override
	public void compute(double integrationStep, double interval, int printStep) {
		T = integrationStep;
		I = interval;
		x.add(x0);

		Matrix U = Matrix.ones(a.getNumberOfRows());
		Matrix a1 = a.clone();
		a1.multiplyScalar(T / 2.0);
		Matrix R = U.substract(a1).inverse();
		Matrix S = R.clone();

		R = R.multiply(U.add(a1));
		S = S.multiply(b);
		S.multiplyScalar(T / 2.0);
		R.printMatrix(System.out);

		int i;
		for (i = 1; interval > 1e-7; i++, interval -= T) {
			Matrix xi = x.get(x.size() - 1);
			Matrix xii = R.multiply(xi);

			x.add(xii);

			if (i % printStep == 0) {
				System.out.println(i + ". Preostali interval: " + interval
						+ " Xk+1: ");
				xii.printMatrix(System.out);
				System.out.println("------------------------");
			}
		}
		fillAxis();
	}

	public Matrix r(double t) {
		return Matrix.zeros(a.getNumberOfRows());
	}

	public Matrix getA() {
		return a;
	}

	public void setA(Matrix a) {
		this.a = a;
	}

	public Matrix getB() {
		return b;
	}

	public void setB(Matrix b) {
		this.b = b;
	}

	public Matrix getX0() {
		return x0;
	}

	public void setX0(Matrix x0) {
		this.x0 = x0;
	}

	public double getT() {
		return T;
	}

	public void setT(double t) {
		T = t;
	}



}
