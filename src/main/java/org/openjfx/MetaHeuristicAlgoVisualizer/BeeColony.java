package org.openjfx.MetaHeuristicAlgoVisualizer;

import java.util.ArrayList;
import java.util.Random;

public class BeeColony implements IMetaHeuristicAlgorithm{
	private static final BeeColony instance = new BeeColony();
	
	public static int iterations_left = 1000;
	private static final int N_FOOD_SOURCES = 50;
	private static final int LIMIT = 10;

	private Random generator = new Random();
	
	private int nCities;
	private CityData data;
	
	FactorialArray fa;
	
	private ArrayList<Integer> permutation = new ArrayList<>();
	
	private ArrayList<Tour> foodSources = new ArrayList<>();
	private double[] costValues = new double[N_FOOD_SOURCES];
	private double[] fitnessValues = new double[N_FOOD_SOURCES];
	private double[] trial = new double[N_FOOD_SOURCES];
	
	private double currentSolution = Double.MAX_VALUE;
	private Tour currentTour;
	
	private BeeColony() {
	}
	
	public static BeeColony getInstance() {
		return instance;
	}
	
	public Tour getCurrentTour() {
		return currentTour;
	}
	
	public double getCurrentSolution() {
		return currentSolution;
	}
	
	public void readData(CityData data) {
		this.data = data;
		variablesInit();
	}
	
	public void iterate() {
		// Call this function to execute one iteration of the algorithm
		if (iterations_left > 0) {
			employedBeePhase();
			onlookerBeePhase();
			scoutBeePhase();

			// Update solution
			updateSolution();
			iterations_left--;
		}
		else {
			System.out.println("Algorithm done running");
		}
	}

	@Override
	public double solve(CityData data) {

		for (int it = 0; it < iterations_left; it++) {
			employedBeePhase();
			onlookerBeePhase();
			scoutBeePhase();
		}
		
		return currentSolution;
	}
	
	private void variablesInit() {
		nCities = data.size();
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
		int permNo;
		for (int i = 0; i < N_FOOD_SOURCES; i++) {
			do {
				permNo = generator.nextInt(fa.getNFactorial());
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
				int permNo;
				do {
					permNo = generator.nextInt(fa.getNFactorial());
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
		int newPerm = fa.generateNewPerm(permutation.get(i), permutation.get(partner));
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
				currentTour = foodSources.get(i);
			}
		}
	}
	
	public String getAlgName() {
		return "Artificial Bee Colony Algorithm ";
	}
}
