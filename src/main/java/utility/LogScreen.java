package utility;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class LogScreen {
	private static final LogScreen instance = new LogScreen();
	private ListView<String> logView;
	ObservableList<String> lines = FXCollections.observableArrayList();
	private boolean displayDisabled = false;
	private int count = 0;
	
	public static LogScreen getInstance() {
		return instance;
	}
	
	public void setListView(ListView<String> logView) {
		this.logView = logView;
		this.logView.setItems(lines);
	}
	
	public void setDisplayDisabled(boolean flag) {
		displayDisabled = flag;
		clear();
	}
	
	public void addLine(String line) {
		if (!displayDisabled) {
			lines.add(line);
			logView.scrollTo(count);
			count++;
		}
	}
	
	public void clear() {
		lines.clear();
		count = 0;
	}
}
