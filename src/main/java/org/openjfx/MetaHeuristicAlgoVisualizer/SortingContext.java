package org.openjfx.MetaHeuristicAlgoVisualizer;

public class SortingContext {
	private MetaHeuristicAlgorithm alg;
	
	public void setAlgorithm(MetaHeuristicAlgorithm alg) {
		this.alg = alg;
	}
	
	public double solve(CityData data) {
		return alg.solve(data);
	}
	
	public boolean iterate() {
		return alg.iterate();
	}
}
