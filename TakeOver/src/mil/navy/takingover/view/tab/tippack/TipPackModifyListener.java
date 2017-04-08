package mil.navy.takingover.view.tab.tippack;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import mil.navy.takingover.MainApp;
import mil.navy.takingover.util.Notification.DesktopNotify;
import mil.navy.takingover.util.file.ModifyHandler;
import mil.navy.takingover.view.logview.LogStage;

public class TipPackModifyListener extends Thread{
	
	
	MainApp mainApp;
	boolean modify = false;
	
	public TipPackModifyListener(MainApp mainApp) {
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
		
		if(ModifyHandler.isCurrentModify(TipLayoutController.fileName)){
			modify = true;
		}
		else{
			if(modify){
				modify = false;
				Platform.runLater(() ->{
					LogStage.append("팁 데이터의 변화가 감지되어 로드하였습니다.");
					mainApp.showNotification("데이터가 변경되었습니다.", "팁 테이블의 데이터 변화가 감지되어 로드하였습니다.", DesktopNotify.INFORMATION, 5000L);
					mainApp.getRootLayoutController().getTipLayoutController().loadDataFromFile();
				});
			}
		}
		
	}
}
