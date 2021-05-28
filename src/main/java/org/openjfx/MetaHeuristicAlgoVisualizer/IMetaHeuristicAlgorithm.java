package org.openjfx.MetaHeuristicAlgoVisualizer;

import java.util.ArrayList;

import javafx.geometry.Point2D;

public interface IMetaHeuristicAlgorithm {
	public double solve(CityData data);
	public String getAlgName();
}
