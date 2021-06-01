package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import algorithm.BeeColony;
import algorithm.SolutionGenerator;
import algorithm.MetaHeuristicAlgorithm;
import algorithm.SimulatedAnnealing;
import algorithm.AlgorithmContext;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import utility.DataUtils;
import utility.LogScreen;

public class PrimaryController implements Initializable{
	@FXML
	ObservableList<MetaHeuristicAlgorithm> algs = FXCollections.observableArrayList
	(SimulatedAnnealing.getInstance(), TabuSearch.getInstance(), BeeColony.getInstance());
	public ListView<MetaHeuristicAlgorithm> listView1;
	public ListView<String> logView;
	public AnchorPane anchor1;
	public AnchorPane anchor2;
	public TextField textFieldCity;
	public Label nCitiesWarningLabel;
	public Label runAlgWarningLabel;
	public Button runBtn;
	public Button resetBtn;
	// Algorithm
	CityData data = new CityData();
	AlgorithmContext alg = new AlgorithmContext();
	SolutionGenerator sg;
	// Chart
	XYChart.Series<Number, Number> series = new XYChart.Series<>();
	XYChart.Series<Number, Number> seriesClone = copySeries(series);
    final NumberAxis xAxisMain = new NumberAxis(-1, 21, 1);
    final NumberAxis yAxisMain = new NumberAxis(-1, 21, 1);
    final NumberAxis xAxisSolution = new NumberAxis(-1, 21, 25);
    final NumberAxis yAxisSolution = new NumberAxis(-1, 21, 25);
    final TSChart<Number,Number> solutionChart = new
            TSChart<Number,Number>(xAxisSolution, yAxisSolution);
    final TSChart<Number,Number> mainChart = new
            TSChart<Number,Number>(xAxisMain, yAxisMain);

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Set the list view component for displaying log
		LogScreen.getInstance().setListView(logView);;
		listView1.setItems(algs);
        listView1.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MetaHeuristicAlgorithm>() {
			@Override
			public void changed(ObservableValue<? extends MetaHeuristicAlgorithm> observable, MetaHeuristicAlgorithm oldValue, MetaHeuristicAlgorithm newValue) {
				alg.setAlgorithm(newValue);
				if (data.size() != 0) {
					alg.readData(data);
			        mainChart.removeTour();
				}
				runBtn.setDisable(false);
				resetBtn.setDisable(true);
			}
		});

        anchor1.getChildren().add(mainChart);
        anchor2.getChildren().add(solutionChart);
        mainChart.setTitle("Traveling salesman map");

        // Hide stuffs to simplify solution chart
        xAxisSolution.setTickLabelsVisible(false);
        yAxisSolution.setTickLabelsVisible(false);
        solutionChart.setLegendVisible(false);
        
        fitChartToPane(mainChart);
        fitChartToPane(solutionChart);
        mainChart.setData(series);
        solutionChart.setData(seriesClone);
        
        resetBtn.setDisable(true);
	}
	
	public void generateNewRandomData() throws IOException {
		int dataSize = getDataSize();
		data = DataUtils.generateData(dataSize, 20, 20);
		addDataToGraph(data);
		if (!alg.notSet()) {
	        alg.readData(data);
		}
		sg = new SolutionGenerator(data);

        mainChart.removeTour();
        solutionChart.removeTour();
        solutionChart.updateTour(sg.getSolutionTour());
		runBtn.setDisable(false);
		resetBtn.setDisable(true);
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
		if (assertRunCondition()) {
			mainChart.updateTour(alg.getBestTour());
			if (alg.iterate()) {
				mainChart.updateTour(alg.getBestTour());
			}
		}
	}
	
	public void runAlgorithm() throws InterruptedException {
		if (assertRunCondition()) {
			mainChart.updateTour(alg.getBestTour());
			while (alg.iterate()) {
			}
			mainChart.updateTour(alg.getBestTour());
			runBtn.setDisable(true);
			resetBtn.setDisable(false);
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
	
	public void resetAlg() {
		if (!alg.notSet() && data.size() > 0)
		{
			alg.readData(data);
	        mainChart.removeTour();
			runBtn.setDisable(false);
			resetBtn.setDisable(true);
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
	
	private boolean assertRunCondition() {
		if (alg.notSet()){
			runAlgWarningLabel.setText("May be set an algorithm, no?");			
		}
		else if (data.size() <= 0) {
			runAlgWarningLabel.setText("Can't run when there's no data");
		}
		else {
			runAlgWarningLabel.setText("");
			return true;
		}
		return false;
		
	}
}

