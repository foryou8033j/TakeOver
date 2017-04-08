package mil.navy.takingover.view.tab.officergetout;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import mil.navy.takingover.MainApp;
import mil.navy.takingover.util.Notification.DesktopNotify;
import mil.navy.takingover.util.file.ModifyHandler;
import mil.navy.takingover.view.logview.LogStage;

public class GetOffModifyListener extends Thread{
	
	
	MainApp mainApp;
	boolean modify = false;
	
	public GetOffModifyListener(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	@Override
	public void run() {
		
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				if(mainApp.uncMode) doAction();
			}
		}, 200, 100);
		
		
	}
	
	private void doAction(){
		
		if(ModifyHandler.isCurrentModify(mainApp.getRootLayoutController().getPhoneLayoutController().fileName)){
			modify = true;
		}
		else{
			if(modify){
				modify = false;
				Platform.runLater(() ->{
					mainApp.showNotification("데이터가 변경되었습니다.", "PC 전원 감지 테이블의 데이터 변화가 감지되어 로드하였습니다.", DesktopNotify.INFORMATION, 5000L);
					LogStage.append("PC 전원 감지 데이터의 변화가 감지되어 로드하였습니다.");
					mainApp.getRootLayoutController().getPhoneLayoutController().loadDataFromFile();
				});
			}
		}
		
	}
}
