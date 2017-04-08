package mil.navy.takingover.model;

import java.time.LocalDate;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mil.navy.takingover.util.LocalDateAdapter;


/**
 * 인수인계 데이터를 감싸는 헬퍼 클래스이다.
 * XML로 저장하는데 사용된다.
 */
@XmlRootElement(name ="data")
public class DataWrapper {

	
	private LocalDate date = null;
	private List<String> names = FXCollections.<String>observableArrayList();
	private List<TakeOverData> takeOverData = FXCollections.<TakeOverData>observableArrayList();
	private List<InternetAcceptData> internetAcceptData = FXCollections.<InternetAcceptData>observableArrayList();
	
	@XmlElement(name = "date")
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	public LocalDate getDate()
	{
		//System.out.println(date);
		return date;
	}
	
	@XmlElement(name = "names")
	public List<String> getNames()
	{
		return names;
	}
	
	@XmlElements ( { @XmlElement ( name = "takeover", type = TakeOverData.class)})
	public List<TakeOverData> getTakeOverData()
	{
		return takeOverData;
	}
	
	@XmlElements ( { @XmlElement ( name = "internet", type = InternetAcceptData.class)})
	public List<InternetAcceptData> getInternetAcceptData()
	{
		return internetAcceptData;
	}
	
	public void setDatas(LocalDate date, ObservableList<String> names, ObservableList<TakeOverData> takeOverData, ObservableList<InternetAcceptData> internetAcceptData){
		this.date = date;
		this.names = (List) names;
		this.takeOverData = (List) takeOverData;
		this.internetAcceptData = (List) internetAcceptData;
	}
	
}
