package mil.navy.takingover.model.officerIdentity;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 개인 패스워드 데이터를 저장
 * @author 병장 서정삼
 *
 */
@XmlType(name = "identity")
public class OfficerIdentity {
	
	
	StringProperty name = new SimpleStringProperty();
	StringProperty id = new SimpleStringProperty();
	StringProperty password = new SimpleStringProperty();
	
	public OfficerIdentity() {
		this(null, null, null);
	}
	
	public OfficerIdentity(String name, String id, String password)
	{
		this.name.set(name);
		this.id.set(id);
		this.password.set(password);
	}
	
	public void setName(String name)
	{
		this.name.set(name);
	}
	
	@XmlAttribute(name ="name", required = false)
	public String getName()
	{
		return name.get();
	}
	public StringProperty getNameProPerty()
	{
		return name;
	}
	
	public void setId(String id)
	{
		this.id.set(id);
	}
	
	@XmlAttribute(name ="id", required = false)
	public String getId()
	{
		return id.get();
	}
	public StringProperty getidProPerty()
	{
		return id;
	}
	
	public void setPassword(String password)
	{
		this.password.set(password);
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
	
	
	

}
