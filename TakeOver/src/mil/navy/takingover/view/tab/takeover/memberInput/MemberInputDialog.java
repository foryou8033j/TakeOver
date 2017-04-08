package mil.navy.takingover.view.tab.takeover.memberInput;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import mil.navy.takingover.MainApp;
import mil.navy.takingover.util.ExceptionDialog;
import mil.navy.takingover.util.file.FileNameConverter;
import mil.navy.takingover.util.file.ModifyHandler;

/**
 * 인계 인원을 지정하는 프레임을 띄운다.
 * @author navy
 *
 */
public class MemberInputDialog extends Stage{
	
	MainApp mainApp;
	
	public MemberInputDialog(MainApp mainApp) {

		this.mainApp = mainApp;
		
		try
		{
			getIcons().add(mainApp.icon);
			
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource("MemberLayout.fxml"));
			AnchorPane layout= (AnchorPane) loader.load();
			
			MemberLayoutController controller = loader.getController();
			controller.setMainApp(mainApp, this);

			setTitle("인계 인원 지정");
			setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent arg0) {
					String fileName = FileNameConverter.toString(mainApp.getHeadLayoutController().getSelectedData().getLocalDate()) + ".xml";
					ModifyHandler.removeModify(fileName);
					close();
				}
			});
			
			initOwner(mainApp.getPrimaryStage());
			initModality(Modality.WINDOW_MODAL);
			
			Scene scene = new Scene(layout);
			setScene(scene);
			
			
		}catch (Exception e)
		{
			e.printStackTrace();
			new ExceptionDialog(AlertType.ERROR, "오류", "오류가 발생했습니다.", "인계 인원 지정 레이아웃을 로드하던 도중 오류가 발생 했습니다.", e.getMessage()).showAndWait();
			System.exit(0);
		}
		
	}
	
	
}
