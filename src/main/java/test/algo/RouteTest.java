package test.algo;

import java.util.ArrayList;

import org.openjfx.MetaHeuristicAlgoVisualizer.Route;

import com.sun.javafx.geom.Point2D;

public class RouteTest {

	public static void main(String[] args) {
		ArrayList<Point2D> order = new ArrayList<Point2D>();
		Point2D p1 = new Point2D();
		Point2D p2 = new Point2D();
		Point2D p3 = new Point2D();
		p2.setLocation(1.0f, 2.0f);
		p3.setLocation(2.0f, 4.0f);
		order.add(p1);
		order.add(p2);
		order.add(p3);
		
		Route r = new Route(order);
		System.out.println(r.getTotalDistance());
	}
}