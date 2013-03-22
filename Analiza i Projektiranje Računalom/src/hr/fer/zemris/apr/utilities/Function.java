package hr.fer.zemris.apr.utilities;

/**
 * Sučelje {@code Function} predstavlja proizvoljnu funkciju čiji je argument
 * vektor.
 * 
 * @author Ivan
 * 
 */
public interface Function {
	/**
	 * Metoda kao ulaz prima vektor ulaznih vrijednosti, a izlaz je vrijednost u
	 * danoj točki.
	 * 
	 * @param x
	 *            argument funkcije u obliku vektora
	 * @return vrijednost funkcije za dani argument
	 */
	public double getValue(double[] x);
}
