package mil.navy.takingover.view.tab.officerIdentity;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import mil.navy.takingover.MainApp;
import mil.navy.takingover.model.officerIdentity.OfficerIdentity;
import mil.navy.takingover.model.officerIdentity.OfficerIdentityWrapper;
import mil.navy.takingover.util.file.ModifyHandler;
import mil.navy.takingover.util.file.NIOFileCopy;

public class OfficerIdentityLayoutController implements Initializable{

	
	//ContextMenu 활성화를 위한 enum
	enum SELECTED_CELL {BLANK, DATA, DATA_NO_UP, DATA_NO_DOWN};
	
	@FXML
	TableView<OfficerIdentity> table;
	
	@FXML
	Button save;
	
	@FXML
	Button load;
	
	TableColumn name;
	
	TableColumn id;
	
	TableColumn password;
	
	private File file = null;
	
	private boolean currentLoading = false;
	private boolean currentSaveing = false;
	
	public static String fileName ="passwordList.xml";
	
	public MainApp mainApp;
	
	final IntegerProperty dataDragFromData = new SimpleIntegerProperty(-1);
	
	
	public OfficerIdentityLayoutController() {
		// TODO Auto-generated constructor stub
	}
	
	public void setMainApp(MainApp mainApp)
	{
		this.mainApp =mainApp; 
		
		table.setItems(mainApp.getPasswordList());
		
		file = new File(MainApp.path + fileName);
		
		File uncFile = new File(MainApp.uncPath +"OptionDatas" + File.separator+ fileName);
		if(!uncFile.exists())
			saveDataToFile(file);
		else
			loadDataFromFile(file);
		
		Platform.runLater(() -> {
			new OfficeIdentityModifyListener(mainApp).start();
		});
		
		if( table.getItems().size() == 0)
			handleAddMember();
		
		//setCellDragAndDrop(name);
		//setCellDragAndDrop(id);
		//setCellDragAndDrop(password);
		//setDefaultSet();
	}
	
	@FXML
	private void handleSave()
	{
		saveDataToFile();
	}
	
	@FXML
	private void handleLoad()
	{
		loadDataFromFile();
	}
	
