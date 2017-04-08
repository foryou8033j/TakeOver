package mil.navy.takingover.view.tab.otherpassword;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import mil.navy.takingover.MainApp;
import mil.navy.takingover.model.otherpassword.OtherPassword;

public class InputOtherPasswordLayoutController implements Initializable{
	
	@FXML
	TextField title;
	
	@FXML
	TextField url;
	
	@FXML
	TextField id;
	
	@FXML
	TextField pw;
	
	final ToggleGroup group = new ToggleGroup();
	
	@FXML
	RadioButton option0;
	@FXML
	RadioButton option1;
	@FXML
	RadioButton option2;
	@FXML
	RadioButton option3;
	
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
	
	public InputOtherPasswordLayoutController() {
		// TODO Auto-generated constructor stub
	}
	
	@FXML
	private void handleAccept()
	{
		ObservableList<Boolean> options = FXCollections.observableArrayList(option0.isSelected(), option1.isSelected(), option2.isSelected(), option3.isSelected());
		if(editMode) 
		{
			mainApp.getOtherPasswordList().remove(index);
			mainApp.getOtherPasswordList().add(index, new OtherPassword(title.getText(), url.getText(), id.getText(), pw.getText(), options)); 
		}
		else 
			mainApp.getOtherPasswordList().add(new OtherPassword(title.getText(), url.getText(), id.getText(), pw.getText(), options));
		
		//파일 저장
		mainApp.getRootLayoutController().getOtherPasswordLayoutController().saveDataToFile();
		
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
				if(arg0.getCode() == KeyCode.ENTER)
				{
					option0.requestFocus();
				}
			};
		});
		
		url.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent arg0) {
				if(arg0.getCode() == KeyCode.ENTER)
				{
					id.requestFocus();
				}
			};
		});
		
		id.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent arg0) {
				if(arg0.getCode() == KeyCode.ENTER)
				{
					pw.requestFocus();
				}
			};
		});
		
		pw.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent arg0) {
				if(arg0.getCode() == KeyCode.ENTER)
				{
					handleAccept();
				}
			};
		});
	}
	
	public void setStage(Stage stage)
	{
		this.stage = stage;
		
		option0.setToggleGroup(group);
		option1.setToggleGroup(group);
		option2.setToggleGroup(group);
		option3.setToggleGroup(group);
	}
	
	public void setContents(int index, String title, String url, String id, String pw, ObservableList<Boolean> options)
	{
		//가장 위의 텍스트 박스에 requestFocus 한다.
		this.title.requestFocus();
		
		//어느 한 데이터라도 자료가 있을때는 수정 모드로 간주.
		if(!title.equals("") || !url.equals("") || !id.equals("") || !pw.equals(""))
			editMode = true;
		
		if(editMode)
		{
			stage.setTitle("로그인 정보 수정");
			accept.setText("수정");
			this.index = index;
		}
		
		this.title.setText(title);
		this.url.setText(url);
		this.id.setText(id);
		this.pw.setText(pw);
		
		if(options.get(0)) option0.setSelected(true);
		if(options.get(1)) option1.setSelected(true);
		if(options.get(2)) option2.setSelected(true);
		if(options.get(3)) option3.setSelected(true);
		
	}
	


}
