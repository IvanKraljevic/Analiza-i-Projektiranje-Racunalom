package hr.fer.zemris.apr.lab4;

import hr.fer.zemris.apr.lab1.Matrix;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.jfree.ui.RefineryUtilities;

public abstract class NumericalIntegration {
	protected Matrix a;
	protected Matrix b;
	protected Matrix x0;
	protected double T;
	protected List<Matrix> x;
	protected double I;
	protected double[] axis;

	public abstract void compute(double integrationStep, double interval,
			int printStep);

	public void plot(String title) {
		Chart c = new Chart(x, axis, title);
		c.pack();
		c.setVisible(true);
		RefineryUtilities.centerFrameOnScreen(c);

	}

	public void matlabScriptOutput(String scriptPath) {
		try {
			BufferedWriter r = new BufferedWriter(new FileWriter(scriptPath));
			r.write("x=[");
			for (Matrix m : x) {
				double[] t = m.transpose().getRow(0);
				for (int i = 0; i < t.length - 1; i++) {
					r.write(t[i] + " ");
				}
				r.write(t[t.length - 1] + "\n");
			}
			r.write("];\nt=[");

			for (int i = 0; i < axis.length; i++) {
				r.write(axis[i] + " ");
			}
			r.write("];\n");

			r.write("\nsubplot(3,1,2), plot (t,x);\nxlabel('t');\nylabel('x_i');"
					+ "axis([0 " + I + " min(min(x)) max(max(x + 1))])");
			r.flush();
			r.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void matlabMatrixOutput(String pathX, String pathT) {
		try {
			BufferedWriter r = new BufferedWriter(new FileWriter(pathX));
			for (Matrix m : x) {
				double[] t = m.transpose().getRow(0);
				for (int i = 0; i < t.length - 1; i++) {
					r.write(t[i] + " ");
				}
				r.write(t[t.length - 1] + "\n");
			}
			r.flush();
			r.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedWriter r = new BufferedWriter(new FileWriter(pathT));
			for (double d = 0.0; d < I; d += T) {
				r.write(d + " ");
			}
			r.flush();
			r.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void fillAxis() {
		int size = 1;
		for (double d = 0.0; I >= d; d += T, size++) {
		}
		axis = new double[size];

		double v = 0.0;
		for (int i = 0; i < axis.length; i++, v += T) {
			axis[i] = v;
		}
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

	public List<Matrix> getX() {
		return x;
	}

	public void setX(List<Matrix> x) {
		this.x = x;
	}

	public double getI() {
		return I;
	}

	public void setI(double i) {
		I = i;
	}
}
