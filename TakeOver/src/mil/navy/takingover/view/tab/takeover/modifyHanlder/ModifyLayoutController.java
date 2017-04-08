package mil.navy.takingover.view.tab.takeover.modifyHanlder;

import java.awt.Robot;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mil.navy.takingover.MainApp;
import mil.navy.takingover.util.Notification.DesktopNotify;
import mil.navy.takingover.util.file.FileNameConverter;
import mil.navy.takingover.util.file.ModifyHandler;
import mil.navy.takingover.view.logview.LogStage;

/**
 * HaedLayout 탭에서 동일한 페이지를 수정중인 사용자가 있을 경우 수정 제한을 위해 Layout을 제한하는 클래스
 * 
 * @author navy
 *
 */
public class ModifyLayoutController extends Thread {

	private MainApp mainApp;

	Stage hoverStage = new Stage();
	BorderPane borderPane = new BorderPane();

	Text text = new Text("현재 수정중인 테이블 입니다");
	Text bottomText = new Text("수정 중인 PC가 없는 경우 " + MainApp.uncPath + ModifyHandler.modifyFileName + " 파일을 삭제 해 보세요.");

	private boolean wasOverlab = false;

	public ModifyLayoutController(MainApp mainApp) {
		this.mainApp = mainApp;

		text.setFont(new Font(24));

		bottomText.setFont(new Font(12));

		borderPane.setCenter(text);
		borderPane.setBottom(bottomText);

		hoverStage.initOwner(mainApp.getPrimaryStage());
		hoverStage.initStyle(StageStyle.UNDECORATED);

		Scene scene = new Scene(borderPane);

		hoverStage.setScene(scene);

		mainApp.getPrimaryStage().xProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

				double posX = newValue.doubleValue() + (mainApp.getPrimaryStage().getWidth()) / 2
						- (hoverStage.getWidth() / 2);
				hoverStage.setX(posX);

			}
		});

		mainApp.getPrimaryStage().yProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

				double posY = newValue.doubleValue() + (mainApp.getPrimaryStage().getHeight()) / 2
						- (hoverStage.getHeight());
				hoverStage.setY(posY);

			}
		});

		mainApp.getPrimaryStage().widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

				double posX = mainApp.getPrimaryStage().getX() + (newValue.doubleValue()) / 2
						- (hoverStage.getWidth() / 2);
				hoverStage.setX(posX);
			}
		});

		mainApp.getPrimaryStage().heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

				double posY = mainApp.getPrimaryStage().getY() + (newValue.doubleValue()) / 2
						- (hoverStage.getHeight());
				hoverStage.setY(posY);
			}
		});

	}

	@Override
	public void run() {

		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {

				if(mainApp.uncMode) doAction();

			}
		}, 50, 300);

	}

	private void doAction() {

		if (isOpenTakeOverTab()) {
			if (mainApp.getHeadLayoutController().getSelectedData() != null) {
				if (isSelectedData()) {
					wasOverlab = true;
					showModifyDialog();
				} else {
					reloadTakeOverTab();
				}

			}
		}else {
			reloadTakeOverTab();
		}
	}

	private void showModifyDialog() {
		Platform.runLater(() -> {
			System.out.println("Current modifying Table.");
			LogStage.append("현재 수정중인 테이블입니다. " + getSelectedDataPath());

			text.setText(getSelectedDataPath() + "에서 수정중인 테이블입니다.");
			mainApp.getHeadLayoutController().setLayoutUseable(false);

			double posX = mainApp.getPrimaryStage().getX() + (mainApp.getPrimaryStage().getWidth()) / 2
					- (hoverStage.getWidth() / 2);
			hoverStage.setX(posX);
			double posY = mainApp.getPrimaryStage().getY() + (mainApp.getPrimaryStage().getHeight()) / 2
					- (hoverStage.getHeight());
			hoverStage.setY(posY);

			hoverStage.show();
		});
		
	}

	/**
	 * 현재 선택된 테이블 데이터의 주소를 반환한다.
	 * 
	 * @return
	 */
	private String getSelectedDataPath() {
		return ModifyHandler.getOverlabInfo(
				FileNameConverter.toString(mainApp.getHeadLayoutController().getSelectedData().getLocalDate())
						+ ".xml");
	}

	/**
	 * 현재 데이터가 선택되어 있는지 확인한다.
	 * 
	 * @return
	 */
	private boolean isSelectedData() {
		return ModifyHandler.isCurrentModify(
				FileNameConverter.toString(mainApp.getHeadLayoutController().getSelectedData().getLocalDate()) + ".xml");
	}

	/**
	 * 현재 TakeOver 탭을 보고 있는지 확인한다.
	 * 
	 * @return
	 */
	private boolean isOpenTakeOverTab() {
		return mainApp.getRootLayoutController().getTakeOverTab().isSelected();
	}
	
	private void reloadTakeOverTab() {
		new Thread(() -> {
			if (wasOverlab) {
				Platform.runLater(() -> {
					wasOverlab = false;
					mainApp.showNotification("데이터가 변경되었습니다.", "인수인계 테이블의 데이터 변화가 감지되어 로드하였습니다.", DesktopNotify.INFORMATION, 5000L);
					LogStage.append("인수인계 " + mainApp.getHeadLayoutController().getSelectedData().getLocalDate() + " 데이터의 변화가 감지되어 로드하였습니다.");
					mainApp.getRootLayoutController().getHeadLayoutController().showDataDetails(mainApp.getRootLayoutController().getHeadLayoutController().getSelectedData());
				});
			}
			
			Platform.runLater(() -> {
				hoverStage.close();
				mainApp.getHeadLayoutController().setLayoutUseable(true);
			});

		}).start();
	}

}
