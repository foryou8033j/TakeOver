package mil.navy.takingover.util;

import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * LocalDate 형태를 XML 형태로 저장할 때 변환 규격을 지정 해 주는 쓰레드
 * @version 1.0
 * @author 병장 서정삼
 */
public class LocalDateAdapter extends XmlAdapter<String, LocalDate>{

	@Override
	public String marshal(LocalDate arg0) throws Exception {
		return arg0.toString();
	}

	@Override
	public LocalDate unmarshal(String arg0) throws Exception {
		return LocalDate.parse(arg0);
	}

	
}
