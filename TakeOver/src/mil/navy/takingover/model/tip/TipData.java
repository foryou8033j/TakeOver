package mil.navy.takingover.model.tip;

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
 * 팁 데이터를 저장
 * @author 병장 서정삼
 *
 */
@XmlType(name = "object")
public class TipData {

	
	StringProperty title = new SimpleStringProperty();
	StringProperty contents = new SimpleStringProperty();
	
	public TipData() {
		this(null, null);
	}
	
	public TipData(String title, String contents)
	{
		try
		{
			this.title.set(title);
			this.contents.set(contents);
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
		this.contents.set(contents);
	}
	@XmlAttribute(name ="contents", required = false)
	public String getContents()
	{
		return contents.get();
	}
	public StringProperty getContentsProperty()
	{
		return contents;
	}
	
	

	
}
