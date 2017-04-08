package mil.navy.takingover;

import java.io.File;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.prefs.Preferences;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import mil.navy.takingover.model.Data;
import mil.navy.takingover.model.FileChecker;
import mil.navy.takingover.util.DateUtil;
import mil.navy.takingover.util.ExceptionDialog;
import mil.navy.takingover.util.Regedit;
import mil.navy.takingover.view.HeadLayoutController;
import mil.navy.takingover.view.InternetUserLayoutController;
import mil.navy.takingover.view.MemberLayoutController;
import mil.navy.takingover.view.RootLayoutController;
import mil.navy.takingover.view.TakeOverInputBoxLayoutController;
import mil.navy.takingover.view.TakeOverLayoutController;

public class MainApp extends Application {

	public String title = "사이버방호센터 통합관제과 인수인계 도구";
	
	private Stage primaryStage; // 기본 스테이지를 지정한다.
	private BorderPane rootLayout; // 루트레이아웃을 지정한다.
	private Tab takeOverTab;
	private HeadLayoutController headLayoutcontroller;
	
	public Image icon = null;
	
	public boolean ableToLoad = false;
	
	//UNC 서버에서 끌어올 데이터가 있는 경로
	public static String uncPath = "\\\\32.1.21.31\\상황관제병\\00. 업무관련\\인수인계\\#인수인계도구\\";
	
	//UNC 서버에서 끌어온 데이터가 저장 될 경로
	public static String path = "C:/Program Files/NavyCert TakeOverTool/Datas/";
	//public static String path = "\\\\32.1.21.31\\상황관제병\\00. 업무관련\\인수인계\\#인수인계도구\\Datas\\";
	
	//인수인계 목록들이 저장되어 있는 리스트
	private ObservableList<Data> datas = FXCollections.observableArrayList();
	
	//인수인계 파일 목록이 저장되어 있는 리스트
	ObservableList<String> fileList;

	
	public static int second = 60;
	public static int minute = 600;
	public static int hour = 3600;
	
	
	@Override
	public void start(Stage primaryStage) {

		this.icon = new Image("file:img/takeOverIcon.png");
		
		this.primaryStage = primaryStage;
		
		//파일 경로로부터 저장된 인수인계 파일을 로드한다.
		loadFileList();
		
		//오늘 파일 생성이 가능한지 확인하고 기존에 인계파일이 존재하지 않으면 새로 생성한다.
		isAbleNewFileToDay();

		initStage(); // Stage를 초기화한다.
		initRootLayout(); // Root 레이아웃을 초기화한다.
		
		//파일 변경 사항을 유지하는 쓰레드
		new FileChecker(this).start();
	}
	
	public void isAbleNewFileToDay()
	{
		
		Calendar cal = Calendar.getInstance(); 	//현재 시각을 구한다.
		
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		
		int size = fileList.size();
		
		LocalDate curDate = LocalDate.of(year, month, day);
		
		for(int i=0; i<size; i++)
		{
			String dateString = fileList.get(i).replaceFirst("saveFile_", "");	//파일명 앞의 문자를 제거한다.
			dateString = dateString.replaceAll(".xml", "");	//확장자를 제거한다.

			//System.out.println(dateString);
			//dateString의 형식은 yyyy.MM.dd 형식으로 남게 된다.
			
			LocalDate date = null;
			
			if(DateUtil.validDate(dateString))
				date = DateUtil.parse(dateString);
			else
				continue;
			
			if(curDate.equals(date))
				return;
		}
		
		Data tmpData = new Data(LocalDate.of(year, month, day));
		
		String monthString = String.valueOf(month);
		String dayString = String.valueOf(day);
		
		if(tmpData.getLocalDate().getMonth().getValue() < 10)
			monthString = monthString.replace(monthString, "0"+monthString);
		
		if(tmpData.getLocalDate().getDayOfMonth() < 10)
			dayString = dayString.replace(dayString, "0"+dayString);
		
		String fileName = "saveFile_" + tmpData.getLocalDate().getYear() + "." + monthString + "." + dayString + ".xml";

		tmpData.saveDataToFile(new File(MainApp.path + fileName));
		datas.add(tmpData);
		loadFileList();
		
		System.out.println("오늘자 인수인계 파일 찾을 수 없음, 새로운 파일 생성.");
		
	}
	
	/**
	 * 파일 리스트를 읽는다
	 */
	public void loadFileList()
	{
		fileList = getFileListString (path);
		fileListToData();
	}
	
