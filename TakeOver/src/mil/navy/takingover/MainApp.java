package mil.navy.takingover;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;
import java.util.prefs.Preferences;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import mil.navy.takingover.model.Data;
import mil.navy.takingover.model.TakeOverData;
import mil.navy.takingover.model.getoff.GetOffData;
import mil.navy.takingover.model.officerIdentity.OfficerIdentity;
import mil.navy.takingover.model.otherpassword.OtherPassword;
import mil.navy.takingover.model.phone.PhoneData;
import mil.navy.takingover.model.tip.TipData;
import mil.navy.takingover.util.ExceptionDialog;
import mil.navy.takingover.util.FileChecker;
import mil.navy.takingover.util.Regedit;
import mil.navy.takingover.util.Notification.DesktopNotify;
import mil.navy.takingover.util.file.FileHandler;
import mil.navy.takingover.util.file.FileNameConverter;
import mil.navy.takingover.util.file.ModifyHandler;
import mil.navy.takingover.view.loading.LoadLayout;
import mil.navy.takingover.view.loading.LoadLayoutController;
import mil.navy.takingover.view.logview.LogStage;
import mil.navy.takingover.view.password.PasswordInputDialog;
import mil.navy.takingover.view.root.RootLayoutController;
import mil.navy.takingover.view.root.option.directoryselector.DirectorySelectorLayoutController;
import mil.navy.takingover.view.startmanager.ManagerStage;
import mil.navy.takingover.view.tab.officerIdentity.OfficerIdentityLayoutController;
import mil.navy.takingover.view.tab.officergetout.GetOffLayoutController;
import mil.navy.takingover.view.tab.otherpassword.OtherPasswordLayoutController;
import mil.navy.takingover.view.tab.phone.PhoneLayoutController;
import mil.navy.takingover.view.tab.securitycheck.SecurityCheckLayoutController;
import mil.navy.takingover.view.tab.takeover.HeadLayoutController;
import mil.navy.takingover.view.tab.tippack.TipLayoutController;

/**
 * 인수인계 도구 메인 클래스<BR>
 * Root 레이아웃 초기화, 파일 입출력 제어
 * 인수인계 컨트롤러, 루트 컨트롤러 접근 제어
 * @since 2017. 1.19
 * @version 1.4.1
 * @author 병장 서정삼
 */
public class MainApp extends Application {

	public String title = "";

	private Stage primaryStage; // 기본 스테이지를 지정한다.
	private BorderPane rootLayout;	 // 루트레이아웃을 지정한다.

	
	/**
	 * Root 컨트롤러를 지정한다.
	 * @see HeadLayoutController
	 * @see OfficerIdentityLayoutController
	 * @see OtherPasswordLayoutController
	 * @see PhoneLayoutController
 	 * @see TipLayoutController
	 * @see SecurityCheckLayoutController
	 * @see GetOffLayoutController
	 */
	private RootLayoutController rootLayoutController;	//Root 컨트롤러를 지정한다.
	
	/**
	 * 인수인계 탭의 컨트롤러를 지정한다.
	 * @see HeadLayoutController
	 * {@link RootLayoutController} 로 부터 호출된다.
	 */
	private HeadLayoutController headLayoutcontroller;	//인수인계 컨트롤러를 지정한다.

	public Image icon = null;	//기본 Icon
	/**
	 * @deprecated
	 */
	public Image pingOk = null;	//핑이 정상적일때 보여지는 이미지
	/**
	 * @deprecated
	 */
	public Image pingFail = null;	//핑이 비정상일때 보여지는 이미지

	public Image emphasisImage = null;
	public Image continueImage = null;
	public Image completeImage = null;

	
	//파일 입출력이 동시에 발생하지 않도록 제어하는 변수
	public boolean ableToLoad = false;

	/**
	 * 로그 스테이지를 정의한다. <BR>
	 * 전역에서 접근이 가능하도록 @see 
	 * @see LogStage
	 */
	private LogStage logStage = null;
	
	// UNC 모드 사용 여부를 결정한다.
	public static boolean uncMode = false;

	// UNC 서버에서 끌어올 데이터가 있는 경로
	public static String uncPath = "";

	// UNC 서버에서 끌어온 데이터가 저장 될 경로
	public static String path = "C:" + File.separator + "TakeOverTempFiles" + File.separator;

