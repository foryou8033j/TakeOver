package mil.navy.takingover.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import mil.navy.takingover.MainApp;
import mil.navy.takingover.model.InternetAcceptData;

public class InternetUserLayoutController implements Initializable{
	
	ObservableList<String> objectDatas = FXCollections.observableArrayList("등록", "변경", "삭제");
	ObservableList<String> policyDatas = FXCollections.observableArrayList("공용", "개인");
	
	@FXML
	ComboBox object;
	
	@FXML
	TextField name;
	
	@FXML
	TextField ip;
	
	@FXML
	ComboBox policy;
	
	@FXML
	TextField ground;
	
	@FXML
	TextField worker;
	
	@FXML
	Button accept;
	
	@FXML
	Button cancle;
	
	private Stage stage;
	
	private MainApp mainApp;
	
	private int index = -1;
	
	private InternetAcceptData curData = null;
	
	private boolean editMode = false;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public InternetUserLayoutController() {
		// TODO Auto-generated constructor stub
	}
	
	@FXML
	private void handleAccept()
	{
		
		if(editMode)
		{
			curData.setObject(object.getSelectionModel().getSelectedItem().toString());
			curData.setName(name.getText());
			curData.setIp(ip.getText());
			curData.setPolicy(policy.getSelectionModel().getSelectedItem().toString());
			curData.setGround(ground.getText());
			curData.setWorker(worker.getText());
		}
		else
		{
			mainApp.getHeadLayoutController().curSelectedData.getInternetAcceptData().add(new InternetAcceptData(
					object.getSelectionModel().getSelectedItem().toString(),
					name.getText(),
					ip.getText(),
					policy.getSelectionModel().getSelectedItem().toString(),
					ground.getText(),
					worker.getText()));
		}
		mainApp.getHeadLayoutController().curSelectedData.saveDataToFile();
		mainApp.ableToLoad = true;
		stage.close();
	}
	
	@FXML
	private void handleCancle()
	{
		mainApp.ableToLoad = true;
		stage.close();
	}
	
	public void setMainApp(MainApp mainApp, Stage stage)
	{
		this.mainApp = mainApp;
		this.stage = stage;
		mainApp.ableToLoad = false;
		
		object.setItems(objectDatas);
		policy.setItems(policyDatas);
		
		object.getSelectionModel().select(0);
		policy.getSelectionModel().select(0);
		
		worker.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent arg0) {
				if(arg0.getCode() == KeyCode.ENTER)
					handleAccept();
			};
		});
		
	}
	
	public void setDefaultData(int index)
	{
		this.index = index;
		
		if(index != -1)
		{
			this.curData = mainApp.getHeadLayoutController().curSelectedData.getInternetAcceptData().get(index);
			
			object.getSelectionModel().select(curData.getObject());
			name.setText(curData.getName());
			ip.setText(curData.getIp());
			policy.getSelectionModel().select(curData.getPolicy());
			ground.setText(curData.getGround());
			worker.setText(curData.getWorker());
			editMode = true;
		}
		
	}
	
	
	
	

}
