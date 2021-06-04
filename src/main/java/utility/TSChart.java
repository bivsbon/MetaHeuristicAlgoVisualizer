package utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import datamodel.Tour;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.Axis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class TSChart<X, Y> extends ScatterChart<X, Y> {
	private static final List<Color> COLOR_LIST = new ArrayList<Color>(Arrays.asList(Color.BLACK, Color.RED, Color.CYAN, 
			Color.BLUE, Color.PURPLE, Color.GREEN, Color.CHOCOLATE, Color.DEEPPINK, Color.DODGERBLUE, Color.GOLD,
			Color.GRAY, Color.MAGENTA, Color.LIGHTGREEN, Color.LIGHTSALMON, Color.MEDIUMSLATEBLUE));
    private List<List<Line>> lineSeries = new ArrayList<>();
	private ObservableList<List<Integer>> tours = FXCollections.observableArrayList();
	private int dataSize;
	private int nTours;
	Color lineColor = Color.GOLD;
	
	public TSChart(Axis<X> xAxis, Axis<Y> yAxis) {
		super(xAxis, yAxis);
        // listen to list changes and re-plot
	    tours.addListener((InvalidationListener)observable -> layoutPlotChildren());
	}
	
	public void updateTour(Tour mainTour, List<Tour> minorTours) {
		if (minorTours == null) minorTours = new ArrayList<Tour>();
		List<List<Integer>> list = new ArrayList<>();
		list.add(mainTour.getTour());
		for (Tour tour : minorTours) {
			list.add(tour.getTour());
		}
		tours.setAll(list);
		dataSize = tours.get(0).size();
		nTours = list.size();
		for (int i = 0; i < nTours; i++) {
			List<Line> lineSerie = new ArrayList<>();
			for (int j = 0; j < dataSize; j++) {
				Line line = new Line();
				getPlotChildren().add(line);
				lineSerie.add(line);
			}
			lineSeries.add(lineSerie);
		}
	}
	
	@Override
    protected void layoutPlotChildren() {
        super.layoutPlotChildren();
		if (dataSize > 0) {
	        for (int i = 0; i < nTours; i++) {
	        	List<Line> lineSerie = lineSeries.get(i);
	        	List<Integer> tour = tours.get(i);
	        	Color c = COLOR_LIST.get(i);
	        	for (int j = 1; j < dataSize; j++) {
		            Line l = lineSerie.get(j);
		            setLineEndPoints(l, tour.get(j), tour.get(j-1));
		            l.setStroke(lineColor);
		            l.setStroke(c);
		        }
	        	lineSerie.get(0).setStroke(c);
		        setLineEndPoints(lineSerie.get(0), tour.get(0), tour.get(dataSize-1));
	        }
		}
    }
	
	private void setLineEndPoints(Line l, int index1, int index2) {
        X startX = getData().get(0).getData().get(index1).getXValue();
        X endX = getData().get(0).getData().get(index2).getXValue();
        Y startY = getData().get(0).getData().get(index1).getYValue();
        Y endY = getData().get(0).getData().get(index2).getYValue();
        l.setStartX(getXAxis().getDisplayPosition(startX));
        l.setEndX(getXAxis().getDisplayPosition(endX));
        l.setStartY(getYAxis().getDisplayPosition(startY));
        l.setEndY(getYAxis().getDisplayPosition(endY));
	}
	
	public void setData(Series<X, Y> data) {
		if (data.getData().size() > 0) {
			getData().remove(0);
		}
		getData().add(data);
	}
	
	public void clearTour() {
		for (int i = 0; i < tours.size(); i++) {
			for (List<Line> lineSerie : lineSeries) {
				for (Line l : lineSerie) {
					getPlotChildren().remove(l);
				}
			}
		}
		dataSize = 0;
		nTours = 0;
		tours.clear();
		lineSeries.clear();
	}
}
