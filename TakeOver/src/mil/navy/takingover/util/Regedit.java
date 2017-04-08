package mil.navy.takingover.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.prefs.Preferences;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mil.navy.takingover.MainApp;
import mil.navy.takingover.view.logview.LogStage;

/**
 * JavaSoft 에 포함 된 Preferences 에 레지스트로 접근을 지원하는 클래스
 * @version 1.5
 * @since 2016.09.12
 * @author 상병 서정삼
 *
 */
public class Regedit 
{

	/**
	 * {@link Preferences} 의 레지스트리 접근을 정의한다.
	 * 
	 */
	static Preferences userRootPrefs = Preferences.userNodeForPackage(MainApp.class);
	
	
	public static boolean isContains(String key)
	{
		return userRootPrefs.get(key, null) != null;
	}
	
	public static void setRegistry(String key, String value)
	{
		try
		{
			if(isContains(key))
				userRootPrefs.put(key, value);
			else
				userRootPrefs.put(key, value);
		}catch (Exception e)
		{
			LogStage.append("[주의]" + key + " " + value + "레지스트리 수정 도중 오류가 발생했습니다. " + e.getMessage());
		}
	}
	
	public static void setRegistry(String key, double value)
	{
		try
		{
			userRootPrefs.putDouble(key, value);
		}catch (Exception e)
		{
			LogStage.append("[주의]" + key + " " + value + "레지스트리 수정 도중 오류가 발생했습니다. " + e.getMessage());
		}
	}
	
	public static void setRegistry(String key, int value)
	{
		try
		{
			userRootPrefs.putInt(key, value);
		}catch (Exception e)
		{
			LogStage.append("[주의]" + key + " " + value + "레지스트리 수정 도중 오류가 발생했습니다. " + e.getMessage());
		}
	}
	
	public static void setRegistry(String key, Boolean value)
	{
		try
		{
			userRootPrefs.putBoolean(key, value);
		}catch (Exception e)
		{
			LogStage.append("[주의]" + key + " " + value + "레지스트리 수정 도중 오류가 발생했습니다. " + e.getMessage());
		}
	}
	
	public static String getStringValue(String key)
	{
		try
		{
			return userRootPrefs.get(key, "");
		}catch (Exception e)
		{
			LogStage.append(key + " 값을 찾을 수 없습니다." + e.getMessage());
			return null;
		}
		
	}
	
	public static Double getDoubleValue(String key)
	{
		try
		{
			return userRootPrefs.getDouble(key, 0);
		}catch (Exception e)
		{
			LogStage.append(key + " 값을 찾을 수 없습니다." + e.getMessage());
			return null;			
		}
	}
	
	public static int getIntegerValue(String key)
	{
		try
		{
			return userRootPrefs.getInt(key, 0);
		}catch (Exception e)
		{
			LogStage.append(key + " 값을 찾을 수 없습니다." + e.getMessage());
			return (Integer) null;
		}
	}
	
	public static boolean getBooleanValue(String key)
	{
		try
		{
			return userRootPrefs.getBoolean(key, false);
		}catch (Exception e)
		{
			LogStage.append(key + " 값을 찾을 수 없습니다." + e.getMessage());
			return (Boolean) null;
		}
	}
	
	/**
	 * 레지스트리 편집기의 사용이 제한 된 PC 의 권한을 강제로 해제한다.<BR>
	 * <code>unHookReg.inf</code>의 파일을 C:\ 에 생성 한 뒤<BR>
	 * <code>Runtime Process</code> 생성을 통하여 해당 파일을 실행하여 접근 권한을 해제한다.
	 * @throws IOException
	 */
	public static void enableRegistryEditor() throws IOException{
		ObservableList<String> builder = FXCollections.observableArrayList(
				"[Version]",
				"Signature=\"$Chicago$\"",
				"Provider=Symantec",
				"",
				"[DefaultInstall]",
				"AddReg=UnhookRegKey",
				"",
				"[UnhookRegKey]",
				"HKLM, Software\\CLASSES\\batfile\\shell\\open\\command,,,\"\"\"%1\"\" %*\"",
				"HKLM, Software\\CLASSES\\comfile\\shell\\open\\command,,,\"\"\"%1\"\" %*\"",
				"HKLM, Software\\CLASSES\\exefile\\shell\\open\\command,,,\"\"\"%1\"\" %*\"",
				"HKLM, Software\\CLASSES\\piffile\\shell\\open\\command,,,\"\"\"%1\"\" %*\"",
				"HKLM, Software\\CLASSES\\regfile\\shell\\open\\command,,,\"regedit.exe \"\"%1\"\"\"",
				"HKLM, Software\\CLASSES\\scrfile\\shell\\open\\command,,,\"\"\"%1\"\" %*\"",
				"HKCU, Software\\Microsoft\\Windows\\CurrentVersion\\Policies\\System,DisableRegistryTools,0x00000020x0"
				);
		
		File file = new File("C:" + File.separator + "unHookReg.inf");
		BufferedWriter fout = new BufferedWriter(new FileWriter(file, false));
		
		for(String str:builder){
			LogStage.append("[C:" +File.separator+ "unHookReg.inf] " + str);
			fout.write(str);
			fout.newLine();
			fout.flush();
		}
		
		fout.close();
		
		String command[] = { "cmd", "/c", "rundll32", "syssetup,SetupInfObjectInstallAction", "DefaultInstall", "128", "C:\\unHookReg.inf" };
		Runtime runtime = Runtime.getRuntime();
		Process process = runtime.exec(command);
		BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "EUC-KR"));
		
		
		String line = "";
		
		while ((line = br.readLine()) != null){
			LogStage.append("[Runtime] " + line);
		}
		LogStage.append("레지스트리 사용 제한을 해제했습니다.");
		
		file.delete();
	}
	
	
}
