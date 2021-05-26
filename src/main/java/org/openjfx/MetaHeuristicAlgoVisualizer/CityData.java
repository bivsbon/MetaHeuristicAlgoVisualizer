package org.openjfx.MetaHeuristicAlgoVisualizer;

import java.util.ArrayList;

import javafx.geometry.Point2D;

public class CityData {
	private ArrayList<Point2D> cityList;
	
	public ArrayList<Point2D> getCityList() {
		return cityList;
	}
	
	public CityData(ArrayList<Point2D> list) {
		cityList = list;
	}
	
	public int size() {
		return cityList.size();
	}

	public double getCostOnTour(Tour tour) {
		double totalDistance = 0.0;
		
		for (int i = 1; i < cityList.size(); i++) {
			// Add the distance of current city to previous city
			Point2D city1 = cityList.get(tour.get(i));
			Point2D city2 = cityList.get(tour.get(i-1));
			totalDistance += city1.distance(city2);
		}
		// Add the distance of the last city to first city
		Point2D firstCity = cityList.get(tour.get(0));
		Point2D lastCity = cityList.get(tour.get(tour.size()-1));
		totalDistance += firstCity.distance(lastCity);
		return totalDistance;
	}
}
