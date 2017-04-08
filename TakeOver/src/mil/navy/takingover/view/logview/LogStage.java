package mil.navy.takingover.view.logview;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LogStage extends Stage{
	
	static TextArea textArea;
	
	private static Button clear;
	
	public LogStage(Stage primaryStage, String title) {
	
		try{
			
			setTitle(title);
			//getIcons().add(primaryStage.getIcons().get(0));
			//initModality(Modality.WINDOW_MODAL);
			initOwner(primaryStage);
			initStyle(StageStyle.UNDECORATED);
			setWidth(700);
			setHeight(500);
			setMinWidth(400);
			setMinHeight(200);
			
			setX(primaryStage.getX() + primaryStage.getWidth());
			setY(primaryStage.getY());
			
			primaryStage.xProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					setX(newValue.doubleValue() + primaryStage.getWidth());
				}
			});
			
			primaryStage.yProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					setY(newValue.doubleValue());
				}
			});
			
			primaryStage.widthProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					setX(primaryStage.getX() + newValue.doubleValue());
				}
			});
			
			primaryStage.heightProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					setY(primaryStage.getY());
				}
			});
			
			
			
			textArea = new TextArea();
			textArea.setEditable(false);
			//textArea.setWrapText(true);
			clear = new Button("삭제");
			clear.setAlignment(Pos.CENTER);
			
			BorderPane borderPane = new BorderPane();
			borderPane.setCenter(textArea);
			borderPane.setBottom(clear);
			
			clear.setOnAction(Event -> {
				handleClear();
			});
			
			Scene scene = new Scene(borderPane);
			setScene(scene);
			
		}catch (Exception e){
			
		}

	}
	
	private void handleClear(){
		textArea.setText("");
	}
	
	int getWrapperdLines(String oldText, String newText){
		Text helper = (Text)textArea.lookup(".text");
		if(helper != null){
			helper.setText(newText);
			FontMetrics fontMetrics = Toolkit.getToolkit().getFontLoader().getFontMetrics(textArea.getFont());
			int preferredHeight = new Double(helper.getLayoutBounds().getHeight()).intValue();
			int lineHeight = new Double(fontMetrics.getLineHeight()).intValue();
			
			preferredHeight = preferredHeight == lineHeight ? preferredHeight : preferredHeight -2;
			helper.setText(oldText);
			return preferredHeight / lineHeight;
		}else{
			return 0;
		}
	}
	
	
	public static void append(String message){

		
		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		try{
			
			Platform.runLater(() -> {
				textArea.appendText("[" + format.format(date)  + "] "+ message + "\n");
			});
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}

	
	
}
