package mil.navy.takingover.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.web.WebView;

/**
 * 예외 발생시 예외 발생 오류를 보여주는 클래스
 * @version 1.0
 * @since 2016.12.14
 * @author 병장 서정삼
 * @see Exception
 * @see Stage
 */
public class ExceptionDialog extends Alert{
	
	String exceptionText;

	/**
	 * 	Exception 형태의 입력으로 StackTrace를 오류창에 보여준다.
	 * @see ExceptionDialog
	 * @param arg0 오류 형태 {@link AlertType}
	 * @param title 오류 창 제목
	 * @param headerText 오류 창 HeadTitle
	 * @param contentsText 오류창 문구
	 * @param exception
	 */
	public ExceptionDialog(AlertType arg0, String title, String headerText, String contentsText, Exception exception) {
		super(arg0);
		setTitle(title);
		setHeaderText(headerText);
		setContentText(contentsText);
		
		drawPane(stackTraceToString(exception));
	}
	
	/**
	 * Exception String 형태의 입력으로 입력을 오류창에 보여준다.
	 * @see ExceptionDialog
	 * @param arg0
	 * @param title
	 * @param headerText
	 * @param contentsText
	 * @param exception
	 */
	public ExceptionDialog(AlertType arg0, String title, String headerText, String contentsText, String exception) {
		super(arg0);
		setTitle(title);
		setHeaderText(headerText);
		setContentText(contentsText);
		
		drawPane(exception);
	}
	
	
	/**
	 * 입력 된 오류 텍스트를 오류 창에 보여준다.
	 * @param exceptionText
	 */
	private void drawPane(String exceptionText)
	{
		
		Label label = new Label("The Exception stacktrace was : ");
		
		TextArea textArea = new TextArea(exceptionText);
		//textArea.getEngine().loadContent(exceptionText);
		
		textArea.setEditable(false);
		textArea.setWrapText(true);
		
		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);
		
		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);
		
		getDialogPane().setExpandableContent(expContent);
	}
	
	/**
	 * 입력 된 {@link Throwable} 의 StackTrace 를 {@link String} 형태로 반환한다.
	 * @param e {@link Exception}
	 * @return {@link String}
	 */
	private static String stackTraceToString(Throwable e){
		if (e == null) return "";
		else{
			try{
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				e.printStackTrace(new PrintStream(bout));
				bout.flush();
				String error = new String(bout.toByteArray());
				
				return error;
			}catch (Exception t){
				return "";
			}
		}
	}

	

}
