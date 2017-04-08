package mil.navy.takingover.view.startmanager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mil.navy.takingover.MainApp;

public class ManagerStage extends Stage{
	
	
	
	public ManagerStage(MainApp mainApp) {

		getIcons().add(mainApp.icon);
		initModality(Modality.WINDOW_MODAL);
		initStyle(StageStyle.UNDECORATED);
		
		try{
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource("ManagerStageLayout.fxml"));
			AnchorPane pane = (AnchorPane) loader.load();
			
			ManagerStageController controller = loader.getController();
			controller.setMainApp(mainApp, this);
			
			Scene scene = new Scene(pane);
			setScene(scene);
			
		}catch (Exception e){
			
		}
		
		
		
	}
	
}
