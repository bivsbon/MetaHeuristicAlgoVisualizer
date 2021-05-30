package controller;

import java.util.List;

import datamodel.Tour;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.Axis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.shape.Line;

public class TSChart<X, Y> extends ScatterChart<X, Y> {
    private ObservableList<Line> lines = FXCollections.observableArrayList();
	private ObservableList<Integer> tour = FXCollections.observableArrayList();
	private int dataSize;

	public TSChart(Axis<X> xAxis, Axis<Y> yAxis, List<Integer> tour) {
		this(xAxis, yAxis);
		updateTour(tour);
	}
	
	public TSChart(Axis<X> xAxis, Axis<Y> yAxis) {
		super(xAxis, yAxis);
        // listen to list changes and re-plot
        this.tour.addListener((InvalidationListener)observable -> layoutPlotChildren());
	}
	
	public void updateTour(List<Integer> list) {
		this.tour.setAll(list);
		dataSize = list.size();
		for (int i = 0; i < dataSize; i++) {
	        Line line = new Line();
	        getPlotChildren().add(line);
	        lines.add(line);
		}
    }
	
	public void updateTour(Tour tour) {
		updateTour(tour.getTour());
	}
	
	@Override
    protected void layoutPlotChildren() {
        super.layoutPlotChildren();
		if (dataSize > 0) {
	        for (int i = 1; i < dataSize; i++) {
	            Line l = lines.get(i);
	            setLineEndPoints(l, tour.get(i), tour.get(i-1));
	        }
	        setLineEndPoints(lines.get(0), tour.get(0), tour.get(dataSize-1));
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
	
	public void removeTour() {
		for (Line l : lines) {
			getPlotChildren().remove(l);
		}
		dataSize = 0;
		tour.clear();
		lines.clear();
	}
}
