package org.openjfx.MetaHeuristicAlgoVisualizer;

import java.util.ArrayList;

import javafx.geometry.Point2D;

public interface IMetaHeuristicAlgorithm {
	public double solve(ArrayList<Point2D> data);
	public String getAlgName();
}
