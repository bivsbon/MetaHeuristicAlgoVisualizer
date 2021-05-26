package org.openjfx.MetaHeuristicAlgoVisualizer;

import java.util.ArrayList;
import java.util.Objects;

import javafx.geometry.Point2D;

public class SortingContext {
	private IMetaHeuristicAlgorithm alg;
	
	public void setAlgorithm(IMetaHeuristicAlgorithm alg) {
		this.alg = alg;
	}
	
	public double solve(CityData data) {
		Objects.requireNonNull(alg, "You haven't set an algorithm!");
		return alg.solve(data);
	}
}
