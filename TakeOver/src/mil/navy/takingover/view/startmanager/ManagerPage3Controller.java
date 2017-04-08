package mil.navy.takingover.view.startmanager;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import mil.navy.takingover.MainApp;

public class ManagerPage3Controller {

	MainApp mainApp;
	
	@FXML RadioButton yes;
	@FXML RadioButton no;
	@FXML Text text;
	
	private boolean error = true;
	
	ToggleGroup group;
	
	public void setMainApp(MainApp mainApp){
		this.mainApp = mainApp;
		text.setText("");
		
		group = new ToggleGroup();
		yes.setToggleGroup(group);
		no.setToggleGroup(group);
		
		text.setText("");
		text.setFill(Color.RED);
	}
	
	@FXML
	private void handleYes(){
		text.setText("로컬 폴더를 지정하였을 때 UNC 모드를 사용하여도 문제는 없지만,\n변경 감지 쓰레드가 동작하여 성능이 저하 될 수 있습니다.");
		error = false;
	}
	
	@FXML
	private void handleNo(){
		text.setText("공유 폴더를 지정하였을 때 UNC 모드를 사용하지 않으면 \n데이터의 손실이 발생 할 수 있습니다.");
		error = false;
	}
	
	public void setText(String message){
		text.setText(message);
	}
	
	public boolean isError(){
		return error;
	}
	
	public boolean isUNCMode(){
		return yes.isSelected();
	}
	
}
