package algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import datamodel.CityData;
import datamodel.Tour;
import helper.FactorialArray;

public class BeeColony extends MetaHeuristicAlgorithm{
	private static final BeeColony instance = new BeeColony();
	private static final int N_ITERATIONS = 1000;
	private static final int N_FOOD_SOURCES = 3;
	private static final int LIMIT = 1;
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
		bestTour = new Tour(nCities);
		variablesInit();
	}
	
	public boolean iterate() {
		// Call this function to execute one iteration of the algorithm
		if (iterations_left > 0) {
			logScreen.addLine("Running employed bee phase...");
			employedBeePhase();
			logScreen.addLine("");
			logScreen.addLine("Running onlooker bee phase...");
			onlookerBeePhase();
			logScreen.addLine("");
			logScreen.addLine("Running scout bee phase...");
			scoutBeePhase();
			logScreen.addLine("");

			// Update solution
			updateSolution();
			iterations_left--;
			logScreen.addLine("Complete 1 iteration");
			return true;
		}
		else {
			logScreen.addLine("Complete 1 iteration");
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
		foodSources = new ArrayList<Tour>();
		permutation = new ArrayList<Long>();
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
		logScreen.addLine("Generating probabilities that food sources will be update this iteration");
		double[] prob = new double[N_FOOD_SOURCES];
		double sum = 0.0;
		for (double f : fitnessValues) {
			sum += f;	
		}
		for (int i = 0; i < N_FOOD_SOURCES; i++) {
			prob[i] = fitnessValues[i] / sum;
		}
		
		int foodIndex = -1;
		double r;
		for (int i = 0; i < N_FOOD_SOURCES; i++) {
			do {
				r = generator.nextDouble();
				foodIndex = (foodIndex+1) % N_FOOD_SOURCES;
				logScreen.addLine("Food source " + Integer.toString(foodIndex+1) + " probability of updating: " + String.format("%.4f", prob[i]));
			} while (r < prob[foodIndex]);

			updateFoodSource(foodIndex);
		}
	}
	
	private void scoutBeePhase() {
		for (int i = 0; i < N_FOOD_SOURCES; i++) {
			if (trial[i] > LIMIT) {
				logScreen.addLine("Food source " + Integer.toString(i+1) + " has trial counter greater than " + Integer.toString(LIMIT));
				logScreen.addLine("Replace food source " + Integer.toString(i+1) + " with a random food source");
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
		logScreen.addLine("Update food source " + Integer.toString(i+1));
		int partner;
		do {
			partner = generator.nextInt(N_FOOD_SOURCES);
		} while (i == partner);

		logScreen.addLine("Create new food source with random partner: " + Integer.toString(partner+1));
		long newPerm = fa.generateNewPerm(permutation.get(i), permutation.get(partner));
		Tour newFoodSource = new Tour(fa.generateIthPermutaion(newPerm));
		
		double newCost = newFoodSource.getCost(data);
		double newFitness = fitness(newCost);
		if (newFitness > fitnessValues[i]) {;
			foodSources.set(i, newFoodSource);
			costValues[i] = newCost;
			fitnessValues[i] = newFitness;
			trial[i] = 0;
			permutation.set(i, newPerm);
			logScreen.addLine("Accept new food source");
			return true;
		}
		else trial[i]++;
		logScreen.addLine("Reject new food source, increase trial counter for this food source by 1");
		return false;
	}
	
	private void updateSolution() {	
		for (int i = 0; i < N_FOOD_SOURCES; i++) {
			if (costValues[i] < currentSolution) {
				currentSolution = costValues[i];
				bestTour = foodSources.get(i);
				
				logScreen.addLine("Found new solution in food source " + Integer.toString(i+1));
			}
		}
	}
	
	public String toString() {
		return "Artificial Bee Colony";
	}
	
	public String getVariableString() {
		return Double.toString(bestTour.getCost(data));
	}

	@Override
	public List<Tour> getMinorTours() {
		return foodSources;
	}
}
