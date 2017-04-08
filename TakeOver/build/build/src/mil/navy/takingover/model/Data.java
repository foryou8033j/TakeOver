package mil.navy.takingover.model;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.sun.org.apache.xerces.internal.util.URI;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;
import mil.navy.takingover.MainApp;
import mil.navy.takingover.util.DateUtil;
import mil.navy.takingover.util.ExceptionDialog;

public class Data {
	
	//당직자
	private final ObservableList<String> members = FXCollections.observableArrayList();
	private final ObservableList<TakeOverData> takeOverData = FXCollections.observableArrayList();
	private final ObservableList<InternetAcceptData> internetAccecptData = FXCollections.observableArrayList();
	
	private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<LocalDate>();
	
	private File file = null;
	
	//파일 멀티 쓰레딩을 위한 java nio 클래스
	private Path path = null;
	
	private boolean currentLoading = false;
	private boolean currentSaveing = false;
	
	public Data() {
		this(null);
	}
	
	public Data(LocalDate date)
	{
		this.date.set(date);
		
		/*internetAccecptData.add(new InternetAcceptData("등록", "병장 김형중", "x.x.1,90", "공용", "통합관제과-001", "병장 서정삼"));
		internetAccecptData.add(new InternetAcceptData("등록", "대장 정호섭", "x.x.1.11", "개인", "통합관제과-034", "병장 서정삼"));
		internetAccecptData.add(new InternetAcceptData("삭제", "대령 양종휴", "x.x.1.130", "개인", "통합관제과-026", "병장 서정삼"));
		internetAccecptData.add(new InternetAcceptData("등록", "대장 엄현성", "x.x.1.80", "공용", "통합관제과-073", "병장 서정삼"));
		*/
		/*members.add("이현섭");
		members.add("황선빈");
		members.add("서정삼");*/
	}
	
	/**
	 * 당직 멤버 지정
	 * @param members
	 */
	public void setMembers(ObservableList<String> members)
	{
			this.members.addAll(members);
	}
	
	/**
	 * 당직 멤버 반환
	 * @return
	 */
	public ObservableList<String> getMembers()
	{
		return members;
	}
	
	
	public LocalDate getLocalDate()
	{
		return date.get();
	}
	
	public StringProperty getDateStringProperty()
	{
		return new SimpleStringProperty(DateUtil.format(date.get()));
	}
	
	public ObservableList<TakeOverData> getTakeOverData()
	{
		return takeOverData;
	}
	
	public ObservableList<InternetAcceptData> getInternetAcceptData()
	{
		return internetAccecptData;
	}
	
	public boolean loadDataFromFile(File file)
	{
		try{
			this.file = file;
			this.path = Paths.get(file.getPath());
			
			JAXBContext context = JAXBContext.newInstance(DataWrapper.class);
			Unmarshaller um = context.createUnmarshaller();
			
			//파일로 부터 XML을 읽은 다음 역 마샬링 한다.
			DataWrapper wrapper = (DataWrapper) um.unmarshal(path.toFile());
			
			members.clear();
			takeOverData.clear();
			internetAccecptData.clear();
			
			members.addAll(wrapper.getNames());
			takeOverData.addAll(wrapper.getTakeOverData());
			internetAccecptData.addAll(wrapper.getInternetAcceptData());
			
			System.out.println("loaded + " + file.getPath());
			
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			new ExceptionDialog(AlertType.ERROR, e.getMessage(), "오류가 발생했습니다.", "파일을 불러오던 도중 오류가 발생 했습니다.", e.getStackTrace().toString())
					.showAndWait();
			//System.exit(0);
			return false;
		}
	}
	
	public void saveDataToFile(File file)
	{
		try
		{
			File dir = new File(MainApp.path);
			if(!dir.exists())
				dir.mkdir();
			
			this.file = file;
			this.path = Paths.get(file.getPath());
			JAXBContext context = JAXBContext.newInstance(DataWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			//데이터를 감싼다.
			DataWrapper wrapper = new DataWrapper();
			wrapper.setDatas(getLocalDate(), getMembers(), getTakeOverData(), getInternetAcceptData());
			
			//마샬링 후 XML파일에 저장한다.
			m.marshal(wrapper, file);
		}catch (Exception e)
		{
			e.printStackTrace();
			new ExceptionDialog(AlertType.ERROR, e.getMessage(), "오류가 발생했습니다.", "파일을 저장하던 도중 오류가 발생 했습니다.", e.getStackTrace().toString())
					.showAndWait();
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
