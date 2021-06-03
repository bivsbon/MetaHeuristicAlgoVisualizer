package algorithm;

import java.util.List;

import datamodel.CityData;
import datamodel.Tour;

public class AlgorithmContext {
	private MetaHeuristicAlgorithm alg;
	
	public void setAlgorithm(MetaHeuristicAlgorithm alg) {
		this.alg = alg;
	}
	
	public MetaHeuristicAlgorithm getAlgorithm() {
		return alg;
	}
	
	public double solve(CityData data) {
		return alg.solve(data);
	}
	
	public boolean iterate() {
		return alg.iterate();
	}
	
	public void readData(CityData data) {
		alg.readData(data);
	}
	
	public boolean notSet() {
		return alg == null;
	}
	
	public Tour getBestTour() {
		return alg.getBestTour();
	}
	
	public List<Tour> getMinorTours() {
		return alg.getMinorTours();
	}
	
	public String getVariableString() {
		return alg.getVariableString();
	}
}
