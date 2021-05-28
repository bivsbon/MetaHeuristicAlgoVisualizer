package controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.Axis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.shape.Line;

public class TSChart<X, Y> extends ScatterChart<X, Y> {
    private ObservableList<Line> lines = FXCollections.observableArrayList();
	private ObservableList<Integer> tour = FXCollections.observableArrayList();
	private int dataSize = 14;

	public TSChart(Axis<X> xAxis, Axis<Y> yAxis) {
		super(xAxis, yAxis);
		for (int i = 0; i < dataSize; i++) {
	        Line line = new Line();
	        getPlotChildren().add(line);
	        lines.add(line);
		}
		
		updateTour(Arrays.asList(3, 4, 5, 7, 0, 6, 12, 10, 11, 2, 1, 9, 8, 13));
		
        // listen to list changes and re-plot
        tour.addListener((InvalidationListener)observable -> layoutPlotChildren());
	}
	
	public void updateTour(List<Integer> list) {
        Objects.requireNonNull(list, "the tour must not be null");
		this.tour.setAll(list);
		dataSize = list.size();
    }
	
	@Override
    protected void layoutPlotChildren() {
        super.layoutPlotChildren();
        for (int i = 1; i < dataSize; i++) {
            Line l = lines.get(i);
            setLineEndPoints(l, i, i-1);
        }
        setLineEndPoints(lines.get(0), 0, dataSize-1);
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
}
