package hr.fer.zemris.apr.lab1;

import java.io.PrintStream;

public class MatrixSolver {
	public static void solve(String matrixPath, String vectorPath,
			PrintStream output) {
		Matrix a = new Matrix(1, 1);
		a.importMatrixFromFile(matrixPath);
		Matrix b = new Matrix(1, 1);
		b.importMatrixFromFile(vectorPath);
		
		output.println("Matrica A prije dekompozicije:");
		a.printMatrix(output);
		output.println("Slobodni vektor b:");
		b.printMatrix(output);
		
		try {
			a.LU();
		} catch (MatrixMathError e) {
			output.println("Matricu nije moguće riješiti LU dekompozicijom.\n");
			try {
				int[] permutationVector = a.LUP();
				b.computeWithPermutation(permutationVector);
			} catch (MatrixMathError ex) {
				output.println("Matricu nije moguće riješiti ni LUP dekompozicijom.\nMožda je singularna.");
				return;
			}
		}
		
		output.println("Matrica A nakon dekompozicije:");
		a.printMatrix(output);
		
		Matrix y = a.forwardSupstitution(b);
		
		output.println("Matrica y nakon supstitucije unaprijed:");
		y.printMatrix(output);

		Matrix x = a.backwardSupstitution(y);

		output.println("Matrica x, tj. rješenje sustava nakon supstitucije unatrag:");
		x.printMatrix(output);
	}
}
