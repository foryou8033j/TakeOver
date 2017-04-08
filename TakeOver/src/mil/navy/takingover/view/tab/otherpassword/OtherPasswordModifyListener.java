package mil.navy.takingover.view.tab.otherpassword;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import mil.navy.takingover.MainApp;
import mil.navy.takingover.util.file.ModifyHandler;
import mil.navy.takingover.view.logview.LogStage;

public class OtherPasswordModifyListener extends Thread{
	
	
	MainApp mainApp;
	boolean modify = false;
	
	public OtherPasswordModifyListener(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	@Override
	public void run() {
		
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {

				if(mainApp.uncMode) doAction();

			}
		}, 200, 300);
		
		
	}
	
	private void doAction(){
		
		if(ModifyHandler.isCurrentModify(mainApp.getRootLayoutController().getOtherPasswordLayoutController().fileName)){
			modify = true;
		}
		else{
			if(modify){
				modify = false;
				Platform.runLater(() ->{
					LogStage.append("기타 패스워드 데이터의 변화가 감지되어 로드하였습니다.");
					mainApp.getRootLayoutController().getOtherPasswordLayoutController().loadDataFromFile();
				});
			}
		}
		
	}
}
