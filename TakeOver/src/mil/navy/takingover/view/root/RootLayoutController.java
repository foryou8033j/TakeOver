package mil.navy.takingover.view.root;

import java.awt.Robot;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mil.navy.takingover.MainApp;
import mil.navy.takingover.util.ExceptionDialog;
import mil.navy.takingover.util.Regedit;
import mil.navy.takingover.view.logview.LogStage;
import mil.navy.takingover.view.root.option.directoryselector.DirectorySelectorLayoutController;
import mil.navy.takingover.view.root.option.passwordchange.PasswordChangeDialog;
import mil.navy.takingover.view.root.option.tabselector.TabSelectorDialog;
import mil.navy.takingover.view.root.option.titlechange.TitleChangeDialog;
import mil.navy.takingover.view.tab.officerIdentity.OfficerIdentityLayoutController;
import mil.navy.takingover.view.tab.officergetout.GetOffLayoutController;
import mil.navy.takingover.view.tab.otherpassword.OtherPasswordLayoutController;
import mil.navy.takingover.view.tab.phone.PhoneLayoutController;
import mil.navy.takingover.view.tab.securitycheck.SecurityCheckLayoutController;
import mil.navy.takingover.view.tab.takeover.HeadLayoutController;
import mil.navy.takingover.view.tab.tippack.TipLayoutController;

public class RootLayoutController implements Initializable{

	public enum NoticeType {ALERT, INFORMATION};
	
	@FXML
	Tab takeOverTab;
	
	@FXML
	Tab officerPasswordTab;
	
	@FXML
	Tab otherPasswordTab;
	
	@FXML
	Tab tipTap;
	
	@FXML
	Tab phoneTab;
	
	@FXML
	Tab getOffTab;
	
	@FXML
	Tab securityTab;
	
	@FXML
	MenuItem changeTitle;
	
	@FXML
	MenuItem fullScreen;
	
	@FXML
	MenuItem useTabs;
	
	@FXML
	MenuItem changePassword;
	
	@FXML
	MenuItem remove;
	
	@FXML
	MenuItem selectDirectory;
	
	@FXML
	CheckMenuItem logView;
	
	@FXML
	MenuItem close;
	
	@FXML
	CheckMenuItem uncMode;
	
	@FXML
	MenuItem about;
	
	
	HeadLayoutController headLayoutcontroller;
	OfficerIdentityLayoutController officerIdentityLayoutController;
	OtherPasswordLayoutController otherPasswordLayoutController;
	PhoneLayoutController phoneLayoutController;
	TipLayoutController tipLayoutController;
	GetOffLayoutController getOffLayoutController;
	
	
	
	/**
	 * 전체화면을 한다.
	 */
	@FXML
	private void handleFullScreen()
	{
		if(mainApp.getPrimaryStage().isFullScreen())
			mainApp.getPrimaryStage().setFullScreen(false);
		else
			mainApp.getPrimaryStage().setFullScreen(true);
	}
	
	/**
	 * 종료한다.
	 */
	@FXML
	private void handleClose()
	{
		mainApp.shutDownProcess();
	}
	
	/**
	 * 경로 설정 창을 띄운다.
	 */
	@FXML
	private void handleShowDirectorySelector()
	{
		
		if(mainApp.showPasswordDialog()){
			try{
				FXMLLoader loader = new FXMLLoader(this.getClass().getResource("option/directoryselector/DirectorySelectorLayout.fxml"));
				GridPane pane = (GridPane) loader.load();
				
				DirectorySelectorLayoutController controller = loader.getController();
				
				
				Stage stage = new Stage();
				stage.setTitle("경로 지정");
				stage.setResizable(false);
				stage.getIcons().add(mainApp.icon);
				stage.initOwner(mainApp.getPrimaryStage());
				stage.initModality(Modality.WINDOW_MODAL);
				
				Scene scene = new Scene(pane);
				
				controller.setMainApp(mainApp, stage);
				
				stage.setScene(scene);
				stage.show();
				
			}catch (Exception e)
			{
				LogStage.append("[주의] 경로 설정 창을 초기화 하던 도중 오류가 발생했습니다." + e.getMessage());
				e.printStackTrace();
			}
		}
		
	}
	
	@FXML
	private void handleChangeTitle(){
		if(mainApp.showPasswordDialog())
			new TitleChangeDialog(mainApp).showAndWait();
	}
	
