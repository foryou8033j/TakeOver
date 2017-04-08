package mil.navy.takingover.view.tab.takeover;

import java.awt.Robot;
import java.awt.TrayIcon.MessageType;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Callback;
import mil.navy.takingover.MainApp;
import mil.navy.takingover.model.Data;
import mil.navy.takingover.model.InternetAcceptData;
import mil.navy.takingover.model.TakeOverData;
import mil.navy.takingover.util.ExceptionDialog;
import mil.navy.takingover.util.Notification.DesktopNotify;
import mil.navy.takingover.util.file.FileNameConverter;
import mil.navy.takingover.util.file.ModifyHandler;
import mil.navy.takingover.view.loading.LoadLayout;
import mil.navy.takingover.view.logview.LogStage;
import mil.navy.takingover.view.tab.takeover.internetInput.InternetInputDialog;
import mil.navy.takingover.view.tab.takeover.memberInput.MemberInputDialog;
import mil.navy.takingover.view.tab.takeover.modifyHanlder.ModifyLayoutController;

public class HeadLayoutController implements Initializable {

	HeadLayoutController headLayoutController = null;

	enum SELECT_DATA {
		TAKEOVER, INTERNET
	};

	// 인수인계 테이블의 Context 메뉴를 만들어 주는 enum
	enum TAKEOVER_CONTEXT_TYPE {
		BLANK, TAKEOVERDATA
	};

	// 인터넷 사용자 테이블의 Context 메뉴를 만들어 주는 enum
	enum INTERNET_CONTEXT_TYPE {
		BLACK, INTERNETDATA
	};

	// 현재 인수 인계 테이블 사용이 가능한지 확인
	public boolean isAbleUse = false;

	// 현재 인수 인계 테이블의 인터넷 사용자 등록 정보 사용이 가능한지 확인
	public boolean isAbleToClickInternetAccept = true;

	// 현재 선택된 테이블의 Data
	Data curSelectedData;

	@FXML
	private TreeView<Data> dataTree;

	/*
	 * @FXML private TableView<Data> dataTable;
	 * 
	 * @FXML private TableColumn<Data, String> dataDateColumn;
	 */

	@FXML
	private Text dateText;

	@FXML
	private Text takeOverTitleText;

	private ObservableList<BorderPane> ObservableTakeOverList = FXCollections.observableArrayList();

	@FXML
	private ListView<BorderPane> takeOverList;

	@FXML
	private Text internetOrderTitleText;

	@FXML
	private TableView<InternetAcceptData> internetOrderTable;

	@FXML
	private TableColumn<InternetAcceptData, String> internetObjectColumn;

	@FXML
	private TableColumn<InternetAcceptData, String> internetGroupColumn;

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

	@FXML
	private TextField textField;

	@FXML
	private Button search;

	private MainApp mainApp;

	final IntegerProperty searchIndex = new SimpleIntegerProperty(-1);
	final IntegerProperty searchTakeOverIndex = new SimpleIntegerProperty(0);
	final IntegerProperty searchInternetIndex = new SimpleIntegerProperty(0);

	final IntegerProperty takeOverDataDragFromIndex = new SimpleIntegerProperty(-1);
	final IntegerProperty internetDataDragFromIndex = new SimpleIntegerProperty(-1);

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		showDataDetails(null);

		dataTree.setCellFactory(new Callback<TreeView<Data>, TreeCell<Data>>() {
			@Override
			public TreeCell<Data> call(TreeView<Data> arg0) {
				return new DataTreeCell();
			}
		});

