package mil.navy.takingover.model.tip;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * 팁 데이터를 감싸는 헬퍼 클래스이다.
 * XML로 저장하는데 사용된다.
 */
@XmlRootElement(name ="data")
public class TipDataWrapper {

	private ObservableList<TipData> data = FXCollections.<TipData>observableArrayList();
	
	@XmlElements ( { @XmlElement ( name = "object", type = TipData.class)})
	public ObservableList<TipData> getData()
	{
		return data;
	}
	
	
	public void setDatas(ObservableList<TipData> data){
		this.data = data;
	}
	
}