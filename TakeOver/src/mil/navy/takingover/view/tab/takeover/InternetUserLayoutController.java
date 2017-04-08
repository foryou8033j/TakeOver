package mil.navy.takingover.view.tab.takeover;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import mil.navy.takingover.MainApp;
import mil.navy.takingover.model.InternetAcceptData;
import mil.navy.takingover.util.file.FileNameConverter;
import mil.navy.takingover.util.file.ModifyHandler;
import mil.navy.takingover.view.loading.LoadLayout;

public class InternetUserLayoutController implements Initializable{
	
	ObservableList<String> objectDatas = FXCollections.observableArrayList("등록", "변경", "삭제");
	ObservableList<String> policyDatas = FXCollections.observableArrayList("공용", "개인");
	
	@FXML
	ComboBox object;
	
	@FXML
	TextField group;
	
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
	Button sameUpper;
	
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
		
		
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				
				
				
				if(editMode)
				{
					curData.setObject(object.getSelectionModel().getSelectedItem().toString());
					curData.setGroup(group.getText());
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
							group.getText(),
							name.getText(),
							ip.getText(),
							policy.getSelectionModel().getSelectedItem().toString(),
							ground.getText(),
							worker.getText()));
				}
				mainApp.getHeadLayoutController().curSelectedData.saveDataToFile();
				mainApp.ableToLoad = true;
				stage.close();
				
				
				
				return null;
			}
		};
		
		LoadLayout worker  = new LoadLayout("데이터를 저장하는 중...", mainApp.getPrimaryStage());
		worker.activateProgress(task);
		
		task.setOnSucceeded(event -> {
			//현재 수정이 끝난 데이터
			String fileName = FileNameConverter.toString(mainApp.getHeadLayoutController().curSelectedData.getLocalDate()) + ".xml";
			ModifyHandler.removeModify(fileName);
			worker.close();
		});
		
		Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();

	}
	
	@FXML
	private void handleCancle()
	{
		mainApp.ableToLoad = true;
		stage.close();
		
		//현재 수정이 끝난 데이터
		String fileName = FileNameConverter.toString(mainApp.getHeadLayoutController().curSelectedData.getLocalDate()) + ".xml";
		ModifyHandler.removeModify(fileName);
	}
	
	@FXML
	private void handleSameUpper()
	{
		if(editMode)
			setUpperData(index - 1);
		else
			setUpperData(mainApp.getHeadLayoutController().curSelectedData.getInternetAcceptData().size() - 1);
	}
	
	public void setMainApp(MainApp mainApp, Stage stage)
	{
		this.mainApp = mainApp;
		this.stage = stage;
		mainApp.ableToLoad = false;
		
		//현재 수정중인 데이터
		String fileName = FileNameConverter.toString(mainApp.getHeadLayoutController().curSelectedData.getLocalDate()) + ".xml";
		ModifyHandler.currentModity(fileName);
		
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent arg0) {
				//현재 수정이 끝난 데이터
				String fileName = FileNameConverter.toString(mainApp.getHeadLayoutController().curSelectedData.getLocalDate()) + ".xml";
				ModifyHandler.removeModify(fileName);
			};
		});
		
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
		
		if(mainApp.getHeadLayoutController().curSelectedData.getInternetAcceptData().size() == 0)
			sameUpper.setDisable(true);
		
	}
	
	public void setDefaultData(int index)
	{
		this.index = index;
		
		if(index != -1)
		{
			this.curData = mainApp.getHeadLayoutController().curSelectedData.getInternetAcceptData().get(index);
			
			object.getSelectionModel().select(curData.getObject());
			group.setText(curData.getGroup());
			name.setText(curData.getName());
			ip.setText(curData.getIp());
			policy.getSelectionModel().select(curData.getPolicy());
			ground.setText(curData.getGround());
			worker.setText(curData.getWorker());
			editMode = true;
		}
		
	}
	
	public void setUpperData(int index)
	{
		if(index != -1)
		{
			InternetAcceptData tmpData = mainApp.getHeadLayoutController().curSelectedData.getInternetAcceptData().get(index);
			
			object.getSelectionModel().select(tmpData.getObject());
			group.setText(tmpData.getGroup());
			ip.setText(tmpData.getIp());
			policy.getSelectionModel().select(tmpData.getPolicy());
			ground.setText(tmpData.getGround());
			worker.setText(tmpData.getWorker());
			//editMode = true;
		}
	}
	
	
	
	

}
