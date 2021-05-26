package org.openjfx.MetaHeuristicAlgoVisualizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class BeeColony implements IMetaHeuristicAlgorithm{
	private static final int N_ITERATIONS = 300;
	private static final int N_FOOD_SOURCES = 25;
	private static final int limit = 10;

	private Random generator = new Random();
	
	private int nCities;
	private CityData data;
	
	private int nFactorial;
	private int[] fact;
	private ArrayList<Integer> permutation = new ArrayList<>();
	
	private ArrayList<Tour> foodSources = new ArrayList<>();
	private double[] costValues = new double[N_FOOD_SOURCES];
	private double[] fitnessValues = new double[N_FOOD_SOURCES];
	private double[] trial = new double[N_FOOD_SOURCES];
	private Tour solutionTour;
	
	public BeeColony() {
	}

	@Override
	public double solve(CityData data) {
		this.data = data;
		double solution = Double.MAX_VALUE;
		variablesInit();

		for (int it = 0; it < N_ITERATIONS; it++) {
			employedBeePhase();
			onlookerBeePhase();
			scoutBeePhase();
			
			// Update solution
			for (int i = 0; i < N_FOOD_SOURCES; i++) {
				if (costValues[i] < solution) {
					solution = costValues[i];
					solutionTour = foodSources.get(i);
				}
			}
		}
		
		System.out.println(solutionTour.toString());
		return solution;
	}
	
	private void variablesInit() {
		nCities = data.size();
		factorialInit();
		foodSourcesInit();
		costValuesInit();
		fitnessInit();
	}
	
	private double fitness(double x) {
		if (x >= 0) return 1/(1+x);
		else return 1 + Math.abs(x);
	}
	
	private void foodSourcesInit() {
		// Generate random permutation
		int permNo;
		for (int i = 0; i < N_FOOD_SOURCES; i++) {
			do {
				permNo = generator.nextInt(nFactorial);
			} while (permutation.contains(permNo));
			
			permutation.add(permNo);
			foodSources.add(new Tour(generateIthPermutaion(nCities, permNo)));
		}
	}
	
	private void employedBeePhase() {
		for (int i = 0; i < N_FOOD_SOURCES; i++) {
			// Select random partner
			updateFoodSource(i);
		}
	}
	
	private void onlookerBeePhase() {
		double[] prob = new double[N_FOOD_SOURCES];
		double sum = 0.0;
		for (double f : fitnessValues) {
			sum += f;
		}
		for (int i = 0; i < N_FOOD_SOURCES; i++) {
			prob[i] = fitnessValues[i] / sum;
		}
		
		int foodIndex = 0;
		double r;
		for (int i = 0; i < N_FOOD_SOURCES; i++) {
			do {
				r = generator.nextDouble();
				foodIndex = (foodIndex+1) % N_FOOD_SOURCES;
			} while (r < prob[i]);

			updateFoodSource(i);
		}
	}
	
	private void scoutBeePhase() {
		for (int i = 0; i < N_FOOD_SOURCES; i++) {
			if (trial[i] > limit) {
				int permNo;
				do {
					permNo = generator.nextInt(nFactorial);
				} while (permutation.contains(permNo));
				foodSources.set(i, new Tour(generateIthPermutaion(nCities, permNo)));
			}
		}
	}
	
	private void costValuesInit() {
		for (int i = 0; i < N_FOOD_SOURCES; i++) {
			costValues[i] = data.getCostOnTour(foodSources.get(i));
		}
	}
	
	private void fitnessInit() {
		for (int i = 0; i < N_FOOD_SOURCES; i++) {
			fitnessValues[i] = fitness(costValues[i]);
		}
	}
	
	private void factorialInit() {
		int k = 0;
		fact = new int[nCities];
		fact[k] = 1;
		while (++k < nCities) {
			fact[k] = fact[k - 1] * k;	
		}
		nFactorial = fact[k-1];
	}
	
	private boolean updateFoodSource(int i) {
		int partner;
		do {
			partner = generator.nextInt(N_FOOD_SOURCES);
		} while (i == partner);
			
		// Create new food source with the random partner
		int newPerm = generateNewPerm(permutation.get(i), permutation.get(partner));
		Tour newFoodSource = new Tour(generateIthPermutaion(nCities, newPerm));
		
		// Greedy selection based on fitness function
		double newCost = data.getCostOnTour(newFoodSource);
		double newFitness = fitness(newCost);
		if (newFitness > fitnessValues[i]) {;
			foodSources.set(i, newFoodSource);
			costValues[i] = newCost;
			fitnessValues[i] = newFitness;
			trial[i] = 0;
			permutation.set(i, newPerm);
			return true;
		}
		else trial[i]++;
		return false;
	}
	
 	private ArrayList<Integer> generateIthPermutaion(int n, int i) {
		int j, k = 0;
		Integer perm[] = new Integer[n];
		
		// compute factorial code
		for (k = 0; k < n; ++k)
		{
			perm[k] = i / fact[n - 1 - k];
			i = i % fact[n - 1 - k];
		}
		
	   // re-adjust values to obtain the permutation
	   // start from the end and check if preceding values are lower
	   for (k = n - 1; k > 0; --k)
	      for (j = k - 1; j >= 0; --j)
	         if (perm[j] <= perm[k])
	            perm[k]++;


	   ArrayList<Integer> permList = new ArrayList<>(Arrays.asList(perm));
	   return permList;
	}
	
	private int generateNewPerm(int p1, int p2) {
		double phi = generator.nextDouble()*2 - 1;
		int newPerm = (int) (p1 + phi * (p1 - p2));
		newPerm = Math.max(0, Math.min(nFactorial, newPerm));
		return newPerm;
	}
	
	public String getAlgName() {
		return "Artificial Bee Colony Algorithm ";
	}
}