	@FXML
	private void handleTabVisibler(){
		if(mainApp.showPasswordDialog())
			new TabSelectorDialog(mainApp).showAndWait();

	}
	
	@FXML
	private void handlePasswordChange(){
		new PasswordChangeDialog(mainApp).showAndWait();
	}
	
	@FXML
	private void handleRemoveAllData(){
		
		Alert alert = new Alert(AlertType.WARNING,"", ButtonType.OK, ButtonType.NO);
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		alertStage.getIcons().add(mainApp.icon);
		alert.setTitle("데이터 설정 초기화");
		alert.setHeaderText("정말 기존 데이터 설정을 초기화 하시겠습니까?");
		alert.setContentText("데이터 삭제 후 도구를 종료 합니다.");
		Optional<ButtonType> result = alert.showAndWait();
		
		if(result.get().equals(ButtonType.OK)){
			if(mainApp.showPasswordDialog()){
				try {
					Regedit.enableRegistryEditor();
					
					String command[] = { "cmd", "/c", "reg", "delete", "HKEY_CURRENT_USER\\Software\\JavaSoft\\Prefs\\mil\\navy\\takingover", "/f" };
					Runtime runtime = Runtime.getRuntime();
					Process process = runtime.exec(command);
					BufferedReader br1 = new BufferedReader(new InputStreamReader(process.getInputStream(), "EUC-KR"));
					
					String line = "";
						
					while ((line = br1.readLine()) != null){
						LogStage.append("[Runtime] " + line);
					}
					LogStage.append("기존 데이터를 모두 삭제 했습니다.");
					
					mainApp.shutDownProcess();
				} catch (IOException e) {
					
				}
			}
		}else{
			
		}
		
	}
	
	
	@FXML
	private void handleUNCMode(){
		if(uncMode.isSelected()){
			Regedit.setRegistry("uncMode", uncMode.isSelected());
			LogStage.append("UNC모드를 시작합니다.");
		}
			
		else{
			Regedit.setRegistry("uncMode", uncMode.isSelected());	
			LogStage.append("UNC모드를 중지합니다.");
		}
			
		
		mainApp.uncMode = uncMode.isSelected();
	}
	
	@FXML
	private void handleLogView(){
		
		if(logView.isSelected()){
			mainApp.getLogView().setX(mainApp.getPrimaryStage().getX() + mainApp.getPrimaryStage().getWidth());
			mainApp.getLogView().setY(mainApp.getPrimaryStage().getY());
			mainApp.getLogView().show();
		}
		else{
			mainApp.getLogView().close();
		}
	}
	
