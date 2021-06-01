package utility;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class LogScreen {
	private ListView<String> logView;
	ObservableList<String> lines = FXCollections.observableArrayList();
	private boolean displayDisabled = false;
	
	public LogScreen(ListView<String> logView) {
		this.logView = logView;
		this.logView.setItems(lines);
	}
	
	public void setDisplayDisabled(boolean flag) {
		displayDisabled = flag;
	}
	
	public void addLine(String line) {
		if (!displayDisabled) {
			lines.add(line);
		}
	}
	
	public void clear() {
		lines.clear();
	}
}
