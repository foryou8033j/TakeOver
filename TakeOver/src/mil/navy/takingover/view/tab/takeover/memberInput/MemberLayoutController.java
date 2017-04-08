package mil.navy.takingover.view.tab.takeover.memberInput;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import mil.navy.takingover.MainApp;
import mil.navy.takingover.util.file.FileNameConverter;
import mil.navy.takingover.util.file.ModifyHandler;

public class MemberLayoutController implements Initializable {

	@FXML
	TextArea textArea;

	@FXML
	Button button;

	private MainApp mainApp;

	private Stage stage;

	private boolean controlKeyPressed = false;
	private boolean enterKeyPressed = false;

	public MemberLayoutController() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

	@FXML
	private void handleAcceptButton() {
		
		String fileName = FileNameConverter
				.toString(mainApp.getSelectedTakeOverData().getLocalDate()) + ".xml";
		ModifyHandler.removeModify(fileName);
		
		String output = textArea.getText();

		String[] members = output.split("\\r?\\n");

		mainApp.getSelectedTakeOverData().getMembers().clear();

		for (int i = 0; i < members.length; i++)
			mainApp.getSelectedTakeOverData().getMembers().add(members[i]);

		stage.close();
		mainApp.getSelectedTakeOverData().saveDataToFile();
		mainApp.getHeadLayoutController().printCurMemberList(mainApp.getSelectedTakeOverData());
		mainApp.ableToLoad = true;
	}

	public void setMainApp(MainApp mainApp, Stage stage) {
		this.mainApp = mainApp;
		this.stage = stage;
		mainApp.ableToLoad = false;

		// 현재 수정중인 데이터
		String fileName = FileNameConverter.toString(mainApp.getSelectedTakeOverData().getLocalDate())
				+ ".xml";
		ModifyHandler.currentModity(fileName);

		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent arg0) {
				// 현재 수정이 끝난 데이터
				String fileName = FileNameConverter
						.toString(mainApp.getSelectedTakeOverData().getLocalDate()) + ".xml";
				ModifyHandler.removeModify(fileName);
			};
		});

		int size = mainApp.getSelectedTakeOverData().getMembers().size();

		if (size > 0) {
			for (int i = 0; i < size; i++)
				textArea.appendText(mainApp.getSelectedTakeOverData().getMembers().get(i) + "\n");
		}

		/**
		 * obejct 텍스트필드에서 컨트롤 + 엔터키를 눌렀을 때 사용자 입력 칸으로 넘어 가도록 한다.
		 */
		textArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent arg0) {

				if (arg0.getCode() == KeyCode.CONTROL) {
					controlKeyPressed = true;
					if (enterKeyPressed && controlKeyPressed) {
						controlKeyPressed = false;
						enterKeyPressed = false;
						handleAcceptButton();
					}
				} else if (arg0.getCode() == KeyCode.ENTER) {
					enterKeyPressed = true;
					if (enterKeyPressed && controlKeyPressed) {
						controlKeyPressed = false;
						enterKeyPressed = false;
						handleAcceptButton();
					}
				}
			};
		});
		textArea.setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent arg0) {
				if (arg0.getCode() == KeyCode.CONTROL) {
					controlKeyPressed = false;
				} else if (arg0.getCode() == KeyCode.ENTER) {
					enterKeyPressed = false;
				}

			};

		});

	}

}
