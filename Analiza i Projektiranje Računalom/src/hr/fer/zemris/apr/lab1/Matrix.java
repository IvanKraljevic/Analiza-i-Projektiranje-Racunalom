package hr.fer.zemris.apr.lab1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/***
 * Razred {@code Matrix} omogućava jednostavno rukovanje objektima
 * dvodimenzionalne matrice.
 * <p>
 * Razred sadrži metode koje izvode LU i LUP dekompozicije kvadratne matrice
 * koristeći isti memorijski prostor za spremanje rezultantnih matrica L i U.
 * <p>
 * Razred sadrži i metode koje izvode supstitucije unaprijed i unatrag.
 * 
 * @author Ivan Kraljević
 * @version 1.0
 */
public class Matrix {
	public static double EPSILON = 10e-9;
	private int numberOfRows;
	private int numberOfColumns;
	private double[][] elements;

	/***
	 * Konstruktor koji zauzima memoriju za matricu čija je veličina zadana u
	 * parametrima.
	 * 
	 * @param rows
	 *            broj redaka matrice
	 * @param columns
	 *            broj stupaca matrice
	 * @throws IllegalArgumentException
	 *             ako su parametri neispravni
	 */
	public Matrix(int rows, int columns) {
		setNumberOfRows(rows);
		setNumberOfColumns(columns);
		setElements(new double[rows][columns]);
	}

	// Static methods

	/**
	 * Metoda stvara kvadratnu jediničnu matricu dimenzija {@code n}.
	 * 
	 * @param n
	 *            dimenzija kvadratne matrice
	 * @return jedinična matrica
	 */
	public static Matrix ones(int n) {
		Matrix m = new Matrix(n, n);
		for (int i = 0; i < n; i++) {
			m.elements[i][i] = 1.0;
		}
		return m;
	}

	/**
	 * Metoda stvara kvadratnu nul matricu dimenzija {@code n}
	 * 
	 * @param n
	 *            dimenzija kvadratne matrice
	 * @return nul matrica
	 */
	public static Matrix zeros(int n) {
		Matrix m = new Matrix(n, n);
		return m;
	}

	/**
	 * Konstruktor koji učitava vrijednosti matrice iz datoteke.
	 * 
	 * @param path
	 *            putanja do datoteke sa matricom
	 */
	public Matrix(String path) {
		importMatrixFromFile(path);
	}

	// Initialization, print and basic math methods

	/***
	 * Metoda koja dinamički mijenja veličinu matrice.
	 * 
	 * @param numberOfRows
	 *            novi broj redaka matrice
	 * @param numberOfColumns
	 *            novi broj stupaca matrice
	 * @throws IllegalArgumentException
	 *             ako su parametri neispravni
	 */
	public void changeMatrixDimensions(int numberOfRows, int numberOfColumns) {
		if (this.numberOfRows < numberOfRows
				|| this.numberOfColumns < numberOfColumns) {
			setElements(new double[numberOfRows][numberOfColumns]);
		}
		setNumberOfRows(numberOfRows);
		setNumberOfColumns(numberOfColumns);
	}

