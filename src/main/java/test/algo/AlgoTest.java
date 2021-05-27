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
	private static double solution = Double.MAX_VALUE;
	private static Tour solutionTour;
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
			double result = alg.solve(data);
			if (checkResult(result)) {
				System.out.println(alg.getAlgName() + "passed");
			}
			else {
				System.out.println(alg.getAlgName() + "failed");
			}
			System.out.println(result);
		}
		// Show correct solution
		System.out.println("Correct solution is: " + solution);
		System.out.println(solutionTour.toString());
	}

	@SuppressWarnings("unchecked")
	private static void search() {
		if (order.size() == N) {
			tour = new Tour(order);
			double currentCost = tour.getCost(data);
			if (solution > currentCost) {
				solution = currentCost;
				solutionTour = new Tour((ArrayList<Integer>) tour.getTour().clone());
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
		if (result > solution-error && result < solution+error) return true;
		else return false;
	}
}
