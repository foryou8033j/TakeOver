package mil.navy.takingover.view.tab.officergetout;

import java.net.InetAddress;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import mil.navy.takingover.MainApp;
import mil.navy.takingover.util.IcmpProtocol;
import mil.navy.takingover.view.logview.LogStage;

public class GetOffContentLayoutController {

	
	@FXML
	Text name;
	
	@FXML
	Text ip;
	
	@FXML
	Text ping;
	
	@FXML
	Text status;
	
	@FXML
	ProgressIndicator progress;
	
	private MainApp mainApp;
	
	Timer timer;
	Timer timerProgress;
	
	long startTime;
	long endTime;
	
	long startDelayTime;
	long endDelayTime;
	
	long delay = 2000;
	long period = (1000 * 60) * 10;
	
	public GetOffContentLayoutController() {
		// TODO Auto-generated constructor stub
	}
	
	public void setContents(String name, String ip)
	{
		this.name.setText(name);
		this.ip.setText(ip);
	}
	
	
	public void setMainApp(MainApp mainApp)
	{
		this.mainApp = mainApp;
		ping.setText("checking...");
		status.setText("확인중..");
		progress.setProgress(-1);
		
		setStatus();
		
	}
	
	private void setStatus()
	{
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				startDelayTime = System.currentTimeMillis();
				endDelayTime = startDelayTime + period;
				try
				{
					startTime = System.nanoTime();
					if(checkPing())
					{
						endTime = System.nanoTime();
						long result = (endTime - startTime) / 1000000;
						if(result > 2500)
						{
							ping.setText(String.valueOf(result) + " ms.");
							status.setFill(Paint.valueOf("black"));
							status.setText("퇴 근?");
							Platform.runLater(new Runnable() {
								
								@Override
								public void run() {
									progress.setProgress(0.5);
								}
							});
						}
						else
						{
							status.setFill(Paint.valueOf("red"));
							status.setText("근무중");
							ping.setText(String.valueOf(result) + " ms.");
							Platform.runLater(new Runnable() {
								
								@Override
								public void run() {
									progress.setProgress(-1);
								}
							});
						}
					}
					else
					{
						status.setFill(Paint.valueOf("black"));
						status.setText("퇴 근");
						Platform.runLater(new Runnable() {
							
							@Override
							public void run() {
								progress.setProgress(1.0);
							}
						});
						ping.setText("Connection Fail.");
						
					}
					
					
				}catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			
			
			
		},delay, period);
		
		
	}
	
	private boolean checkPing()
	{
		boolean reachAble = IcmpProtocol.isReachable(ip.getText());
		
		if(reachAble){
			System.out.println("[PingCheck] " + ip.getText() + " Connection OK");
			LogStage.append("[PingCheck] " +ip.getText() + " Connection OK");
			return true;
		}else
		{
			System.out.println("[PingCheck] " +ip.getText() + " Connection Fail");
			LogStage.append("[PingCheck] " +ip.getText() + " Connection Fail");
			return false;
		}
	}
	
	public void close()
	{
		timer.cancel();
		timer = null;
	}
	
	
	
}