	/***
	 * Metoda čita matricu iz tekstualne datoteke.
	 * <p>
	 * Jedan redak datoteke odgovara retku matrice.
	 * <p>
	 * Broj redaka datoteke odgovara broju redaka matrice.
	 * <p>
	 * Pojedini elementi matrice su odvojeni razmakom ili tabulatorom.
	 * 
	 * @param path
	 *            putanja do datoteke.
	 * @throws MatrixParseError
	 *             ukoliko datoteku nije moguće pročitati.
	 */
	public void importMatrixFromFile(String path) {
		if (path.length() == 0) {
			throw new MatrixParseError();
		}

		BufferedReader reader = null;
		List<String> lines = new ArrayList<String>();

		try {
			reader = new BufferedReader(new FileReader(path));
			String line = "";
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
			numberOfRows = lines.size();
			numberOfColumns = lines.get(0).split(" +|\t+").length;
			elements = new double[numberOfRows][numberOfColumns];

			for (int i = 0; i < lines.size(); i++) {
				String[] s = lines.get(i).split(" +|\t+");
				for (int j = 0; j < s.length; j++) {
					elements[i][j] = Double.parseDouble(s[j]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR: Greška pri čitanju ulazne datoteke.");
		} finally {
			try {
				reader.close();
			} catch (IOException | NullPointerException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Metoda ispisuje matricu u datoteku određenu paramterom {@code path} ili
	 * na standardni izlaz ukoliko {@code path} nije zadan.
	 * 
	 * @param path
	 *            putanja do datoteke.
	 */
	public void printMatrix(PrintStream writer) {
			try {
				for (int i = 0; i < numberOfRows; i++) {
					for (int j = 0; j < numberOfColumns - 1; j++) {
					writer.print(Double.toString(elements[i][j]) + " ");
					}
				writer.print(Double
							.toString(elements[i][numberOfColumns - 1]) + "\n");
				}
			writer.println();
			} catch (Exception e) {
				e.printStackTrace();
			System.out.println("ERROR: Greška pri ispisu matrice.");
			} finally {
				try {
					writer.flush();
					writer.close();
				} catch (NullPointerException e) {
					e.printStackTrace();
			}
		}
	}

	/**
	 * Metoda računa inverz matrice. Prvo se vrši LUP dekompozicija matrice, a
	 * zatim n puta se vrši supstitucija unaprijed i unatrag.
	 * 
	 * @return inverz matrice
	 */
	public Matrix inverse() {
		if (numberOfRows != numberOfColumns) {
			System.out
					.println("ERROR: Ne mogu obaviti inverz jer broj stupaca != broj redaka");
			throw new MatrixMathError();
		}
		Matrix inverse = new Matrix(numberOfColumns, numberOfRows);
		Matrix m = this.clone();
		int[] permutationVector = m.LUP();
		for (int i = 0; i < numberOfColumns; i++) {
			Matrix e = new Matrix(1, numberOfRows);
			e.setElement(0, i, 1.0);
			e.computeWithPermutation(permutationVector);
			Matrix y = m.forwardSupstitution(e);
			Matrix x = m.backwardSupstitution(y);
			inverse.setColumn(i, x.getRow(0));
		}
		return inverse;
	}

	/**
	 * Trenutnoj matrici se dodaje vrijednost matrice {@code m}. a += m
	 * 
	 * @param m
	 * @throws MatrixMathError
	 *             ukoliko dimenzije matrica nisu kompatibilne.
	 */
	public void addTo(Matrix m) {
		// a += m
		if (m.getNumberOfRows() != numberOfRows
				|| m.getNumberOfColumns() != numberOfColumns) {
			System.out.println("ERROR: Dimenzije matrica ne odgovaraju.");
			throw new MatrixMathError();
		}
		for (int i = 0; i < numberOfRows; i++) {
			for (int j = 0; j < numberOfColumns; j++) {
				elements[i][j] += m.getElement(i, j);
			}
		}
	}

	/**
	 * Zbrajamo dvije matrice i njihovu sumu vraćamo kao povratnu vrijednost.
	 * return a + m
	 * 
	 * @param m
	 * @return zbroj matrica
	 * @throws MatrixMathError
	 *             ukoliko dimenzije matrica nisu kompatibilne.
	 */
	public Matrix add(Matrix m) {
		if (m.getNumberOfRows() != numberOfRows
				|| m.getNumberOfColumns() != numberOfColumns) {
			System.out.println("ERROR: Dimenzije matrica ne odgovaraju.");
			throw new MatrixMathError();
		}
		Matrix b = new Matrix(numberOfRows, numberOfColumns);
		for (int i = 0; i < numberOfRows; i++) {
			for (int j = 0; j < numberOfColumns; j++) {
				b.setElement(i, j, elements[i][j] + m.getElement(i, j));
			}
		}
		return b;
	}

	/**
	 * Trenutnoj matrici oduzimamo vrijednost matrice {@code m}. a -= m
	 * 
	 * @param m
	 * @throws MatrixMathError
	 *             ukoliko dimenzije matrica nisu kompatibilne.
	 */
	public void substractFrom(Matrix m) {
		if (m.getNumberOfRows() != numberOfRows
				|| m.getNumberOfColumns() != numberOfColumns) {
			System.out.println("ERROR: Dimenzije matrica ne odgovaraju.");
			throw new MatrixMathError();
		}
		for (int i = 0; i < numberOfRows; i++) {
			for (int j = 0; j < numberOfColumns; j++) {
				elements[i][j] -= m.getElement(i, j);
			}
		}
	}

	/**
	 * Oduzimamo dvije matrice i razliku vraćamo kao povratnu vrijednost. return
	 * a - m
	 * 
	 * @param m
	 * @return razlika dviju matrica
	 * @throws MatrixMathError
	 *             ukoliko dimenzije matrica nisu kompatibilne.
	 */
	public Matrix substract(Matrix m) {
		if (m.getNumberOfRows() != numberOfRows
				|| m.getNumberOfColumns() != numberOfColumns) {
			throw new MatrixMathError();
		}
		Matrix b = new Matrix(numberOfRows, numberOfColumns);
		for (int i = 0; i < numberOfRows; i++) {
			for (int j = 0; j < numberOfColumns; j++) {
				b.setElement(i, j, elements[i][j] - m.getElement(i, j));
			}
		}
		return b;
	}

	/**
	 * Množimo dvije matrice. return a * m
	 * 
	 * @param m
	 * @return umnožak dvije matrice.
	 * @throws MatrixMathError
	 *             ukoliko dimenzije matrica nisu kompatibilne.
	 */
	public Matrix multiply(Matrix m) {
		// return a * m
		if (numberOfColumns != m.getNumberOfRows()) {
			System.out.println("ERROR: Dimenzije matrica ne odgovaraju.");
			throw new MatrixMathError();
		}

		Matrix multiplied = new Matrix(numberOfRows, m.getNumberOfColumns());
		double[][] multipElements = new double[numberOfRows][m
				.getNumberOfColumns()];

		for (int i = 0; i < numberOfRows; i++) {
			for (int j = 0; j < m.getNumberOfColumns(); j++) {
				multipElements[i][j] = 0;
				for (int k = 0; k < numberOfColumns; k++) {
					multipElements[i][j] += elements[i][k] * m.getElement(k, j);
				}
			}
		}

		multiplied.setElements(multipElements);
		return multiplied;
	}

	/**
	 * Transponiramo trenutnu matricu.
	 * 
	 * @return transponirana matrica
	 */
	public Matrix transpose() {
		double[][] transponedElements = new double[numberOfColumns][numberOfRows];
		for (int i = 0; i < numberOfRows; i++) {
			for (int j = 0; j < numberOfColumns; j++) {
				transponedElements[j][i] = elements[i][j];
			}
		}
		Matrix transponed = new Matrix(numberOfColumns, numberOfRows);
		transponed.setElements(transponedElements);
		return transponed;
	}

	/**
	 * Množimo trenutnu matricu skalarom.
	 * 
	 * @param scalar
	 *            skalar s kojim se množi matrica
	 */
	public void multiplyScalar(double scalar) {
		for (int i = 0; i < numberOfRows; i++) {
			for (int j = 0; j < numberOfColumns; j++) {
				elements[i][j] *= scalar;
			}
		}
	}

	/**
	 * Zamjenjujemo elemente matrice s obzirom na permutacijski vektor
	 * 
	 * @param permutationVector
	 *            permutacijski vektor
	 */
	public void computeWithPermutation(int[] permutationVector) {
		double[] tmp = new double[numberOfColumns];
		for (int i = 0; i < permutationVector.length; i++) {
			tmp[i] = elements[0][permutationVector[i]];
		}
		setRow(0, tmp);
	}

	// SUPSTITUCIJE I DEKOMPOZICIJE

	/**
	 * Supstitucija unaprijed.
	 * 
	 * @param b
	 *            slobodni vektor desne strane jednadžbe
	 * @return vektor y rješenje sustava L*y=b
	 */
	public Matrix forwardSupstitution(Matrix b) {
		Matrix m = this.clone();

		double[] bRow = b.getRow(0).clone();
		for (int i = 0; i < numberOfColumns - 1; i++) {// stupac
			for (int j = i + 1; j < numberOfColumns; j++) {// redak
				bRow[j] -= m.getElement(j, i) * bRow[i];
			}
		}
		m.setNumberOfRows(1);
		m.setElements(new double[1][m.getNumberOfColumns()]);
		m.setRow(0, bRow);
		return m;
	}

	/**
	 * Supstitucija unatrag.
	 * 
	 * @param y
	 *            vektor y
	 * @return rješenje sustava linearnih jednadžbi
	 */
	public Matrix backwardSupstitution(Matrix y) {
		Matrix m = this.clone();

		double[] bRow = y.getRow(0);
		for (int i = numberOfColumns - 1; i >= 0; i--) {
			if (Math.abs(this.getElement(i, i)) < EPSILON) {
				System.out.println("ERROR: Stožerni element je ~0.");
				throw new MatrixMathError();
			}
			bRow[i] /= m.getElement(i, i);
			for (int j = 0; j < i; j++) {
				bRow[j] -= m.getElement(j, i) * bRow[i];
			}
		}
		m.setNumberOfRows(1);
		m.setElements(new double[1][m.getNumberOfColumns()]);
		m.setRow(0, bRow);
		return m;
	}

	/**
	 * LU dekompozicija trenutne matrice.
	 * 
	 * @throws MatrixMathError
	 *             ukoliko je stožerni element ~0.
	 */
	public void LU() {
		Matrix m = this.clone();

		for (int i = 0; i < numberOfColumns - 1; i++) {
			for (int j = i + 1; j < numberOfRows; j++) {
				if (Math.abs(elements[i][i]) <= EPSILON) {
					elements = m.getElements();
					System.out.println("ERROR: Stožerni element je ~0.");
					throw new MatrixMathError();
				}
				elements[j][i] /= elements[i][i];
				for (int k = i + 1; k < numberOfColumns; k++) {
					elements[j][k] -= elements[j][i] * elements[i][k];
				}
			}
		}
		if (Math.abs(elements[numberOfRows - 1][numberOfColumns - 1]) <= EPSILON) {
			// u slučaju da je posljednji element ~0
			elements = m.getElements(); // vraćamo matricu u početno stanje
			System.out.println("ERROR: Stožerni element je ~0.");
			throw new MatrixMathError();
		}
	}

	/**
	 * LUP dekompozicija trenutne matrice.
	 * 
	 * @return permutacijski vektor
	 * @throws MatrixMathError
	 *             ukoliko je stožerni element ~0.
	 */
	public int[] LUP() {
		Matrix m = this.clone();

		int[] p = new int[numberOfColumns];
		for (int i = 0; i < numberOfRows; i++) {
			p[i] = i;
		}

		for (int i = 0; i < numberOfColumns - 1; i++) {
			int pivot = i;
			for (int j = i + 1; j < numberOfRows; j++) {
				if (Math.abs(elements[j][i]) > Math
						.abs(elements[pivot][i])) {
					pivot = j;
				}
			}
			if (Math.abs(elements[pivot][i]) < EPSILON) {
				System.out.println(this.toString());
				elements = m.getElements(); // vraćamo matricu u početno stanje
				System.out.println("ERROR: Stožerni element je ~0."
						+ elements[pivot][i]);
				throw new MatrixMathError();
			}
			switchRows(i, pivot);

			int tmp = p[i]; // zamjena permutacijskog vektora
			p[i] = p[pivot];
			p[pivot] = tmp;

			for (int j = i + 1; j < numberOfRows; j++) {
				elements[j][i] /= elements[i][i];

				for (int k = i + 1; k < numberOfColumns; k++) {
					elements[j][k] -= elements[j][i] * elements[i][k];
				}
			}
		}
		if (Math.abs(elements[numberOfRows - 1][numberOfColumns - 1]) <= EPSILON) {
			// u slučaju da je posljednji element ~0
			elements = m.getElements(); // vraćamo matricu u početno stanje
			System.out.println("ERROR: Stožerni element je ~0.");
			throw new MatrixMathError();
		}
		return p;
	}

	// Getters, setters, utility methods

	public int getNumberOfRows() {
		return numberOfRows;
	}

	public void setNumberOfRows(int numberOfRows) {
		if (numberOfRows <= 0) {
			throw new IllegalArgumentException();
		}
		this.numberOfRows = numberOfRows;
	}

	public int getNumberOfColumns() {
		return numberOfColumns;
	}

	public void setNumberOfColumns(int numberOfColumns) {
		if (numberOfColumns <= 0) {
			throw new IllegalArgumentException();
		}
		this.numberOfColumns = numberOfColumns;
	}

	public double[][] getElements() {
		return elements;
	}

	public void setElements(double[][] elements) {
		setNumberOfRows(elements.length);
		setNumberOfColumns(elements[0].length);
		this.elements = elements;
	}

	public void setElement(int row, int column, double d) {
		elements[row][column] = d;
	}

	public double getElement(int row, int column) {
		return elements[row][column];
	}

	public void setRow(int row, double[] value) {
		if (row < 0 || row > numberOfRows || value == null) {
			throw new IllegalArgumentException();
		}
		elements[row] = value;
	}

	public void setColumn(int column, double[] value) {
		if (column < 0 || column > numberOfColumns || value == null) {
			throw new IllegalArgumentException();
		}
		int i = 0;
		for (i = 0; i < value.length; i++) {
			elements[i][column] = value[i];
		}
		// ako nam je ulazni stupac "kraći", ostale retke popunjavamo sa 0
		for (; i < numberOfColumns; i++) {
			elements[i][column] = 0.0;
		}
	}

	public double[] getRow(int row) {
		if (row < 0 || row > numberOfRows) {
			throw new IllegalArgumentException();
		}
		return elements[row];
	}

	public void switchRows(int i, int j) {
		if (i < 0 || j < 0 || j >= numberOfRows || i >= numberOfRows) {
			throw new IllegalArgumentException();
		}
		double[] tmp = new double[numberOfColumns];
		tmp = elements[i];
		elements[i] = elements[j];
		elements[j] = tmp;
	}

	@Override
	public String toString() {
		return "Matrix [rows=" + numberOfRows + ", columns=" + numberOfColumns
				+ ", elements=" + Arrays.deepToString(elements) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + numberOfColumns;
		result = prime * result + Arrays.hashCode(elements);
		result = prime * result + numberOfRows;
		return result;
	}

	/**
	 * Usporedba jednakosti dvije matrice. Pošto su vrijednosti realni brojevi,
	 * pri računanju jedankosti uzima se u obzir apsolutna greška.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Matrix other = (Matrix) obj;
		if (numberOfColumns != other.numberOfColumns)
			return false;
		if (numberOfRows != other.numberOfRows)
			return false;

		int numberOfEquals = 0;
		for (int i = 0; i < numberOfRows; i++) {
			for (int j = 0; j < numberOfColumns; j++) {
				if (Math.abs(elements[i][j] - other.getElement(i, j)) < EPSILON) {
					numberOfEquals++;
				}
			}
		}
		if (numberOfEquals == numberOfColumns * numberOfRows) {
			return true;
		}
		return false;
	}

	/**
	 * "Copy konstruktor". Metoda vraća novi objekt tipa {@code Matrix} koji je
	 * identičan trenutnom objektu.
	 */
	public Matrix clone() {
		Matrix m = new Matrix(numberOfRows, numberOfColumns);
		for (int i = 0; i < numberOfRows; i++) {
			for (int j = 0; j < numberOfColumns; j++) {
				m.setElement(i, j, elements[i][j]);
			}
		}
		return m;
	}
}