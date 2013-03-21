package hr.fer.zemris.apr.lab1;

/**
 * Iznimka se baca u slučajevima kada neku matematičku operaciju nije moguće
 * izvršiti nad matricom (npr. pokušaj LUP dekompozicije nad singularnom
 * matricom).
 * 
 * @author Ivan
 * 
 */
public class MatrixMathError extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1429004739299585846L;

}
