package test.algo;

import java.util.ArrayList;
import org.openjfx.MetaHeuristicAlgoVisualizer.Route;

import javafx.geometry.Point2D;

public class AlgoTest {
	private static final int N = 10;
	private static double shortest = Double.MAX_VALUE;
	static ArrayList<Point2D> cities = new ArrayList<Point2D>();
	static ArrayList<Integer> order = new ArrayList<>();
	static boolean[] chosen = new boolean[N];

	public static void main(String[] args) {
		cities.add(new Point2D(5.8, 0.2));
		cities.add(new Point2D(1.0, 9.2));
		cities.add(new Point2D(9.8, 2.5));
		cities.add(new Point2D(5.3, 7.0));
		cities.add(new Point2D(3.7, 6.3));
		cities.add(new Point2D(0.8, 9.5));
		cities.add(new Point2D(6.2, 7.6));
		cities.add(new Point2D(8.4, 8.2));
		cities.add(new Point2D(9.7, 1.2));
		cities.add(new Point2D(5.7, 5.7));
		Route route = new Route(cities);
		search(route);
		
		System.out.println(shortest);
	}

	private static void search(Route route) {
		if (order.size() == N) {
			route.setOrder(order);
			double currentCost = route.getTotalDistance();
			if (shortest > currentCost) {
				shortest = currentCost;
			}
		}
		else {
			for (int i = 0; i < N; i++) {
				if (chosen[i]) continue;
				chosen[i] = true;
				order.add(i);
				search(route);
				order.remove(order.size()-1);
				chosen[i] = false;
			}
		}
	}
}
