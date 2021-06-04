package datamodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javafx.geometry.Point2D;

public class Tour {
	private ArrayList<Integer> tour = new ArrayList<>();
	
	Random generator = new Random();
	
	public ArrayList<Integer> getTour() {
		return tour;
	}
	
	public int[] swapRanDomCity() {
		int[] index = new int[2];
		index[0] = generator.nextInt(tour.size());
		do {
			index[1] = generator.nextInt(tour.size());
		} while (index[0] == index[1]);
		
		int tmp = tour.get(index[0]);
		tour.set(index[0], tour.get(index[1]));
		tour.set(index[1], tmp);
		return index;
	}
	
	public Tour(int n) {
		super();
		for (int i = 0; i < n; i++) {
			tour.add(i);
		}
	}
	
	public Tour(ArrayList<Integer> tour) {
		super();
		this.tour = tour;
	}
	
	@SuppressWarnings("unchecked")
	public Tour(Tour tour) {
		super();
		this.tour = (ArrayList<Integer>) tour.tour.clone();
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
	
	public void shuffle() {
		Collections.shuffle(tour);
	}
	
	@Override
	public String toString() {
		return tour.toString();
	}
}