	private ContextMenu setDefaultSet(SELECTED_CELL TYPE)
	{
		ContextMenu contextMenu = new ContextMenu();
		MenuItem up = new MenuItem(" △");
		MenuItem down =  new MenuItem(" ▽");
		MenuItem add = new MenuItem("추가");
		MenuItem delete = new MenuItem("삭제");

		table.setContextMenu(contextMenu);
		
		
		up.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				int index = table.getSelectionModel().getSelectedIndex();
				int desIndex = index-1;
				
				if(desIndex < 0) return;
				else{	
					OfficerIdentity tmpData = table.getItems().get(desIndex);
					table.getItems().set(desIndex, table.getItems().get(index));
					table.getItems().set(index, tmpData);
				}
				
				saveDataToFile();
			}
		});
		
		down.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				int index = table.getSelectionModel().getSelectedIndex();
				int desIndex = index+1;
				
				if(desIndex > table.getItems().size()) return;
				else{	
					OfficerIdentity tmpData = table.getItems().get(desIndex);
					table.getItems().set(desIndex, table.getItems().get(index));
					table.getItems().set(index, tmpData);
				}
				
				saveDataToFile();
			}
		});
		
		
		add.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				handleAddMember();
			}

		});
		
		delete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				mainApp.getPasswordList().remove(table.getSelectionModel().getSelectedItem());
				saveDataToFile();
			}

		});
		
		
		if(SELECTED_CELL.BLANK == TYPE)
			contextMenu.getItems().addAll(add);
		else if(SELECTED_CELL.DATA == TYPE)
			contextMenu.getItems().addAll(up, add, delete, down);
		else if(SELECTED_CELL.DATA_NO_UP == TYPE)
			contextMenu.getItems().addAll(add, delete, down);
		else if(SELECTED_CELL.DATA_NO_DOWN == TYPE)
			contextMenu.getItems().addAll(up, add, delete);
		
		return contextMenu;
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		/*File file = new File(MainApp.path + fileName);
		loadDataFromFile(file);*/
		
		table.setEditable(true);
		
		Callback<TableColumn, TableCell> cellFactory =
				new Callback<TableColumn, TableCell>() {
			public TableCell call(TableColumn p){
				return new EditingCell();
			}
			
			
		};
		
		name = new TableColumn("이름");
		name.setCellValueFactory(
				new PropertyValueFactory<OfficerIdentity, String>("fieldValue"));
		
		id = new TableColumn("ID");
		id.setCellValueFactory(
				new PropertyValueFactory<OfficerIdentity, String>("fieldValue"));
		
		password = new TableColumn("패스워드");
		password.setCellValueFactory(
				new PropertyValueFactory<OfficerIdentity, String>("fieldValue"));
		
		
		name.setCellValueFactory(cellData -> ((CellDataFeatures<OfficerIdentity, String>) cellData).getValue().getNameProPerty());
		id.setCellValueFactory(cellData -> ((CellDataFeatures<OfficerIdentity, String>) cellData).getValue().getidProPerty());
		password.setCellValueFactory(cellData -> ((CellDataFeatures<OfficerIdentity, String>) cellData).getValue().getPasswordProPerty());
		
		name.setCellFactory(cellFactory);
		name.setOnEditCommit(
				new EventHandler<TableColumn.CellEditEvent<OfficerIdentity,String>>() {
					
					public void handle(CellEditEvent<OfficerIdentity, String> arg0) {
						((OfficerIdentity)arg0.getTableView().getItems().get(arg0.getTablePosition().getRow())).setName(arg0.getNewValue());
						saveDataToFile();
					}
		});
		
		id.setCellFactory(cellFactory);
		id.setOnEditCommit(
				new EventHandler<TableColumn.CellEditEvent<OfficerIdentity,String>>() {
					
					public void handle(CellEditEvent<OfficerIdentity, String> arg0) {
						((OfficerIdentity)arg0.getTableView().getItems().get(arg0.getTablePosition().getRow())).setId(arg0.getNewValue());
						saveDataToFile();
					}
		});
		
		password.setCellFactory(cellFactory);
		password.setOnEditCommit(
				new EventHandler<TableColumn.CellEditEvent<OfficerIdentity,String>>() {
					
					public void handle(CellEditEvent<OfficerIdentity, String> arg0) {
						((OfficerIdentity)arg0.getTableView().getItems().get(arg0.getTablePosition().getRow())).setPassword(arg0.getNewValue());
						saveDataToFile();
					}
		});
		

		table.getColumns().addAll(name, id , password);
			
		
		showDataDetails(null);
		//table.getSelectionModel().selectedItemProperty()
		//		.addListener((Observable, oldValue, newValue) -> showDataDetails(newValue));
	}
	
	private void showDataDetails(OfficerIdentity data)
	{
		
	}
	
	private void handleAddMember()
	{
		mainApp.getPasswordList().add(new OfficerIdentity("이름을 입력하세요", "ID를 입력하세요", "PW를 입력하세요"));
	}
	
	public class EditingCell extends TableCell<OfficerIdentity, String>{
		
		
		private TextField textField;
		
		final StringProperty file = new SimpleStringProperty();
		
		
		
		
		
		public EditingCell() {
	
		}
		
		@Override
		public void startEdit(){
			super.startEdit();
			
			
			if(textField == null){
				createTextField();
			}
			
			setGraphic(textField);
			setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			textField.selectAll();
			
		}
		
		@Override
		public void cancelEdit(){
			super.cancelEdit();
			
			setText(String.valueOf(getItem()));
			setContentDisplay(ContentDisplay.TEXT_ONLY);
			
			
		}
		
		
		
		
		@Override
		public void updateItem(String item, boolean empty){
			super.updateItem(item, empty);
			
			if(empty){
				setText(null);
				setGraphic(null);
				setContextMenu(setDefaultSet(SELECTED_CELL.BLANK));
				setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent arg0) {
							table.getSelectionModel().clearSelection();
					}
				});
			}else{
				if(isEditing()){
					if(textField != null){
						textField.setText(getString());
					}
					setGraphic(textField);

					setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
				}else{
					setText(getString());
					setContentDisplay(ContentDisplay.TEXT_ONLY);
					
				}
				

				if(getIndex() == 0)
					setContextMenu(setDefaultSet(SELECTED_CELL.DATA_NO_UP));
				else if(getIndex() == table.getItems().size()-1)
					setContextMenu(setDefaultSet(SELECTED_CELL.DATA_NO_DOWN));
				else
					setContextMenu(setDefaultSet(SELECTED_CELL.DATA));
				
				setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent arg0) {
						if(arg0.getButton() == MouseButton.PRIMARY)
							table.getSelectionModel().select(getIndex());
					}
				});
			}
		}
		
		private void createTextField(){
			final BooleanProperty entered = new SimpleBooleanProperty();
			textField = new TextField(getString());
			textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
			textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
				public void handle(KeyEvent t) {
					if(t.getCode() == KeyCode.ENTER)
						commitEdit(textField.getText());
					else if(t.getCode() == KeyCode.ESCAPE)
						cancelEdit();
				};
			});
			textField.setOnMouseExited(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent arg0) {
					entered.set(false);
				}
			});
			
			textField.setOnMouseEntered(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent arg0) {
					entered.set(true);
				}
			});
			textField.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent arg0) {
					if(!entered.get())
						cancelEdit();
				}
			});
		}
		
		private String getString(){
			return getItem() == null ? "" : getItem().toString();
		}
		
		

	}
	
	public boolean loadDataFromFile(File file)
	{
		try{
			//불러온 파일 목록을 로컬 드라이브로 복사한다.
			
			NIOFileCopy.copy(MainApp.uncPath +"OptionDatas" + File.separator + fileName, MainApp.path + fileName);

			JAXBContext context = JAXBContext.newInstance(OfficerIdentityWrapper.class);
			Unmarshaller um = context.createUnmarshaller();
			
			//파일로 부터 XML을 읽은 다음 역 마샬링 한다.
			OfficerIdentityWrapper wrapper = (OfficerIdentityWrapper) um.unmarshal(file);
			
			
			mainApp.getPasswordList().clear();
			mainApp.getPasswordList().addAll(wrapper.getData());
			
			
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
			
			JAXBContext context = JAXBContext.newInstance(OfficerIdentityWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			//데이터를 감싼다.
			OfficerIdentityWrapper wrapper = new OfficerIdentityWrapper();
			wrapper.setDatas(mainApp.getPasswordList());
			
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
			//ignore
			/*e.printStackTrace();
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
	
	
	/**
	 * 드래그 온 한 데이터의 스냅샷을 구현한다.
	 * @param node
	 * @return
	 */
	public WritableImage createSnapShot(Node node){
		SnapshotParameters snapshotParameters = new SnapshotParameters();
		WritableImage image = node.snapshot(snapshotParameters, null);
		return image;
	}
	
}
