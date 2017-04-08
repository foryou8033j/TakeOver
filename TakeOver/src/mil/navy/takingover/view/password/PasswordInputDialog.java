package mil.navy.takingover.view.password;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import mil.navy.takingover.MainApp;
import mil.navy.takingover.util.Regedit;
import mil.navy.takingover.view.logview.LogStage;

public class PasswordInputDialog extends Stage{

	VBox vb = new VBox(10);
	
	
	PasswordField password = new PasswordField();
	Text title = new Text("패스워드를 입력하세요.");
	Text text = new Text("");
	
	private int maxLength = 13;
	private boolean pass = false;
	
	public PasswordInputDialog(MainApp mainApp) {
		
		getIcons().add(mainApp.icon);
		
		vb.getChildren().add(title);
		vb.getChildren().add(password);
		vb.getChildren().add(text);
		
		vb.setAlignment(Pos.CENTER);
		
		title.setFont(Font.font(14));
		title.setTextAlignment(TextAlignment.CENTER);
		
		text.setTextAlignment(TextAlignment.CENTER);
		
		setTitle("인수인계 도구");
		initOwner(mainApp.getPrimaryStage());
		
		setWidth(240);
		setHeight(120);
		setResizable(false);
		
		Scene scene = new Scene(vb);
		setScene(scene);
		
		setOnCloseRequest(Event -> {
			pass = false;
		});
		
		
		
		password.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> obersvable, String oldValue, String newValue) {
				
				if(newValue.length() > maxLength){
					String s = newValue.substring(0, maxLength);
					password.setText(s);
					return;
				}
				
				if(newValue.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")){
					text.setFill(Color.RED);
					text.setText("한글이 입력되고 있습니다.");
				}else{
					text.setText("");
				}
				
			}
		});
		
		password.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent arg0) {
				
				if(arg0.getCode() == KeyCode.ENTER){
					if(Regedit.getStringValue("password").equals(password.getText()))
					{
						LogStage.append("패스워드가 일치하여 인증이 허가되었습니다.");
						close();
						pass = true;
					}
					else{
						password.setText("");
						text.setFill(Color.RED);
						text.setText("패스워드가 올바르지 않습니다.");
						LogStage.append("[주의] 패스워드 실패");
						pass = false;
					}
				}
				
			};
		});
		
	}
	
	public boolean isPass(){
		return pass;
	}
	
}