	/**
	 * 파일 리스트로부터 파일 목록을 형성한다.
	 */
	public void fileListToData()
	{
		int size = fileList.size();
		datas.clear();
		
		for(int i=0; i<size; i++)
		{
			String dateString = fileList.get(i).replaceFirst("saveFile_", "");	//파일명 앞의 문자를 제거한다.
			dateString = dateString.replaceAll(".xml", "");	//확장자를 제거한다.

			//System.out.println(dateString);
			//dateString의 형식은 yyyy.MM.dd 형식으로 남게 된다.
			
			LocalDate date = null;
			
			if(DateUtil.validDate(dateString))
				date = DateUtil.parse(dateString);
			else
				continue;
		
			datas.add(new Data(date));
			//datas.get(i).loadDataFromFile(new File("./Datas/"+fileList.get(i)));
			
			//System.out.println(date.getYear() + "/" + date.getMonthValue() + "/" + date.getDayOfMonth());
			//System.out.println(dateString);
		}
	}
	
	private ObservableList<String> getFileListString (String path)
	{
		ObservableList<String> fileListString = FXCollections.observableArrayList();
		
		File dir = new File(path);
		File[] fileList = dir.listFiles();
		
		try
		{
			for(int i=0; i<fileList.length; i++)
			{
				File file = fileList[i];
				if(file.isFile())
				{
					fileListString.add(file.getName());
					System.out.println("FileListAdded " + path + file.getName());
				}else if(file.isDirectory())
				{
					//서브 디렉토리까지 추가 될 경우
					//fileListString.add(file.getName());
					//ignore
				}
			}
		}catch (Exception e)
		{
			
		}
		
		
		return fileListString;
	}

