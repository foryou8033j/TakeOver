package mil.navy.takingover.util.file;

import java.io.File;
import java.util.Observable;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mil.navy.takingover.view.logview.LogStage;

/**
 * 파일 복사/이동/삭제를 지원하는 클래스
 * @since 2016.08.12
 * @author 상병 서정삼
 */
public class FileHandler {

	
	/**
	 * 경로의 모든 파일과 폴더를 삭제한다.
	 * @param filePath 대상 경로
	 * @return 삭제된 파일 리스트
	 */
	public static ObservableList<StringProperty> Delete(String filePath){
		
		ObservableList<StringProperty> result = FXCollections.observableArrayList();
		
		try{
				File file = new File(filePath);
				File files[] = file.listFiles();
				
				for(File f:files){
					
					if(f.isFile()){
						try{
							f.delete();
							result.add(new SimpleStringProperty(f.getPath()));
						}catch (Exception e){
							//ignore
						}
						LogStage.append("File Delete " + f.getPath());
						System.out.println("FileDelete :" + f.getPath());
					}else{
						Delete(f.getPath());
					}
					try{
						f.delete();
						LogStage.append("File Delete " + f.getPath());
						result.add(new SimpleStringProperty(f.getPath()));
					}catch (Exception e){
						//ignore
					}
				}
				file.delete();
				LogStage.append("File Delete " + file.getPath());
				result.add(new SimpleStringProperty(file.getPath()));
		}catch (Exception e){
			LogStage.append("[주의] 파일 삭제 도중 오류가 발생하였습니다.");
		}
		
		return result;
		

		
	}
	
}
