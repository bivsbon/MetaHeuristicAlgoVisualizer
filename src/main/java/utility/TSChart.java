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
	private static final List<Color> COLOR_LIST = new ArrayList<Color>(Arrays.asList(Color.RED, Color.CYAN, 
			Color.BLUE, Color.PURPLE, Color.GREEN, Color.CHOCOLATE, Color.DEEPPINK, Color.DODGERBLUE, Color.GOLD,
			Color.GRAY, Color.MAGENTA, Color.LIGHTGREEN, Color.LIGHTSALMON, Color.MEDIUMSLATEBLUE));
    private List<List<Line>> lineSeries = new ArrayList<>();
	private ObservableList<List<Integer>> tours = FXCollections.observableArrayList();
	private int dataSize;
	Color lineColor = Color.GOLD;
	
	public TSChart(Axis<X> xAxis, Axis<Y> yAxis) {
		super(xAxis, yAxis);
        // listen to list changes and re-plot
	    tours.addListener((InvalidationListener)observable -> layoutPlotChildren());
	}
	
	public void updateTour(int index, List<Tour> tourList) {
		for (Tour t : tourList) {
			updateTour(index++, t);
		}
	}
	
	public void updateTour(Tour mainTour, List<Tour> minorTours) {
		
	}
	
	public void updateTour(Tour mainTour, Tour minortour) {
		ObservableList<Integer> t = tours.get(index);
		t.setAll(tour.getTour());
		dataSize = tour.size();
		for (int i = 0; i < dataSize; i++) {
	        Line line = new Line();
	        getPlotChildren().add(line);
	        lineSeries.get(index).add(line);
		}
	}
	
	@Override
    protected void layoutPlotChildren() {
        super.layoutPlotChildren();
		if (dataSize > 0) {
	        for (int i = 1; i < dataSize; i++) {
	            Line l = lines.get(i);
	            setLineEndPoints(l, tour.get(i), tour.get(i-1));
	            l.setStroke(lineColor);
	        }
	        setLineEndPoints(lines.get(0), tour.get(0), tour.get(1));
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
			for (List<lines> l : line) {
				getPlotChildren().remove(l);
			}
			
		}
		dataSize = 0;
		tour.clear();
		lines.clear();
	}
	
	public void changeColor() {
		lineColor = lineColor.invert();
	}
}
