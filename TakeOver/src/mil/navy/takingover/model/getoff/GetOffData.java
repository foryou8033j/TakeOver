package mil.navy.takingover.model.getoff;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * PC 전원 상태 확인 데이터를 저장하는 클래스
 * @author 병장 서정삼
 *
 */
@XmlType(name = "identity")
public class GetOffData {
	
	
	StringProperty name = new SimpleStringProperty();
	StringProperty ip = new SimpleStringProperty();
	
	public GetOffData() {
		this(null, null);
	}
	
	public GetOffData(String name, String ip)
	{
		this.name.set(name);
		this.ip.set(ip);
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
	
	public void setIp(String ip)
	{
		this.ip.set(ip);
	}
	
	@XmlAttribute(name ="ip", required = false)
	public String getIp()
	{
		return ip.get();
	}
	public StringProperty getIpProPerty()
	{
		return ip;
	}
	
	
	

}
