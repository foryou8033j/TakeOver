package mil.navy.takingover.model.phone;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * 전화번호 데이터를 감싸는 헬퍼 클래스이다.
 * XML로 저장하는데 사용된다.
 */
@XmlRootElement(name ="data")
public class PhoneDataWrapper {

	private ObservableList<PhoneData> data = FXCollections.<PhoneData>observableArrayList();
	
	@XmlElements ( { @XmlElement ( name = "object", type = PhoneData.class)})
	public ObservableList<PhoneData> getData()
	{
		return data;
	}
	
	
	public void setDatas(ObservableList<PhoneData> data){
		this.data = data;
	}
	
}