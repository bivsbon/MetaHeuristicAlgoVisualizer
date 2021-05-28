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
	
	public Point2D get(int index) {
		return cityList.get(index);
	}
}
