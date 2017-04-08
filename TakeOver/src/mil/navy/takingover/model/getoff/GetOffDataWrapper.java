package mil.navy.takingover.model.getoff;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * PC 전원 상태 확인 데이터를 감싸는 헬퍼 클래스이다.
 * XML로 저장하는데 사용된다.
 */
@XmlRootElement(name ="data")
public class GetOffDataWrapper {

	private ObservableList<GetOffData> data = FXCollections.<GetOffData>observableArrayList();
	
	@XmlElements ( { @XmlElement ( name = "getoff", type = GetOffData.class)})
	public ObservableList<GetOffData> getData()
	{
		return data;
	}
	
	
	public void setDatas(ObservableList<GetOffData> data){
		this.data = data;
	}
	
}