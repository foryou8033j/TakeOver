package mil.navy.takingover.model.otherpassword;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * 기타 패스워드 데이터를 감싸는 헬퍼 클래스이다.
 * XML로 저장하는데 사용된다.
 */
@XmlRootElement(name ="data")
public class OtherPasswordWrapper {

	private ObservableList<OtherPassword> data = FXCollections.<OtherPassword>observableArrayList();
	
	@XmlElements ( { @XmlElement ( name = "object", type = OtherPassword.class)})
	public ObservableList<OtherPassword> getData()
	{
		return data;
	}
	
	
	public void setDatas(ObservableList<OtherPassword> data){
		this.data = data;
	}
	
}