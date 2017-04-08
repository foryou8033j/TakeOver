package mil.navy.takingover.view.tab.tippack;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import mil.navy.takingover.MainApp;
import mil.navy.takingover.model.otherpassword.OtherPassword;
import mil.navy.takingover.model.tip.TipData;
import mil.navy.takingover.model.tip.TipDataWrapper;
import mil.navy.takingover.util.ExceptionDialog;
import mil.navy.takingover.util.file.ModifyHandler;
import mil.navy.takingover.util.file.NIOFileCopy;

public class TipLayoutController implements Initializable{
	
	ObservableList<GridPane> pane = FXCollections.observableArrayList();
	
	@FXML
	ListView<GridPane> listView;
	
	MainApp mainApp;
	
	@FXML
	TextField textField;
	
	@FXML
	Button search;
	
	//현재 검색 중인 index넘버
	int index = 0;
	
	private File file = null;
	
	//파일 멀티 쓰레딩을 위한 java nio 클래스
	private Path path = null;
	
	private boolean currentLoading = false;
	private boolean currentSaveing = false;
	
	public static String fileName ="tipData.xml";
	
	
	final IntegerProperty dragFromIndex = new SimpleIntegerProperty(-1);
	private boolean wasOverlab = false;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

	public TipLayoutController() {
		// TODO Auto-generated constructor stub
	}
	
	@FXML
	private void handleSearch()
	{
		doSearch(textField.getText());
	}
	
	private void doSearch(String text)
	{
		//빈 박스일 경우 -> 무시
		if(text.equals(""))
			return;
		else
		{
			if(index >= mainApp.getTipDatas().size())
				index = 0;
			
			for(TipData list:mainApp.getTipDatas())
			{
				
				String str = list.getTitle() + list.getContents();
				
				if(str.contains(text))
				{
					listView.getSelectionModel().select(index);
					listView.getFocusModel().focus(index);
					listView.scrollTo(index);
					listView.getSelectionModel().getSelectedItem().requestFocus();
					index++;
					textField.requestFocus();
					return;
				}
				index++;
			}

		}
	}
	
