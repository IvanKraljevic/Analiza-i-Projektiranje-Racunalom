package hr.fer.zemris.apr.lab3;

import java.util.Arrays;
import java.util.Random;

public class Chromosome implements Comparable<Chromosome> {
	// Bitovi kromosoma
	byte[][] bits;
	// Vrijednost funkcije u promatranoj točci
	private double fitness;
	// Vrijednost realnih varijabli
	double[] variables;

	public Chromosome(ChromosomeDecoder decoder) {
		this.bits = new byte[decoder.getVariableNumber()][decoder.getBits()];
		this.fitness = 0.0;
		this.variables = new double[decoder.getVariableNumber()];
	}

	public Chromosome(ChromosomeDecoder decoder, Random rand) {
		this.bits = new byte[decoder.getVariableNumber()][decoder.getBits()];
		this.fitness = 0.0;
		this.variables = new double[decoder.getVariableNumber()];
		for (int j = 0; j < decoder.getVariableNumber(); j++) {
			for (int i = 0; i < decoder.getBits(); i++) {
				bits[j][i] = rand.nextBoolean() ? (byte) 1 : (byte) 0;
			}
		}
	}

	public Chromosome(int variableNumber, int bitsPerVariable) {
		this.bits = new byte[variableNumber][bitsPerVariable];
		this.fitness = 0.0;
		this.variables = new double[variableNumber];
	}

	public Chromosome clone() {
		Chromosome c = new Chromosome(this.variables.length,
				this.bits[0].length);
		c.setFitness(this.getFitness());
		c.bits = this.bits.clone();
		c.variables = this.variables.clone();

		return c;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public double[] getVariables() {
		return variables;
	}

	public void setVariables(double[] variables) {
		this.variables = variables;
	}

	@Override
	public int compareTo(Chromosome o) {
		if (this.fitness < o.fitness) {
			return -1;
		}
		if (this.fitness > o.fitness) {
			return 1;
		}
		return 0;
	}


	@Override
	public String toString() {
		return "Chromosome [fitness=" + fitness + ", variables="
				+ Arrays.toString(variables) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(bits);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Chromosome other = (Chromosome) obj;
		if (!Arrays.deepEquals(bits, other.bits))
			return false;
		return true;
	}

}
