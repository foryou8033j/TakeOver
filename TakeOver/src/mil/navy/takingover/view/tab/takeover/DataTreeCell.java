package mil.navy.takingover.view.tab.takeover;


import java.util.List;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TreeCell;
import mil.navy.takingover.model.Data;

public final class DataTreeCell extends TreeCell<Data>{
	
	List<Data> datas;
	
	HeadLayoutController controller;
	
	public DataTreeCell() {
	}
	
	@Override
	protected void updateItem(Data item, boolean empty) {
		super.updateItem(item, empty);
		
		if(empty){
			setText(null);
			setGraphic(null);
		}else{
			if(item.getLocalDate() == null){
				setText("인수인계 데이터");
			}
			else
				setText(item.getLocalDate().toString());
			setGraphic(getTreeItem().getGraphic());
		}
		setContentDisplay(ContentDisplay.TEXT_ONLY);
	}

	
	
}
