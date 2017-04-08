package mil.navy.takingover.view.tab.takeover;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import mil.navy.takingover.MainApp;
import mil.navy.takingover.model.TakeOverData;
import mil.navy.takingover.util.file.FileNameConverter;
import mil.navy.takingover.util.file.ModifyHandler;
import mil.navy.takingover.util.file.NIOFileCopy;

public class TakeOverInputBoxLayoutController implements Initializable{
	
	
	@FXML
	TextField title;
	
	@FXML
	TextArea object;
	
	@FXML
	TextField orderer;
	
	//현재 표시되는 이미지 경로
	private File imageFile;
	
	@FXML
	Button accept;
	
	@FXML
	Button cancle;
	
	@FXML
	BorderPane imagePane;
	
	@FXML
	Button addImage;
	
	@FXML
	RadioButton noSelectItem;
	
	@FXML
	RadioButton emphasisItem;
	
	@FXML
	RadioButton continueItem;
	
	@FXML
	RadioButton completeItem;
	
	ToggleGroup toggleGroup = new ToggleGroup();
	
	private MainApp mainApp;
	
	private BorderPane curInputBorderPane;
	
	private boolean isEditMode = false;
	
	private int index = -1;

	private boolean controlKeyPressed = false;
	private boolean enterKeyPressed = false;
	
	private String originalTitle = "";
	private String originalObject = "";
	private String originalOrderer = "";
	private String originalImage = "";
	private Boolean[] originalSelect = {false, false, false, false};
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	
	@FXML
	private void handleAccept()
	{
		
		new Thread(() -> {
			Platform.runLater(() -> {
				
				mainApp.getHeadLayoutController().getTakeOverList().remove(curInputBorderPane);
				
				if(title.getText().equals("") && object.getText().equals("") && orderer.getText().equals(""))
				{	
					mainApp.ableToLoad = true;
					return;
				}
				if(title.getText().equals("") && object.getText().equals(""))
				{
					mainApp.ableToLoad = true;
					return;
				}
				else
				{
					
					//수정 모드가 아닐 때 -> 새로운 데이터 추가
					if(!isEditMode) 
					{
						mainApp.getSelectedTakeOverData().getTakeOverData().add(getCurTakeOverData());
					}
					else
					{
						//수정 모드 일 때 -> 기존 데이터 삭제 후 새 데이터 추가
						mainApp.getSelectedTakeOverData().getTakeOverData().remove(index);
						mainApp.getSelectedTakeOverData().getTakeOverData().add(index, getCurTakeOverData());
					}
					
					//데이터를 저장한다.
					mainApp.getSelectedTakeOverData().saveDataToFile();
				}

				mainApp.ableToLoad = true;
				
				String fileName = FileNameConverter.toString(mainApp.getSelectedTakeOverData().getLocalDate()) + ".xml";
				ModifyHandler.removeModify(fileName);
				
			});
		}).start();
		
	}
	
	@FXML
	private void handleCanle()
	{
		
		new Thread(() -> {
			
			Platform.runLater(() -> {
				//수정 모드가 아닐때
				if(!isEditMode) mainApp.getHeadLayoutController().getTakeOverList().remove(curInputBorderPane);
				else
				{
					mainApp.getSelectedTakeOverData().getTakeOverData().remove(index);
					mainApp.getSelectedTakeOverData().getTakeOverData().add(index, getOriginalTakeOverData());
				}
				
				mainApp.ableToLoad = true;
				
				//현재 수정이 끝난 데이터
				String fileName = FileNameConverter.toString(mainApp.getSelectedTakeOverData().getLocalDate()) + ".xml";
				ModifyHandler.removeModify(fileName);
			});
			
		}).start();

	}
	
	
	//인수인계에 이미지를 추가하는 메소드
	@FXML
	private void handleAddImage()
	{
		//이미지를 추가할 경로 지정
		FileChooser chooser = new FileChooser();
		chooser.setTitle("등록 할 이미지 선택");
		
		chooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("All Images", "*.jpg", "*.jpeg", "*.png", "*.bmp", "*.gif"),
				new FileChooser.ExtensionFilter("JPG", "*.jpg", "*.jpeg"),
				new FileChooser.ExtensionFilter("PNG", "*.png"),
				new FileChooser.ExtensionFilter("BMP", "*.bmp"),
				new FileChooser.ExtensionFilter("GIF", "*.gif")
				);
		imageFile = chooser.showOpenDialog(mainApp.getPrimaryStage());
		