	/**
	 * About을 띄워준다.
	 */
	@FXML
	private void handleAbout()
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(mainApp.title);
		alert.setHeaderText("통합 인수인계 도구" + " v1.4.1");
		alert.initOwner(mainApp.getPrimaryStage());
		alert.setContentText("Copyrightⓒ 2017 All rights reserved by 병장 서정삼\n");
		alert.show();
	}
	
	/**
	 * head Layout을 로드한다.
	 */
	private void initHeadLayout()
	{
		try
		{
			LogStage.append("인수인계 탭을 초기화합니다.");
			FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource("mil/navy/takingover/view/tab/takeover/HeadLayout.fxml"));
			BorderPane layout= (BorderPane) loader.load();
			
			headLayoutcontroller = loader.getController();
			headLayoutcontroller.setMainApp(mainApp);
			
			takeOverTab.setContent(layout);
		}catch (Exception e)
		{
			LogStage.append("[경고] 인수인계 탭 부분을 로드 하던 도중 오류가 발생하였습니다. " + e.getMessage());
			e.printStackTrace();
			new ExceptionDialog(AlertType.ERROR, "오류", "오류가 발생했습니다.", "Head 레이아웃을 로드하던 도중 오류가 발생 했습니다.", e)
					.showAndWait();
			System.exit(0);
		}
		
	}
	
	/**
	 * 패스워드 탭을 로드한다.
	 */
	private void initOfficerPasswordLayout()
	{
		try
		{
			LogStage.append("간부 패스워드 탭을 초기화합니다.");
			FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource("mil/navy/takingover/view/tab/officerIdentity/OfficerIdentityLayout.fxml"));
			AnchorPane layout= (AnchorPane) loader.load();
			
			officerIdentityLayoutController = loader.getController();
			officerIdentityLayoutController.setMainApp(mainApp);
			
			officerPasswordTab.setContent(layout);
		}catch (Exception e)
		{
			LogStage.append("[경고] 간부패스워드 탭 부분을 로드 하던 도중 오류가 발생하였습니다. " + e.getMessage());
			e.printStackTrace();
			new ExceptionDialog(AlertType.ERROR, "오류", "오류가 발생했습니다.", "OfficerPassword 레이아웃을 로드하던 도중 오류가 발생 했습니다.", e)
					.showAndWait();
			System.exit(0);
		}
	}
	
	/**
	 * 패스워드 탭을 로드한다.
	 */
	private void initOtherPasswordLayout()
	{
		try
		{
			LogStage.append("기타 패스워드 탭을 초기화합니다.");
			FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource("mil/navy/takingover/view/tab/otherpassword/OtherPasswordLayout.fxml"));
			BorderPane layout= (BorderPane) loader.load();
			
			otherPasswordLayoutController = loader.getController();
			otherPasswordLayoutController.setMainApp(mainApp);
			
			otherPasswordTab.setContent(layout);
		}catch (Exception e)
		{
			LogStage.append("[경고] 기타패스워드 탭 부분을 로드 하던 도중 오류가 발생하였습니다. " + e.getMessage());
			e.printStackTrace();
			new ExceptionDialog(AlertType.ERROR, "오류", "오류가 발생했습니다.", "기타 패스워드 탭 레이아웃을 초기화 하던 도중 오류가 발생 했습니다.", e)
					.showAndWait();
			System.exit(0);
		}
	}
	
	/**
	 * 기타 팁 탭을 로드한다.
	 */
	private void initTipLayout()
	{
		try
		{
			LogStage.append("기타 팁 탭을 초기화합니다.");
			FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource("mil/navy/takingover/view/tab/tippack/TipLayoutController.fxml"));
			BorderPane layout= (BorderPane) loader.load();
			
			tipLayoutController = loader.getController();
			tipLayoutController.setMainApp(mainApp);
			
			tipTap.setContent(layout);
		}catch (Exception e)
		{
			LogStage.append("[경고] 기타 팁 탭 부분을 로드 하던 도중 오류가 발생하였습니다. " + e.getMessage());
			e.printStackTrace();
			new ExceptionDialog(AlertType.ERROR, "오류", "오류가 발생했습니다.", "팁 레이아웃을 초기화 하던 도중 오류가 발생 했습니다.", e)
					.showAndWait();
			System.exit(0);
		}
	}
	
	/**
	 * 전화번호 탭을 로드한다.
	 */
	private void initPhoneLayout()
	{
		try
		{
			LogStage.append("전화번호 탭을 초기화합니다.");
			FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource("mil/navy/takingover/view/tab/phone/PhoneLayout.fxml"));
			AnchorPane layout= (AnchorPane) loader.load();
			
			phoneLayoutController = loader.getController();
			phoneLayoutController.setMainApp(mainApp);
			
			phoneTab.setContent(layout);
		}catch (Exception e)
		{
			LogStage.append("[경고] 전화번호 탭 부분을 로드 하던 도중 오류가 발생하였습니다. " + e.getMessage());
			e.printStackTrace();
			new ExceptionDialog(AlertType.ERROR, "오류", "오류가 발생했습니다.", "전화번호 모음 레이아웃을 로드하던 도중 오류가 발생 했습니다.", e)
					.showAndWait();
			System.exit(0);
		}
	}
	
	/**
	 * 기무 대비 탭을 로드한다.
	 */
	private void initSecurityLayout()
	{
		try
		{
			LogStage.append("기무대비 탭을 초기화합니다.");
			FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource("mil/navy/takingover/view/tab/securitycheck/SecurityCheckLayout.fxml"));
			AnchorPane layout= (AnchorPane) loader.load();
			
			SecurityCheckLayoutController controller = loader.getController();
			controller.setMainApp(mainApp);
			
			securityTab.setContent(layout);
		}catch (Exception e)
		{
			LogStage.append("[경고] 기무 대비 탭 부분을 로드 하던 도중 오류가 발생하였습니다. " + e.getMessage());
			e.printStackTrace();
			new ExceptionDialog(AlertType.ERROR, "오류", "오류가 발생했습니다.", "기무대비 모음 레이아웃을 로드하던 도중 오류가 발생 했습니다.", e)
					.showAndWait();
			System.exit(0);
		}
	}
	
	/**
	 * 관찰 탭을 로드한다.
	 */
	private void initGetOffLayout()
	{
		try
		{
			LogStage.append("PC 관찰 탭을 초기화합니다.");
			FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource("mil/navy/takingover/view/tab/officergetout/GetoffLayout.fxml"));
			BorderPane layout= (BorderPane) loader.load();
			
			getOffLayoutController = loader.getController();
			getOffLayoutController.setMainApp(mainApp);
			
			getOffTab.setContent(layout);
		}catch (Exception e)
		{
			LogStage.append("[경고] PC 관찰 탭 부분을 로드 하던 도중 오류가 발생하였습니다. " + e.getMessage());
			e.printStackTrace();
			new ExceptionDialog(AlertType.ERROR, "오류", "오류가 발생했습니다.", "PC 관찰 탭 레이아웃을 초기화 하던 도중 오류가 발생 했습니다.", e).showAndWait();
			System.exit(0);
		}
	}
	
	public void setTabVisibleFromRegistry(){
		
		takeOverTab.setDisable(!Regedit.getBooleanValue("takeovertab"));
		officerPasswordTab.setDisable(!Regedit.getBooleanValue("officerpasswordtab"));
		otherPasswordTab.setDisable(!Regedit.getBooleanValue("otherpasswordtab"));
		phoneTab.setDisable(!Regedit.getBooleanValue("phonetab"));
		tipTap.setDisable(!Regedit.getBooleanValue("tiptab"));
		securityTab.setDisable(!Regedit.getBooleanValue("securitytab"));
		getOffTab.setDisable(!Regedit.getBooleanValue("getofftab"));
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
	
	public Tab getOfficerPasswordTab()
	{
		return officerPasswordTab;
	}
	
	public Tab getOtherPasswordTab()
	{
		return otherPasswordTab;
	}
	
	public Tab getTipTab()
	{
		return tipTap;
	}
	
	public Tab getPhoneTab()
	{
		return phoneTab;
	}
	
	public Tab getGetOffTab()
	{
		return getOffTab;
	}
	
	public HeadLayoutController getHeadLayoutController()
	{
		return headLayoutcontroller;
	}
	
	public OfficerIdentityLayoutController getOfficeIdentityLayoutController(){
		return officerIdentityLayoutController;
	}
	
	public OtherPasswordLayoutController getOtherPasswordLayoutController(){
		return otherPasswordLayoutController;
	}
	
	public TipLayoutController getTipLayoutController(){
		return tipLayoutController;
	}
	
	public PhoneLayoutController getPhoneLayoutController(){
		return phoneLayoutController;
	}
	
	public GetOffLayoutController getGetOffLayoutController(){
		return getOffLayoutController;
	}
	
	public void setMainApp(MainApp mainApp)
	{
		this.mainApp = mainApp;
		fullScreen.setText("전체화면 전환");
		
		/**
		 * 로그뷰의 상태를 리스닝하여, 로그뷰 show/close CheckMenuItem 의 상태를 결정한다.
		 */
		mainApp.getLogView().showingProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) logView.setSelected(true);
				else logView.setSelected(false);
			}
		});
		
		/**
		 * 전체화면 상태를 리스닝하고 텍스트를 변경한다.
		 */
		mainApp.getPrimaryStage().fullScreenProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue.booleanValue())
					fullScreen.setText("창모드 전환");
				else
					fullScreen.setText("전체화면 전환");
			}
		});

		uncMode.setSelected(Regedit.getBooleanValue("uncMode"));
		
		//Haed Layout 을 초기화 한다.
		initHeadLayout();

		//기타 로그인 정보 탭을 초기화 한다.
		initOtherPasswordLayout();
		
		//부직사관 패스워드 탭을 초기화 한다.
		initOfficerPasswordLayout();
		
		initTipLayout();
		
		initSecurityLayout();
		
		initPhoneLayout();
		
		//관찰 탭을 초기화 한다.
		initGetOffLayout();
		
		setTabVisibleFromRegistry();
		
	}
	
}
