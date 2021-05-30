package org.openjfx.MetaHeuristicAlgoVisualizer;

import java.util.Random;

public abstract class MetaHeuristicAlgorithm {
	protected int nCities;
	protected CityData data;
	protected double currentSolution;
	protected Tour currentTour;
	protected Random generator = new Random();
	
	public Tour getCurrentTour() {
		return currentTour;
	}
	
	public double getCurrentSolution() {
		return currentSolution;
	}

	public abstract double solve(CityData data);
	public abstract String getAlgName();
	public abstract boolean iterate();
}
