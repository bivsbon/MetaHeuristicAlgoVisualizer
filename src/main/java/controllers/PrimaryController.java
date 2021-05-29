package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import org.openjfx.MetaHeuristicAlgoVisualizer.App;
import org.openjfx.MetaHeuristicAlgoVisualizer.DataUtils;
import org.openjfx.MetaHeuristicAlgoVisualizer.FactorialArray;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

public class PrimaryController implements Initializable{
	@FXML
	ObservableList<String> algs = FXCollections.observableArrayList("alg1", "alg2", "alg3");
	public ListView<String> listView1;
	public AnchorPane anchor1;
	Random generator = new Random();
	FactorialArray fa = new FactorialArray(10);
	XYChart.Series series = new XYChart.Series();
    final NumberAxis xAxis = new NumberAxis(-1, 21, 1);
    final NumberAxis yAxis = new NumberAxis(-1, 21, 1);
    final TSChart<Number,Number> chart = new
        TSChart<Number,Number>(xAxis, yAxis);

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("Reset stage");
		listView1.setItems(algs);
        
        anchor1.getChildren().add(chart);
        AnchorPane.setTopAnchor(chart, 0.0);
        AnchorPane.setBottomAnchor(chart, 0.0);
        AnchorPane.setLeftAnchor(chart, 0.0);
        AnchorPane.setRightAnchor(chart, 0.0);
        chart.setData(series);
	}
//	
//	public void updateGraph() {
//		while (true) {
//            try {
//    			Thread.sleep(1000);
//    		} catch (InterruptedException e) {
//    			// TODO Auto-generated catch block
//    			e.printStackTrace();
//    		}
//            
//        }
//	}
	
	public void updateGraph() throws IOException {
		App.setRoot("primary");
	}
	
	public void showRandomData() throws IOException {
		ArrayList<Point2D> data = DataUtils.generateData(10, 20, 20);
		addDataToGraph(data);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addDataToGraph(ArrayList<Point2D> data) {
        chart.setTitle("Traveling salesman map");
        series = new XYChart.Series();
        series.setName("City");
        for (Point2D p : data) {
            series.getData().add(new XYChart.Data(p.getX(), p.getY()));
        }
        chart.setData(series);
	}
}

