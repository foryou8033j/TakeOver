package mil.navy.takingover.view;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import mil.navy.takingover.MainApp;
import mil.navy.takingover.model.Data;
import mil.navy.takingover.model.InternetAcceptData;
import mil.navy.takingover.model.TakeOverData;

public class HeadLayoutController implements Initializable {

	public boolean isAbleUse = false;
	//public boolean isAbleToClickTakeOverList = true;
	public boolean isAbleToClickInternetAccept = true;

	public Data curSelectedData;

	@FXML
	private TableView<Data> dataTable;
	@FXML
	private TableColumn<Data, String> dataDateColumn;

	@FXML
	private Text dateText;

	@FXML
	private Text takeOverTitleText;

	ObservableList<BorderPane> ObservableTakeOverList = FXCollections.observableArrayList();

	@FXML
	private ListView<BorderPane> takeOverList;

	@FXML
	private Text internetOrderTitleText;

	@FXML
	private TableView<InternetAcceptData> internetOrderTable;

	@FXML
	private TableColumn<InternetAcceptData, String> internetObjectColumn;

	@FXML
	private TableColumn<InternetAcceptData, String> internetNameColumn;

	@FXML
	private TableColumn<InternetAcceptData, String> internetIPColumn;

	@FXML
	private TableColumn<InternetAcceptData, String> internetPolicyColumn;

	@FXML
	private TableColumn<InternetAcceptData, String> internetGroundColumn;

	@FXML
	private TableColumn<InternetAcceptData, String> internetWorkerColumn;

	@FXML
	private Text todayMemberText;

