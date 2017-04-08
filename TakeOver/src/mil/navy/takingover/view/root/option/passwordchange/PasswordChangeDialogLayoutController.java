package mil.navy.takingover.view.root.option.passwordchange;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mil.navy.takingover.MainApp;
import mil.navy.takingover.util.Regedit;
import mil.navy.takingover.view.logview.LogStage;

public class PasswordChangeDialogLayoutController {

	@FXML PasswordField oldPassword;
	@FXML Text hintText;
	
	@FXML PasswordField password;
	@FXML PasswordField retypePassword;
	@FXML CheckBox noUsePassword;
	@FXML Text hintText1;
	@FXML Text hintText2;
	
	@FXML Button set;
	
	MainApp mainApp;
	Stage stage;
	
	int maxLength = 13;
	
	public void setMainApp(MainApp mainApp, Stage stage){
		this.mainApp = mainApp;
		this.stage = stage;
		
		password.setDisable(true);
		retypePassword.setDisable(true);
		set.setDisable(true);
		noUsePassword.setDisable(true);
		
		hintText.setText("");
		hintText1.setText("");
		hintText2.setText("");
		
		hintText1.setFill(Color.RED);
		hintText2.setFill(Color.RED);
		
		
		if(Regedit.getStringValue("password").equals("")){
			oldPassword.setDisable(true);
			
			oldPassword.setDisable(true);
			password.setDisable(false);
			retypePassword.setDisable(false);
			noUsePassword.setDisable(false);
		}
		
		
		Pattern pattern1 = Pattern.compile("([a-z0-9]*)");
		Pattern pattern2 = Pattern.compile("([a-zA-Z0-9]*)");
		Pattern pattern3 = Pattern.compile("([a-zA-Z0-9].*[!,@,#,$,%,^,&,*,?,_,~]|[!,@,#,$,%,^,&,*,?,_,~].*[a-zA-Z0-9])");
		
		oldPassword.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> obersvable, String oldValue, String newValue) {
				
				if(newValue.length() > maxLength){
					String s = newValue.substring(0, maxLength);
					password.setText(s);
					return;
				}
				
				if(newValue.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")){
					hintText.setFill(Color.RED);
					hintText.setText("한글이 입력되고 있습니다.");
				}else{
					hintText.setText("");
				}
				
			}
		});
		
		
		oldPassword.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent arg0) {
				
				if(arg0.getCode() == KeyCode.ENTER){
					if(Regedit.getStringValue("password").equals(oldPassword.getText())){
						hintText.setFill(Color.GREEN);
						hintText.setText("일치합니다.");
						
						oldPassword.setDisable(true);
						password.setDisable(false);
						retypePassword.setDisable(false);
						noUsePassword.setDisable(false);
					}
							
					else{
						oldPassword.setText("");
						hintText.setFill(Color.RED);
						hintText.setText("패스워드가 올바르지 않습니다.");
					}
				}
				
			};
		});
		
		
		
		
		
		password.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				
				if(newValue.length() > maxLength){
					String s = newValue.substring(0, maxLength);
					password.setText(s);
					return;
				}
				
				if(newValue.equals("")){
					hintText1.setText("");
					return;
				}
				
				if(newValue.length() > 5){
					
					Matcher matcher = pattern1.matcher(newValue);
					if(matcher.find()){
						hintText1.setFill(Color.ORANGE);
						hintText1.setText("보통");
						
						matcher = pattern3.matcher(newValue);
						if(matcher.find()){
							hintText1.setFill(Color.BLUE);
							hintText1.setText("좋음");
							
							if(newValue.length() > 8){
								hintText1.setFill(Color.GREEN);
								hintText1.setText("높음");
							}
						}
					}
				}else{
					hintText1.setFill(Color.RED);
					hintText1.setText("최소 6자 이상 입력하세요.");
				}
				
				if(newValue.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")){
					hintText1.setFill(Color.RED);
					hintText1.setText("한글이 입력되고 있습니다.");
				}
					
					
			}
		});
		
		retypePassword.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(newValue.length() > maxLength){
					String s = newValue.substring(0, maxLength);
					retypePassword.setText(s);
					return;
				}
				
				if(newValue.equals("")){
					hintText2.setText("");
					return;
				}
				
				if(newValue.equals(password.getText())){
					hintText2.setFill(Color.GREEN);
					hintText2.setText("일치합니다.");
					set.setDisable(false);
				}else{
					hintText2.setFill(Color.RED);
					hintText2.setText("일치하지 않습니다.");
					set.setDisable(true);
				}
					
			}
		});
		
	}
	
	@FXML
	private void handleNoUsePassword(){
		if(noUsePassword.isSelected()){
			hintText1.setText("");
			hintText2.setText("");
			password.setText("");
			retypePassword.setText("");
			password.setDisable(true);
			retypePassword.setDisable(true);
			set.setDisable(false);
		}else{
			password.setDisable(false);
			retypePassword.setDisable(false);
			set.setDisable(true);
		}
	}
	
	@FXML
	private void handleSet(){
		
		
		if(isUsePassword()){
			if(!isSame() || isNoEnterPassword()){
				return;
			}else{
				
				if(isUsePassword()){
					Regedit.setRegistry("password", getPassword());
				}
				
				Alert alert = new Alert(AlertType.CONFIRMATION, "",  ButtonType.OK);
				Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
				alertStage.getIcons().add(mainApp.icon);
				alert.setTitle("완료");
				alert.setHeaderText(null);
				alert.setContentText("패스워드가 변경되었습니다.");
				alert.show();
				LogStage.append("사용자에 의해 패스워드가 변경되었습니다.");
				stage.close();
			}
		}else{
			Regedit.setRegistry("password", "");
			Alert alert = new Alert(AlertType.CONFIRMATION, "",  ButtonType.OK);
			Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
			alertStage.getIcons().add(mainApp.icon);
			alert.setTitle("완료");
			alert.setHeaderText(null);
			alert.setContentText("패스워드가 해제되었습니다.");
			LogStage.append("패스워드가 해제되었습니다.");
			alert.show();
			stage.close();
		}
		
		
		
	}
	
	
	
	
	
	
	private boolean isSame(){
		return password.getText().equals(retypePassword.getText());
	}
	
	private boolean isUsePassword(){
		return !noUsePassword.isSelected();
	}
	
	private String getPassword(){
		return password.getText();
	}
	
	private boolean isNoEnterPassword(){
		return password.getText().equals("");
	}
	
	
}
