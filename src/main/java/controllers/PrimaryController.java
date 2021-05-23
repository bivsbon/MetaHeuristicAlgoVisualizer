package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

public class PrimaryController implements Initializable{
	@FXML
	ObservableList<String> algs = FXCollections.observableArrayList("alg1", "alg2", "alg3");
	public ListView<String> listView1;
	public AnchorPane anchor1;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		listView1.setItems(algs);
	}
}

