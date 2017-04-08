package mil.navy.takingover.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@XmlType(name = "internet")
public class InternetAcceptData {

	
		//인터넷 운용승인 내용
		private final  StringProperty object = new SimpleStringProperty();
		private final  StringProperty name = new SimpleStringProperty();
		private final  StringProperty ip = new SimpleStringProperty();
		private final  StringProperty policy = new SimpleStringProperty();
		private final  StringProperty ground = new SimpleStringProperty();
		private final  StringProperty worker = new SimpleStringProperty();
	
		
		public InternetAcceptData() {
			this(null, null, null, null, null, null);
		}
		
		public InternetAcceptData(String object, String name, String ip, String policy, String ground, String worker) {
			this.object.set(object);
			this.name.set(name);
			this.ip.set(ip);
			this.policy.set(policy);
			this.ground.set(ground);
			this.worker.set(worker);
		} 
		
		
		public void setObject(String object)
		{
			this.object.set(object);
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
		
		public void setName(String name)
		{
			this.name.set(name);
		}
		
		@XmlAttribute(name ="name", required = false)
		public String getName()
		{
			return name.get();
		}
		
		public StringProperty getNameProperty()
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
		
		public StringProperty getIpProperty()
		{
			return ip;
		}
		
		public void setPolicy(String policy)
		{
			this.policy.set(policy);
		}
		
		@XmlAttribute(name ="policy", required = false)
		public String getPolicy()
		{
			return policy.get();
		}
		
		public StringProperty getPolicyProperty()
		{
			return policy;
		}
		
		public void setGround(String ground)
		{
			this.ground.set(ground);
		}
		
		@XmlAttribute(name ="ground", required = false)
		public String getGround()
		{
			return ground.get();
		}
		
		public StringProperty getGroundProperty()
		{
			return ground;
		}
		
		public void setWorker(String worker)
		{
			this.worker.set(worker);
		}
		
		@XmlAttribute(name ="worker", required = false)
		public String getWorker()
		{
			return worker.get();
		}
		
		public StringProperty getWorkerProperty()
		{
			return worker;
		}
		
}
