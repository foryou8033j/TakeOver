package mil.navy.takingover.model;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mil.navy.takingover.MainApp;
import mil.navy.takingover.util.DateUtil;
import mil.navy.takingover.util.file.FileNameConverter;
import mil.navy.takingover.util.file.NIOFileCopy;
import mil.navy.takingover.view.loading.LoadLayout;
import mil.navy.takingover.view.logview.LogStage;

/**
 * 인수인계 탭의 데이터를 구성한다.<BR>
 * 인수인계 데이터와 인터넷 데이터를 합쳐서 구성하고 LocalDate 를 통해 해당 데이터의 날짜 정보를 저장한다.<BR>
 * <code>ObservableList<String></code> 를 통해 당일 멤버 정보를 저장한다.
 * 
 * @see TakeOverData
 * @see InternetAcceptData
 * @see LocalDate
 * 
 * @version 1.2
 * @author 병장 서정삼
 *
 */
public class Data {
	
	//당직자
	private final ObservableList<String> members = FXCollections.observableArrayList();
	private final ObservableList<TakeOverData> takeOverData = FXCollections.observableArrayList();
	private final ObservableList<InternetAcceptData> internetAccecptData = FXCollections.observableArrayList();
	private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<LocalDate>();
	
	private File originalFile = null;
	private File file = null;
	
	//파일 멀티 쓰레딩을 위한 java nio 클래스
	private Path path = null;
	
	private boolean currentLoading = false;
	private boolean currentSaveing = false;
	
	public Data() {
		this(null);
	}
	
	/**
	 * 다음 링크 참조 {@link Data}
	 * @param date
	 */
	public Data(LocalDate date)
	{
		this.date.set(date);
	}
	
	/**
	 * 당직 멤버값 저장
	 * @param members
	 */
	public void setMembers(ObservableList<String> members)
	{
			this.members.addAll(members);
	}
	
	/**
	 * 당직 멤버값 반환
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
	
	public Data setFile(File file)
	{
		this.originalFile = file.getAbsoluteFile();
		return this;
	}
	
	public boolean loadDataFromFile(File file)
	{
		try{
			this.file = file.getAbsoluteFile();
			this.path = Paths.get(file.getPath());
			
			//불러온 파일 목록을 로컬 드라이브로 복사한다.
			NIOFileCopy.copy(originalFile.getPath(), path.toString());
			
			JAXBContext context = JAXBContext.newInstance(DataWrapper.class);
			Unmarshaller um = context.createUnmarshaller();
			
			//파일로 부터 XML을 읽은 다음 역 마샬링 한다.
			DataWrapper wrapper = (DataWrapper) um.unmarshal(file);
			
			members.clear();
			takeOverData.clear();
			internetAccecptData.clear();
			
			members.addAll(wrapper.getNames());
			takeOverData.addAll(wrapper.getTakeOverData());
			internetAccecptData.addAll(wrapper.getInternetAcceptData());
			
			LogStage.append("File Load " + file.getPath());
			
			return true;
		}
		catch (Exception e)
		{
			LogStage.append("File Load Exception " + file.getPath() + "[" + e.getMessage() + "]");
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
			
			//마샬링한 데이터를 unc경로로 전송한다.
			if(originalFile != null)
				NIOFileCopy.copy(path.toString(), originalFile.getPath());
			else
				NIOFileCopy.copy(path.toString(), MainApp.uncPath + "TakeOverDatas" + File.separator +  FileNameConverter.toString(getLocalDate()) + ".xml");
		}catch (Exception e)
		{
			LogStage.append("File Save Exception " + file.getPath() + "[" + e.getMessage() + "]");
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
