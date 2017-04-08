package mil.navy.takingover.model.officerIdentity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * 인수인계 데이터를 감싸는 헬퍼 클래스이다.
 * XML로 저장하는데 사용된다.
 */
@XmlRootElement(name ="data")
public class OfficerIdentityWrapper {

	private ObservableList<OfficerIdentity> data = FXCollections.<OfficerIdentity>observableArrayList();
	
	@XmlElements ( { @XmlElement ( name = "identity", type = OfficerIdentity.class)})
	public ObservableList<OfficerIdentity> getData()
	{
		return data;
	}
	
	
	public void setDatas(ObservableList<OfficerIdentity> data){
		this.data = data;
	}
	
}