	// 인수인계 목록들이 저장되어 있는 리스트
	private ObservableList<Data> datas = FXCollections.observableArrayList();

	// 패스워드 목록이 저장되어 있는 리스트
	private ObservableList<OfficerIdentity> passwords = FXCollections.observableArrayList();

	// 기타 패스워드 목록이 저장되어 있는 리스트
	private ObservableList<OtherPassword> otherPasswords = FXCollections.observableArrayList();

	// 팁 목록이 저장되어 있는 리스트
	private ObservableList<TipData> tipDatas = FXCollections.observableArrayList();

	// 전화번호 목록이 저장되어 있는 리스트
	private ObservableList<PhoneData> phoneData = FXCollections.observableArrayList();

	// 관찰 대상 PC 목록이 저장되어 있는 리스트
	private ObservableList<GetOffData> getOffData = FXCollections.observableArrayList();

	// 인수인계 파일 목록이 저장되어 있는 리스트
	ObservableList<File> fileList;

	public static int second = 60;
	public static int minute = 600;
	public static int hour = 3600;
	

	@Override
	public void start(Stage primaryStage) throws IOException {
		this.primaryStage = primaryStage;
		
		this.icon = new Image("takeOverIcon.png");
		this.pingOk = new Image("pcon.png");
		this.pingFail = new Image("pcoff.png");
		this.continueImage = new Image("continueImg.png");

		this.logStage = new LogStage(primaryStage, "로그 뷰");
		LogStage.append("로그뷰 초기화");

		//레지스트리로 부터 저장된 데이터를 가져온다.
		setDataFromRegistry();
		
		//패스워드가 있으면 패스워드 입력창을 띄운다.
		if(!showPasswordDialog())
			shutDownProcess();
		
		//최초 실행인지 확인
		checkFirstStart();
		
		//인수인계 툴을 초기화한다.
		initTakeOverTool();

		// 파일 변경 사항을 유지하는 쓰레드
		new FileChecker(this).start();

	}

	private void checkFirstStart(){
		if(isFirstStart()){
			new ManagerStage(this).showAndWait();
		}
	}
	
	public boolean showPasswordDialog(){
		if(!Regedit.getStringValue("password").equals("")){
			PasswordInputDialog password = new PasswordInputDialog(this);
			password.showAndWait();
			
			return password.isPass();
		}else{
			return true;
		}
	}
	

