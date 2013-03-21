package hr.fer.zemris.apr.lab3;

import hr.fer.zemris.apr.utilities.Function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GeneticAlgorithm {
	private Function function;
	private Random rand = new Random();
	private ChromosomeDecoder decoder;
	static int VEL_POP;
	static double VJER_MUT = 0.1;
	private Chromosome[] population;

	public GeneticAlgorithm(Function function, ChromosomeDecoder decoder) {
		this.function = function;
		this.decoder = decoder;
	}
	
	public Chromosome run(int numberOfGenerations, int populationSize) {
		VEL_POP = populationSize;
		population = createPopulation();
		evaluatePopulation();

		// Chromosome best = population[0];

		for (int generation = 0; generation < numberOfGenerations; generation++) {
			int[] randomPick = pickChromosomes(3);
			int worstIndex = getWorstIndex(randomPick);
			Chromosome[] parents = pickParents(randomPick);
			Chromosome child = crossChild(parents[0], parents[1]);
			mutate(child);

			population[worstIndex] = child;
			evaluateChromosome(child);
			/*
			 * evaluatePopulation(); if (!best.equals(population[0])) { best =
			 * population[0]; System.out.println(best); }
			 */
		}
		Arrays.sort(population);
		System.out.println("najbolja: " + population[0].toString());
		return population[0];
	}

	public Chromosome run2(int numberOfGenerations, int populationSize) {
		VEL_POP = populationSize;
		population = createPopulation();
		Chromosome[] newPopulation = createPopulation();

		evaluatePopulation();
		
		Chromosome best = null;
		for (int generation = 0; generation < numberOfGenerations; generation++) {
			Arrays.sort(population);
			newPopulation[0] = population[0].clone();
			for (int i = 1; i < VEL_POP; i++) {
				Chromosome parent1 = pickParent();
				Chromosome parent2 = pickParent();
				Chromosome c = crossChild(parent1, parent2);
				mutate(c);
				newPopulation[i] = c;
			}

			Chromosome[] tmp = population;
			population = newPopulation;
			newPopulation = tmp;

			evaluatePopulation();
			// System.out.println(population[0].toString());
			if (generation % 5000 == 0)
				System.out.println(generation);
		}
		evaluatePopulation();
		System.out.println(population[0]);
		return best;
	}

	public Chromosome pickParent() {
		double fitnesSum = 0;
		double maxFitnes = 0;
		for (int i = 0; i < population.length; i++) {
			fitnesSum += population[i].getFitness();
			if (i == 0 || maxFitnes < population[i].getFitness()) {
				maxFitnes = population[i].getFitness();
			}
		}

		fitnesSum = population.length * maxFitnes - fitnesSum; // relativna
																// dobrota
		double random = rand.nextDouble() * fitnesSum; // vrtimo kolo sreće :D
		double acumulated = 0;
		for (int i = 0; i < population.length; i++) {
			acumulated += maxFitnes - population[i].getFitness();
			if (random < acumulated) {
				return population[i];
			}
		}
		return population[population.length - 1];
	}

	public Chromosome[] createPopulation() {
		Chromosome[] population = new Chromosome[VEL_POP];
		for (int i = 0; i < population.length; i++) {
			population[i] = new Chromosome(decoder, rand);
		}
		return population;
	}

	public void evaluatePopulation() {
		for (int i = 0; i < population.length; i++) {
			evaluateChromosome(population[i]);
		}
	}

	public void evaluateChromosome(Chromosome c) {
		decoder.decodeChromosom(c);
		c.setFitness(function.getValue(c.getVariables()));
	}

	public Chromosome crossChild(Chromosome parent1, Chromosome parent2) {
		// uniformno križanje!
		Chromosome r = new Chromosome(decoder, rand);
		Chromosome c = new Chromosome(decoder);
		for (int i = 0; i < r.bits.length; i++) {
			for (int j = 0; j < r.bits[i].length; j++) {
				c.bits[i][j] = (byte) ((byte) (parent1.bits[i][j] & parent2.bits[i][j]) | (r.bits[i][j] & (parent1.bits[i][j] ^ parent2.bits[i][j])));
			}
		}
		return c;
	}

	public void mutate(Chromosome c) {
		for (int i = 0; i < c.bits.length; i++) {
			for (int j = 0; j < c.bits[i].length; j++) {
				if (rand.nextDouble() <= VJER_MUT) {
					c.bits[i][j] = (byte) (1 - c.bits[i][j]);
				}
			}
		}
	}

	public int[] pickChromosomes(int n) {
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		while (indexes.size() < n) {
			Integer value = rand.nextInt(VEL_POP);
			if (!indexes.contains(value)) {
				indexes.add(value);
			}
		}
		int[] ind = new int[n];
		for (int i = 0; i < ind.length; i++) {
			ind[i] = indexes.get(i);
		}
		return ind;
	}

	public Chromosome[] pickParents(int[] indexes) {
		// 3 turnirski odabir
		int worstIndex = getWorstIndex(indexes);

		Chromosome[] parents = new Chromosome[indexes.length - 1];
		for (int i = 0, j = 0; i < indexes.length; i++) {
			if (worstIndex == indexes[i])
				continue;
			parents[j] = population[indexes[i]];
			j++;
		}
		return parents;
	}

	public int getWorstIndex(int[] indexes) {
		int index = indexes[0];
		for (int i = 0; i < indexes.length; i++) {
			if (population[indexes[i]].getFitness() > population[index]
					.getFitness()) {
				index = indexes[i];
			}
		}
		return index;
	}
}
