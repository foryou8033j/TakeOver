package mil.navy.takingover.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 인수인계 데이터를 저장한다.
 * @author 병장 서정삼
 */
@XmlType( name = "takeover")
public class TakeOverData {

	//인계 내용
	private final StringProperty title = new SimpleStringProperty();	//제목
	private final StringProperty object = new SimpleStringProperty();	//내용
	private final StringProperty orderer = new SimpleStringProperty();	//지시자
	private final StringProperty image = new SimpleStringProperty();	//이미지경로
	
	private final BooleanProperty isEmphasis = new SimpleBooleanProperty(); //강조사항
	private final BooleanProperty isContinue = new SimpleBooleanProperty();	//진행중인 인계인지
	private final BooleanProperty isComplete = new SimpleBooleanProperty();	//완료된 인계인지
	
	
	public TakeOverData() {
		this(null, null, null);
	}
	
	public TakeOverData(String title, String object, String orderer)
	{
		this.title.set(title);
		this.object.set(object);
		this.orderer.set(orderer);
		this.image.set("");
		this.isEmphasis.set(false);
		this.isComplete.set(false);
		this.isContinue.set(false);
	}
	
	public TakeOverData(String title, String object, String orderer, String image)
	{
		this.title.set(title);
		this.object.set(object);
		this.orderer.set(orderer);
		this.image.set(image);
		this.isEmphasis.set(false);
		this.isComplete.set(false);
		this.isContinue.set(false);
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
	
	public void setImage(String image)
	{
		this.image.set(image);
	}
	
	@XmlAttribute(name ="image", required = false)
	public String getImage()
	{
		return image.get();
	}
	
	public StringProperty getImageProperty()
	{
		return image;
	}
	
	public void setContinue(boolean set)
	{
		this.isContinue.set(set);
	}
	
	@XmlAttribute(name ="isContinue", required = false)
	public boolean getContinue()
	{
		if (isContinue == null)
			return false;
		else
			return isContinue.get();
	}
	
	public BooleanProperty getContinueProperty()
	{
		return isContinue;
	}
	
	public void setComplete(boolean set)
	{
		this.isComplete.set(set);
	}
	
	@XmlAttribute(name ="isComplete", required = false)
	public boolean getComplete()
	{
		if(isComplete == null)
			return false;
		else
			return isComplete.get();
	}
	
	public BooleanProperty getCompleteProperty()
	{
		return isComplete;
	}
	
	public void setEmphasis(boolean set)
	{
		this.isEmphasis.set(set);
	}
	
	@XmlAttribute(name ="isEmphasis", required = false)
	public boolean getEmphasis()
	{
		if(isEmphasis == null)
			return false;
		else
			return isEmphasis.get();
	}
	
	public BooleanProperty getEmphasisProperty()
	{
		return isEmphasis;
	}
	
	public boolean isNoSelect()
	{
		if(!isEmphasis.get() && !isContinue.get() && !isComplete.get())
			return true;
		else
			return false;
	}
	
	
	
	
}
