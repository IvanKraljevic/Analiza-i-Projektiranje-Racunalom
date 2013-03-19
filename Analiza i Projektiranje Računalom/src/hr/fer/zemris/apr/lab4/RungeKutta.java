package hr.fer.zemris.apr.lab4;

import hr.fer.zemris.apr.lab1.Matrix;

import java.util.ArrayList;

public class RungeKutta extends NumericalIntegration {

	public RungeKutta(String pathA, String pathB, String pathX0) {
		this.a = new Matrix(pathA);
		this.b = new Matrix(pathB);
		this.x0 = new Matrix(pathX0);
		x = new ArrayList<Matrix>();
	}

	public RungeKutta(Matrix a, Matrix b, Matrix x0) {
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

		int i;
		for (i = 1; interval > 1e-7; i++, interval -= T) {
			Matrix xi = x.get(x.size() - 1);

			Matrix m1 = a.multiply(xi).add(b);

			Matrix m12 = m1.clone();
			m12.multiplyScalar(T / 2.0);
			Matrix m2 = a.multiply(xi.add(m12)).add(b);

			Matrix m22 = m2.clone();
			m22.multiplyScalar(T / 2.0);
			Matrix m3 = a.multiply(xi.add(m22)).add(b);

			Matrix m32 = m3.clone();
			m32.multiplyScalar(T);
			Matrix m4 = a.multiply(xi.add(m32)).add(b);

			m22 = m2.clone();
			m22.multiplyScalar(2.0);
			m32 = m3.clone();
			m32.multiplyScalar(2.0);
			Matrix s = m1.add(m22).add(m32).add(m4);
			s.multiplyScalar(T / 6.0);

			s.add(xi);

			Matrix xii = xi.add(s);
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
