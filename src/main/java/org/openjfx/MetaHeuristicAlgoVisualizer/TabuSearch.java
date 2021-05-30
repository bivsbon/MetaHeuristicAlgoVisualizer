package org.openjfx.MetaHeuristicAlgoVisualizer;

public class TabuSearch extends MetaHeuristicAlgorithm{
	private static final TabuSearch instance = new TabuSearch();
	
	private TabuSearch() {
		// TODO Auto-generated constructor stub
	}
	
	public static TabuSearch getInstance() {
		return instance;
	}

	@Override
	public double solve(CityData data) {
		// TODO Auto-generated method stub
		return 1.0;
	}

	@Override
	public String toString() {
		return "Tabu Search";
	}

	@Override
	public boolean iterate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void readData(CityData data) {
		this.data = data;
		nCities = data.size();
	}
}