		dataTree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<Data>>() {
			@Override
			public void changed(ObservableValue<? extends TreeItem<Data>> observable, TreeItem<Data> oldValue,
					TreeItem<Data> newValue) {

				if (!dataTree.getRoot().equals(newValue)) {
					try {
						showDataDetails(newValue.getValue());
					} catch (Exception e) {

					}

				}

			}
		});

	}

	/**
	 * MainApp을 반환한다.
	 * 
	 * @return
	 */
	public MainApp getMainApp() {
		return mainApp;
	}

	/**
	 * 현재 선택된 데이터를 반환한다.
	 * 
	 * @return Data
	 */
	public Data getSelectedData() {
		return curSelectedData;
	}

	public void setLayoutUseable(boolean set) {
		if (set) {
			todayMemberText.setDisable(false);
			takeOverList.setDisable(false);
			internetOrderTable.setDisable(false);
		} else {
			todayMemberText.setDisable(true);
			takeOverList.setDisable(true);
			internetOrderTable.setDisable(true);
		}
	}

	private void printData() {
		System.out.println(searchIndex.get() + " " + searchTakeOverIndex.get() + " " + searchInternetIndex.get());
	}

	// TODO 검색 기능 문제 확인 필요
	@FXML
	private void doSearch() {

		if (searchIndex.get() < 0)
			searchIndex.set(mainApp.getDatas().size() - 1);

		boolean searched = false;

		// 일별 인수인계 데이터를 마지막 부터 첫번째 까지 순차적으로 이동한다.
		for (int DataLoop = searchIndex.get(); DataLoop >= 0; DataLoop--) {

			// 일별 인수인계 데이터를 로드한다.
			Data data = mainApp.getDatas().get(DataLoop);
			String fileName = FileNameConverter.toString(data.getLocalDate()) + ".xml";
			data.loadDataFromFile(new File(MainApp.path + fileName));
			printData();

			boolean searchTakeOverData = false;

			ObservableList<TakeOverData> takeOverDatas = data.getTakeOverData();
			if ((searchTakeOverIndex.get() < takeOverDatas.size())) {

				if (searchTakeOverIndex.get() < 0)
					searchTakeOverIndex.set(0);

				for (int takeOverLoop = searchTakeOverIndex.get(); takeOverLoop < takeOverDatas
						.size(); takeOverLoop++) {

					TakeOverData selectedTakeOverData = takeOverDatas.get(takeOverLoop);

					String result = selectedTakeOverData.getTitle().concat(selectedTakeOverData.getObject());
					printData();
					if (result.contains(textField.getText())) {
						searched = true;
						searchTakeOverData = true;
						showDataDetails(data, SELECT_DATA.TAKEOVER, takeOverLoop);
						searchTakeOverIndex.set(searchTakeOverIndex.get() + 1);
						printData();
						break;
					}
					searchTakeOverIndex.set(searchTakeOverIndex.get() + 1);
					printData();
				}
				searchTakeOverData = false;
			}

			if (!searchTakeOverData) {
				searchTakeOverIndex.set(0);
				printData();

				ObservableList<InternetAcceptData> internetDatas = data.getInternetAcceptData();

				if (searchInternetIndex.get() < internetDatas.size()) {

					if (searchInternetIndex.get() < 0)
						searchInternetIndex.set(0);

					for (int internetLoop = searchInternetIndex.get(); internetLoop < internetDatas
							.size(); internetLoop++) {

						InternetAcceptData selectedInternetData = internetDatas.get(internetLoop);
						String result = selectedInternetData.getGround() + selectedInternetData.getGroup()
								+ selectedInternetData.getIp() + selectedInternetData.getName()
								+ selectedInternetData.getObject() + selectedInternetData.getPolicy();
						if (result.contains(textField.getText())) {
							searched = true;
							showDataDetails(data, SELECT_DATA.INTERNET, internetLoop);
							searchInternetIndex.set(searchInternetIndex.get() + 1);
							printData();
							break;
						}
						searchInternetIndex.set(searchInternetIndex.get() + 1);
						printData();
					}

				} else {
					searchTakeOverIndex.set(0);
					searchInternetIndex.set(0);
					printData();
				}

			}

			if (searched)
				break;
			else {
				searchIndex.set(searchIndex.get() - 1);
				printData();
			}
		}
		searchIndex.set(searchIndex.get() - 1);
		printData();

		if (searchIndex.get() < 0) {
			searchIndex.set(mainApp.getDatas().size() - 1);
			searchTakeOverIndex.set(0);
			searchInternetIndex.set(0);
			System.out.println("검색결과 없음");
			printData();
			
			mainApp.showNotification("검색 실패", "검색 된 결과가 없습니다.", DesktopNotify.FAIL, 10000L);
			
			showDataDetails(null);
		}

	}

	public void showDataDetails(Data data) {
		showDataDetails(data, null, -1);
	}

	/**
	 * 현재 화면에 선택된 테이블의 데이터를 보여 주는 작업을 한다.
	 * 
	 * @param data
	 */
	public void showDataDetails(Data data, SELECT_DATA selData, int index) {

		// 새로운 데이터를 클릭했을 때 기존 데이터의 수정 진행을 삭제한다.
		if (curSelectedData != null) {
			String fileName = FileNameConverter
					.toString(mainApp.getHeadLayoutController().curSelectedData.getLocalDate()) + ".xml";
			ModifyHandler.removeModify(fileName);
		}

		if (data != null) {

			Platform.runLater(() -> {
				LoadLayout worker = new LoadLayout("데이터를 불러오는 중...", mainApp.getPrimaryStage());

				Task<Void> task = new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						// TODO Auto-generated method stub
						LogStage.append(data.getLocalDate() + " 인수인계 데이터를 로드합니다.");

						// 데이터의 변경에 따른 기본 옵션을 변경하는 쓰레드
						new Thread(new Runnable() {
							@Override
							public void run() {
								LogStage.append("인수인계 데이터 레이아웃 변경 사항을 초기화합니다.");
								curSelectedData = data;
								mainApp.ableToLoad = true;
								takeOverList.setDisable(false);
								todayMemberText.setDisable(false);
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

								String fileName = FileNameConverter.toString(data.getLocalDate()) + ".xml";

								data.loadDataFromFile(new File(MainApp.path + fileName));

								dateText.setText(
										data.getLocalDate().getYear() + "년 " + data.getLocalDate().getMonthValue()
												+ "월 " + data.getLocalDate().getDayOfMonth() + "일 인수인계사항");

							}
						});

						// 하단 오늘 당직자 인원을 표시해 주는 부분
						Platform.runLater(new Runnable() {

							@Override
							public void run() {
								data.getMembers().addListener(new ListChangeListener<String>() {
									@Override
									public void onChanged(
											javafx.collections.ListChangeListener.Change<? extends String> arg0) {
										printCurMemberList(data);
									}
								});

								if (data.getMembers().size() != 0) {
									printCurMemberList(data);
									todayMemberText.setFill(Paint.valueOf("black"));
								} else {
									todayMemberText.setText("여기를 클릭해 오늘 근무자를 지정 해 주세요.");
									todayMemberText.setFill(Paint.valueOf("red"));

									mainApp.showNotification("근무자를 지정 해 주세요", "오늘 근무자가 비어있습니다.", DesktopNotify.HELP, 5000L);
									
								}

							}
						});

						// 인터넷 운용 승인의 값을 처리 하기 위한 부분

						Platform.runLater(new Runnable() {

							@Override
							public void run() {
								LogStage.append("인터넷 운용 승인 데이터를 초기화합니다.");
								internetOrderTable.setItems(data.getInternetAcceptData());
								internetObjectColumn
										.setCellValueFactory(cellData -> cellData.getValue().getObjectProperty());
								internetGroupColumn
										.setCellValueFactory(cellData -> cellData.getValue().getGroupProperty());
								internetNameColumn
										.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
								internetIPColumn.setCellValueFactory(cellData -> cellData.getValue().getIpProperty());
								internetPolicyColumn
										.setCellValueFactory(cellData -> cellData.getValue().getPolicyProperty());
								internetGroundColumn
										.setCellValueFactory(cellData -> cellData.getValue().getGroundProperty());
								internetWorkerColumn
										.setCellValueFactory(cellData -> cellData.getValue().getWorkerProperty());

								data.getInternetAcceptData().addListener(new ListChangeListener<InternetAcceptData>() {
									@Override
									public void onChanged(
											javafx.collections.ListChangeListener.Change<? extends InternetAcceptData> arg0) {

										internetObjectColumn.setCellValueFactory(
												cellData -> cellData.getValue().getObjectProperty());
										internetGroupColumn.setCellValueFactory(
												cellData -> cellData.getValue().getGroupProperty());
										internetNameColumn
												.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
										internetIPColumn
												.setCellValueFactory(cellData -> cellData.getValue().getIpProperty());
										internetPolicyColumn.setCellValueFactory(
												cellData -> cellData.getValue().getPolicyProperty());
										internetGroundColumn.setCellValueFactory(
												cellData -> cellData.getValue().getGroundProperty());
										internetWorkerColumn.setCellValueFactory(
												cellData -> cellData.getValue().getWorkerProperty());

									}

								});

								if (SELECT_DATA.INTERNET == selData) {
									internetOrderTable.getSelectionModel().select(index);
									internetOrderTable.getFocusModel().focus(index);
									internetOrderTable.scrollTo(index);
								}
							}

						});

						// 인수인계 데이터의 값을 처리하기 위한 부분
						Platform.runLater(new Runnable() {

							@Override
							public void run() {

								LogStage.append("인수인계 데이터를 초기화합니다.");
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
									for (TakeOverData loopData : data.getTakeOverData()) {
										ObservableTakeOverList.add(addTakeOverLayout(loopData));
									}

								}

								// 각 데이터의 변동 사항을 리스닝 하기 위한 부분.
								data.getTakeOverData().addListener(new ListChangeListener<TakeOverData>() {

									@Override
									public void onChanged(
											javafx.collections.ListChangeListener.Change<? extends TakeOverData> arg0) {

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
											for (TakeOverData loopData : data.getTakeOverData()) {
												ObservableTakeOverList.add(addTakeOverLayout(loopData));
											}
										}
									}
								});

								if (SELECT_DATA.TAKEOVER == selData) {

									takeOverList.getSelectionModel().select(index);
									takeOverList.getFocusModel().focus(index);
									takeOverList.scrollTo(index);

								}

							}
						});

						return null;
					}
				};

				worker.activateProgress(task);

				task.setOnSucceeded(event -> {
					worker.close();
				});

				Thread thread = new Thread(task);
				thread.setDaemon(true);
				thread.start();

			});

		}

		else {

			// 기존 추가, 변경 창을 끄지 않고 바로 테이블을 옮겼다고 가정할 때.
			if (curSelectedData != null) {
				String fileName = FileNameConverter
						.toString(mainApp.getHeadLayoutController().curSelectedData.getLocalDate()) + ".xml";
				ModifyHandler.removeModify(fileName);
			}

			isAbleUse = false;
			dateText.setText("인수인계 날짜를 지정해주세요.");
			ObservableTakeOverList.clear();
			takeOverList.setDisable(true);
			todayMemberText.setDisable(true);
			internetOrderTitleText.setDisable(true);
			internetOrderTable.setDisable(true);
			internetOrderTable.setItems(null);
			todayMemberText.setText("");
		}

	}

	public HeadLayoutController() {
		// TODO Auto-generated constructor stub
	}

	@FXML
	private void setTodayMembers() {

		if (isAbleUse) {
			new MemberInputDialog(mainApp).showAndWait();
		}

	}

	private ContextMenu getTakeOverContextMenu(TAKEOVER_CONTEXT_TYPE TYPE) {
		return getTakeOverContextMenu(TYPE, 0);
	}

	private ContextMenu getTakeOverContextMenu(TAKEOVER_CONTEXT_TYPE TYPE, int index) {
		ContextMenu contextMenu = new ContextMenu();
		MenuItem addTakeOver = new MenuItem("인계사항 추가");
		MenuItem editTakeOver = new MenuItem("인계사항 변경");
		MenuItem removeTakeOver = new MenuItem("인계사항 삭제");

		/**
		 * 인계사항 추가
		 */
		addTakeOver.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				takeOverList.getSelectionModel().clearSelection();
				if (curSelectedData.getTakeOverData().size() == 0) {
					ObservableTakeOverList.clear();
				}

				ObservableTakeOverList.add(addTakeOver());

			}

		});

		/**
		 * 인계 사항 변경
		 */
		editTakeOver.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {

				int index = takeOverList.getSelectionModel().getSelectedIndex();
				takeOverList.getSelectionModel().clearSelection();
				ObservableTakeOverList.remove(index);
				ObservableTakeOverList.add(index, editTakeOver(index, curSelectedData.getTakeOverData().get(index)));
			}

		});

		/**
		 * 인계 사항 삭제
		 */
		removeTakeOver.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {

				curSelectedData.getTakeOverData().remove(takeOverList.getSelectionModel().getSelectedIndex());
				curSelectedData.saveDataToFile();

				String fileName = FileNameConverter
						.toString(mainApp.getHeadLayoutController().curSelectedData.getLocalDate()) + ".xml";

				new Thread(() -> {

					ModifyHandler.currentModity(fileName);

					try {
						new Robot().delay(500);
					} catch (Exception e) {

					}

					Platform.runLater(() -> {
						ModifyHandler.removeModify(fileName);
					});

				}).start();

			}

		});

		if (TYPE == TAKEOVER_CONTEXT_TYPE.TAKEOVERDATA) {
			contextMenu.getItems().addAll(addTakeOver, editTakeOver, removeTakeOver);
		} else {
			contextMenu.getItems().addAll(addTakeOver);
		}

		return contextMenu;

	}

	/**
	 * 수동으로 인수 인계 데이터를 생성 한다.
	 */
	private void setTakeOverDateListContextMenu() {
		ContextMenu contextMenu = new ContextMenu();
		MenuItem refresh = new MenuItem("오늘 인계 생성하기(새로고침)");

		showDataDetails(null);
		contextMenu.getItems().add(refresh);

		refresh.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {

				mainApp.loadFileList();

				mainApp.isAbleNewFileToDay();
				showDataDetails(null);

			}
		});

		dataTree.setContextMenu(contextMenu);

	}

	private ContextMenu getInternetContextMenu(INTERNET_CONTEXT_TYPE TYPE) {
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
				new InternetInputDialog(headLayoutController, -1).showAndWait();
			}

		});

		/**
		 * 인터넷 운용승인 변경
		 */
		editInternetAccept.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				int index = internetOrderTable.getSelectionModel().getSelectedIndex();
				new InternetInputDialog(headLayoutController, index).showAndWait();
				contextMenu.hide();
			}

		});

		/**
		 * 인터넷 운용승인 삭제
		 */
		removeInternetAccept.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {

				String fileName = FileNameConverter
						.toString(mainApp.getHeadLayoutController().curSelectedData.getLocalDate()) + ".xml";
				curSelectedData.getInternetAcceptData()
						.remove(internetOrderTable.getSelectionModel().getSelectedItem());
				contextMenu.hide();
				curSelectedData.saveDataToFile();

				new Thread(() -> {

					ModifyHandler.currentModity(fileName);

					try {
						new Robot().delay(500);
					} catch (Exception e) {

					}

					Platform.runLater(() -> {
						ModifyHandler.removeModify(fileName);
					});

				}).start();

			}

		});

		if (INTERNET_CONTEXT_TYPE.BLACK.equals(TYPE)) {
			contextMenu.getItems().addAll(addInternetAccept);
		} else {
			contextMenu.getItems().addAll(addInternetAccept, editInternetAccept, removeInternetAccept);
		}

		return contextMenu;
	}

	/**
	 * 현재 선택 된 데이터 정보를 인수인계 테이블에 보여준다.
	 * 
	 * @param data
	 */
	public void printCurMemberList(Data data) {
		if (data.getMembers().size() == 0 || (data.getMembers().get(0).equals(""))) {
			todayMemberText.setText("여기를 클릭해 오늘 당직자를 지정 해 주세요.");
			todayMemberText.setFill(Paint.valueOf("red"));
		} else {
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

	private void setDataTree() {
		LogStage.append("인수인계 데이터 날짜 트리를 구성합니다.");
		mainApp.getDatas().addListener(new ListChangeListener<Data>() {
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Data> arg0) {
				loadDataTree();
			}
		});
		loadDataTree();
	}

	private void loadDataTree() {
		try {
			// 검색을 위해 설정
			searchIndex.set(mainApp.getDatas().size() - 1);

			int month = 0;

			TreeItem<Data> root = new TreeItem<Data>(new Data());

			ObservableList<TreeItem<Data>> monthItem = FXCollections.observableArrayList();

			for (Data dataLoop : mainApp.getDatas()) {

				TreeItem<Data> monthItemTree = new TreeItem<Data>();
				if (month != dataLoop.getLocalDate().getMonthValue()) {
					month = dataLoop.getLocalDate().getMonthValue();
					monthItemTree = new TreeItem<Data>(dataLoop);
				} else {
					continue;
				}

				for (Data data : mainApp.getDatas()) {
					if (month == data.getLocalDate().getMonthValue()) {
						TreeItem<Data> item = new TreeItem<Data>(data);
						monthItemTree.getChildren().add(item);
					}
				}

				monthItem.add(monthItemTree);

			}

			monthItem.get(monthItem.size() - 1).setExpanded(true);
			root.getChildren().addAll(monthItem);

			root.setExpanded(true);

			dataTree.setRoot(root);
		} catch (ArrayIndexOutOfBoundsException aio) {
			// ignore
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * MainApp 클래스와 연동한다.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		this.headLayoutController = this;

		// 인수인계 데이터 목록을 구성한다.
		setDataTree();

		// dataTable.setItems(mainApp.getDatas()); //인수인계 테이블의 데이터 지정
		takeOverList.setItems(ObservableTakeOverList); // 인터넷사용자등록 테이블의 데이터 지정

		setTakeOverDateListContextMenu(); // 날짜 테이블의 컨텍스트 메뉴 지정

		// 현재 테이블의 수정 상황을 감지하는 쓰레드
		Platform.runLater(() -> {
			new ModifyLayoutController(mainApp).start();
		});

		textField.setPromptText("검색 할 내용을 입력하세요");
		textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent arg0) {
				if (arg0.getCode() == KeyCode.ENTER)
					doSearch();
			};
		});

		// 인수인계 구역 드래그앤 드랍 구현 지정
		setTakeOverListCellDragAndDrop();

		// 인터넷 사용자 등록 정보 영역 드래그앤 드랍 구현 지정
		setInternetCellDragAndDrop(internetObjectColumn);
		setInternetCellDragAndDrop(internetGroupColumn);
		setInternetCellDragAndDrop(internetNameColumn);
		setInternetCellDragAndDrop(internetIPColumn);
		setInternetCellDragAndDrop(internetPolicyColumn);
		setInternetCellDragAndDrop(internetGroundColumn);
		setInternetCellDragAndDrop(internetWorkerColumn);

		internetOrderTable.setContextMenu(getInternetContextMenu(INTERNET_CONTEXT_TYPE.BLACK));

	}

	/**
	 * 인수인계 데이터의 드래그 앤 드롭을 구현한다.
	 */
	private void setTakeOverListCellDragAndDrop() {

		takeOverList.setCellFactory(new Callback<ListView<BorderPane>, ListCell<BorderPane>>() {

			@Override
			public ListCell<BorderPane> call(ListView<BorderPane> lv) {
				final ListCell<BorderPane> cell = new ListCell<BorderPane>() {
					@Override
					protected void updateItem(BorderPane item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setContextMenu(getTakeOverContextMenu(TAKEOVER_CONTEXT_TYPE.BLANK));
							setGraphic(null);
						} else {
							if (curSelectedData.getTakeOverData().size() == 0)
								setContextMenu(getTakeOverContextMenu(TAKEOVER_CONTEXT_TYPE.BLANK));
							else
								setContextMenu(getTakeOverContextMenu(TAKEOVER_CONTEXT_TYPE.TAKEOVERDATA));
							setGraphic(item);
						}
						setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
					}
				};

				cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent arg0) {

						if (arg0.getButton() == MouseButton.PRIMARY) {
							if (cell.isEmpty())
								takeOverList.getSelectionModel().clearSelection();
							else
								takeOverList.getSelectionModel().select(cell.getItem());
						}

					}
				});

				// 셀 드래그를 탐지 했을 경우
				cell.setOnDragDetected(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent arg0) {
						if (!cell.isEmpty()) {

							// ModifyHandler.currentModity(name);
							String fileName = FileNameConverter.toString(
									mainApp.getHeadLayoutController().curSelectedData.getLocalDate()) + ".xml";
							ModifyHandler.currentModity(fileName);

							takeOverDataDragFromIndex.set(cell.getIndex());
							Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
							db.setDragView(createSnapShot(cell), arg0.getX(), arg0.getY());

							ClipboardContent content = new ClipboardContent();
							content.putString("");
							db.setContent(content);
							arg0.consume();
						}
					}
				});

				cell.setOnDragOver(new EventHandler<DragEvent>() {
					@Override
					public void handle(DragEvent event) {
						if (takeOverDataDragFromIndex.get() >= 0
								&& takeOverDataDragFromIndex.get() != cell.getIndex()) {
							event.acceptTransferModes(TransferMode.MOVE);
						}
					}
				});

				cell.setOnDragEntered(new EventHandler<DragEvent>() {
					@Override
					public void handle(DragEvent arg0) {
						if (takeOverDataDragFromIndex.get() >= 0
								&& takeOverDataDragFromIndex.get() != cell.getIndex()) {
							cell.setStyle("-fx-background-color: #99FFFF;");
						}

					}
				});

				cell.setOnDragExited(new EventHandler<DragEvent>() {
					@Override
					public void handle(DragEvent arg0) {
						cell.setStyle("");
					}
				});

				cell.setOnDragDropped(new EventHandler<DragEvent>() {
					@Override
					public void handle(DragEvent arg0) {

						int dragItemsStartIndex;
						int dragItemsEndIndex;
						int direction;
						if (cell.isEmpty()) {
							dragItemsStartIndex = takeOverDataDragFromIndex.get();
							dragItemsEndIndex = takeOverList.getItems().size();
							direction = -1;
						} else {
							if (cell.getIndex() < takeOverDataDragFromIndex.get()) {
								dragItemsStartIndex = cell.getIndex();
								dragItemsEndIndex = takeOverDataDragFromIndex.get() + 1;
								direction = 1;
							} else {
								dragItemsStartIndex = takeOverDataDragFromIndex.get();
								dragItemsEndIndex = cell.getIndex() + 1;
								direction = -1;
							}
						}

						List<TakeOverData> rotatingItems = curSelectedData.getTakeOverData()
								.subList(dragItemsStartIndex, dragItemsEndIndex);
						List<TakeOverData> rotatingItemsCopy = new ArrayList<>(rotatingItems);
						Collections.rotate(rotatingItemsCopy, direction);
						rotatingItems.clear();
						rotatingItems.addAll(rotatingItemsCopy);
						takeOverDataDragFromIndex.set(-1);
					}
				});

				// 드래그가 끝났을 때
				cell.setOnDragDone(new EventHandler<DragEvent>() {
					@Override
					public void handle(DragEvent arg0) {
						takeOverDataDragFromIndex.set(-1);
						curSelectedData.saveDataToFile();

						String fileName = FileNameConverter
								.toString(mainApp.getHeadLayoutController().curSelectedData.getLocalDate()) + ".xml";
						ModifyHandler.removeModify(fileName);
					}
				});

				return cell;
			}
		});

	}

	/**
	 * 인터넷 사용자 등록 정보 구역의 드래그앤드롭을 구현한다.
	 * 
	 * @param Column
	 *            적용 대상 컬럼
	 */
	private void setInternetCellDragAndDrop(TableColumn<InternetAcceptData, String> Column) {

		Column.setCellFactory(
				new Callback<TableColumn<InternetAcceptData, String>, TableCell<InternetAcceptData, String>>() {
					public TableCell<InternetAcceptData, String> call(TableColumn<InternetAcceptData, String> arg0) {
						final TableCell<InternetAcceptData, String> cell = new TableCell<InternetAcceptData, String>() {
							@Override
							protected void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (empty) {
									setText(null);
									internetOrderTable
											.setContextMenu(getInternetContextMenu(INTERNET_CONTEXT_TYPE.BLACK));
									setOnMouseClicked(new EventHandler<MouseEvent>() {
										@Override
										public void handle(MouseEvent arg0) {
											internetOrderTable.getSelectionModel().clearSelection();
										}
									});

								} else {
									setText(item);
									setContextMenu(getInternetContextMenu(INTERNET_CONTEXT_TYPE.INTERNETDATA));
									setOnMouseClicked(new EventHandler<MouseEvent>() {
										@Override
										public void handle(MouseEvent arg0) {
											if (arg0.getButton() == MouseButton.PRIMARY)
												internetOrderTable.getSelectionModel().select(getIndex());
										}
									});
								}
								setContentDisplay(ContentDisplay.TEXT_ONLY);
							}
						};

						// 셀 드래그를 탐지 했을 경우
						cell.setOnDragDetected(new EventHandler<MouseEvent>() {
							@Override
							public void handle(MouseEvent arg0) {
								if (!cell.isEmpty()) {

									// ModifyHandler.currentModity(name);
									String fileName = FileNameConverter.toString(
											mainApp.getHeadLayoutController().curSelectedData.getLocalDate()) + ".xml";
									ModifyHandler.currentModity(fileName);

									internetDataDragFromIndex.set(cell.getIndex());
									Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
									db.setDragView(createSnapShot(cell), arg0.getX(), arg0.getY());

									ClipboardContent content = new ClipboardContent();
									content.putString("");
									db.setContent(content);
									arg0.consume();
								}
							}
						});

						cell.setOnDragOver(new EventHandler<DragEvent>() {
							@Override
							public void handle(DragEvent event) {
								if (internetDataDragFromIndex.get() >= 0
										&& internetDataDragFromIndex.get() != cell.getIndex()) {
									event.acceptTransferModes(TransferMode.MOVE);
								}
							}
						});

						cell.setOnDragEntered(new EventHandler<DragEvent>() {
							@Override
							public void handle(DragEvent arg0) {
								if (internetDataDragFromIndex.get() >= 0
										&& internetDataDragFromIndex.get() != cell.getIndex()) {
									cell.setStyle("-fx-background-color: gold;");
								}

							}
						});

						cell.setOnDragExited(new EventHandler<DragEvent>() {
							@Override
							public void handle(DragEvent arg0) {
								cell.setStyle("");
							}
						});

						cell.setOnDragDropped(new EventHandler<DragEvent>() {
							@Override
							public void handle(DragEvent arg0) {

								int dragItemsStartIndex;
								int dragItemsEndIndex;
								int direction;
								if (cell.isEmpty()) {
									dragItemsStartIndex = internetDataDragFromIndex.get();
									dragItemsEndIndex = internetOrderTable.getItems().size();

									direction = -1;
								} else {
									if (cell.getIndex() < internetDataDragFromIndex.get()) {
										dragItemsStartIndex = cell.getIndex();
										dragItemsEndIndex = internetDataDragFromIndex.get() + 1;
										direction = 1;
									} else {
										dragItemsStartIndex = internetDataDragFromIndex.get();
										dragItemsEndIndex = cell.getIndex() + 1;
										direction = -1;
									}
								}

								List<InternetAcceptData> rotatingItems = curSelectedData.getInternetAcceptData()
										.subList(dragItemsStartIndex, dragItemsEndIndex);
								List<InternetAcceptData> rotatingItemsCopy = new ArrayList<>(rotatingItems);
								Collections.rotate(rotatingItemsCopy, direction);
								rotatingItems.clear();
								rotatingItems.addAll(rotatingItemsCopy);
								internetDataDragFromIndex.set(-1);
								internetOrderTable.getSelectionModel().clearSelection();
							}
						});

						// 드래그가 끝났을 때
						cell.setOnDragDone(new EventHandler<DragEvent>() {
							@Override
							public void handle(DragEvent arg0) {
								internetDataDragFromIndex.set(-1);
								curSelectedData.saveDataToFile();

								String fileName = FileNameConverter.toString(
										mainApp.getHeadLayoutController().curSelectedData.getLocalDate()) + ".xml";
								ModifyHandler.removeModify(fileName);
							}
						});

						return cell;
					}

				});

	}

	/**
	 * 드래그 온 한 데이터의 스냅샷을 구현한다.
	 * 
	 * @param node
	 * @return
	 */
	public WritableImage createSnapShot(Node node) {
		SnapshotParameters snapshotParameters = new SnapshotParameters();
		WritableImage image = node.snapshot(snapshotParameters, null);
		return image;
	}

	/**
	 * 인수인계를 입력하는 BorderPane를 인수인계 목록에 추가한다.
	 * 
	 * @param title
	 * @param object
	 * @param orderer
	 * @return
	 */
	public BorderPane addTakeOver() {
		try {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource("TakeOverInputBoxLayout.fxml"));
			BorderPane layout = (BorderPane) loader.load();

			TakeOverInputBoxLayoutController controller = loader.getController();
			controller.setMainApp(getMainApp(), layout);

			return layout;

		} catch (Exception e) {
			LogStage.append("[주의] 인수인계 입력 레이아웃을 초기화 하던 도중 오류가 발생하였습니다. " + e.getMessage());
			e.printStackTrace();
			new ExceptionDialog(AlertType.ERROR, "오류", "오류가 발생했습니다.", "인수인계를 입력하는 레이아웃을 로드하던 도중 오류가 발생 했습니다.", e)
					.showAndWait();
			System.exit(0);
		}

		return null;

	}

	/**
	 * 인수 인계를 변경하는 BorderPane을 인수인계 목록에 추가한다.
	 * 
	 * @param index
	 * @param title
	 * @param object
	 * @param orderer
	 * @return
	 */
	public BorderPane editTakeOver(int index, TakeOverData data) {
		try {

			FXMLLoader loader = new FXMLLoader(this.getClass().getResource("TakeOverInputBoxLayout.fxml"));
			BorderPane layout = (BorderPane) loader.load();

			TakeOverInputBoxLayoutController controller = loader.getController();
			controller.setMainApp(getMainApp(), layout);
			controller.setTextFieldForEdit(index, data);

			return layout;

		} catch (Exception e) {
			LogStage.append("[주의] 인수인계 수정 레이아웃을 초기화 하던 도중 오류가 발생하였습니다. " + e.getMessage());
			e.printStackTrace();
			new ExceptionDialog(AlertType.ERROR, "오류", "오류가 발생했습니다.", "인수인계를 수정하는 레이아웃을 로드하던 도중 오류가 발생 했습니다.", e)
					.showAndWait();
			System.exit(0);
		}
		return null;
	}

	/**
	 * 현재 인수인계 목록을 보여주는 BorderPane을 정의 한다.
	 * 
	 * @param title
	 * @param object
	 * @param orderer
	 * @return
	 */
	public BorderPane addTakeOverLayout(TakeOverData data) {
		try {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource("TakeOverLayout.fxml"));
			BorderPane layout = (BorderPane) loader.load();

			TakeOverLayoutController controller = loader.getController();
			controller.setMainApp(getMainApp(), layout);
			controller.setContents(data);
			return layout;

		} catch (Exception e) {
			LogStage.append("[주의] 인수인계 레이아웃을 초기화 하던 도중 오류가 발생하였습니다. " + e.getMessage());
			e.printStackTrace();
			new ExceptionDialog(AlertType.ERROR, "오류", "오류가 발생했습니다.", "인수인계를 보여주는 레이아웃을 로드하던 도중 오류가 발생 했습니다.", e)
					.showAndWait();
			System.exit(0);
		}

		return null;

	}

}
