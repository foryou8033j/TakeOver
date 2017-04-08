package mil.navy.takingover.view.tab.tippack;

import java.util.HashSet;
import java.util.Set;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

public class TipContentController {

	@FXML
	Text title;
	
	@FXML
	TextArea contents;
	
	
	GridPane pane;
	
	public TipContentController() {
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

	public void setContents(String title, String contents)
	{
		this.title.setText(title);
		this.contents.setText(contents);
		
	/*	if(title.equals(""))
			deleteRow(pane, pane.getRowIndex(this.title));
		
		if(url.equals(""))
			deleteRow(pane, pane.getRowIndex(this.url));
		
		if(id.equals(""))
			deleteRow(pane, pane.getRowIndex(this.id));
		
		if(pw.equals(""))
			deleteRow(pane, pane.getRowIndex(this.pw));*/
		
	}
}
