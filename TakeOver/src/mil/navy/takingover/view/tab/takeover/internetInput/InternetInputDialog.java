package mil.navy.takingover.view.tab.takeover.internetInput;

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
import mil.navy.takingover.view.tab.takeover.HeadLayoutController;

/**
 * 인터넷 운용 승인 정보를 입력하는 Dialog를 보여준다.
 * @author navy
 *
 */
public class InternetInputDialog extends Stage {


	public InternetInputDialog(HeadLayoutController takeOverController, int index) {

		try {
			getIcons().add(takeOverController.getMainApp().icon);
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource("InternetUserLayout.fxml"));
			AnchorPane layout = (AnchorPane) loader.load();

			InternetUserLayoutController controller = loader.getController();
			controller.setMainApp(takeOverController.getMainApp(), this);

			String title = "인터넷 운용승인 추가";

			if (index == -1)
				title = "인터넷 운용승인 추가";
			else {
				title = "인터넷 운용승인 변경";
				controller.setDefaultData(index);
			}

			setTitle(title);
			setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent arg0) {
					String fileName = FileNameConverter
							.toString(takeOverController.getMainApp().getHeadLayoutController().getSelectedData().getLocalDate()) + ".xml";
					ModifyHandler.removeModify(fileName);
					close();
				}
			});
			initOwner(takeOverController.getMainApp().getPrimaryStage());
			initModality(Modality.WINDOW_MODAL);

			Scene scene = new Scene(layout);
			setScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
			new ExceptionDialog(AlertType.ERROR, "오류", "오류가 발생했습니다.", "인터넷 사용자 등록 레이아웃을 로드하던 도중 오류가 발생 했습니다.",
					e.getMessage()).showAndWait();
			System.exit(0);
		}

	}
}
