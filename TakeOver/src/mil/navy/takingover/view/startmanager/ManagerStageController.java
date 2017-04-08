package mil.navy.takingover.view.startmanager;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import mil.navy.takingover.MainApp;
import mil.navy.takingover.util.Regedit;
import mil.navy.takingover.view.logview.LogPopup;
import mil.navy.takingover.view.logview.LogStage;

public class ManagerStageController {

	int curPage = 0;
	
	MainApp mainApp;
	
	@FXML Text title;
	@FXML RadioButton tree;
	@FXML RadioButton tree1;
	@FXML RadioButton tree2;
	@FXML RadioButton tree3;
	@FXML RadioButton tree4;
	@FXML RadioButton tree5;
	@FXML RadioButton tree6;
	
	@FXML Button previous;
	@FXML Button next;
	@FXML Button cancle;
	
	@FXML BorderPane pane;
	
	
	FXMLLoader[] page;
	AnchorPane[] loadedPane;
	
	ManagerPage1Controller controller1;
	ManagerPage2Controller controller2;
	ManagerPage3Controller controller3;
	ManagerPage4Controller controller4;
	ManagerPage5Controller controller5;
	
	Stage stage;
	
	
	
	@FXML
	private void handlePrivious() throws IOException{
		curPage--;
		showCurrentTree();
	}
	
	@FXML
	private void handleNext() throws IOException{
		curPage++;
		showCurrentTree();
	}
	
	@FXML
	private void handleCancle(){
		System.exit(0);
	}
	
	public void setMainApp(MainApp mainApp, Stage stage) throws IOException{
		this.mainApp = mainApp;
		this.stage = stage;
		this.page = new FXMLLoader[7];
		this.loadedPane = new AnchorPane[7];
		
		loadPages();
		showCurrentTree();
	}
	
	private void loadPages(){
		for(int i=0; i<7; i++){
			try {
				page[i] = new FXMLLoader(this.getClass().getResource("ManagerPage" + i + ".fxml"));
				loadedPane[i] = (AnchorPane) page[i].load();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		controller1 = page[1].getController();
		controller1.setMainApp(mainApp);
		
		controller2 = page[2].getController();
		controller2.setMainApp(mainApp);
		
		controller3 = page[3].getController();
		controller3.setMainApp(mainApp);
		
		controller4 = page[4].getController();
		controller4.setMainApp(mainApp);
		
		controller5 = page[5].getController();
		controller5.setMainApp(mainApp);
		
	}
	
	private void setDatas(){
		//데이터 폴더 경로 지정
		
		mainApp.title = controller1.getTitle();
		Regedit.setRegistry("title", controller1.getTitle());
		
		mainApp.uncPath = controller2.getUncPath();
		Regedit.setRegistry("uncPath", mainApp.uncPath);
		
		mainApp.uncMode = controller3.isUNCMode();
		Regedit.setRegistry("uncMode", mainApp.uncMode);
		
		boolean takeOver = controller4.getSelectedGroup().get(0);
		boolean officer = controller4.getSelectedGroup().get(1);
		boolean other = controller4.getSelectedGroup().get(2);
		boolean phone = controller4.getSelectedGroup().get(3);
		boolean tip = controller4.getSelectedGroup().get(4);
		boolean security = controller4.getSelectedGroup().get(5);
		boolean getoff = controller4.getSelectedGroup().get(6);
		
		Regedit.setRegistry("takeovertab", takeOver);
		Regedit.setRegistry("officerpasswordtab", officer);
		Regedit.setRegistry("otherpasswordtab", other);
		Regedit.setRegistry("phonetab", phone);
		Regedit.setRegistry("tiptab", tip);
		Regedit.setRegistry("securitytab", security);
		Regedit.setRegistry("getofftab", getoff);
		
		
		if(controller5.isUsePassword()){
			String password = controller5.getPassword(); 
			Regedit.setRegistry("password", password);
		}else{
			Regedit.setRegistry("password", "");
		}
		
		LogStage.append("[설정 된 초기데이터 정보]");
		LogStage.append("Title : " + controller1.getTitle());
		LogStage.append("UNCPath : " + controller2.getUncPath());
		LogStage.append("TakeOver Tab : " + controller4.getSelectedGroup().get(0));
		LogStage.append("OfficerPassword Tab : " + controller4.getSelectedGroup().get(1));
		LogStage.append("OtherPassword Tab : " + controller4.getSelectedGroup().get(2));
		LogStage.append("PhoneNumber Tab : " + controller4.getSelectedGroup().get(3));
		LogStage.append("Tip Tab : " + controller4.getSelectedGroup().get(4));
		LogStage.append("Security Tab : " + controller4.getSelectedGroup().get(5));
		LogStage.append("GetOff Tab : " + controller4.getSelectedGroup().get(6));
		
		if(controller5.isUsePassword()){
			int size = controller5.getPassword().length();
			String pw = "";
			for(int i=0; i<size; i++)
				pw = pw.concat("*");
			LogStage.append("Password : " + pw);
		}else{
			LogStage.append("Password : NO");
		}
			
		LogStage.append("초기 데이터 설정 완료");
	}
	
	public void showCurrentTree(){
		try{
			
			switch (curPage) {
			case 0:
				LogStage.append("기존에 설정 된 데이터를 찾을 수 없어 초기설정을 시작합니다.");
				title.setText("환영합니다.");
				previous.setVisible(false);
				tree.setSelected(true);
				tree1.setSelected(false);
				next.setText("동의");
				pane.setCenter(loadedPane[0]);
				break;
			case 1:
				title.setText("타이틀 명 지정");
				previous.setVisible(true);
				tree.setSelected(false);
				tree1.setSelected(true);
				tree2.setSelected(false);
				next.setText("다음");
				pane.setCenter(loadedPane[1]);
				break;
			case 2:
				if(controller1.getTitle().equals("")){
					controller1.showErrorMeesage();
					curPage--;
					break;
				}
				title.setText("데이터 폴더 지정");
				previous.setVisible(true);
				tree1.setSelected(false);
				tree2.setSelected(true);
				tree3.setSelected(false);
				pane.setCenter(loadedPane[2]);
				break;
			case 3:
				if(controller2.isError()){
					if(controller2.isPathVoid())
						controller2.setText("경로를 지정 해 주세요.");
					curPage--;
					break;
				}
				title.setText("UNC 모드 확인");
				tree2.setSelected(false);
				tree3.setSelected(true);
				tree4.setSelected(false);
				pane.setCenter(loadedPane[3]);
				break;
			case 4:
				if(controller3.isError()){
					controller3.setText("옵션을 지정 해 주세요.");
					curPage--;
					break;
				}
				title.setText("탭 활성화/비활성화");
				tree3.setSelected(false);
				tree4.setSelected(true);
				tree5.setSelected(false);
				pane.setCenter(loadedPane[4]);
				break;
			case 5:
				title.setText("패스워드 설정");
				tree4.setSelected(false);
				tree5.setSelected(true);
				tree6.setSelected(false);
				next.setText("다음");
				pane.setCenter(loadedPane[5]);
				break;
			case 6:
				if(controller5.isUsePassword()){
					if(!controller5.isSame() || controller5.isNoEnterPassword()){
						curPage--;
						break;
					}
				}
				
				title.setText("설정이 완료되었습니다.");
				tree5.setSelected(false);
				tree6.setSelected(true);
				next.setText("완료");
				pane.setCenter(loadedPane[6]);
				break;
			case 7:
				setDatas();
				Regedit.setRegistry("dataExists", true);
				stage.close();
				break;
			default:
				break;
			}
			
			
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	
}
