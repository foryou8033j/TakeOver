package mil.navy.takingover.view.root.option.passwordchange;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mil.navy.takingover.MainApp;

public class PasswordChangeDialog extends Stage{

	public PasswordChangeDialog(MainApp mainApp) {
	
		initOwner(mainApp.getPrimaryStage());
		initModality(Modality.WINDOW_MODAL);
		getIcons().add(mainApp.icon);
		setTitle("패스워드 변경");
		setResizable(false);
		
		setOnCloseRequest(Event -> {
			close();
		});
		
		try{
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource("PasswordChangeDialogLayout.fxml"));
			AnchorPane pane = (AnchorPane) loader.load();
			
			PasswordChangeDialogLayoutController controller = loader.getController();
			controller.setMainApp(mainApp, this);
			
			Scene scene = new Scene(pane);
			setScene(scene);
			
		}catch (Exception e){
			
		}
		
		
		
	}
	
	
	
}
