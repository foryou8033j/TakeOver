package mil.navy.takingover.util.file;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.StringTokenizer;

import mil.navy.takingover.MainApp;

/**
 * 현재 수정 중인 테이블 현황을 출력하는 파일을 관리하는 클래스
 * @since 2017.2.12
 * @version 1.6
 * @author 병장 서정삼
 *
 */
public class ModifyHandler {

	/**
	 * 변경 감지 대상 파일<BR>
	 * {@value}
	 * 
	 */
	public static String modifyFileName = "CurrentModify.txt";
	
	/**
	 * 현재 수정 중인 대상 파일 입력
	 * @param name
	 */
	public static void currentModity(String name)
	{
		//현재 파일을 수정하고 있다는 데이터 파일을 임시 생성한다.
		try
		{
			File curModifyFiles = new File(MainApp.uncPath + modifyFileName);
			BufferedWriter fout = new BufferedWriter(new FileWriter(curModifyFiles, true));
			fout.write(name+"|"+ MainApp.getMyIP());
			fout.newLine();
			
			fout.close();
		}catch (Exception e)
		{
			
		}
	}
	
	/**
	 * 대상 파일이 수정이 끝남을 입력
	 * @param name
	 */
	public static void removeModify(String name)
	{
		//UNC 경로의 파일을 로컬로 가져온다.
		//NIOFileCopy.copy(MainApp.uncPath + modifyFileName, MainApp.path + modifyFileName);
		try{
			File file = new File(MainApp.uncPath + modifyFileName);
			BufferedReader bufferReader = new BufferedReader(new FileReader(file));
			
			//1. 삭제하고자 하는 position 이전까지는 이동하여 dummy에 저장
			
			StringBuilder strBuilder = new StringBuilder();
			
			char[] buf = new char[(int)file.length()];
			
			bufferReader.read(buf);
			strBuilder.append(buf);
			
			System.out.println(strBuilder.toString().replaceAll(name+"[|]"+ MainApp.getMyIP() +"\r\n", ""));
			
			bufferReader.close();
			
			BufferedWriter fout = new BufferedWriter(new FileWriter(file, false));
			
			fout.write(strBuilder.toString().replaceAll(name+"[|]"+ MainApp.getMyIP() +"\r\n", ""));
			
			fout.close();
			
			//수정 된 파일을 UNC 경로로 이동한다.
			//NIOFileCopy.copy(MainApp.path + modifyFileName, MainApp.uncPath + modifyFileName);
			
		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 현재 대상 파일이 수정 중인지 확인
	 * @param name
	 * @return
	 */
	public static boolean isCurrentModify(String name)
	{
		//UNC 경로의 파일을 로컬로 가져온다.
				//NIOFileCopy.copy(MainApp.uncPath + modifyFileName, MainApp.path + modifyFileName);
				try{
					File file = new File(MainApp.uncPath + modifyFileName);
					BufferedReader bufferReader = new BufferedReader(new FileReader(file));
					
					//1. 삭제하고자 하는 position 이전까지는 이동하여 dummy에 저장
					
					StringBuilder strBuilder = new StringBuilder();
					
					char[] buf = new char[(int)file.length()];
					
					bufferReader.read(buf);
					strBuilder.append(buf);
					
					bufferReader.close();
					
					//현재 사용중인 파일명이 맞다면
					if(strBuilder.toString().contains(name))
					{
						//만약 자신의 IP가 포함되어 있다묜
						if(strBuilder.toString().contains(name+"|"+ MainApp.getMyIP()))
							return false; //그것은 내가 수정하고 있다는 의미.
						else 
							return true;
					}
					else
						return false;
					
					
				}catch (Exception e)
				{
					//ignore
				}
				return false;
	}
	
	/**
	 * 현재 대상 파일이 수정 중인 경우 수정중인 정보를 반환
	 * @param name
	 * @return
	 */
	public static String getOverlabInfo(String name)
	{
		//UNC 경로의 파일을 로컬로 가져온다.
		//NIOFileCopy.copy(MainApp.uncPath + modifyFileName, MainApp.path + modifyFileName);
		try{
			File file = new File(MainApp.uncPath + modifyFileName);
			BufferedReader bufferReader = new BufferedReader(new FileReader(file));
			
			//1. 삭제하고자 하는 position 이전까지는 이동하여 dummy에 저장
			
			StringBuilder strBuilder = new StringBuilder();
			
			char[] buf = new char[(int)file.length()];
			
			bufferReader.read(buf);
			strBuilder.append(buf);
			
			bufferReader.close();
			
			//현재 사용중인 파일명이 맞다면
			if(strBuilder.toString().contains(name))
			{
				//만약 자신의 IP가 포함되어 있다묜
				if(strBuilder.toString().contains(name+"|"+ MainApp.getMyIP()))
					return null; //그것은 내가 수정하고 있다는 의미.
				else 
				{
					String tmp = strBuilder.toString();
					StringTokenizer rootToken = new StringTokenizer(tmp, "\r\n");
					while(rootToken.hasMoreTokens())
					{
						String data = rootToken.nextToken();
						StringTokenizer token = new StringTokenizer(data, "|");
						if(token.nextToken().equals(name))
								return token.nextToken();
					}
				}
			}
			else
				return null;
			
			
		}catch (Exception e)
		{
			//ignore
		}
		return null;
	}
	
}
