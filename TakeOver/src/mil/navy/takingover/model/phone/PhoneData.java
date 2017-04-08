package mil.navy.takingover.model.phone;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * 전화 번호 데이터를 저장
 * @author 병장 서정삼
 *
 */
@XmlType(name = "object")
public class PhoneData {

	
	StringProperty title = new SimpleStringProperty();
	StringProperty number = new SimpleStringProperty();
	
	public PhoneData() {
		this(null, null);
	}
	
	public PhoneData(String title, String contents)
	{
		try
		{
			this.title.set(title);
			this.number.set(contents);
		}catch (Exception e)
		{
			
		}
		
	}
	
	public void setTitle(String name)
	{
		this.title.set(name);
	}
	@XmlAttribute(name ="title", required = false)
	public String getTitle()
	{
		return title.get();
	}
	public StringProperty getTitleProPerty()
	{
		return title;
	}
	
	public void setContents(String contents)
	{
		this.number.set(contents);
	}
	@XmlAttribute(name ="number", required = false)
	public String getContents()
	{
		return number.get();
	}
	public StringProperty getContentsProperty()
	{
		return number;
	}
	
	

	
}
