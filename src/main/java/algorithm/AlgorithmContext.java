package algorithm;

import java.util.ArrayList;
import java.util.List;

import datamodel.CityData;
import datamodel.Tour;

public class AlgorithmContext {
	private MetaHeuristicAlgorithm alg;
	
	public void setAlgorithm(MetaHeuristicAlgorithm alg) {
		this.alg = alg;
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
	
	public static ArrayList<MetaHeuristicAlgorithm> getAlgorithmList() {
		ArrayList<MetaHeuristicAlgorithm> list = new ArrayList<>();
		list.add(SimulatedAnnealing.getInstance());
		list.add(TabuSearch.getInstance());
		list.add(BeeColony.getInstance());
		return list;
	}
}
