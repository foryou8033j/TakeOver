package mil.navy.takingover.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@XmlType( name = "takeover")
public class TakeOverData {

	//인계 내용
	private final StringProperty title = new SimpleStringProperty();
	private final StringProperty object = new SimpleStringProperty();
	private final StringProperty orderer = new SimpleStringProperty();
	
	public TakeOverData() {
		this(null, null, null);
	}
	
	public TakeOverData(String title, String object, String orderer)
	{
		this.title.set(title);
		this.object.set(object);
		this.orderer.set(orderer);
	}
	
	public void setTitle(String title)
	{
		this.title.set(title);
	}
	
	@XmlAttribute(name ="title", required = false)
	public String getTitle()
	{
		return title.get();
	}
	
	public StringProperty getTitleProperty()
	{
		return title;
	}
	
	public void setObject(String Object)
	{
		this.object.set(Object);
	}
	
	@XmlAttribute(name ="object", required = false)
	public String getObject()
	{
		return object.get();
	}
	
	public StringProperty getObjectProperty()
	{
		return object;
	}
	
	public void setOrderer(String Orderer)
	{
		this.orderer.set(Orderer);
	}
	
	@XmlAttribute(name ="orderer", required = false)
	public String getOrderer()
	{
		return orderer.get();
	}
	
	public StringProperty getOrdererProperty()
	{
		return orderer;
	}
	
	
	
	
	
}
