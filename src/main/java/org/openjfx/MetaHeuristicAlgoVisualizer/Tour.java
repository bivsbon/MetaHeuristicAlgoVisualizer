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
	
	@Override
	public String toString() {
		return tour.toString();
	}
}
