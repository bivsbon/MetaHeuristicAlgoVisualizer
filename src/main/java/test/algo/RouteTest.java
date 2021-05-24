package test.algo;

import org.openjfx.MetaHeuristicAlgoVisualizer.DataUtils;
import org.openjfx.MetaHeuristicAlgoVisualizer.Route;

public class RouteTest {

	public static void main(String[] args) {
		
		Route r = new Route(DataUtils.generateData(6, 8, 10));
		System.out.println(r.getTotalDistance());
	}
}