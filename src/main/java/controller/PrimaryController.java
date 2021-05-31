package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import algorithm.BeeColony;
import algorithm.MetaHeuristicAlgorithm;
import algorithm.SimulatedAnnealing;
import algorithm.SortingContext;
import algorithm.TabuSearch;
import datamodel.CityData;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
	// Chart
	XYChart.Series<Number, Number> series = new XYChart.Series<>();
	Task<Void> timer = new Task<Void>() {
	    @Override
	    protected Void call() throws Exception {
	       try {
	            Thread.sleep(1);
	       } catch (InterruptedException e) {}
	       return null;
	    }
	};
    final NumberAxis xAxis = new NumberAxis(-1, 21, 1);
    final NumberAxis yAxis = new NumberAxis(-1, 21, 1);
    final TSChart<Number,Number> chart = new
        TSChart<Number,Number>(xAxis, yAxis);

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("Reset stage");
		listView1.setItems(algs);
        listView1.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MetaHeuristicAlgorithm>() {
			@Override
			public void changed(ObservableValue<? extends MetaHeuristicAlgorithm> observable, MetaHeuristicAlgorithm oldValue, MetaHeuristicAlgorithm newValue) {
				alg.setAlgorithm(newValue);
				if (data.size() != 0) {
					alg.readData(data);
				}
			}
		});
        
		timer.setOnSucceeded(event -> finishedSleeping());

        anchor1.getChildren().add(chart);
        AnchorPane.setTopAnchor(chart, 5.0);
        AnchorPane.setBottomAnchor(chart, 5.0);
        AnchorPane.setLeftAnchor(chart, 5.0);
        AnchorPane.setRightAnchor(chart, 5.0);
        chart.setData(series);
        
	}
	
	private Object finishedSleeping() {
		chart.updateTour(alg.getBestTour());
		return null;
	}
	
	public void generateNewRandomData() throws IOException {
		int dataSize = getDataSize();
		data = DataUtils.generateData(dataSize, 20, 20);
		addDataToGraph(data);
		if (!alg.notSet()) {
	        alg.readData(data);
		}
        chart.removeTour();
	}

	private void addDataToGraph(CityData data) {
        chart.setTitle("Traveling salesman map");
        series = new XYChart.Series<Number, Number>();
        series.setName("City");
        for (Point2D p : data) {
            series.getData().add(new Data<Number, Number>(p.getX(), p.getY()));
        }
        chart.setData(series);
	}
	
	public void showNextIteration() {
		chart.updateTour(alg.getBestTour());
		if (alg.iterate()) {
			chart.updateTour(alg.getBestTour());
		}
	}
	
	public void runAlgorithm() throws InterruptedException {
		chart.updateTour(alg.getBestTour());
		if (!alg.notSet() && data.size() > 0)
		{
			while (alg.iterate()) {
			}	
			chart.updateTour(alg.getBestTour());
		}
	}
	
	private int getDataSize() {
		String text = textFieldCity.getText();
		int n = Integer.parseInt(text);
		if (n > 15) {
			nCitiesWarningLabel.setText("Must not be greater than 15");
			textFieldCity.setText("15");
			return 15;
		}
		else if (n < 3) {
			nCitiesWarningLabel.setText("Must not be less than 3");
			textFieldCity.setText("3");
			return 3;
		}
		else {
			nCitiesWarningLabel.setText("");
			return n;
		}
	}
	
}

