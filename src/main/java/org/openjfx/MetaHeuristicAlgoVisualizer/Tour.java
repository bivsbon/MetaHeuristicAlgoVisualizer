package org.openjfx.MetaHeuristicAlgoVisualizer;

import java.util.ArrayList;

import javafx.geometry.Point2D;

public class Tour {
	private ArrayList<Integer> tour = new ArrayList<>();
	
	public ArrayList<Integer> getTour() {
		return tour;
	}
	
	public void swapRanDomCity(int index1, int index2) {
		//TODO: Swap the order of 2 city in route
	}
	
	public Tour() {
		super();
	}
	
	public Tour(ArrayList<Integer> tour) {
		super();
		this.tour = tour;
	}
	
	public int get(int index) {
		return tour.get(index);
	}
	
	public int size() {
		return tour.size();
	}
	
	public double getCost(CityData data) {
		double totalDistance = 0.0;
		
		for (int i = 1; i < data.size(); i++) {
			// Add the distance of current city to previous city
			Point2D city1 = data.get(tour.get(i));
			Point2D city2 = data.get(tour.get(i-1));
			totalDistance += city1.distance(city2);
		}
		// Add the distance of the last city to first city
		Point2D firstCity = data.get(tour.get(0));
		Point2D lastCity = data.get(tour.get(tour.size()-1));
		totalDistance += firstCity.distance(lastCity);
		return totalDistance;
	}
	
	@Override
	public String toString() {
		return tour.toString();
	}
}
