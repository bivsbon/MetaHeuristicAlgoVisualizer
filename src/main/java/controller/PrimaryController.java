package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import algorithm.BeeColony;
import algorithm.SolutionGenerator;
import algorithm.MetaHeuristicAlgorithm;
import algorithm.SimulatedAnnealing;
import algorithm.AlgorithmContext;
import algorithm.TabuSearch;
import datamodel.CityData;
import helper.DataUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import utility.LogScreen;
import utility.TSChart;

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
	public TextField delayField;
	public Slider delaySlider;
	public TextArea variableArea;
	public TitledPane solutionPane;
	LogScreen logScreen = utility.LogScreen.getInstance();
	// Algorithm
	CityData data = new CityData();
	AlgorithmContext alg = new AlgorithmContext();
	SolutionGenerator sg;
	boolean runningFlag = true;
	Timeline timer;
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
		logScreen.setListView(logView);
		listView1.setItems(algs);
		// Logic when switching algorithms
        listView1.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MetaHeuristicAlgorithm>() {
			@Override
			public void changed(ObservableValue<? extends MetaHeuristicAlgorithm> observable, MetaHeuristicAlgorithm oldValue, MetaHeuristicAlgorithm newValue) {
				alg.setAlgorithm(newValue);
				if (data.size() != 0) {
					runningFlag = false;
					alg.readData(data);
			        mainChart.clearTour();
				}
				
				logScreen.clear();
				runBtn.setDisable(false);
				resetBtn.setDisable(true);
			}
		});

        anchor1.getChildren().add(mainChart);
        anchor2.getChildren().add(solutionChart);
        
        configureDelayField();
        configureChart();
        configureSlider();
        
        resetBtn.setDisable(true);;
	}
	
	private void finishedSleeping() {
		timer.stop();
		if (alg.iterate() && runningFlag == true) {
			mainChart.updateTour(alg.getBestTour(), alg.getMinorTours());
			variableArea.setText(alg.getVariableString());
			timer.play();
		}
	}

	public void generateNewRandomData() throws IOException {
		runningFlag = false;
		logScreen.setDisplayDisabled(true);
		int dataSize = getDataSize();
		data = DataUtils.generateData(dataSize, 20, 20);
		addDataToGraph(data);
		if (!alg.notSet()) {
	        alg.readData(data);
			variableArea.clear();
		}
		sg = new SolutionGenerator(data);
		solutionPane.setText("Correct solution: " + sg.getTourCost());
		
		// Update chart and log screen
        mainChart.clearTour();
        solutionChart.clearTour();
        solutionChart.updateTour(sg.getSolutionTour(), new ArrayList<>());
        logScreen.clear();

        // Button logic
		runBtn.setDisable(false);
		resetBtn.setDisable(true);
		logScreen.setDisplayDisabled(false);
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
		logScreen.clear();
		if (assertRunCondition()) {
			mainChart.updateTour(alg.getBestTour(), alg.getMinorTours());
			if (alg.iterate()) {
				mainChart.updateTour(alg.getBestTour(), alg.getMinorTours());
				variableArea.setText(alg.getVariableString());
			}
		}
	}
	
	public void runAlgorithm() throws InterruptedException {
		runningFlag = true;
		timer = new Timeline(new KeyFrame(Duration.millis((int)delaySlider.getValue()), event -> finishedSleeping()));
		logScreen.setDisplayDisabled(true);
		if (assertRunCondition()) {
	        // Button logic
			runBtn.setDisable(true);
			resetBtn.setDisable(false);
			mainChart.updateTour(alg.getBestTour(), alg.getMinorTours());
			timer.play();
		}
		logScreen.setDisplayDisabled(false);
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
		runningFlag = false;
		logScreen.clear();
		if (!alg.notSet() && data.size() > 0)
		{
			alg.readData(data);
	        mainChart.clearTour();
			variableArea.clear();

	        // Button logic
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
	
	private void configureDelayField() {
		delayField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try {
					int value = Integer.parseInt(newValue);
					if (value < (int) delaySlider.getMin()) {
						delayField.setText(Integer.toString((int) delaySlider.getMin()));
						delaySlider.setValue(delaySlider.getMin());
					} else if (value > delaySlider.getMax()) {
						delayField.setText(Integer.toString((int) delaySlider.getMax()));
						delaySlider.setValue(delaySlider.getMax());
					}
					else {
						delaySlider.setValue(value);
					}
				}
				catch (NumberFormatException e) {
					delayField.setText(Integer.toString((int) delaySlider.getMin()));
					delaySlider.setValue(delaySlider.getMin());
				}
			}
		});
	}
	
	private void configureSlider() {
        delaySlider.setValue(20);
        delaySlider.setMin(1);
        delaySlider.setMax(100);
        delaySlider.setShowTickLabels(true);
        delaySlider.setShowTickMarks(true);
        delaySlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				Integer value = newValue.intValue();
				delayField.setText(Integer.toString(value));
				timer = new Timeline(new KeyFrame(Duration.millis((int)delaySlider.getValue()), event -> finishedSleeping()));
			}
		});
	}
	
	private void configureChart() {
		mainChart.setTitle("Traveling salesman map");

        // Hide stuffs to simplify solution chart
        xAxisSolution.setTickLabelsVisible(false);
        yAxisSolution.setTickLabelsVisible(false);
        solutionChart.setLegendVisible(false);
        
        fitChartToPane(mainChart);
        fitChartToPane(solutionChart);
        mainChart.setData(series);
        solutionChart.setData(seriesClone);
	}
}

