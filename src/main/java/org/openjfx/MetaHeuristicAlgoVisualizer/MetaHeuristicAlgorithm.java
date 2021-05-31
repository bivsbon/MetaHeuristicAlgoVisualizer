package org.openjfx.MetaHeuristicAlgoVisualizer;

import java.util.Random;

public abstract class MetaHeuristicAlgorithm {
	protected int nCities;
	protected CityData data;
	protected double currentSolution;
	protected Tour bestTour;
	protected Random generator = new Random();
	
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