	/**
	 * 스테이지를 초기화한다.
	 */
	private void initStage() {
		primaryStage.setTitle(title);
		primaryStage.getIcons().add(icon);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				
				if(getHeadLayoutController() != null)
				{
					if(getHeadLayoutController().curSelectedData != null)
					{
						getHeadLayoutController().curSelectedData.saveDataToFile();
						System.out.println("saved!");
					}
				}
				
				System.out.println("도구를 종료합니다.");
				System.exit(0);
			}
		});

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
		
		
	}

	/**
	 * Root 레이아웃을 초기화한다.
	 */
	private void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			RootLayoutController controller = loader.getController();
			controller.setMainApp(this);
			takeOverTab = controller.getTakeOverTab();              

			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
			

			initHeadLayout();
			
		} catch (Exception e) {
			e.printStackTrace();
			new ExceptionDialog(AlertType.ERROR, "오류", "오류가 발생했습니다.", "Root 레이아웃을 로드하던 도중 오류가 발생 했습니다.", e.getMessage())
					.showAndWait();
			System.exit(0);
		}

	}
	
	/**
	 * head Layout을 로드한다.
	 */
	private void initHeadLayout()
	{
		try
		{
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource("view/HeadLayout.fxml"));
			BorderPane layout= (BorderPane) loader.load();
			
			headLayoutcontroller = loader.getController();
			headLayoutcontroller.setMainApp(this);
			
			takeOverTab.setContent(layout);
		}catch (Exception e)
		{
			e.printStackTrace();
			new ExceptionDialog(AlertType.ERROR, "오류", "오류가 발생했습니다.", "Head 레이아웃을 로드하던 도중 오류가 발생 했습니다.", e.getMessage())
					.showAndWait();
			System.exit(0);
		}
		
	}
	
	/**
	 * 인수인계를 입력하는 BorderPane를 인수인계 목록에 추가한다.
	 * @param title
	 * @param object
	 * @param orderer
	 * @return
	 */
	public BorderPane addTakeOverInputLayout(String title, String object, String orderer)
	{
		try
		{
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource("view/TakeOverInputBoxLayout.fxml"));
			BorderPane layout= (BorderPane) loader.load();
			
			TakeOverInputBoxLayoutController controller = loader.getController();
			controller.setMainApp(this, layout);
			controller.setTextFieldForEdit(title, object, orderer);
			
			return layout;
			
		}catch (Exception e)
		{
			e.printStackTrace();
			new ExceptionDialog(AlertType.ERROR, "오류", "오류가 발생했습니다.", "인수인계를 입력하는 레이아웃을 로드하던 도중 오류가 발생 했습니다.", e.getMessage())
					.showAndWait();
			System.exit(0);
		}
		
		return null;

	}
	
	/**
	 * 인수 인계를 변경하는 BorderPane을 인수인계 목록에 추가한다.
	 * @param index
	 * @param title
	 * @param object
	 * @param orderer
	 * @return
	 */
	public BorderPane addTakeOverInputLayout(int index, String title, String object, String orderer)
	{
		try
		{
			
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource("view/TakeOverInputBoxLayout.fxml"));
			BorderPane layout= (BorderPane) loader.load();
			
			TakeOverInputBoxLayoutController controller = loader.getController();
			controller.setMainApp(this, layout);
			controller.setTextFieldForEdit(title, object, orderer);
			controller.setEditMode(true);
			controller.setIndex(index);
			
			return layout;
			
		}catch (Exception e)
		{
			e.printStackTrace();
			new ExceptionDialog(AlertType.ERROR, "오류", "오류가 발생했습니다.", "인수인계를 입력하는 레이아웃을 로드하던 도중 오류가 발생 했습니다.", e.getMessage())
					.showAndWait();
			System.exit(0);
		}
		return null;
	}
	
	/**
	 * 현재 인수인계 목록을 보여주는 BorderPane을 정의 한다.
	 * @param title
	 * @param object
	 * @param orderer
	 * @return
	 */
	public BorderPane addTakeOverLayout(String title, String object, String orderer)
	{
		try
		{
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource("view/TakeOverLayout.fxml"));
			BorderPane layout= (BorderPane) loader.load();
			
			TakeOverLayoutController controller = loader.getController();
			controller.setMainApp(this, layout);
			controller.setContents(title, object, orderer);
			return layout;
			
		}catch (Exception e)
		{
			e.printStackTrace();
			new ExceptionDialog(AlertType.ERROR, "오류", "오류가 발생했습니다.", "인수인계를 보여주는 레이아웃을 로드하던 도중 오류가 발생 했습니다.", e.getMessage())
					.showAndWait();
			System.exit(0);
		}
		
		return null;
		
	}
	
	public void showInternetUserDialog(int index)
	{
		try
		{
			Stage stage = new Stage();
			stage.getIcons().add(icon);
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource("view/InternetUserLayout.fxml"));
			AnchorPane layout= (AnchorPane) loader.load();
			
			InternetUserLayoutController controller = loader.getController();
			controller.setMainApp(this, stage);
			
			String title = "인터넷 운용승인 추가";
			
			if(index == -1)
				title = "인터넷 운용승인 추가";
			else
			{
				title = "인터넷 운용승인 변경";
				controller.setDefaultData(index);
			}

			
			stage.setTitle(title);
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent arg0) {
					stage.close();
				}
			});
			stage.initOwner(primaryStage);
			stage.initModality(Modality.WINDOW_MODAL);
			
			Scene scene = new Scene(layout);
			stage.setScene(scene);
			
			stage.show();
			
			
		}catch (Exception e)
		{
			e.printStackTrace();
			new ExceptionDialog(AlertType.ERROR, "오류", "오류가 발생했습니다.", "인터넷 사용자 등록 레이아웃을 로드하던 도중 오류가 발생 했습니다.", e.getMessage())
					.showAndWait();
			System.exit(0);
		}
		
	}
	
	public void showMemberInputLayout()
	{
		try
		{
			Stage stage = new Stage();
			stage.getIcons().add(icon);
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource("view/MemberLayout.fxml"));
			AnchorPane layout= (AnchorPane) loader.load();
			
			MemberLayoutController controller = loader.getController();
			controller.setMainApp(this, stage);

			stage.setTitle("인계 인원 지정");
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent arg0) {
					stage.close();
				}
			});
			stage.initOwner(primaryStage);
			stage.initModality(Modality.WINDOW_MODAL);
			
			Scene scene = new Scene(layout);
			stage.setScene(scene);
			
			stage.showAndWait();
			
			
		}catch (Exception e)
		{
			e.printStackTrace();
			new ExceptionDialog(AlertType.ERROR, "오류", "오류가 발생했습니다.", "인계 인원 지정 레이아웃을 로드하던 도중 오류가 발생 했습니다.", e.getMessage())
					.showAndWait();
			System.exit(0);
		}
		
	}

	/**
	 * 파일 환경설정을 반환한다. 즉 파일은 마지막으로 열린 것이고, 환경 설정은 OS 특정 레스트리로부터 읽는다. 만일
	 * preference 를 찾지 못하면 null을 반환한다.
	 * 
	 * @return
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
	 * @param file
	 */
	public void setDataFilePath(File file) {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		if( file != null){
			prefs.put("filePath", file.getPath());
			primaryStage.setTitle(title + " - " + file.getName());
		}else{
			prefs.remove("filePath");
			primaryStage.setTitle(title);
		}
	}
	
	
	
	public HeadLayoutController getHeadLayoutController()
	{
		return headLayoutcontroller;
	}

	/**
	 * primaryStage를 반환한다.
	 * @return
	 */
	public Stage getPrimaryStage()
	{
		return primaryStage;
	}
	
	/**
	 * 인수인계 데이터들을 반환한다.
	 * @return
	 */
	public ObservableList<Data> getDatas(){
		return datas;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
