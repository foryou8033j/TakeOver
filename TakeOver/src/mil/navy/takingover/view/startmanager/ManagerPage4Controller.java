package mil.navy.takingover.view.startmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import mil.navy.takingover.MainApp;
import mil.navy.takingover.view.logview.LogPopup;

public class ManagerPage4Controller {

	MainApp mainApp;
	
	@FXML CheckBox check0;
	@FXML CheckBox check1;
	@FXML CheckBox check2;
	@FXML CheckBox check3;
	@FXML CheckBox check4;
	@FXML CheckBox check5;
	@FXML CheckBox check6;
	
	
	LogPopup logPopup;
	
	public void setMainApp(MainApp mainApp){
		this.mainApp = mainApp;
		
		setMouseEvent(check0, "인수인계 도구", "이 프로그램의 기본적인 기능이며, 비활성화 할 수 없습니다.");
		setMouseEvent(check1, "개인 패스워드", "개개인의 패스워드를 한눈에 볼 수 있게 관리 할 수 있습니다.");
		setMouseEvent(check2, "기타 패스워드", "각 체계나, 기타 여러 패스워드를 관리 할 수 있습니다.");
		setMouseEvent(check3, "전화번호 모음", "전화번호를 관리할 수 있습니다.");
		setMouseEvent(check4, "기타 팁", "기타 정보를 수집 할 수 있습니다.");
		setMouseEvent(check5, "기무 대비모음", "PC 정리 프로세스를 제공합니다.");
		setMouseEvent(check6, "PC 전원 상태 확인", "각 PC의 전원 인가 상태를 한 눈에 파악 할 수 있습니다.");
		
		
	}
	
	private void setMouseEvent(CheckBox checkBox, String title, String object){
		checkBox.setOnMouseEntered(Event -> {
			logPopup = new LogPopup(Event, title, object);
			logPopup.show();
		});
		checkBox.setOnMouseExited(Event -> {
			logPopup.close();
		});
	}
	
	@FXML
	private void handleButton(){
		
	}
	
	public ObservableList<Boolean> getSelectedGroup(){
		ObservableList<Boolean> list = FXCollections.observableArrayList(
				new Boolean(check0.isSelected()),
				new Boolean(check1.isSelected()),
				new Boolean(check2.isSelected()),
				new Boolean(check3.isSelected()),
				new Boolean(check4.isSelected()),
				new Boolean(check5.isSelected()),
				new Boolean(check6.isSelected())
				);
		return list;
	}
	
}
