package algorithm;

import java.util.ArrayList;

import datamodel.CityData;
import datamodel.Tour;

public class BruteForce {
	private int dataSize;
	private double solution = Double.MAX_VALUE;
	public Tour solutionTour;
	private Tour tour;
	private ArrayList<Integer> order = new ArrayList<>();
	private CityData data;
	static boolean[] chosen;
	
	public BruteForce(CityData data) {
		this.data = data;
		dataSize = data.size();
		chosen = new boolean[dataSize];
		search();
	}
	
	private void search() {
		if (order.size() == dataSize) {
			tour = new Tour(order);
			double currentCost = tour.getCost(data);
			if (solution > currentCost) {
				solution = currentCost;
				solutionTour = new Tour(tour);
			}
		}
		else {
			for (int i = 0; i < dataSize; i++) {
				if (chosen[i]) continue;
				chosen[i] = true;
				order.add(i);
				search();
				order.remove(order.size()-1);
				chosen[i] = false;
			}
		}
	}
}
