package mil.navy.takingover.util;

import java.util.StringTokenizer;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * 인수인계 데이터에서 {@link StringProperty} 형태로 저장되어 있는 멤버 이름 데이터 재조합 하는 클래스
 * @author 병장 서정삼
 */
public class MemberNameAdapter extends XmlAdapter<String, ObservableList<String>>{

	/**
	 * {@link ObservableList<String>} 형태로 입력 된 데이터를 {@link String} 형태로 변환한다.
	 */
	@Override
	public String marshal(ObservableList<String> arg0) throws Exception {
		
		int size = arg0.size();
		String str;
		
		if(size > 0)
		{
			str = arg0.get(0);
			
			for(int i=1; i<size; i++)
				str = str.concat("|" + arg0.get(i));
		}
		else
			return "";
		
		return str;
	}

	/**
	 * {@link String} 형태로 입력 된 데이터를 {@link ObservableList<String>} 형태로 변환한다.
	 */
	@Override
	public ObservableList<String> unmarshal(String arg0) throws Exception {
		
		StringTokenizer token = new StringTokenizer(arg0, "|");
		ObservableList<String> list = FXCollections.observableArrayList();
		
		while(token.hasMoreTokens())
		{
			list.add(token.nextToken());
		}
		
		return list;
	}

	
	

}
