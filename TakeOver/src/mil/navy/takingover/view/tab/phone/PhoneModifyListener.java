package mil.navy.takingover.view.tab.phone;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import mil.navy.takingover.MainApp;
import mil.navy.takingover.util.Notification.DesktopNotify;
import mil.navy.takingover.util.file.ModifyHandler;
import mil.navy.takingover.view.logview.LogStage;

public class PhoneModifyListener extends Thread{
	
	
	MainApp mainApp;
	boolean modify = false;
	
	public PhoneModifyListener(MainApp mainApp) {
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
					LogStage.append("전화번호 데이터의 변화가 감지되어 로드하였습니다.");
					mainApp.showNotification("데이터가 변경되었습니다.", "전화번호 테이블의 데이터 변화가 감지되어 로드하였습니다.", DesktopNotify.INFORMATION, 5000L);
					mainApp.getRootLayoutController().getPhoneLayoutController().loadDataFromFile();
				});
			}
		}
		
	}
}
