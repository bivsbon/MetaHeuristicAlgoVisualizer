package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import org.openjfx.MetaHeuristicAlgoVisualizer.BeeColony;
import org.openjfx.MetaHeuristicAlgoVisualizer.CityData;
import org.openjfx.MetaHeuristicAlgoVisualizer.DataUtils;
import org.openjfx.MetaHeuristicAlgoVisualizer.FactorialArray;
import org.openjfx.MetaHeuristicAlgoVisualizer.MetaHeuristicAlgorithm;
import org.openjfx.MetaHeuristicAlgoVisualizer.SimulatedAnnealing;
import org.openjfx.MetaHeuristicAlgoVisualizer.SortingContext;
import org.openjfx.MetaHeuristicAlgoVisualizer.TabuSearch;

import javafx.beans.InvalidationListener;
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
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

public class PrimaryController implements Initializable{
	@FXML
	ObservableList<MetaHeuristicAlgorithm> algs = FXCollections.observableArrayList
	(SimulatedAnnealing.getInstance(), TabuSearch.getInstance(), BeeColony.getInstance());
	public ListView<MetaHeuristicAlgorithm> listView1;
	public AnchorPane anchor1;
	// Utility objects
	Random generator = new Random();
	FactorialArray fa = new FactorialArray(dataSize);
	// Algorithm
	CityData data = new CityData();
	SortingContext alg = new SortingContext();
	// Chart
	XYChart.Series series = new XYChart.Series();
	private static int dataSize = 10;
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
		timer.setOnSucceeded(event -> finishedSleeping());
        
        anchor1.getChildren().add(chart);
        AnchorPane.setTopAnchor(chart, 5.0);
        AnchorPane.setBottomAnchor(chart, 5.0);
        AnchorPane.setLeftAnchor(chart, 5.0);
        AnchorPane.setRightAnchor(chart, 5.0);
        chart.setData(series);
        listView1.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MetaHeuristicAlgorithm>() {
			@Override
			public void changed(ObservableValue<? extends MetaHeuristicAlgorithm> observable, MetaHeuristicAlgorithm oldValue, MetaHeuristicAlgorithm newValue) {
				alg.setAlgorithm(newValue);
				if (data.size() != 0) {
					alg.readData(data);
				}
			}
		});
        
	}
	
	private Object finishedSleeping() {
		chart.updateTour(alg.getCurrentTour());
		return null;
	}
	
	public void generateNewRandomData() throws IOException {
		data = DataUtils.generateData(dataSize, 20, 20);
		addDataToGraph(data);
		if (!alg.notSet()) {
	        alg.readData(data);
		}
        chart.removeTour();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addDataToGraph(CityData data) {
        chart.setTitle("Traveling salesman map");
        series = new XYChart.Series();
        series.setName("City");
        for (Point2D p : data) {
            series.getData().add(new XYChart.Data(p.getX(), p.getY()));
        }
        chart.setData(series);
	}
	
	public void showNextIteration() {
		if (alg.iterate()) {
			chart.updateTour(alg.getCurrentTour());
		}
	}
	
	public void runAlgorithm() throws InterruptedException {
		if (!alg.notSet() && data.size() > 0)
		{
			while (alg.iterate()) {
			}	
			chart.updateTour(alg.getCurrentTour());
		}
	}
	
}

