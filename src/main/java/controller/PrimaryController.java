package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import algorithm.BeeColony;
import algorithm.BruteForce;
import algorithm.MetaHeuristicAlgorithm;
import algorithm.SimulatedAnnealing;
import algorithm.SortingContext;
import algorithm.TabuSearch;
import datamodel.CityData;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import utility.DataUtils;

public class PrimaryController implements Initializable{
	@FXML
	ObservableList<MetaHeuristicAlgorithm> algs = FXCollections.observableArrayList
	(SimulatedAnnealing.getInstance(), TabuSearch.getInstance(), BeeColony.getInstance());
	public ListView<MetaHeuristicAlgorithm> listView1;
	public AnchorPane anchor1;
	public AnchorPane anchor2;
	public TextField textFieldCity;
	public Label nCitiesWarningLabel;
	// Algorithm
	CityData data = new CityData();
	SortingContext alg = new SortingContext();
	BruteForce bf;
	// Chart
	XYChart.Series<Number, Number> series = new XYChart.Series<>();
	XYChart.Series<Number, Number> seriesClone = copySeries(series);
    final NumberAxis xAxis = new NumberAxis(-1, 21, 1);
    final NumberAxis yAxis = new NumberAxis(-1, 21, 1);
    final TSChart<Number,Number> solutionChart = new
            TSChart<Number,Number>(xAxis, yAxis);
    final TSChart<Number,Number> mainChart = new
            TSChart<Number,Number>(xAxis, yAxis);

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		listView1.setItems(algs);
        listView1.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MetaHeuristicAlgorithm>() {
			@Override
			public void changed(ObservableValue<? extends MetaHeuristicAlgorithm> observable, MetaHeuristicAlgorithm oldValue, MetaHeuristicAlgorithm newValue) {
				alg.setAlgorithm(newValue);
				if (data.size() != 0) {
					alg.readData(data);
			        mainChart.removeTour();
				}
			}
		});

        anchor1.getChildren().add(mainChart);
        anchor2.getChildren().add(solutionChart);
        mainChart.setTitle("Traveling salesman map");

        fitChartToPane(mainChart);
        fitChartToPane(solutionChart);
        mainChart.setData(series);
        solutionChart.setData(seriesClone);
	}
	
	public void generateNewRandomData() throws IOException {
		int dataSize = getDataSize();
		data = DataUtils.generateData(dataSize, 20, 20);
		addDataToGraph(data);
		if (!alg.notSet()) {
	        alg.readData(data);
		}
		bf = new BruteForce(data);

        mainChart.removeTour();
        solutionChart.removeTour();
        solutionChart.updateTour(bf.getSolutionTour());
	}

	private void addDataToGraph(CityData data) {
        series = new XYChart.Series<Number, Number>();
        for (Point2D p : data) {
            series.getData().add(new Data<Number, Number>(p.getX(), p.getY()));
        }
        mainChart.setData(series);
        seriesClone = copySeries(series);
        solutionChart.setData(seriesClone);
        series.setName("City");
	}
	
	public void showNextIteration() {
		if (!alg.notSet() && data.size() > 0)
		{
			mainChart.updateTour(alg.getBestTour());
			if (alg.iterate()) {
				mainChart.updateTour(alg.getBestTour());
			}
		}
	}
	
	public void runAlgorithm() throws InterruptedException {
		if (!alg.notSet() && data.size() > 0)
		{
			mainChart.updateTour(alg.getBestTour());
			while (alg.iterate()) {
			}	
			mainChart.updateTour(alg.getBestTour());
		}
	}
	
	private int getDataSize() {
		String text = textFieldCity.getText();
		int n;
		try{
			n = Integer.parseInt(text);
			if (n > 15) {
				nCitiesWarningLabel.setText("Must not be greater than 15");
				textFieldCity.setText("15");
				return 15;
			}
			else if (n < 5) {
				nCitiesWarningLabel.setText("Must not be less than 5");
				textFieldCity.setText("5");
				return 5;
			}
			else {
				nCitiesWarningLabel.setText("");
				return n;
			}
		} catch (NumberFormatException e) {
			nCitiesWarningLabel.setText("Not an integer number, use 5");
			textFieldCity.setText("5");
			return 5;
		}
	}
	
	private void fitChartToPane(TSChart<Number, Number> chart) {
        AnchorPane.setTopAnchor(chart, 5.0);
        AnchorPane.setBottomAnchor(chart, 5.0);
        AnchorPane.setLeftAnchor(chart, 5.0);
        AnchorPane.setRightAnchor(chart, 5.0);
	}
	
	
	private <S, T> XYChart.Series<S, T> copySeries(XYChart.Series<S, T> series) {
	    XYChart.Series<S, T> copy = new XYChart.Series<>(series.getName(),
	            series.getData().stream()
	                  .map(data -> new XYChart.Data<S, T>(data.getXValue(), data.getYValue()))
	                  .collect(Collectors.toCollection(FXCollections::observableArrayList)));
	    return copy;
	}
}

