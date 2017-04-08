package mil.navy.takingover.view.tab.officergetout;

import java.util.regex.Pattern;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import mil.navy.takingover.MainApp;
import mil.navy.takingover.model.getoff.GetOffData;
import mil.navy.takingover.view.logview.LogStage;

public class GetOffInputLayoutController {

	
	@FXML
	TextField name;
	
	@FXML
	TextField ip;
	
	@FXML
	Button accept;
	
	private int index;
	
	private String originalName;
	
	private String originalIP;
	
	private MainApp mainApp;
	
	private Stage stage;
	
	private boolean editMode = false;

	
	public GetOffInputLayoutController() {
		// TODO Auto-generated constructor stub
	}
	
	@FXML
	private void handleAccept()
	{
		if(invalidIPCheck(ip.getText()))
		{
			if(editMode) 
			{
				mainApp.getGetOffData().remove(index);
				mainApp.getGetOffData().add(index, new GetOffData(name.getText(), ip.getText()));
			}else
				mainApp.getGetOffData().add(new GetOffData(name.getText(), ip.getText()));
			//파일 저장
			mainApp.getRootLayoutController().getGetOffLayoutController().saveDataToFile();
			
			stage.close();
		}else
		{
			ip.setText("");
			LogStage.append("[주의] 입력 된 IP에 오류가 있습니다.");
			ip.setPromptText("IP가 올바르지 않습니다");
			name.requestFocus();
		}
		
		
		
		
	}
	
	/**
	 * ip 유효성 검사
	 * @param ip 입력 ip
	 * @return 결과 반환
	 */
	private boolean invalidIPCheck(String ip)
	{
		String validIp = "^([1-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])){3}$";
		if(!Pattern.matches(validIp, ip)){
			return false;
		}else
			return true;
	}
	
	public void setMainApp(MainApp mainApp)
	{
		this.mainApp = mainApp;
		
		name.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent arg0) {
				if(arg0.getCode() == KeyCode.ENTER)
				{
					handleAccept();
				}
				else if(arg0.getCode() == KeyCode.ESCAPE)
				{
					stage.close();
				}
			};
		});
		
		ip.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent arg0) {
				if(arg0.getCode() == KeyCode.ENTER)
				{
					handleAccept();
				}
				else if(arg0.getCode() == KeyCode.ESCAPE)
				{
					stage.close();
				}
			};
		});
	}
	
	public void setStage(Stage stage)
	{
		this.stage = stage;
		
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent arg0) {
				stage.close();
			}
		});
	}
	
	public void setContents(int index, String name, String ip)
	{
		this.index = index;
		this.originalName = name;
		this.originalIP = ip;
		
		this.name.setText(name);
		this.ip.setText(ip);
		
		editMode = true;
	}
	
	
	
	
}