	private MainApp mainApp;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		dataDateColumn.setCellValueFactory(cellData -> cellData.getValue().getDateStringProperty());
		showDataDetails(null);
		dataTable.getSelectionModel().selectedItemProperty()
				.addListener((Observable, oldValue, newValue) -> showDataDetails(newValue));

	}

	/**
	 * 현재 화면에 선택된 테이블의 데이터를 보여 주는 작업을 한다.
	 * 
	 * @param data
	 */
	private void showDataDetails(Data data) {
		if (data != null) {
			
			// 데이터의 변경에 따른 기본 옵션을 변경하는 쓰레드
			new Thread(new Runnable() {
				@Override
				public void run() {
					curSelectedData = data;
					mainApp.ableToLoad = true;
					takeOverList.setDisable(false);
					internetOrderTitleText.setDisable(false);
					internetOrderTable.setDisable(false);
					isAbleUse = true;
					isAbleToClickInternetAccept = true;
				}
			}).start();

			// 데이터의 초기화, 값 수정에 필요한 쓰레드
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					String month = String.valueOf(data.getLocalDate().getMonth().getValue());

					if (data.getLocalDate().getMonth().getValue() < 10)
						month = month.replace(month, "0" + month);

					String fileName = "saveFile_" + data.getLocalDate().getYear() + "." + month + "."
							+ data.getLocalDate().getDayOfMonth() + ".xml";
					
						data.loadDataFromFile(new File(MainApp.path + fileName));

						dateText.setText(data.getLocalDate().getYear() + "년 " + data.getLocalDate().getMonthValue() + "월 "
								+ data.getLocalDate().getDayOfMonth() + "일 인수인계사항");

					//data.loadDataFromFile();
				}
			});

			// 하단 오늘 당직자 인원을 표시해 주는 부분
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					data.getMembers().addListener(new ListChangeListener<String>() {
						@Override
						public void onChanged(javafx.collections.ListChangeListener.Change<? extends String> arg0) {
							printCurMemberList(data);
						}
					});

					if (data.getMembers().size() != 0)
					{
						printCurMemberList(data);
						todayMemberText.setFill(Paint.valueOf("black"));
					}
					else {
						todayMemberText.setText("여기를 클릭해 오늘 당직자를 지정 해 주세요.");
						todayMemberText.setFill(Paint.valueOf("red"));
						//mainApp.showMemberInputLayout();
					}

				}
			});

			

			// 인터넷 운용 승인의 값을 처리 하기 위한 부분

			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					internetOrderTable.setItems(data.getInternetAcceptData());
					internetObjectColumn.setCellValueFactory(cellData -> cellData.getValue().getObjectProperty());
					internetNameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
					internetIPColumn.setCellValueFactory(cellData -> cellData.getValue().getIpProperty());
					internetPolicyColumn.setCellValueFactory(cellData -> cellData.getValue().getPolicyProperty());
					internetGroundColumn.setCellValueFactory(cellData -> cellData.getValue().getGroundProperty());
					internetWorkerColumn.setCellValueFactory(cellData -> cellData.getValue().getWorkerProperty());

					data.getInternetAcceptData().addListener(new ListChangeListener<InternetAcceptData>() {
						@Override
						public void onChanged(
								javafx.collections.ListChangeListener.Change<? extends InternetAcceptData> arg0) {
							new Thread(new Runnable() {
								
								@Override
								public void run() {
									data.saveDataToFile();
								}
							}).start();
						}
					});
				}
			});
			
			int size = data.getTakeOverData().size();


			// 등록된 인계사항이 없을 때 임시로 보여지는 Pane

			if (data.getTakeOverData().size() == 0) {
				ObservableTakeOverList.clear();
				BorderPane pane = new BorderPane();
				Text text = new Text("등록 된 인계사항이 없습니다.");
				text.setFont(new Font(24));

				pane.setCenter(text);
				pane.setDisable(true);

				ObservableTakeOverList.add(pane);
			} else {
				ObservableTakeOverList.clear();
				// 데이터에 기존에 저장되어 있는 값을 보여주기 위한 쓰레드
				for (int i = 0; i < size; i++) {
					ObservableTakeOverList
							.add(mainApp.addTakeOverLayout(data.getTakeOverData().get(i).getTitle(),
									data.getTakeOverData().get(i).getObject(),
									data.getTakeOverData().get(i).getOrderer()
									));
				}
				data.saveDataToFile();
				
			}

			// 각 데이터의 변동 사항을 리스닝 하기 위한 부분.
			data.getTakeOverData().addListener(new ListChangeListener<TakeOverData>() {

				@Override
				public void onChanged(
						javafx.collections.ListChangeListener.Change<? extends TakeOverData> arg0) {
					

					int size = data.getTakeOverData().size();

					if (size == 0) {
						ObservableTakeOverList.clear();
						BorderPane pane = new BorderPane();
						Text text = new Text("등록 된 인계사항이 없습니다.");
						text.setFont(new Font(24));

						pane.setCenter(text);
						pane.setDisable(true);

						ObservableTakeOverList.add(pane);
					} else {
						ObservableTakeOverList.clear();
						for (int i = 0; i < size; i++) {
							ObservableTakeOverList
							.add(mainApp.addTakeOverLayout(data.getTakeOverData().get(i).getTitle(),
									data.getTakeOverData().get(i).getObject(),
									data.getTakeOverData().get(i).getOrderer()
									));
						}
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								data.saveDataToFile();
							}
						}).start();
					}
				}
			});
			
			

		}

		else {
			isAbleUse = false;
			dateText.setText("인수인계 날짜를 지정해주세요.");
			takeOverList.setDisable(true);
			internetOrderTitleText.setDisable(true);
			internetOrderTable.setDisable(true);
		}

	}

	public HeadLayoutController() {
		// TODO Auto-generated constructor stub
	}

	@FXML
	private void setTodayMembers() {
		if (isAbleUse) {
			mainApp.showMemberInputLayout();
		}

	}

	private void setTakeOverAddMenu() {
		ContextMenu contextMenu = new ContextMenu();
		MenuItem addTakeOver = new MenuItem("인계사항 추가");
		MenuItem editTakeOver = new MenuItem("인계사항 변경");
		MenuItem removeTakeOver = new MenuItem("인계사항 삭제");
		contextMenu.getItems().addAll(addTakeOver);

		addTakeOver.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				takeOverList.getSelectionModel().clearSelection();
				if (curSelectedData.getTakeOverData().size() == 0) {
					ObservableTakeOverList.clear();
				}
				ObservableTakeOverList.add(mainApp.addTakeOverInputLayout("", "", ""));
			}

		});

		editTakeOver.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {

				int index = takeOverList.getSelectionModel().getSelectedIndex();
				takeOverList.getSelectionModel().clearSelection();
				ObservableTakeOverList.remove(index);
				TakeOverData tmpData = curSelectedData.getTakeOverData().get(index);
				String title = tmpData.getTitle();
				String contents = tmpData.getObject();
				String orderer = tmpData.getOrderer();
				ObservableTakeOverList.add(index, mainApp.addTakeOverInputLayout(index, title, contents, orderer));
			}

		});

		removeTakeOver.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				curSelectedData.getTakeOverData().remove(takeOverList.getSelectionModel().getSelectedIndex());
			}

		});

		takeOverList.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {

				if (arg0.getButton() == MouseButton.SECONDARY
						&& takeOverList.getSelectionModel().getSelectedItem() == null) {
					contextMenu.getItems().clear();
					contextMenu.getItems().addAll(addTakeOver);
					contextMenu.show(takeOverList, arg0.getScreenX(), arg0.getScreenY());
				} else if (arg0.getButton() == MouseButton.SECONDARY
						&& takeOverList.getSelectionModel().getSelectedItem() != null
						&& (curSelectedData.getTakeOverData().size() > 0)) {
					contextMenu.getItems().clear();
					contextMenu.getItems().addAll(addTakeOver, editTakeOver, removeTakeOver);
					contextMenu.show(takeOverList, arg0.getScreenX(), arg0.getScreenY());
				} else if (arg0.getButton() == MouseButton.SECONDARY
						&& takeOverList.getSelectionModel().getSelectedItem() != null) {
					contextMenu.getItems().clear();
					contextMenu.getItems().addAll(addTakeOver);
					contextMenu.show(takeOverList, arg0.getScreenX(), arg0.getScreenY());
				} else {
					// TODO 지속적으로 클릭하면 파일 읽는 도중 다시 읽어와 Exception 이 일어나는 문제점 있음.
					// curSelectedData.loadDataFromFile();
					takeOverList.getSelectionModel().clearSelection();
					contextMenu.hide();
				}

			}
		});

	}

	private void setTakeOverDateListRefresh() {
		ContextMenu contextMenu = new ContextMenu();
		MenuItem refresh = new MenuItem("오늘 인계 생성하기(새로고침)");

		contextMenu.getItems().add(refresh);

		refresh.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				mainApp.loadFileList();
				mainApp.isAbleNewFileToDay();
				reLoad();
			}
		});

		dataTable.setContextMenu(contextMenu);

	}

	private void setAddInternetOrderMenu() {
		isAbleToClickInternetAccept = false;

		ContextMenu contextMenu = new ContextMenu();
		MenuItem addInternetAccept = new MenuItem("인터넷 운용승인 추가");
		MenuItem editInternetAccept = new MenuItem("인터넷 운용승인 변경");
		MenuItem removeInternetAccept = new MenuItem("인터넷 운용승인 삭제");

		/**
		 * 인터넷 운용승인 등록
		 */
		addInternetAccept.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				mainApp.showInternetUserDialog(-1);
			}

		});

		/**
		 * 인터넷 운용승인 변경
		 */
		editInternetAccept.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				int index = internetOrderTable.getSelectionModel().getSelectedIndex();
				mainApp.showInternetUserDialog(index);
				contextMenu.hide();
			}

		});

		/**
		 * 인터넷 운용승인 삭제
		 */
		removeInternetAccept.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				curSelectedData.getInternetAcceptData()
						.remove(internetOrderTable.getSelectionModel().getSelectedItem());
				contextMenu.hide();
			}

		});

		internetOrderTable.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			public void handle(MouseEvent arg0) {

				if (arg0.getButton() == MouseButton.SECONDARY
						&& internetOrderTable.getSelectionModel().getSelectedItem() == null) {
					contextMenu.getItems().clear();
					contextMenu.getItems().addAll(addInternetAccept);
					contextMenu.show(internetOrderTable, arg0.getScreenX(), arg0.getScreenY());
				} else if (arg0.getButton() == MouseButton.SECONDARY
						&& internetOrderTable.getSelectionModel().getSelectedItem() != null) {
					contextMenu.getItems().clear();
					contextMenu.getItems().addAll(addInternetAccept, editInternetAccept, removeInternetAccept);
					contextMenu.show(internetOrderTable, arg0.getScreenX(), arg0.getScreenY());
				} else {
					internetOrderTable.getSelectionModel().clearSelection();
					contextMenu.hide();
				}
			};
		});
	}

	public void printCurMemberList(Data data) {
		if (data.getMembers().size() == 0 || (data.getMembers().get(0).equals("")))
		{
			todayMemberText.setText("여기를 클릭해 오늘 당직자를 지정 해 주세요.");
			todayMemberText.setFill(Paint.valueOf("red"));
		}
		else {
			int memberSize = data.getMembers().size();
			String members = data.getMembers().get(0);
			for (int i = 1; i < memberSize; i++)
				members = members.concat("\t" + data.getMembers().get(i));
			todayMemberText.setText(members);
			todayMemberText.setFill(Paint.valueOf("black"));
		}
	}

	/**
	 * 현재 화면에 보여지는 데이터를 갱신한다.
	 */
	public void reLoad() {
		if (curSelectedData != null)
			curSelectedData.loadDataFromFile();
		showDataDetails(curSelectedData);
	}

	/**
	 * 인수인계에 표시되는 BorderPane 리스트를 반환한다.
	 * 
	 * @return
	 */
	public ObservableList<BorderPane> getTakeOverList() {
		return ObservableTakeOverList;
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

		dataTable.setItems(mainApp.getDatas());
		takeOverList.setItems(ObservableTakeOverList);
		setTakeOverAddMenu();
		setAddInternetOrderMenu();
		setTakeOverDateListRefresh();
		todayMemberText.setText("");
	}

}
