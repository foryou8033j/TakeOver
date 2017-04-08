package mil.navy.takingover.view.loading;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public class LoadLayoutController {
	
	@FXML
	Text text;
	
	@FXML
	ProgressIndicator progress;
	
	
	public LoadLayoutController() {
		// TODO Auto-generated constructor stub
	}
	
	public void setTitle(String title)
	{
		text.setText(title);
	}
	
	public ProgressIndicator getProgress()
	{
		return progress;
	}


}
