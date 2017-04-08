package mil.navy.takingover.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import mil.navy.takingover.view.logview.LogStage;

public class IcmpProtocol {

	
	/**
	 * 대상 호스트의 ICMP Protocol 도달 여부를 확인한다.
	 * @param host 대상 호스트
	 * @return 결과 값
	 */
	public static boolean isReachable(String host)
	{
		try{
			String cmd = "";
			
			if(System.getProperty("os.name").startsWith("Windows")){
				//windows
				cmd = "ping -n 1 " + host;
			}else {
				//linux or Os X
				cmd = "ing -c 1 " + host;
			}
			Process myProcess = Runtime.getRuntime().exec(cmd);
			BufferedReader br = new BufferedReader(new InputStreamReader(myProcess.getInputStream(), "EUC-KR"));
			
			String line = "\n";
			
			//while ((line = br.readLine()) != null)
			//	LogStage.append("[Runtime] "+ line);
			
			myProcess.waitFor();
			
			if(myProcess.exitValue() == 0)
				return true;
			else
				return false;
		}catch (Exception e)
		{
			//ignore
			return false;
		}
	}
	
}
