package mil.navy.takingover.model;

import java.awt.AWTException;
import java.awt.Robot;
import java.time.LocalDate;

import javafx.application.Platform;
import mil.navy.takingover.MainApp;

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
					
					//loadAndWait();
				}
			});
			try {
				new Robot().delay(MainApp.minute * 30);
			} catch (AWTException e) {
				// TODO Auto-generated catch block
//				/e.printStackTrace();
			}
		}
		
		
		
	}
	
	
	private synchronized void loadAndWait()
	{
		try
		{
			if(mainApp.getHeadLayoutController() != null)
			{
				if(mainApp.getHeadLayoutController().curSelectedData != null)
				{
					if(mainApp.ableToLoad)
					{
						mainApp.getHeadLayoutController().reLoad();
						System.out.println(loop + " loaded!");
						loop ++;
					}
				}
			}
			

		}catch (Exception e)
		{
			//ignore
		}
	}
	
}
