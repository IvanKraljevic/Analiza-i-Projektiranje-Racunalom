package hr.fer.zemris.apr.lab2;

import hr.fer.zemris.apr.utilities.F3;
import hr.fer.zemris.apr.utilities.Function;

import java.util.Arrays;


public class Test {
	public static void main(String[] args) {
		Function f = new F3("99 99 5 44");
		double[] x0 = new double[] { 0, 0, 5, 11 };
		HookeJeves h = new HookeJeves(f);
		h.sleep = 0;
		Box b = new Box(10e-9, 1.3, f);

		System.out.println(Arrays.toString(b.optimize(x0)));
	}
}