		LocalDateTime curTime = LocalDateTime.now();
		String pattern = "yyyy-MM-dd-hhmmss";
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
		
		int pos = imageFile.getName().lastIndexOf(".");
		
		
		String target = imageFile.getName().substring(0, imageFile.getName().lastIndexOf('.'));
		File rename = new File(target + " - " +dateFormatter.format(curTime) +"." +  imageFile.getName().substring(pos + 1));
		
		
		
		if(imageFile != null)
		{
			//로컬의 파일을 UNC 경로로 업로드
			NIOFileCopy.copy(imageFile.getAbsolutePath(), mainApp.uncPath + "Images" + File.separator + rename.getName());
			
			imageFile = new File(mainApp.uncPath + "Images" + File.separator + rename.getName());
			
			showImage(imageFile.getAbsolutePath());
			System.out.println("File Selected : " + imageFile.getName());
		}
		
	}
	
	private void showImage(String filePath)
	{
		imagePane.setCenter(null);
		ImageView imageView = new ImageView(loadImage(filePath));
		imageView.setFitHeight(80);
		imageView.setPreserveRatio(true);
		imagePane.setCenter(imageView);
		
		MenuItem modify = new MenuItem("파일 변경");
		MenuItem delete = new MenuItem("삭제");
		ContextMenu menu = new ContextMenu(modify, delete);
		
		imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				menu.show(imageView, arg0.getScreenX(), arg0.getScreenY());
			};
		});
		
		modify.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				handleAddImage();
			}
		});
		
		delete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				imageFile = null;
				imageView.setImage(null);
				imageView.setFitHeight(0);
				imagePane.setCenter(addImage);
			}
		});
		
	}
	
	private Image loadImage(String fileName)
	{
		File imageFile = new File(fileName);
		
		return new Image(imageFile.toURI().toString());
	}
	
	public TakeOverInputBoxLayoutController() {
		// TODO Auto-generated constructor stub
	}
	
	public void setMainApp(MainApp mainApp, BorderPane curLayout)
	{
		this.mainApp = mainApp;
		this.curInputBorderPane = curLayout;
		this.isEditMode = false;
		mainApp.ableToLoad = false;
		
		noSelectItem.setToggleGroup(toggleGroup);
		emphasisItem.setToggleGroup(toggleGroup);
		continueItem.setToggleGroup(toggleGroup);
		completeItem.setToggleGroup(toggleGroup);
		
		
		
		title.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent arg0) {
				
				if (arg0.getCode() == KeyCode.ENTER)
				{
					object.requestFocus();
				}
				else if(arg0.getCode() == KeyCode.ESCAPE)
				{
					handleCanle();
				}
			};
		});
		
		//현재 수정중인 데이터
		String fileName = FileNameConverter.toString(mainApp.getSelectedTakeOverData().getLocalDate()) + ".xml";
		ModifyHandler.currentModity(fileName);
		
		/**
		 * obejct 텍스트필드에서 컨트롤 + 엔터키를 눌렀을 때 사용자 입력 칸으로 넘어 가도록 한다.
		 */
		this.object.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent arg0) {
				
				if(arg0.getCode() == KeyCode.CONTROL)
				{
					controlKeyPressed = true;
					if(enterKeyPressed && controlKeyPressed)
					{
						controlKeyPressed = false;
						enterKeyPressed = false;
						orderer.requestFocus();
					}
				}
				else if (arg0.getCode() == KeyCode.ENTER)
				{
					enterKeyPressed = true;
					if(enterKeyPressed && controlKeyPressed)
					{
						controlKeyPressed = false;
						enterKeyPressed = false;
						orderer.requestFocus();
					}
				}
				else if(arg0.getCode() == KeyCode.ESCAPE)
				{
					handleCanle();
				}
			};
		});
		this.object.setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent arg0) {
				if(arg0.getCode() == KeyCode.CONTROL)
				{
					controlKeyPressed = false;
				}
				else if (arg0.getCode() == KeyCode.ENTER)
				{
					enterKeyPressed = false;
				}
				
				
			};
			
		});
		
		this.orderer.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent arg0) {
				if(arg0.getCode() == KeyCode.ENTER)
					handleAccept();
				else if(arg0.getCode() == KeyCode.ESCAPE)
				{
					handleCanle();
				}
			}
			
		});
		
		this.cancle.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent arg0) {
				if(arg0.getCode() == KeyCode.TAB)
				{
					title.requestFocus();
				}
			}
		});
	}
	
	
	
	public void setTextFieldForEdit(int index, TakeOverData data)
	{
		
		this.title.setText(data.getTitle());
		this.object.setText(data.getObject());
		this.orderer.setText(data.getOrderer());
		this.imageFile = new File(mainApp.uncPath + "Images" +File.separator + data.getImage());
		
		if(!data.getImage().equals("") && data.getImage() != null)
		{
			showImage(imageFile.getAbsolutePath());
			
			if(!imageFile.exists())
			{
				imageFile = null;
				imagePane.setCenter(addImage);
			}
		}
		else
			imageFile = null;
		
		emphasisItem.setSelected(data.getEmphasis());
		continueItem.setSelected(data.getContinue());
		completeItem.setSelected(data.getComplete());
		
		if(data.isNoSelect())
			noSelectItem.setSelected(true);
		
		originalTitle = data.getTitle();
		originalObject = data.getObject();
		originalOrderer = data.getOrderer();
		originalImage = data.getImage();
		originalSelect[0] = data.getEmphasis();
		originalSelect[1] = data.getContinue();
		originalSelect[2] = data.getComplete();
		
		//제목, 내용, 인수자 내용중 하나라도 입력되어 있는 경우 수정 모드로 판단.
		if(!title.equals("") || !object.equals("") || !orderer.equals(""))
			setEditMode(true);
		
		setIndex(index);
	}
	
	private TakeOverData getCurTakeOverData(){
		TakeOverData data = new TakeOverData(title.getText(), object.getText(), orderer.getText());
		
		if(imageFile != null)
			data.setImage(imageFile.getName());
		else
			data.setImage("");
		
		data.setEmphasis(emphasisItem.isSelected());
		data.setContinue(continueItem.isSelected());
		data.setComplete(completeItem.isSelected());
		
		return data;
	}
	
	private TakeOverData getOriginalTakeOverData(){
		
		TakeOverData data = new TakeOverData(originalTitle, originalObject, originalOrderer);
		
		if(originalImage != null)
			data.setImage(originalImage);
		else
			data.setImage("");
		
		data.setEmphasis(originalSelect[0]);
		data.setContinue(originalSelect[1]);
		data.setComplete(originalSelect[2]);
		
		return data;
		
	}
	
	public void setEditMode(boolean edit)
	{
		this.isEditMode = edit;
	}
	
	public void setIndex(int index)
	{
		this.index = index; 
	}
	
	public boolean isEditMode()
	{
		return isEditMode;
	}
	
	public String getTtile()
	{
		return title.getText();
	}
	
	public String getContents()
	{
		return object.getText();
	}
	
	public String getOrderer()
	{
		return orderer.getText();
	}

}


