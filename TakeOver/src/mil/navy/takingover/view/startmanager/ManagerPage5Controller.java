package mil.navy.takingover.view.startmanager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import mil.navy.takingover.MainApp;

public class ManagerPage5Controller {

	MainApp mainApp;
	
	@FXML PasswordField password;
	@FXML PasswordField retypePassword;
	@FXML CheckBox noUsePassword;
	@FXML Text hintText1;
	@FXML Text hintText2;
	
	int maxLength = 13;
	
	public void setMainApp(MainApp mainApp){
		this.mainApp = mainApp;
		hintText1.setText("");
		hintText2.setText("");
		
		hintText1.setFill(Color.RED);
		hintText2.setFill(Color.RED);
		
		Pattern pattern1 = Pattern.compile("([a-z0-9]*)");
		Pattern pattern2 = Pattern.compile("([a-zA-Z0-9]*)");
		Pattern pattern3 = Pattern.compile("([a-zA-Z0-9].*[!,@,#,$,%,^,&,*,?,_,~]|[!,@,#,$,%,^,&,*,?,_,~].*[a-zA-Z0-9])");
		
		
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
				}else{
					hintText2.setFill(Color.RED);
					hintText2.setText("일치하지 않습니다.");
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
		}else{
			password.setDisable(false);
			retypePassword.setDisable(false);
		}
	}
	
	public boolean isSame(){
		return password.getText().equals(retypePassword.getText());
	}
	
	public boolean isUsePassword(){
		return !noUsePassword.isSelected();
	}
	
	public String getPassword(){
		return password.getText();
	}
	
	public boolean isNoEnterPassword(){
		return password.getText().equals("");
	}
	
}
