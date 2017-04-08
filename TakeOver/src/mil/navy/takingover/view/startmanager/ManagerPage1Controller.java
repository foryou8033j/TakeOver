package mil.navy.takingover.view.startmanager;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import mil.navy.takingover.MainApp;

public class ManagerPage1Controller {

	MainApp mainApp;
	
	@FXML TextField title;
	@FXML Text text;
	
	ToggleGroup group;
	
	public void setMainApp(MainApp mainApp){
		this.mainApp = mainApp;
		
		title.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(!newValue.equals(""))
					text.setVisible(false);
			}
		});
		
	}
	
	public String getTitle(){
		return title.getText();
	}
	
	public void showErrorMeesage(){
		text.setVisible(true);
	}
	
	
}
