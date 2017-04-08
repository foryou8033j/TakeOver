package mil.navy.takingover.view.root.option.titlechange;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mil.navy.takingover.MainApp;
import mil.navy.takingover.util.Regedit;
import mil.navy.takingover.view.logview.LogStage;

public class TitleChangeDialog extends Stage{

	VBox vb = new VBox(10);
	
	
	TextField input = new TextField();
	Text title = new Text("변경 할 타이틀을 입력하세요.");
	Text text = new Text("");
	
	int maxLength = 13;
	
	public TitleChangeDialog(MainApp mainApp) {
		
		getIcons().add(mainApp.icon);
		
		vb.getChildren().add(title);
		vb.getChildren().add(input);
		vb.getChildren().add(text);
		
		vb.setAlignment(Pos.CENTER);
		
		title.setFont(Font.font(14));
		title.setTextAlignment(TextAlignment.CENTER);
		
		text.setTextAlignment(TextAlignment.CENTER);
		
		setTitle("타이틀 명 변경");
		initOwner(mainApp.getPrimaryStage());
		initModality(Modality.WINDOW_MODAL);
		
		input.setText(Regedit.getStringValue("title"));
		
		setWidth(240);
		setHeight(120);
		setResizable(false);
		
		Scene scene = new Scene(vb);
		setScene(scene);
		
		setOnCloseRequest(Event -> {
			close();
		});
		
		
		input.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> obersvable, String oldValue, String newValue) {

			}
		});
		
		input.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent arg0) {
				
				if(arg0.getCode() == KeyCode.ENTER){
					if(input.getText().equals("")){
						text.setFill(Color.RED);
						text.setText("타이틀을 입력하세요.");
					}else{
						mainApp.title = input.getText();
						Regedit.setRegistry("title", input.getText());
						mainApp.setTitle();
						LogStage.append("타이틀을 변경하였습니다.");
						close();
					}
					
					
				}
				
			};
		});
		
	}
	
}
