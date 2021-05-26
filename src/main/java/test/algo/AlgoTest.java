package test.algo;

import java.util.ArrayList;

import org.openjfx.MetaHeuristicAlgoVisualizer.BeeColony;
import org.openjfx.MetaHeuristicAlgoVisualizer.CityData;
import org.openjfx.MetaHeuristicAlgoVisualizer.IMetaHeuristicAlgorithm;
import org.openjfx.MetaHeuristicAlgoVisualizer.Tour;
import org.openjfx.MetaHeuristicAlgoVisualizer.SimulatedAnnealing;
import org.openjfx.MetaHeuristicAlgoVisualizer.SortingContext;
import org.openjfx.MetaHeuristicAlgoVisualizer.TabuSearch;

import javafx.geometry.Point2D;

public class AlgoTest {
	private static final int N = 10;
	private static ArrayList<IMetaHeuristicAlgorithm> algs = new ArrayList<>();
	private static final double error = 0.01;
	private static SortingContext sc = new SortingContext();
	private static double shortest = Double.MAX_VALUE;
	static CityData data;
	static Tour tour;
	static ArrayList<Point2D> cityList = new ArrayList<Point2D>();
	static ArrayList<Integer> order = new ArrayList<>();
	static boolean[] chosen = new boolean[N];

	public static void main(String[] args) {
		// Create data set for testing
		cityList.add(new Point2D(5.8, 0.2));
		cityList.add(new Point2D(1.0, 9.2));
		cityList.add(new Point2D(9.8, 2.5));
		cityList.add(new Point2D(5.3, 7.0));
		cityList.add(new Point2D(3.7, 6.3));
		cityList.add(new Point2D(0.8, 9.5));
		cityList.add(new Point2D(6.2, 7.6));
		cityList.add(new Point2D(8.4, 8.2));
		cityList.add(new Point2D(9.7, 1.2));
		cityList.add(new Point2D(5.7, 5.7));
		data = new CityData(cityList);
		// Perform backtracking search to find the solution
		search();
		
		// Add the algorithm to test
		algs.add(new SimulatedAnnealing());
		algs.add(new TabuSearch());
		algs.add(new BeeColony());
		
		// Test the algorithm
		for (IMetaHeuristicAlgorithm alg : algs) {
			sc.setAlgorithm(alg);
			if (checkResult(alg.solve(data))) {
				System.out.println(alg.getAlgName() + "passed");
			}
			else {
				System.out.println(alg.getAlgName() + "failed");
			}
		}
		// Show correct solution
		System.out.println("Correct result is: " + shortest);
	}

	private static void search() {
		if (order.size() == N) {
			tour = new Tour(order);
			double currentCost = data.getCostOnTour(tour);
			if (shortest > currentCost) {
				shortest = currentCost;
			}
		}
		else {
			for (int i = 0; i < N; i++) {
				if (chosen[i]) continue;
				chosen[i] = true;
				order.add(i);
				search();
				order.remove(order.size()-1);
				chosen[i] = false;
			}
		}
	}
	
	private static boolean checkResult(double result) {
		if (result > shortest-error && result < shortest + error) return true;
		else return false;
	}
}
