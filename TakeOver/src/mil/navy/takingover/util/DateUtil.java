package mil.navy.takingover.util;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import mil.navy.takingover.view.logview.LogStage;

/**
 * {@link LocalDate} 타입과 {@link String} 타입 간의 변환을 지원한다. 
 * @author 병장 서정삼
 * @version 1.0
 * @since 2017.01.20
 * @see LocalDate
 * @see String
 */
public class DateUtil {

	/**
	 * 변환 날짜 형태를 지정<BR><BR>
	 * 형태 : {@value}
	 */
	private static final String DATE_PATTERN = "yyyy.MM.dd";
	
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
	
	/**
	 * {@link LocalDate} 형태를 {@link String} 형태로 변환한다.
	 * @param date 변환 대상 값
	 * @return 변환 된 값
	 */
	public static String format(LocalDate date){
		if (date == null)
			return null;
		
		return DATE_FORMATTER.format(date);
	}
	
	/**
	 * {@link String} 형태를 {@link LocalDate} 형태로 변환한다.
	 * @param dateString 변환 대상 문자열
	 * @return 변환 된 값
	 */
	public static LocalDate parse(String dateString){
		try{
			return DATE_FORMATTER.parse(dateString, LocalDate::from);
		}catch (DateTimeException e){
			LogStage.append("[주의] DateUtil.class 에서 날짜 변환 도중 오류가 발생하였습니다.");
			return null;
		}
	}
	
	/**
	 * 변환 대상 문자열이 유효한지 확인한다.
	 * @param dateString 확인 대상 문자열
	 * @return 확인 여부
	 */
	public static boolean validDate(String dateString){
		return DateUtil.parse(dateString) != null;
	}
	
}
