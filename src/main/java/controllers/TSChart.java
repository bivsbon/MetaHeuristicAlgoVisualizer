package controllers;

import java.util.Objects;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.chart.Axis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.ValueAxis;
import javafx.scene.shape.Line;

public class TSChart<X, Y> extends ScatterChart<X, Y> {
	private ObservableList<Data<X, Y>> lines;

	public TSChart(Axis<X> xAxis, Axis<Y> yAxis) {
		super(xAxis, yAxis);
		
		lines = FXCollections.observableArrayList(d -> new Observable[] {d.YValueProperty(), d.XValueProperty()});
        // listen to list changes and re-plot
        lines.addListener((ListChangeListener<? super Data<X, Y>>)observable -> layoutPlotChildren());
	}
	
	public void addHorizontalValueMarker(Data<X, Y> marker) {
        Objects.requireNonNull(marker, "the marker must not be null");
        if (lines.contains(marker)) return;
        Line line = new Line();
        marker.setNode(line);
        getPlotChildren().add(line);
        lines.add(marker);
    }
	
	@Override
    protected void layoutPlotChildren() {
        super.layoutPlotChildren();
        for (Data<X, Y> line : lines) {
            double lower = ((ValueAxis) getXAxis()).getLowerBound();
            X lowerX = getXAxis().toRealValue(lower);
            double upper = ((ValueAxis) getXAxis()).getUpperBound();
            X upperX = getXAxis().toRealValue(upper);
            Line l = (Line) line.getNode();
            l.setStartX(getXAxis().getDisplayPosition(lowerX));
            l.setEndX(getXAxis().getDisplayPosition(upperX));
            l.setStartY(getYAxis().getDisplayPosition(line.getYValue()));
            l.setEndY(l.getStartY());
        }
    }
}
