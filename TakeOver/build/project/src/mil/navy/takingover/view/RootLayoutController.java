package mil.navy.takingover.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import mil.navy.takingover.MainApp;

public class RootLayoutController implements Initializable{

	@FXML
	Tab takeOverTab;
	
	@FXML
	Tab officerPasswordTab;
	
	@FXML
	MenuItem fullScreen;
	
	@FXML
	MenuItem close;
	
	@FXML
	MenuItem help;
	
	@FXML
	MenuItem about;
	
	
	/**
	 * 전체화면을 한다.
	 */
	@FXML
	private void handleFullScreen()
	{
		if(mainApp.getPrimaryStage().isFullScreen())
		{
			fullScreen.setText("전체화면 전환");
			mainApp.getPrimaryStage().setFullScreen(false);
		}
		else
		{
			fullScreen.setText("창모드 전환");
			mainApp.getPrimaryStage().setFullScreen(true);
		}
	}
	
	/**
	 * 종료한다.
	 */
	@FXML
	private void handleClose()
	{
		if(mainApp.getHeadLayoutController() != null)
		{
			if(mainApp.getHeadLayoutController().curSelectedData != null)
			{
				mainApp.getHeadLayoutController().curSelectedData.saveDataToFile();
				System.out.println("saved!");
			}
		}
		
		System.out.println("도구를 종료합니다.");
		System.exit(0);
	}
	
	/**
	 * 도움말을 띄운다.
	 */
	@FXML
	private void handleHelp()
	{
		
	}
	
	/**
	 * About을 띄워준다.
	 */
	@FXML
	private void handleAbout()
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(mainApp.title);
		alert.setHeaderText(mainApp.title + " v1.0");
		alert.initOwner(mainApp.getPrimaryStage());
		alert.setContentText("ㅎㅎ");
		alert.show();
	}
	

	private MainApp mainApp;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public RootLayoutController() {
		// TODO Auto-generated constructor stub
	}
	
	public Tab getTakeOverTab()
	{
		return takeOverTab;
	}
	
	public void setMainApp(MainApp mainApp)
	{
		this.mainApp = mainApp;
		fullScreen.setText("전체화면 전환");
		
		
	}
	
}
