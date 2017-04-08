package mil.navy.takingover.view.tab.otherpassword;

import java.util.HashSet;
import java.util.Set;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

public class OtherPasswordContentController {

	@FXML
	Text title;
	
	@FXML
	Text option;
	
	@FXML
	TextField url;
	
	@FXML
	TextField id;
	
	@FXML
	TextField pw;
	
	GridPane pane;
	
	public OtherPasswordContentController() {
		// TODO Auto-generated constructor stub
	}
	
	public void setPane(GridPane pane)
	{
		this.pane = pane;
	}
	
	private void deleteRow(GridPane grid, final int row)
	{
		Set<Node> deleteNode = new HashSet<>();
		for (Node child : grid.getChildren()){
			Integer rowIndex = GridPane.getRowIndex(child);
			
			int r = rowIndex == null ? 0 : rowIndex;
			
			if(r > row){
				GridPane.setRowIndex(child, r-1);
			}else if(r == row){
				deleteNode.add(child);
			}
		}
		grid.getChildren().removeAll(deleteNode);
	}

	public void setContents(String title, String url, String id, String pw, ObservableList<Boolean> options)
	{
		this.title.setText(title);
		this.url.setText(url);
		this.id.setText(id);
		this.pw.setText(pw);
		
	/*	if(title.equals(""))
			deleteRow(pane, pane.getRowIndex(this.title));
		
		if(url.equals(""))
			deleteRow(pane, pane.getRowIndex(this.url));
		
		if(id.equals(""))
			deleteRow(pane, pane.getRowIndex(this.id));
		
		if(pw.equals(""))
			deleteRow(pane, pane.getRowIndex(this.pw));*/
		
		if(options.get(0)){
			this.option.setFill(Paint.valueOf("black"));
			this.option.setText("국방망");
		}
		if(options.get(1)){
			this.option.setFill(Paint.valueOf("blue"));
			this.option.setText("인터넷망");
		}
		if(options.get(2)){
			this.option.setFill(Paint.valueOf("red"));
			this.option.setText("전장망");
		}
		if(options.get(3)) this.option.setText("기타");
		
	}
}
