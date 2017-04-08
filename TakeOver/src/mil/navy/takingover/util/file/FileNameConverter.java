package mil.navy.takingover.util.file;

import java.time.LocalDate;

import mil.navy.takingover.util.DateUtil;

/**
 * 저장되는 인수인계 데이터의 파일 명 형태를 지정한다.
 * @since 2016. 1.30
 * @author 병장 서정삼
 */
public class FileNameConverter {

	public static String name = "saveFile_";
	private static String ext = ".xml";

	/**
	 * {@link LocalDate} 형태를 {@link String} 형태로 변환한다.
	 * @param date 입력 데이터
	 * @return 결과 문자열
	 */
	public static String toString(LocalDate date) {

		String monthString = String.valueOf(date.getMonthValue());
		String dayString = String.valueOf(date.getDayOfMonth());
		
		if (date.getMonth().getValue() < 10)
			monthString = monthString.replace(monthString, "0" + monthString);

		if (date.getDayOfMonth() < 10)
			dayString = dayString.replace(dayString, "0" + dayString);
		
		return name + date.getYear() + "." + monthString + "." + dayString;
	}

	/**
	 * 입력 된 파일명을 {@link LocalDate} 로 변환한다.
	 * @param date 입력 문자열
	 * @return {@link LocalDate}
	 */
	public static LocalDate toDate(String date) {

		String dateString = date.replaceFirst(name, "");	//파일명 앞의 문자를 제거한다.
		dateString = dateString.replaceAll(ext, "");	//확장자를 제거한다.
		
		if(DateUtil.validDate(dateString))
			return DateUtil.parse(dateString);
		
		return null;
	}

}