	private void setContextMenu()
	{
		
		MenuItem add = new MenuItem("추가");
		MenuItem edit = new MenuItem("변경");
		MenuItem delete = new MenuItem("삭제");
		MenuItem refresh = new MenuItem("새로 고침");
		
		ContextMenu menu = new ContextMenu(add);
		
		
		listView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			public void handle(MouseEvent arg0) {

				if (arg0.getButton() == MouseButton.SECONDARY && listView.getSelectionModel().getSelectedItem() == null) {
					menu.getItems().clear();
					menu.getItems().addAll(add, refresh);
					menu.show(listView, arg0.getScreenX(), arg0.getScreenY());
				} else if (arg0.getButton() == MouseButton.SECONDARY && listView.getSelectionModel().getSelectedItem() != null) {
					menu.getItems().clear();
					menu.getItems().addAll(add, edit, delete, refresh);
					menu.show(listView, arg0.getScreenX(), arg0.getScreenY());
				} else {
					listView.getSelectionModel().clearSelection();
					menu.hide();
				}
			};
		});
		
		add.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				showEditStage();
			}
		});
		
		edit.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				
				int index = listView.getSelectionModel().getSelectedIndex();
				TipData data = mainApp.getTipDatas().get(index); 
				
				showEditStage().setContents(index, data.getTitle(), data.getContents() );
				
			}
		});
		
		delete.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				int index = listView.getSelectionModel().getSelectedIndex();
				TipData data = mainApp.getTipDatas().remove(index);
				
				saveDataToFile();
				
			}
		});
		
		refresh.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				loadDataFromFile();
			}
		});
		
		listView.setContextMenu(menu);
		
	}
	
	private TipInputLayoutController showEditStage()
	{
		try
		{
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource("TipInputLayoutControllerLayout.fxml"));
			GridPane layout= (GridPane) loader.load();

			TipInputLayoutController controller = loader.getController();
			
			Stage stage = new Stage();
			stage.setTitle("팁 정보 추가");
			stage.initOwner(mainApp.getPrimaryStage());
			stage.initModality(Modality.WINDOW_MODAL);
			stage.getIcons().add(mainApp.icon);
			stage.setResizable(false);
			
			controller.setMainApp(mainApp);
			controller.setStage(stage);

			
			Scene scene = new Scene(layout);
			stage.setScene(scene);
			
			stage.show();
			
			return controller;

		}catch (Exception e)
		{
			e.printStackTrace();
			new ExceptionDialog(AlertType.ERROR, "오류", "오류가 발생했습니다.", "기타 패스워드 탭 레이아웃을 초기화 하던 도중 오류가 발생 했습니다.", e.getMessage())
					.showAndWait();
			System.exit(0);
		}
		
		return null;
	}
	
	public void setMainApp(MainApp mainApp)
	{
		this.mainApp = mainApp;
		
		setContextMenu();
		listView.setItems(pane);
		
		
		file = new File(MainApp.path + fileName);
		File uncFile = new File(MainApp.uncPath +"OptionDatas" + File.separator+ fileName);
		
		if(!uncFile.exists())
			saveDataToFile(file);
		
		
		Platform.runLater(() -> {
			new TipPackModifyListener(mainApp).start();
		});
		
		
		
		//리스트에대한 리스닝 옵션 활성화
		mainApp.getTipDatas().addListener(new ListChangeListener<TipData>() {
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends TipData> arg0) {
				
				pane.clear();
				
				for(TipData list:mainApp.getTipDatas())
				{
					try
					{
						FXMLLoader loader = new FXMLLoader(this.getClass().getResource("TipContentControllerLayout.fxml"));
						GridPane layout= (GridPane) loader.load();
						
						TipContentController controller = loader.getController();
						controller.setPane(layout);
						controller.setContents(list.getTitle(), list.getContents());
						
						pane.add(layout);
						
						
						
					}catch (Exception e)
					{
						e.printStackTrace();
					}
					
				}
					
			}
		});
		
		//검색창에서 Enter키를 입력했을 때 옵션
		textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent arg0) {
				if(arg0.getCode() == KeyCode.ENTER)
				{
					doSearch(textField.getText());
				}
			}
		});
		
		setListCellDragAndDrop();
		
		loadDataFromFile();
		//listView.setItems(mainApp.getOtherPasswordList());
		
		//detectModifyHandler();
	}
	
	@FXML
	private void tabClickHandler()
	{
		
	}
	
	private void setListCellDragAndDrop()
	{
		
		listView.setCellFactory(new Callback<ListView<GridPane>, ListCell<GridPane>>() {
			
			@Override
			public ListCell<GridPane> call(ListView<GridPane> lv) {
				final ListCell<GridPane> cell = new ListCell<GridPane>(){
					@Override
					protected void updateItem(GridPane item, boolean empty) {
						super.updateItem(item, empty);
						if(empty){
							setGraphic(null);
						}else{
							setGraphic(item);
						}
						setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
					}
				};
				
				
				
				cell.setOnDragDetected(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent arg0) {
						if(!cell.isEmpty()){
							dragFromIndex.set(cell.getIndex());
							Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
							db.setDragView(mainApp.getHeadLayoutController().createSnapShot(cell), arg0.getX(), arg0.getY());
							
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
						if(dragFromIndex.get() >= 0 && dragFromIndex.get() != cell.getIndex()){
							event.acceptTransferModes(TransferMode.MOVE);
							
						}
					}
				});
				
				cell.setOnDragEntered(new EventHandler<DragEvent>() {
					@Override
					public void handle(DragEvent arg0) {
						if(dragFromIndex.get() >=0 && dragFromIndex.get() != cell.getIndex()){
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
						if(cell.isEmpty()){
							dragItemsStartIndex = dragFromIndex.get();
							dragItemsEndIndex = listView.getItems().size();
							direction = -1;
						}else{
							if(cell.getIndex() < dragFromIndex.get()){
								dragItemsStartIndex = cell.getIndex();
								dragItemsEndIndex = dragFromIndex.get() + 1;
								direction = 1;
							}else{
								dragItemsStartIndex = dragFromIndex.get();
								dragItemsEndIndex = cell.getIndex() + 1;
								direction = -1;
							}
						}
						
						List<OtherPassword> rotatingItems = mainApp.getOtherPasswordList().subList(dragItemsStartIndex, dragItemsEndIndex);
						List<OtherPassword> rotatingItemsCopy = new ArrayList<>(rotatingItems);
						Collections.rotate(rotatingItemsCopy, direction);
						rotatingItems.clear();
						rotatingItems.addAll(rotatingItemsCopy);
						dragFromIndex.set(-1);
					}
				});
				
				
				cell.setOnDragDone(new EventHandler<DragEvent>() {
					@Override
					public void handle(DragEvent arg0) {
						dragFromIndex.set(-1);
						//파일 저장
						saveDataToFile();
					}
				});
				
				return cell;
			}
		});
	}
	
	
	
	public boolean loadDataFromFile(File file)
	{
		try{
			//불러온 파일 목록을 로컬 드라이브로 복사한다.
			NIOFileCopy.copy(MainApp.uncPath +"OptionDatas" + File.separator+ fileName, MainApp.path + fileName);

			JAXBContext context = JAXBContext.newInstance(TipDataWrapper.class);
			Unmarshaller um = context.createUnmarshaller();
			
			//파일로 부터 XML을 읽은 다음 역 마샬링 한다.
			TipDataWrapper wrapper = (TipDataWrapper) um.unmarshal(file);
			
			
			mainApp.getTipDatas().clear();
			mainApp.getTipDatas().addAll(wrapper.getData());
			
			
			System.out.println("loaded + " + file.getPath());
			
			return true;
		}
		catch (Exception e)
		{
			
			//ignore
			e.printStackTrace();
			/*
			new ExceptionDialog(AlertType.ERROR, e.getMessage(), "오류가 발생했습니다.", "파일을 불러오던 도중 오류가 발생 했습니다.", e.getStackTrace().toString())
					.showAndWait();*/
			//System.exit(0);
			return false;
		}
	}
	
	public void saveDataToFile(File file)
	{
		try
		{
			ModifyHandler.currentModity(fileName);
			File dir = new File(MainApp.path);
			if(!dir.exists())
				dir.mkdir();
			
			this.path = Paths.get(file.getPath());
			
			JAXBContext context = JAXBContext.newInstance(TipDataWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			//데이터를 감싼다.
			TipDataWrapper wrapper = new TipDataWrapper();
			wrapper.setDatas(mainApp.getTipDatas());
			
			//마샬링 후 XML파일에 저장한다.
			m.marshal(wrapper, file);
			
			//마샬링한 데이터를 unc경로로 전송한다.
			NIOFileCopy.copy(MainApp.path + fileName, MainApp.uncPath +"OptionDatas" + File.separator+ fileName);
			
			new Thread(() -> {
				try {
					new Robot().delay(1000);
				} catch (AWTException e) {
					//ignore
				}
				ModifyHandler.removeModify(fileName);
			}).start();
		}catch (Exception e)
		{
			e.printStackTrace();
			//ignore
			/*
			new ExceptionDialog(AlertType.ERROR, e.getMessage(), "오류가 발생했습니다.", "파일을 저장하던 도중 오류가 발생 했습니다.", e.getStackTrace().toString())
					.showAndWait();*/
			//System.exit(0);
		}
	}
	
	
	public boolean loadDataFromFile()
	{
		currentLoading = true;
		if(file != null && !currentSaveing)
		{
			if(loadDataFromFile(file))
			{
				currentLoading = false;
				return true;	
			}
		}
		
		currentLoading = false;
		return true;
	}
	
	public void saveDataToFile()
	{
		currentSaveing = true;
		if(file != null && !currentLoading)
			saveDataToFile(file);
		currentSaveing = false;
	}
	
	
	
	
}