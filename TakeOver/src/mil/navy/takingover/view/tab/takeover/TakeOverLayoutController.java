package mil.navy.takingover.view.tab.takeover;


import java.awt.Robot;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mil.navy.takingover.MainApp;
import mil.navy.takingover.model.TakeOverData;

public class TakeOverLayoutController implements Initializable{

private MainApp mainApp;
	
	private int index;
	
	@FXML
	Text title;
	
	@FXML
	Text object;
	
	@FXML
	Text orderer;
	
	@FXML
	ImageView imageView;
	
	
	private File imageFile; 
	
	private TakeOverData curTakeOverData;
	private BorderPane curBorderPane;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public TakeOverLayoutController() {
		// TODO Auto-generated constructor stub
	}
	
	public void setMainApp(MainApp mainApp, BorderPane curLayout)
	{
		this.mainApp = mainApp;
		this.curBorderPane = curLayout;
	}
	
	public TakeOverData getTakeOverData()
	{
		return curTakeOverData;
	}
	
	private void showImageBigFrame(String filePath)
	{
		Stage stage = new Stage();
		stage.initOwner(mainApp.getPrimaryStage());
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initStyle(StageStyle.UTILITY);
		stage.setTitle(imageFile.getName());
		
		
		stage.setMinHeight(100);
		stage.setMinWidth(120);
		stage.setMaxHeight(Double.MAX_VALUE);
		stage.setMaxWidth(Double.MAX_VALUE);
		
		ImageView bigImageView = new ImageView(loadImage(filePath));
		
		bigImageView.setFitWidth(bigImageView.getFitWidth());
		stage.setWidth(bigImageView.getFitWidth());
		//stage.setHeight(bigImageView.getFitHeight());
		//stage.setHeight(image.getHeight());
		bigImageView.setPreserveRatio(true);
		
		
		stage.heightProperty().addListener(new ChangeListener<Number>() {
		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			
			bigImageView.setFitHeight(newValue.doubleValue());
		}
		});
	
		//각 브라우저의 Width 리스너 등록
		stage.widthProperty().addListener(new ChangeListener<Number>() {
		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue,Number newValue) {
			bigImageView.setFitWidth(newValue.doubleValue());
		}
		});
		
		
		bigImageView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				stage.close();
			};
		});

		BorderPane border = new BorderPane();
		border.setCenter(bigImageView);

		Scene scene = new Scene(border);
		stage.setScene(scene);
		
		stage.show();
	}
	
	private void showImage(String filePath)
	{
		imageView.setImage(loadImage(filePath));
		imageView.setFitHeight(80);
		imageView.setPreserveRatio(true);
		
		imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				if(arg0.getButton() == MouseButton.PRIMARY)
				{
					showImageBigFrame(filePath);
				}
			};
		});
		
	}
	
	private Image loadImage(String fileName)
	{
		File imageFile = new File(fileName);
		
		if(!imageFile.exists())
			return new Image("imageLoadError.png");
		
		return new Image(imageFile.toURI().toString());
	}
	
	public void setContents(TakeOverData data)
	{
		this.title.setText(data.getTitle());
		this.object.setText(data.getObject());
		this.orderer.setText(data.getOrderer());
		
		this.imageFile = new File(mainApp.uncPath + "Images" +File.separator + data.getImage());
		
		if(!data.getImage().equals("") && data.getImage() != null)
		{
			showImage(imageFile.getAbsolutePath());
			
			if(!imageFile.exists())
			{
				imageFile = null;
				curBorderPane.setLeft(new ImageView("imageLoadError.png"));
			}
		}
		else
			curBorderPane.setLeft(null);
		
		if(title.equals("") || title==null)
			curBorderPane.setTop(null);
		if (object.equals("") || object == null)
			curBorderPane.setCenter(null);
		if(orderer.equals("") || orderer == null)
			curBorderPane.setBottom(null);
		
		if(data.getEmphasis())
			emphasisDesignSet();
		else if(data.getContinue())
			continueDesignSet();
		else if(data.getComplete())
			completeDesignSet();
		else
			curBorderPane.setRight(null);
		
		curTakeOverData = data;
	}
	
	private void emphasisDesignSet()
	{
		title.setFill(Color.rgb(255, 0, 0));
		title.setFont(Font.font("Malgun Gothic", FontWeight.EXTRA_BOLD, 18));
		//itemView.setImage(mainApp.continueImage);
		curBorderPane.setRight(null);
	}
	
	private void continueDesignSet(){
		Text text = new Text("진행중인 사항");
		text.setFill(Color.rgb(255, 0, 0));
		text.setFont(Font.font("Malgun Gothic", FontWeight.EXTRA_BOLD, 16));
		curBorderPane.setRight(text);
	}
	
	private void completeDesignSet(){
		Text text = new Text("완료된 사항");
		text.setFill(Color.rgb(0, 0, 255));
		text.setFont(Font.font("Malgun Gothic", FontWeight.EXTRA_BOLD, 16));
		curBorderPane.setRight(text);
	}
	
	public int getIndex()
	{
		return index;
	}
	
	public void editIndex(int index)
	{
		this.index = index;
	}
	
}
