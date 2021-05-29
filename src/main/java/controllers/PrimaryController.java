package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import org.openjfx.MetaHeuristicAlgoVisualizer.App;
import org.openjfx.MetaHeuristicAlgoVisualizer.FactorialArray;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
	FactorialArray fa = new FactorialArray(14);
    final NumberAxis xAxis = new NumberAxis(0, 10, 1);
    final NumberAxis yAxis = new NumberAxis(0, 500, 100);
    final TSChart<Number,Number> sc = new
        TSChart<Number,Number>(xAxis,yAxis,fa.generateIthPermutaion(14, generator.nextInt(123456)));

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("Reset stage");
		listView1.setItems(algs);
        sc.setTitle("Traveling salesman map");
       
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("City");
        series1.getData().add(new XYChart.Data(4.2, 193.2));
        series1.getData().add(new XYChart.Data(2.8, 33.6));
        series1.getData().add(new XYChart.Data(6.2, 24.8));
        series1.getData().add(new XYChart.Data(1, 14));
        series1.getData().add(new XYChart.Data(1.2, 26.4));
        series1.getData().add(new XYChart.Data(4.4, 114.4));
        series1.getData().add(new XYChart.Data(8.5, 323));
        series1.getData().add(new XYChart.Data(6.9, 289.8));
        series1.getData().add(new XYChart.Data(9.9, 287.1));
        series1.getData().add(new XYChart.Data(3.2, 150.8));
        series1.getData().add(new XYChart.Data(4.8, 20.8));
        series1.getData().add(new XYChart.Data(1.8, 81.4));
        series1.getData().add(new XYChart.Data(7.3, 110.3));
        series1.getData().add(new XYChart.Data(2.7, 41.2));
        
        sc.getData().add(series1);
        anchor1.getChildren().add(sc);
        AnchorPane.setTopAnchor(sc, 0.0);
        AnchorPane.setBottomAnchor(sc, 0.0);
        AnchorPane.setLeftAnchor(sc, 0.0);
        AnchorPane.setRightAnchor(sc, 0.0);
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
}