	/**
	 * 인수인계 도구를 초기화 한다.<BR><BR>
	 * 
	 * 1. UNC 접속 확인<BR>
	 * 2. 파일 목록 확인<BR>
	 * 3. 파일 목록 갱신<BR>
	 * 4. 레이아웃 초기화<BR>
	 * @version 1.2.0
	 */
	private void initTakeOverTool(){
		
		LoadLayout worker = new LoadLayout("인수인계 도구를 초기화하는 중...", getPrimaryStage());

		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				LogStage.append("인수인계 도구 초기화 시작");

				// UNC서버에서 끌어올 폴더가 없으면 새로 생성한다.
				File file = new File(path);
				file.delete();
				if (!file.exists())
					file.mkdir();

				LogStage.append("UNC 접속 확인...");
				worker.setContents("UNC 접속 확인 중...");
				// 눈속임용 ㅎㅎ
				for (int i = 0; i < 30; i++) {
					updateProgress(i, 100);
					Thread.sleep(new Random().nextInt(10));
				}
				doDirectoryCheck(); // 원격 서버 접속 확인용 명령 -> 여기서 Exception 발생 시 관제
									// 툴 종료 -> 경로 재지정 모드

				LogStage.append("파일 목록 확인...");
				worker.setContents("파일 목록 확인 중...");
				for (int i = 30; i < 60; i++) {
					updateProgress(i, 100);
					Thread.sleep(new Random().nextInt(25));
				}
				loadFileList(); // uncPath로부터 인수인계 파일 목록을 확인한다.

				LogStage.append("파일 목록 갱신...");
				worker.setContents("파일 갱신 중...");
				for (int i = 60; i < 80; i++) {
					updateProgress(i, 100);
					Thread.sleep(new Random().nextInt(50));
				}
				updateProgress(-1, 100);
				isAbleNewFileToDay(); // 오늘 파일 생성이 가능한지 확인하고 기존에 인계파일이 존재하지 않으면
										// 새로 생성한다.

				LogStage.append("레이아웃 초기화...");
				worker.setContents("레이아웃 초기화중...");
				for (int i = 80; i <= 100; i++) {
					updateProgress(i, 100);
					Thread.sleep(new Random().nextInt(80));
				}
				// updateProgress(-1, 100);
				initStage(); // Stage를 초기화한다.

				return null;
			}
		};

		worker.activateProgress(task);

		task.setOnSucceeded(Event -> {
			worker.close();
			LogStage.append("인수인계 도구 초기화 완료");
		});

		Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();
		
	}
	
	/**
	 * 종료 시 동작하는 메소드
	 */
	public void shutDownProcess(){
		if (getHeadLayoutController() != null) {
			if (getSelectedTakeOverData() != null) {
				// 수정이 끝난걸로 가정하고 데이터 수정중임을 종료한다.
				String fileName = FileNameConverter.toString(getSelectedTakeOverData().getLocalDate()) + ".xml";
				ModifyHandler.removeModify(fileName);

				getSelectedTakeOverData().saveDataToFile();
			}
		}

		File localFolder = new File(path);
		FileHandler.Delete(localFolder.getPath());

		LogStage.append("도구를 종료합니다.");
		System.exit(0);
	}
	
	/**
	 * 스테이지를 초기화한다.
	 */
	private void initStage() {
		primaryStage.setTitle(title + " [" + uncPath + "]");
		primaryStage.getIcons().add(icon);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				shutDownProcess();
			}
		});
		primaryStage.setMinWidth(1100);
		primaryStage.setMinHeight(650);

		// 레지스트리로부터 기본 위치 X를 받는다.
		if (Regedit.getDoubleValue("posX") == null || Regedit.getDoubleValue("posX") < 0)
			Regedit.setRegistry("posX", 200);
		else
			primaryStage.setX(Regedit.getDoubleValue("posX"));

		// 레지스트리로부터 기본 위치 Y를 받는다.
		if (Regedit.getDoubleValue("posY") == null || Regedit.getDoubleValue("posY") < 0)
			Regedit.setRegistry("posY", 200);
		else
			primaryStage.setY(Regedit.getDoubleValue("posY"));

		// Stage의 기본 위치 X를 레지스트리로 저장한다.
		primaryStage.xProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				Regedit.setRegistry("posX", newValue.doubleValue());
			}
		});

		// Stage의 기본 위치 Y를 레지스트리로 저장한다.
		primaryStage.yProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				Regedit.setRegistry("posY", newValue.doubleValue());
			}
		});

		// Root 레이아웃을 초기화한다
		initRootLayout();
	}

	/**
	 * Root 레이아웃을 초기화한다.
	 */
	private void initRootLayout() {
		LogStage.append("Root 레이아웃을 초기화합니다.");
		Platform.runLater(() -> {

			try {

				FXMLLoader loader = new FXMLLoader(this.getClass().getResource("view/root/RootLayout.fxml"));
				rootLayout = (BorderPane) loader.load();

				rootLayoutController = loader.getController();
				rootLayoutController.setMainApp(this);
				headLayoutcontroller = rootLayoutController.getHeadLayoutController();

				Scene scene = new Scene(rootLayout);
				primaryStage.setScene(scene);
				primaryStage.show();

			} catch (Exception e) {
				LogStage.append("[오류] Root 레이아웃을 로드하던 도중 오류가 발생했습니다. " + e.getMessage());
				e.printStackTrace();
				new ExceptionDialog(AlertType.ERROR, "오류", "오류가 발생했습니다.", "Root 레이아웃을 로드하던 도중 오류가 발생 했습니다.",
						e.getMessage()).showAndWait();
				System.exit(0);
			}
		});

	}
	
	
	/**
	 * 오늘 새로운 인수인계를 만들어도 되는지 확인한다. 만들 수 있으면 만든다.
	 */
	public void isAbleNewFileToDay() {
		LogStage.append("오늘 날짜의 인수인계 데이터가 있는지 확인");
		for (File file : fileList)
			if (LocalDate.now().equals(FileNameConverter.toDate(file.getName())))
				return;
		// 오늘 날짜의 파일이 있으면 함수를 탈출

		// 없다고 가정하고 오늘 날짜 파일 생성 작업 시작.
		Data tmpData = new Data(LocalDate.now());
		
		//어제 날짜 데이터에서 진행사항이 있으면 가져온다.
		tmpData = makeContinueDataFromYesterDay(tmpData);
		
		String fileName = FileNameConverter.toString(tmpData.getLocalDate()) + ".xml";
		tmpData.saveDataToFile(new File(MainApp.path + fileName));

		datas.add(tmpData);

		loadFileList();

		LogStage.append("오늘 날짜의 인수인계 데이터를 찾을 수 없습니다, 새로운 파일을 생성합니다.");

	}
	

	/**
	 * 오늘자 인수인계 데이터를 생성하면서 어제 데이터에서 <strong>진행사항</STRONG> 이 있으면 데이터를 가져온다.
	 * @param tmpData 가장 최근 데이터
	 * @return 진행사항 데이터를 복사한 결과 데이터
	 */
	private Data makeContinueDataFromYesterDay(Data tmpData){
		
		//가장 마지막에 있는 데이터를 어제 데이터라고 가정한다.
		try{
			
			Data yesterday;
			try{
				yesterday = datas.get(datas.size()-1);	
			}catch (Exception e){
				return tmpData;
			}
			
			//어제 데이터를 로드한다.
			String fileName = FileNameConverter.toString(yesterday.getLocalDate()) + ".xml";
			yesterday.loadDataFromFile(new File(MainApp.path + fileName));
			
			for(TakeOverData data:yesterday.getTakeOverData()){
				if(data.getContinue()){
					//'진행사항' 있으면 오늘 임시데이터에 추가한다.
					tmpData.getTakeOverData().add(data);
				}
			}
			
			return tmpData;
		}catch (Exception e){
			LogStage.append("[오류] 오늘 인수인계 데이터를 생성하는 도중 오류가 발생하였습니다.");
			e.printStackTrace();
		}
		
		return tmpData;
		
		
	}

	/**
	 * 파일 리스트를 읽는다
	 */
	public void loadFileList() {
		fileList = getFileListString(uncPath);
		fileListToData();
	}

	/**
	 * 파일 리스트로부터 파일 목록을 형성한다.
	 */
	public void fileListToData() {
		datas.clear();

		for (File file : fileList) {
			datas.add(new Data(FileNameConverter.toDate(file.getName())).setFile(file));
		}
	}

	/**
	 * 실제 경로로부터 파일 목록을 받는다.
	 * @param path 검색 대상 경로
	 * @return 검색 된 인수인계 데이터들
	 *	@see Data 인수인계 데이터
	 */
	private ObservableList<File> getFileListString(String path) {
		ObservableList<File> fileObservableList = FXCollections.observableArrayList();

		File dir = new File(path);
		File[] fileList = dir.listFiles();

		try {
			for (int i = 0; i < fileList.length; i++) {
				File file = fileList[i];
				if (file.isFile()) {
					// 파일 수정과 관련된 임시파일은 무시한다.
					if (file.getName().equals(ModifyHandler.modifyFileName))
						continue;
					// 패스워드 파일과 관련된 임시파일은 무시한다
					if (file.getName().equals(OfficerIdentityLayoutController.fileName))
						continue;
					// 접속 정보 파일과 관련된 임시파일은 무시한다.
					if (file.getName().equals(OtherPasswordLayoutController.fileName))
						continue;
					// 관찰 정보 파일과 관련된 임시파일은 무시한다.
					if (file.getName().equals(GetOffLayoutController.fileName))
						continue;

					if (file.getName().contains(FileNameConverter.name)) {
						fileObservableList.add(file);
						LogStage.append("데이터 파일 추가 " + path + file.getName());
					}
				} else if (file.isDirectory()) {
					fileObservableList.addAll(getFileListString(file.getAbsolutePath() + File.separator));
				}
			}
		} catch (Exception e) {
			// ignore
		}

		return fileObservableList;
	}
	
	
	
	
	
	

	/**
	 * 파일 환경설정을 반환한다. 즉 파일은 마지막으로 열린 것이고, 환경 설정은 OS 특정 레스트리로부터 읽는다. 만일
	 * preference 를 찾지 못하면 null을 반환한다.
	 * 
	 * @deprecated
	 * @return 기존 환경 파일 경로
	 */
	public File getDataFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		String filePath = prefs.get("filePath", null);
		if (filePath != null) {
			return new File(filePath);
		} else {
			return null;
		}
	}

	/**
	 * 현재 불러온 파일의 경로를 설정한다. 이 경로는 OS 특정 레지스트리에 저장된다.
	 * 
	 * @deprecated
	 * @param file 현재 불러온 파일의 경로
	 */
	public void setDataFilePath(File file) {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		if (file != null) {
			prefs.put("filePath", file.getPath());
			primaryStage.setTitle(title + " - " + file.getName());
		} else {
			prefs.remove("filePath");
			primaryStage.setTitle(title);
		}
	}

	/**
	 * 내 IP주소를 반환한다.
	 * 
	 * @return 현재 로컬 PC 의 InetAddress 주소
	 * @throws UnknownHostException
	 */
	public static String getMyIP() throws UnknownHostException {
		return Inet4Address.getLocalHost().getHostAddress();
	}

	/**
	 * 현재 인수인계 도구에서 선택되어 있는 인수인계 데이터를 반환한다.
	 * @return 현재 선택된 인수인계 데이터
	 * @see HeadLayoutController
	 * @see Data
	 */
	public Data getSelectedTakeOverData() {
		return headLayoutcontroller.getSelectedData();
	}

	/**
	 * 루트 레이아웃의 컨트롤러를 반환한다.
	 * 
	 * @return 루트 레이아웃의 컨트롤러
	 */
	public RootLayoutController getRootLayoutController() {
		return rootLayoutController;
	}

	/**
	 * 인수인계 탭의 컨트롤러를 반환한다.
	 * 
	 * @return 인수인계 컨트롤러
	 */
	public HeadLayoutController getHeadLayoutController() {
		return headLayoutcontroller;
	}

	/**
	 * primaryStage를 반환한다.
	 * 
	 * @return
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	/**
	 * LogStage를 반환한다.
	 * 
	 * @return
	 * @see LogStage
	 */
	public LogStage getLogView() {
		return logStage;
	}

	/**
	 * 인수인계 데이터들을 반환한다.
	 * 
	 * @return 인수인계 데이터
	 * @see Data
	 */
	public ObservableList<Data> getDatas() {
		return datas;
	}

	/**
	 * 패스워드 목록 데이터들을 반환한다.
	 * 
	 * @return 패스워드 목록 데이터
	 * @see OfficerIdentity
	 */
	public ObservableList<OfficerIdentity> getPasswordList() {
		return passwords;
	}

	/**
	 * 팁 데이터 목록을 반환한다
	 * 
	 * @return 팁 데이터 목록
	 * @see TipData
	 */
	public ObservableList<TipData> getTipDatas() {
		return tipDatas;
	}

	/**
	 * 전화번호 목록을 반환한다.
	 * 
	 * @return 전화번호 목록
	 * @see PhoneData
	 */
	public ObservableList<PhoneData> getPhoneDatas() {
		return phoneData;
	}

	/**
	 * 관찰 대상 PC 데이터를 반환한다.
	 * 
	 * @return 관찰 대상 PC 목록
	 * @see GetOffData
	 */
	public ObservableList<GetOffData> getGetOffData() {
		return getOffData;
	}

	/**
	 * 기타 패스워드 목록 데이터들을 반환한다.
	 * 
	 * @return 기타 패스워드 목록
	 * @see OtherPassword
	 */
	public ObservableList<OtherPassword> getOtherPasswordList() {
		return otherPasswords;
	}

	/**
	 * Notification 액션 수행, 전역에서 접근이 가능하도록 하고 static 으로 관리하지 않는다.
	 * @param title 제목
	 * @param message 메세지
	 * @param messageType 메시지 타입, 메세지 타입은 {@link DesktopNotify} 를 참조한다.
	 * @param time 출력 시간, 출력시간은 {@link Long} 타입으로 한다.
	 * 
	 * @see DesktopNotify
	 */
	public void showNotification(String title, String message, int messageType, long time)
	{
		DesktopNotify.showDesktopMessage(title, message, messageType, time, new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					getPrimaryStage().show();
				}
			});
	}
	
	/**
	 * 현재 파일을 받아오는 경로가 존재하는지 확인한다.<BR>
	 * 파일 경로가 존재 하지 않을 경우, UNC 모드를 해제한다.<BR><BR>
	 * 
	 * <code> throw new Exception("UNC Server \"" + uncPath + "\" Connection Fail"); </code> 를 통해<BR>
	 * 발생 된 강제 Exception 을 통해 UNC 경로 제어 설정 구문으로 이동한다.
	 */
	public void doDirectoryCheck() {
		while (true) {
			try {
				File sourceFile = new File(uncPath + ModifyHandler.modifyFileName);
				
				sourceFile.createNewFile();
				
				if (sourceFile.exists()) {
					System.out.println("UNC Server Connection OK.");
					break;
				} else
					throw new Exception("UNC Server \"" + uncPath + "\" Connection Fail");
			} catch (Exception e) {
				e.printStackTrace();
				LogStage.append("[경고] UNC 서버에 연결 할 수 없습니다." + e.getMessage());
				new ExceptionDialog(AlertType.ERROR, "오류", "오류가 발생했습니다.", "지정한 경로에 접속 할 수 없습니다.", e).showAndWait();

				ButtonType yes = new ButtonType("예");
				ButtonType no = new ButtonType("아니요(exit)");
				ButtonType etc = new ButtonType("로컬로 사용하겠습니다.");

				Alert dialog = new Alert(AlertType.CONFIRMATION, "경로를 재지정하시겠습니까?", yes, no, etc);
				dialog.getDialogPane().setPrefWidth(600);
				Optional<ButtonType> result = dialog.showAndWait();
				if (result.get().equals(no))
					System.exit(0);
				else if (result.get().equals(yes)) {
					try {
						FXMLLoader loader = new FXMLLoader(this.getClass().getResource("view/root/directoryselector/DirectorySelectorLayout.fxml"));
						GridPane pane = (GridPane) loader.load();

						DirectorySelectorLayoutController controller = loader.getController();

						Stage stage = new Stage();
						stage.setTitle("경로 지정");
						stage.getIcons().add(icon);
						// stage.initOwner(getPrimaryStage());
						stage.initModality(Modality.WINDOW_MODAL);

						Scene scene = new Scene(pane);

						controller.setMainApp(this, stage);

						stage.setScene(scene);
						uncMode = false;
						stage.showAndWait();

					} catch (Exception ae) {
						LogStage.append("[경고] 데이터 폴더 경로 지정 중 오류가 발생했습니다." + e.getMessage());
						e.printStackTrace();
						new ExceptionDialog(AlertType.ERROR, "오류", "오류가 발생했습니다.", "지정한 경로에 접속 할 수 없습니다.", e).showAndWait();
					}
				} else {
					uncPath = path;
					path = path.concat("temp" + File.separator);
				}

			}
		}
	}

	/**
	 * 기본 데이터를 레지스트리로 부터 구성한다.
	 */
	private void setDataFromRegistry() {
		
		title = Regedit.getStringValue("title");
		uncMode = Regedit.getBooleanValue("uncMode");
		
		// 레지스트리로부터 unc 경로를 받는다.
		if (Regedit.getStringValue("uncPath").equals(""))
			Regedit.setRegistry("uncPath", uncPath);
		else
			uncPath = Regedit.getStringValue("uncPath");

		// 레지스트리로부터 local 경로를 받는다.
		if (Regedit.getStringValue("localPath").equals(""))
			Regedit.setRegistry("localPath", path);
		else
			path = Regedit.getStringValue("localPath");
	}
	
	/**
	 * 프로그램을 최초 실행 하는지 여부를 레지스트리로부터 검사한다.
	 * {@value dataExists}
	 * @return 최초 실행 여부
	 */
	private boolean isFirstStart(){
		if(Regedit.getBooleanValue("dataExists") == false)
			return true;
		else
			return false;
	}

	/**
	 * 타이틀을 지정한다. <BR><BR>
	 * <code> title+ UNC path</code>
	 */
	public void setTitle() {
		primaryStage.setTitle(title + " [" + uncPath + "]");
	}

	public static void main(String[] args) {
		
		launch(args);
	}
}
