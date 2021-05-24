package org.openjfx.MetaHeuristicAlgoVisualizer;

import java.util.ArrayList;

import javafx.geometry.Point2D;

public class Route {
	private final ArrayList<Point2D> cityList;
	private ArrayList<Integer> order = new ArrayList<>();
	
	public ArrayList<Integer> getRoute() {
		return order;
	}
	
	public void swapCity(int index1, int index2) {
		//TODO: Swap the order of 2 city in route
	}
	
	public Route(ArrayList<Point2D> cityList) {
		this.cityList = cityList;
		for (int i = 0; i < cityList.size(); i++) {
			order.add(i);
		}
	}

	public ArrayList<Integer> getOrder() {
		return order;
	}

	public void setOrder(ArrayList<Integer> order) {
		this.order = order;
	}
	
	public double getTotalDistance() {
		double totalDistance = 0.0;
		
		for (int i = 1; i < order.size(); i++) {
			// Add the distance of current city to previous city
			Point2D city1 = cityList.get(order.get(i));
			Point2D city2 = cityList.get(order.get(i-1));
			totalDistance += city1.distance(city2);
		}
		// Add the distance of the last city to first city
		Point2D firstCity = cityList.get(order.get(0));
		Point2D lastCity = cityList.get(order.get(order.size()-1));
		totalDistance += firstCity.distance(lastCity);
		return totalDistance;
	}
}
