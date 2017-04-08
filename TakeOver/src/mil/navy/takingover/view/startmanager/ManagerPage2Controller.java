package mil.navy.takingover.view.startmanager;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import mil.navy.takingover.MainApp;

public class ManagerPage2Controller {

	MainApp mainApp;
	
	@FXML TextField textField;
	@FXML Button button;
	@FXML Text text;
	
	private boolean error = true;
	
	public void setMainApp(MainApp mainApp){
		this.mainApp = mainApp;
		text.setText("");
		text.setFill(Color.RED);
	}
	
	@FXML
	private void handleButton(){
		textField.setText(showDirectoryChooser(textField.getText()).getAbsolutePath());
	}
	
	private File showDirectoryChooser(String originalPath)
    {
    	
    	while(true)
    	{
    		DirectoryChooser directoryChooser = new javafx.stage.DirectoryChooser();
    		java.io.File selectedDirectory = directoryChooser.showDialog(null);
    		
    		if(selectedDirectory == null){
    				
    			return new File(originalPath);
    			
    		}else
    		{
    			if(selectedDirectory.getPath().equals("C:" + File.separator) || selectedDirectory.getPath().equals("D:" + File.separator)){
    				text.setText("로컬 경로가 지정되면 추후 심각한 문제가 발생 될 수 있습니다.\n다른 경로를 지정 해 주세요.");
    				error = true;
    			}
    			else if(isHasFileList(selectedDirectory)){
    				text.setText("이미 폴더안에 다른 파일이 있습니다. \n사용 할 수는 있지만 오류가 발생 할 수 있습니다.");
    				error = false;
    			}
    			else{
    				text.setText("");
    				error = false;
    			}
    				
    			
    			return selectedDirectory;
    		}
    	}
    }
	
	private boolean isHasFileList(File path){
		
		File files[] = path.listFiles();
		if(files.length > 0)
			return true;
		else 
			return false;
		
	}
	
	public void setText(String message){
		text.setText(message);
	}
	
	public String getUncPath(){
		return textField.getText() + File.separator;
	}
	
	public boolean isPathVoid(){
		return textField.getText().equals("");
	}
	
	public boolean isError(){
		return error;
	}
	
}
