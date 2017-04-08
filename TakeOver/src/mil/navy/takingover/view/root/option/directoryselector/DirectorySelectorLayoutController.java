package mil.navy.takingover.view.root.option.directoryselector;

import java.io.File;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import mil.navy.takingover.MainApp;
import mil.navy.takingover.util.Regedit;
import mil.navy.takingover.util.file.FileHandler;
import mil.navy.takingover.view.loading.LoadLayout;
import mil.navy.takingover.view.logview.LogStage;

public class DirectorySelectorLayoutController {
    
 
    @FXML
    private TextField umlPath;
    
    @FXML
    private TextField localPath;
    
    @FXML
    private Button umlButton;
    
    @FXML
    private Button localButton;
    
    @FXML
    private Button save;

    private MainApp mainApp;
    private Stage stage;
    
    LoadLayout loadStage;
    
    @FXML
    void initialize() {

    }
    
    @FXML
    void handleSave()
    {
    	try{
    		
    		new Thread(() -> {
    			
    			Platform.runLater(() -> {
    				
    				FileHandler.Delete(mainApp.path);
            		
            		mainApp.uncPath = umlPath.getText();
                	mainApp.path = localPath.getText();
                	
                	Regedit.setRegistry("uncPath", umlPath.getText());
                	Regedit.setRegistry("localPath", localPath.getText());
        			
        			mainApp.setTitle();
                	mainApp.loadFileList();
                	mainApp.isAbleNewFileToDay();
                	
                	mainApp.getRootLayoutController().getOfficeIdentityLayoutController().loadDataFromFile();
                	mainApp.getRootLayoutController().getOtherPasswordLayoutController().loadDataFromFile();
                	mainApp.getRootLayoutController().getTipLayoutController().loadDataFromFile();
                	mainApp.getRootLayoutController().getPhoneLayoutController().loadDataFromFile();
                	mainApp.getRootLayoutController().getGetOffLayoutController().loadDataFromFile();
                	
                	LogStage.append("사용자에 의해 데이터 폴더 경로가 수정되었습니다.\n" + umlPath.getText());
                	
                	stage.close();
    				
    			});

    			
    		}).start();
    		
        	
    	}catch (Exception e)
    	{
    		
    	}
    	
    }
    
    @FXML
    void handleSetUmlPath()
    {
    	umlPath.setText(showDirectoryChooser(umlPath.getText()).getAbsolutePath() + "\\");
    }
    
    @FXML
    void handleSetLocalPath()
    {
    	localPath.setText(showDirectoryChooser(localPath.getText()).getAbsolutePath() + "\\");
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
    			return selectedDirectory;
    		}
    	}
    	
    }
    
    
    
    public void setMainApp(MainApp mainApp, Stage stage)
    {
    	this.mainApp = mainApp;
    	this.stage = stage;
    	
    	umlPath.setText(mainApp.uncPath);
    	localPath.setText(mainApp.path);
    }
}
