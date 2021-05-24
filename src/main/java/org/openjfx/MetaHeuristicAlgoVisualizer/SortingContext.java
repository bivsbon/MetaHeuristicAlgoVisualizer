package org.openjfx.MetaHeuristicAlgoVisualizer;

import java.util.ArrayList;

import javafx.geometry.Point2D;

public class SortingContext {
	private IMetaHeuristicAlgorithm alg;
	
	public void setAlgorithm(IMetaHeuristicAlgorithm alg) {
		this.alg = alg;
	}
	
	public SortingContext() {
		super();
	}
	
	public double solve(ArrayList<Point2D> data) {
		return alg.solve(data);
	}
}
