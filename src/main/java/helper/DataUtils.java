package helper;

import java.util.ArrayList;
import java.util.Random;

import datamodel.CityData;
import javafx.geometry.Point2D;

public class DataUtils {
	
	public static CityData generateData(int nCities, double xLimit, double yLimit) {
		ArrayList<Point2D> cities = new ArrayList<Point2D>();
		Random generator = new Random();
		double x, y;
		for (int i = 0; i < nCities; i++) {
			// Keep generate random cities if list contains duplicate
			do {
				x = generator.nextDouble() * xLimit;
				y = generator.nextDouble() * yLimit;;
			} while (cities.contains(new Point2D(x, y)));
			cities.add(new Point2D(x, y));
		}
		CityData data = new CityData(cities);
		return data;
	}
	

	
	
}
