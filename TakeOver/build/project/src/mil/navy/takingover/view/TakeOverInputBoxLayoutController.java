package mil.navy.takingover.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import mil.navy.takingover.MainApp;
import mil.navy.takingover.model.TakeOverData;

public class TakeOverInputBoxLayoutController implements Initializable{
	
	
	@FXML
	TextField title;
	
	@FXML
	TextArea object;
	
	@FXML
	TextField orderer;
	
	@FXML
	Button accept;
	
	@FXML
	Button cancle;
	
	private MainApp mainApp;
	
	private BorderPane curInputBorderPane;
	
	private boolean isEditMode = false;
	
	private int index = -1;
	
	private String titleString;
	
	private String contentsString;
	
	private String ordererString;
	
	private boolean controlKeyPressed = false;
	private boolean enterKeyPressed = false;
	
	private String originalTitle = "";
	private String originalObject = "";
	private String originalorderer = "";
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private void handleAccept()
	{
		
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
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
					titleString = title.getText();
					contentsString = object.getText();
					ordererString = orderer.getText();
					
					if(!isEditMode) mainApp.getHeadLayoutController().curSelectedData.getTakeOverData().add(new TakeOverData(title.getText(), object.getText(), orderer.getText()));
					else
					{
						mainApp.getHeadLayoutController().curSelectedData.getTakeOverData().remove(index);
						mainApp.getHeadLayoutController().curSelectedData.getTakeOverData().add(index, new TakeOverData(title.getText(), object.getText(), orderer.getText()));
					}
					mainApp.getHeadLayoutController().curSelectedData.saveDataToFile();
				}
				mainApp.ableToLoad = true;
			}
		});
		
		
		
			
	}
	
	@FXML
	private void handleCanle()
	{
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				if(!isEditMode) mainApp.getHeadLayoutController().getTakeOverList().remove(curInputBorderPane);
				else
				{
					mainApp.getHeadLayoutController().curSelectedData.getTakeOverData().remove(index);
					mainApp.getHeadLayoutController().curSelectedData.getTakeOverData().add(index, new TakeOverData(originalTitle, originalObject, originalorderer));
				}
				mainApp.getHeadLayoutController().curSelectedData.saveDataToFile();
				
				if(mainApp.getHeadLayoutController().curSelectedData.getTakeOverData().size() == 0)
					mainApp.getHeadLayoutController().reLoad();
				
				mainApp.ableToLoad = true;
			}
		});
		
		
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
		
		title.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent arg0) {
				
				if (arg0.getCode() == KeyCode.ENTER)
				{
					object.requestFocus();
				}
			};
		});
		
		
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
	
	public void setTextFieldForEdit(String title, String object, String orderer)
	{
		this.title.setText(title);
		this.object.setText(object);
		this.orderer.setText(orderer);
		
		originalTitle = title;
		originalObject = object;
		originalorderer = orderer;
		
		//제목, 내용, 인수자 내용중 하나라도 입력되어 있는 경우 수정 모드로 판단.
		if(!title.equals("") || !object.equals("") || !orderer.equals(""))
			setEditMode(true);
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
		return titleString;
	}
	
	public String getContents()
	{
		return contentsString;
	}
	
	public String getOrderer()
	{
		return ordererString;
	}

}


