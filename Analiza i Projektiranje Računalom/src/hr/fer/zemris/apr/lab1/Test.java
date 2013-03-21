package hr.fer.zemris.apr.lab1;


/**
 * Samo za testiranje...
 * 
 * @author Ivan
 * 
 */
public class Test {
	public static void main(String[] args) {
		String path = "matrix.txt";
		String slobodniVektor = "b.txt";
		/*
		 * PrintStream printer = null; try { printer = new
		 * PrintStream("asd.txt", "UTF-8"); } catch (Exception e1) {
		 * e1.printStackTrace(); }
		 */
		MatrixSolver.solve(path, slobodniVektor, System.out);

		Matrix a = new Matrix(path);
		a.inverse().printMatrix(System.out);

	}
}
