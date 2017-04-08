package mil.navy.takingover.util;

import java.awt.AWTException;
import java.awt.Robot;
import java.time.LocalDate;

import javafx.application.Platform;
import mil.navy.takingover.MainApp;

/**
 * 오늘 날짜의 인수인계 데이터 생성 여부를 지속적으로 확인하는 쓰레드
 * @version 1.0
 * @author 병장 서정삼
 *
 */
public class FileChecker extends Thread{

	private LocalDate appOpenDay;
	
	private MainApp mainApp;
	private int loop=0;
	
	public FileChecker(MainApp mainApp) {
		this.mainApp = mainApp;
		
		appOpenDay = LocalDate.now();
	}
	
	@Override
	public void run() {
		
		while(true)
		{
			
			Platform.runLater(new Runnable() {
				
				@Override
				public void run() {
					//프로그램을 실행한 날짜와 현재 시간이 다르면
					if(LocalDate.now().getDayOfMonth() != appOpenDay.getDayOfMonth())
						mainApp.isAbleNewFileToDay(); //다음날 인수인계를 생성한다.
					
				}
			});
			try {
				new Robot().delay(MainApp.minute * 30);
			} catch (AWTException e) {
				//ignore
			}
		}
		

	}
	
	
}
