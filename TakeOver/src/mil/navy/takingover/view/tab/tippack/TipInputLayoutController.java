package mil.navy.takingover.view.tab.tippack;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import mil.navy.takingover.MainApp;
import mil.navy.takingover.model.otherpassword.OtherPassword;
import mil.navy.takingover.model.tip.TipData;

public class TipInputLayoutController implements Initializable{
	
	@FXML
	TextField title;
	
	@FXML
	TextArea contents;
	
	@FXML
	Button accept;
	
	@FXML
	Button cancle;
	
	private boolean editMode = false;
	private int index;
	

	
	//현재 스테이지
	private Stage stage;
	
	private MainApp mainApp;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public TipInputLayoutController() {
		// TODO Auto-generated constructor stub
	}
	
	@FXML
	private void handleAccept()
	{
		if(editMode) 
		{
			mainApp.getTipDatas().remove(index);
			mainApp.getTipDatas().add(index, new TipData(title.getText(), contents.getText())); 
		}
		else 
			mainApp.getTipDatas().add(new TipData(title.getText(), contents.getText()));
		
		//파일 저장
		mainApp.getRootLayoutController().getTipLayoutController().saveDataToFile();
		
		stage.close();
		
	}
	
	@FXML
	private void handleCancle()
	{
		
		stage.close();
	}
	
	
	public void setMainApp(MainApp mainApp)
	{
		this.mainApp = mainApp;
		
		
		
		
		
		title.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent arg0) {
				if(arg0.getCode() == KeyCode.ENTER || arg0.getCode() == KeyCode.TAB)
				{
					contents.requestFocus();
				}
			};
		});
	
	}
	
	public void setStage(Stage stage)
	{
		this.stage = stage;
	}
	
	public void setContents(int index, String title, String contents)
	{
		//가장 위의 텍스트 박스에 requestFocus 한다.
		this.title.requestFocus();
		
		//어느 한 데이터라도 자료가 있을때는 수정 모드로 간주.
		if(!title.equals("") || !contents.equals(""))
			editMode = true;
		
		if(editMode)
		{
			stage.setTitle("팁 정보 수정");
			accept.setText("수정");
			this.index = index;
		}
		
		this.title.setText(title);
		this.contents.setText(contents);
		
	}
	


}
