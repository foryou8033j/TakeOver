package mil.navy.takingover.util;

import java.util.StringTokenizer;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MemberNameAdapter extends XmlAdapter<String, ObservableList<String>>{

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
