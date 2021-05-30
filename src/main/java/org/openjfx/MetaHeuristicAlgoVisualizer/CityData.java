package org.openjfx.MetaHeuristicAlgoVisualizer;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.geometry.Point2D;

public class CityData implements Iterable<Point2D>{
	private ArrayList<Point2D> cityList = new ArrayList<>();
	
	public ArrayList<Point2D> getCityList() {
		return cityList;
	}
	
	public CityData() {
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

	@Override
	public Iterator<Point2D> iterator() {
		return cityList.iterator();
	}
}
