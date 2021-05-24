package org.openjfx.MetaHeuristicAlgoVisualizer;

import java.util.ArrayList;

import javafx.geometry.Point2D;

public class Route {
	private ArrayList<Point2D> cityOrder;
	
	public ArrayList<Point2D> getCityOrder() {
		return cityOrder;
	}
	
	public void swapCity(int index1, int index2) {
		//TODO: Swap the order of 2 city in route
	}
	
	public Route(ArrayList<Point2D> cityOrder) {
		this.cityOrder = cityOrder;
	}
	
	public double getTotalDistance() {
		double totalDistance = 0.0;
		
		for (int i = 1; i < cityOrder.size(); i++) {
			// Add the distance of current city to previous city
			totalDistance += cityOrder.get(i).distance(cityOrder.get(i-1));
		}
		// Add the distance of the last city to first city
		totalDistance += cityOrder.get(0).distance(cityOrder.get(cityOrder.size()-1));
		return totalDistance;
	}
}
