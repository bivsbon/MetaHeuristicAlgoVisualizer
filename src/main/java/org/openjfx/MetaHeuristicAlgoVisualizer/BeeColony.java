package org.openjfx.MetaHeuristicAlgoVisualizer;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class BeeColony extends MetaHeuristicAlgorithm{
	private static final BeeColony instance = new BeeColony();
	
	private static final int N_ITERATIONS = 10000;
	private static final int N_FOOD_SOURCES = 150;
	private static final int LIMIT = 30;
	private static int iterations_left;
	
	FactorialArray fa;
	
	private ArrayList<Long> permutation = new ArrayList<>();
	
	private ArrayList<Tour> foodSources = new ArrayList<>();
	private double[] costValues = new double[N_FOOD_SOURCES];
	private double[] fitnessValues = new double[N_FOOD_SOURCES];
	private double[] trial = new double[N_FOOD_SOURCES];
	
	private BeeColony() {}
	
	public static BeeColony getInstance() {
		return instance;
	}
	
	public void readData(CityData data) {
		this.data = data;
		nCities = data.size();
		variablesInit();
	}
	
	public boolean iterate() {
		// Call this function to execute one iteration of the algorithm
		if (iterations_left > 0) {
			employedBeePhase();
			onlookerBeePhase();
			scoutBeePhase();

			// Update solution
			updateSolution();
			iterations_left--;
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public double solve(CityData data) {
		readData(data);
		for (int it = 0; it < iterations_left; it++) {
			employedBeePhase();
			onlookerBeePhase();
			scoutBeePhase();
		}
		updateSolution();
		
		return currentSolution;
	}
	
	private void variablesInit() {
		iterations_left = N_ITERATIONS;
		currentSolution = Double.MAX_VALUE;
		bestTour = new Tour(nCities);
		
		factorialInit();
		foodSourcesInit();
		costValuesInit();
		fitnessInit();
	}
	
	private void factorialInit() {
		fa = new FactorialArray(nCities);
	}

	private double fitness(double x) {
		if (x >= 0) return 1/(1+x);
		else return 1 + Math.abs(x);
	}
	
	private void foodSourcesInit() {
		// Generate random permutation
		long permNo;
		for (int i = 0; i < N_FOOD_SOURCES; i++) {
			do {
				permNo = ThreadLocalRandom.current().nextLong(fa.getNFactorial());
			} while (permutation.contains(permNo));
			
			permutation.add(permNo);
			foodSources.add(new Tour(fa.generateIthPermutaion(permNo)));
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
			if (trial[i] > LIMIT) {
				long permNo;
				do {
					permNo = ThreadLocalRandom.current().nextLong(fa.getNFactorial());
				} while (permutation.contains(permNo));
				foodSources.set(i, new Tour(fa.generateIthPermutaion(permNo)));
			}
		}
	}
	
	private void costValuesInit() {
		for (int i = 0; i < N_FOOD_SOURCES; i++) {
			costValues[i] = foodSources.get(i).getCost(data);
		}
	}
	
	private void fitnessInit() {
		for (int i = 0; i < N_FOOD_SOURCES; i++) {
			fitnessValues[i] = fitness(costValues[i]);
		}
	}
	
	private boolean updateFoodSource(int i) {
		int partner;
		do {
			partner = generator.nextInt(N_FOOD_SOURCES);
		} while (i == partner);
			
		// Create new food source with the random partner
		long newPerm = fa.generateNewPerm(permutation.get(i), permutation.get(partner));
		Tour newFoodSource = new Tour(fa.generateIthPermutaion(newPerm));
		
		// Greedy selection based on fitness function
		double newCost = newFoodSource.getCost(data);
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
	
	private void updateSolution() {
		for (int i = 0; i < N_FOOD_SOURCES; i++) {
			if (costValues[i] < currentSolution) {
				currentSolution = costValues[i];
				bestTour = foodSources.get(i);
			}
		}
	}
	
	public String toString() {
		return "Artificial Bee Colony";
	}
	
	public String getVariableString() {
		// TODO return variable for user
		return "";
	}
}
