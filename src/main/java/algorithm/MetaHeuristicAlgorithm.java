package algorithm;

import java.util.Random;

import datamodel.CityData;
import datamodel.Tour;
import utility.LogScreen;

public abstract class MetaHeuristicAlgorithm {
	protected int nCities;
	protected CityData data;
	protected double currentSolution;
	protected Tour bestTour;
	protected Random generator = new Random();
	protected LogScreen logScreen = LogScreen.getInstance();
	
	public Tour getBestTour() {
		return bestTour;
	}
	
	public double getCurrentSolution() {
		return currentSolution;
	}

	public abstract double solve(CityData data);
	public abstract boolean iterate();
	public abstract void readData(CityData data);
	public abstract String getVariableString();
	
	@Override
	public abstract String toString();
}
