package mil.navy.takingover.model.otherpassword;

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
 * 기타 패스워드 데이터 저장
 * @author 병장 서정삼
 *
 */
@XmlType(name = "object")
public class OtherPassword {

	
	StringProperty title = new SimpleStringProperty();
	ObservableList<Boolean> options = FXCollections.observableArrayList();
	StringProperty url = new SimpleStringProperty();
	StringProperty id = new SimpleStringProperty();
	StringProperty password = new SimpleStringProperty();
	
	BooleanProperty option0 = new SimpleBooleanProperty();
	BooleanProperty option1 = new SimpleBooleanProperty();
	BooleanProperty option2 = new SimpleBooleanProperty();
	BooleanProperty option3 = new SimpleBooleanProperty();
	
	public OtherPassword() {
		this(null, null, null, null, null);
	}
	
	public OtherPassword(String title, String url, String id, String pw, ObservableList<Boolean> options)
	{
		try
		{
			this.title.set(title);
			this.url.set(url);
			this.id.set(id);
			this.password.set(pw);
			
			this.options.addAll(options);
			saveData(options);
		}catch (Exception e)
		{
			
		}
		
	}
	
	public OtherPassword(String title, String url, String id, String pw, boolean option0, boolean option1, boolean option2, boolean option3)
	{
		this.title.set(title);
		this.url.set(url);
		this.id.set(id);
		this.password.set(pw);
		
		this.options.set(0, option0);
		this.options.set(1, option1);
		this.options.set(2, option2);
		this.options.set(3, option3);
		saveData(option0, option1, option2, option3);
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
	
	public void setUrl(String name)
	{
		this.url.set(name);
	}
	@XmlAttribute(name ="url", required = false)
	public String getUrl()
	{
		return url.get();
	}
	public StringProperty getUrlProPerty()
	{
		return url;
	}
	
	public void setId(String name)
	{
		this.id.set(name);
	}
	@XmlAttribute(name ="id", required = false)
	public String getId()
	{
		return id.get();
	}
	public StringProperty getIdProPerty()
	{
		return id;
	}
	
	public void setPassword(String name)
	{
		this.password.set(name);
	}
	@XmlAttribute(name ="password", required = false)
	public String getPassword()
	{
		return password.get();
	}
	public StringProperty getPasswordProPerty()
	{
		return password;
	}
	
	public void setOptions(ObservableList<Boolean> options)
	{
		this.options.clear();
		this.options.addAll(options);
		saveData(options);
	}
	
	public ObservableList<Boolean> getOptions()
	{
		return options;
	}
	
	@XmlAttribute(name ="option0", required = false)
	public boolean getOption0()
	{
		return option0.get();
	}
	
	@XmlAttribute(name ="option1", required = false)
	public boolean getOption1()
	{
		return option1.get();
	}
	
	@XmlAttribute(name ="option2", required = false)
	public boolean getOption2()
	{
		return option2.get();
	}
	
	@XmlAttribute(name ="option3", required = false)
	public boolean getOption3()
	{
		return option3.get();
	}
	
	
	public void saveData(ObservableList<Boolean> list)
	{
		option0.set(list.get(0));
		option1.set(list.get(1));
		option2.set(list.get(2));
		option3.set(list.get(3));
	}
	
	public void saveData(boolean option0, boolean option1, boolean option2, boolean option3)
	{
		options.set(0, option0);
		options.set(1, option1);
		options.set(2, option2);
		options.set(3, option3);
	}
	
}
