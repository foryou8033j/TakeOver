package mil.navy.takingover.view.root.option.tabselector;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mil.navy.takingover.MainApp;
import mil.navy.takingover.util.Regedit;
import mil.navy.takingover.view.logview.LogStage;

public class TabSelectorDialog extends Stage{

	VBox vb = new VBox(15);
	
	CheckBox[] checkBox = new CheckBox[7];
	
	Button set = new Button("저장");
	
	MainApp mainApp;
	
	public TabSelectorDialog(MainApp mainApp) {
		
		this.mainApp = mainApp;
	
		initOwner(mainApp.getPrimaryStage());
		initModality(Modality.WINDOW_MODAL);
		getIcons().add(mainApp.icon);
		setTitle("탭 활성화/비활성화");
		setResizable(false);
		
		vb.setAlignment(Pos.CENTER);
		setWidth(500);
		setHeight(300);
		
		Scene scene = new Scene(vb);
		setScene(scene);
		
		setOnCloseRequest(Event -> {
			close();
		});
		
		checkBox[0] = new CheckBox("인수인계 도구");
		checkBox[1] = new CheckBox("개인 패스워드");
		checkBox[2] = new CheckBox("기타 패스워드");
		checkBox[3] = new CheckBox("전화번호");
		checkBox[4] = new CheckBox("기타 팁");
		checkBox[5] = new CheckBox("기무 대비 모음");
		checkBox[6] = new CheckBox("PC 전원 상태 확인");
		
		Text text = new Text("활성화 하거나 비활성화 할 탭을 지정 해 주세요.");
		text.setFont(Font.font("Malgun Gothic", FontWeight.EXTRA_BOLD, 16));
		
		vb.getChildren().add(text);
		
		for(CheckBox chk:checkBox)
			vb.getChildren().add(chk);
		
		vb.getChildren().add(set);
	
		selectByRegistry();
		
		set.setOnAction(Event -> {
			saveToRegistryAndAction();
			close();
		});
		
	}
	
	
	private void selectByRegistry(){
		
		checkBox[0].setSelected(Regedit.getBooleanValue("takeovertab"));
		checkBox[1].setSelected(Regedit.getBooleanValue("officerpasswordtab"));
		checkBox[2].setSelected(Regedit.getBooleanValue("otherpasswordtab"));
		checkBox[3].setSelected(Regedit.getBooleanValue("phonetab"));
		checkBox[4].setSelected(Regedit.getBooleanValue("tiptab"));
		checkBox[5].setSelected(Regedit.getBooleanValue("securitytab"));
		checkBox[6].setSelected(Regedit.getBooleanValue("getofftab"));
		
		checkBox[0].setDisable(true);
	}
	
	private void saveToRegistryAndAction(){
		
		Regedit.setRegistry("takeovertab", checkBox[0].isSelected());
		Regedit.setRegistry("officerpasswordtab", checkBox[1].isSelected());
		Regedit.setRegistry("otherpasswordtab", checkBox[2].isSelected());
		Regedit.setRegistry("phonetab", checkBox[3].isSelected());
		Regedit.setRegistry("tiptab", checkBox[4].isSelected());
		Regedit.setRegistry("securitytab", checkBox[5].isSelected());
		Regedit.setRegistry("getofftab", checkBox[6].isSelected());
		
		mainApp.getRootLayoutController().setTabVisibleFromRegistry();
		LogStage.append("탭 활성화 정보를 변경하였습니다.");
	}
	
	
}
