package mil.navy.takingover.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
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
	
	TakeOverData curTakeOverData;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	private BorderPane curBorderPane;
	
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
	
	public void setContents(String title, String object, String orderer)
	{
		this.title.setText(title);
		this.object.setText(object);
		this.orderer.setText(orderer);
		
		if(title.equals(""))
			curBorderPane.setTop(null);
		if (object.equals(""))
			curBorderPane.setCenter(null);
		if(orderer.equals(""))
			curBorderPane.setBottom(null);
		
		curTakeOverData = new TakeOverData(title, object, orderer);
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
