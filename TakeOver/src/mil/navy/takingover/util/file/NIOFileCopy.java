package mil.navy.takingover.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import javafx.scene.control.Alert.AlertType;
import mil.navy.takingover.MainApp;
import mil.navy.takingover.util.ExceptionDialog;
import mil.navy.takingover.view.logview.LogStage;

/**
 * NIO 방식을 통한 파일 복사 스트림 클래스
 * @since 2017.01.12
 * @version 1.2
 * @author 병장 서정삼
 *
 */
public class NIOFileCopy {
	
	/**
	 * NIO 방식을 통한 파일 복사 메소드
	 * {@link NIOFileCopy}
	 * @param source 복사 대상 원본
	 * @param target 복사 결과 대상
	 */
	public static void copy(String source, String target)
	{

		System.out.println("# try File transfer " + source +" to " + target);
		
		//복사 대상이 되는 파일 생성
		File sourceFile = new File(source);
		File targetFile = new File(target);
		
		targetFile.getParentFile().mkdirs();
		
		//스트림, 채널 생성
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		FileChannel fcin = null;
		FileChannel fcout = null;
		
		try
		{
			//스트림 생성
			inputStream = new FileInputStream(sourceFile);
			outputStream = new FileOutputStream(target);
			
			//채널 생성
			fcin = inputStream.getChannel();
			fcout = outputStream.getChannel();
			
			//채널을 통한 스트림 전송
			long size = fcin.size();
			fcin.transferTo(0, size, fcout);
			
			LogStage.append("[NIOFileCopy][성공] " + source + " -> " + target);
			System.out.println("# success File transfer " + source +" to " + target);
		}catch (Exception e)
		{
			//MainApp.errorOnLoad = true;
			LogStage.append("[NIOFileCopy][실패] " + source + " ->X " + target + "[" + e.getMessage() + "]");
			System.out.println("# NIO File Transfer : error code " + e.getMessage());
			System.out.println("# a error ocoured file transfer " + source +" to " + target);
			e.printStackTrace();
			
			if(target.contains(ModifyHandler.modifyFileName))
			{
				e.printStackTrace();
				
			}
		}
		finally {
			try{
				fcout.close();
				fcin.close();
				outputStream.close();
				inputStream.close();
			}catch (Exception e)
			{
				
			}
		}
		
		
	}
	
}
