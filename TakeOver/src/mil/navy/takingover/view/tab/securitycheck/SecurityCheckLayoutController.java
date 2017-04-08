package mil.navy.takingover.view.tab.securitycheck;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import mil.navy.takingover.MainApp;
import mil.navy.takingover.util.file.FileHandler;
import mil.navy.takingover.view.logview.LogPopup;
import mil.navy.takingover.view.logview.LogStage;

public class SecurityCheckLayoutController {

	@FXML
	Button action0;

	@FXML
	Button action1;

	@FXML
	Button action2;

	@FXML
	Button action3;

	@FXML
	Button action4;

	@FXML
	Button action5;

	@FXML
	Button action6;

	@FXML
	CheckBox checkBox0;

	@FXML
	CheckBox checkBox1;

	@FXML
	CheckBox checkBox2;

	@FXML
	CheckBox checkBox3;

	@FXML
	CheckBox checkBox4;

	@FXML
	CheckBox checkBox5;

	@FXML
	CheckBox checkBox6;

	@FXML
	Text text0;

	@FXML
	Text text1;

	@FXML
	Text text2;

	@FXML
	Text text3;

	@FXML
	Text text4;

	@FXML
	Text text5;

	@FXML
	Text text6;

	@FXML
	ProgressBar progress0;

	@FXML
	ProgressBar progress1;

	@FXML
	ProgressBar progress2;

	@FXML
	ProgressBar progress3;

	@FXML
	ProgressBar progress4;

	@FXML
	ProgressBar progress5;

	@FXML
	ProgressBar progress6;

	LogPopup logStage = null;

	private MainApp mainApp;

	public SecurityCheckLayoutController() {
		// TODO Auto-generated constructor stub
	}

	@FXML
	private void handleManager() {

	}

	@FXML
	private void handleCleanInternetTempFile() {
		action0.setDisable(true);

		String tmpPath = System.getProperty("java.io.tmpdir");

		File file = new File(tmpPath);

		Task<String> task = new Task<String>() {
			
			int i = 0;

			@Override
			protected String call() throws Exception {

				LogStage.append("인터넷 임시파일을 정리합니다.");
				String message = "";

				ObservableList<StringProperty> result = FileHandler.Delete(file.getPath());

				int i = 0;
				for (StringProperty str : result) {
					message = message.concat(str.get() + "\n");
					i++;
					if (i > 10) {
						message = message.concat("등 " + result.size() + " 개의 파일을 정리하였습니다.");
						break;
					}
				}

				return message;
			}

		};

		progress0.progressProperty().bind(task.progressProperty());

		task.setOnSucceeded(event -> {
			Platform.runLater(() -> {
				progress0.progressProperty().unbind();
				progress0.setProgress(1.0);
				checkBox0.setSelected(true);
				action0.setDisable(false);
				text0.setText("인터넷 임시 파일을 정리하였습니다.");
				LogStage.append("인터넷 임시파일 정리 완료.");
			});

			progress0.setOnMouseEntered(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent arg0) {

					if (logStage == null) {
						logStage = new LogPopup(arg0, "정리된 인터넷 임시파일은 다음과 같습니다.", task.getValue());
						logStage.show();
					}
				}

			});

			progress0.setOnMouseExited(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent arg0) {
					if (logStage != null) {
						logStage.close();
						logStage = null;
					}
				}

			});

		});

		Thread thread = new Thread(task);
		thread.start();

	}

	@FXML
	private void handleCleanInternetUseHistory() {
		action1.setDisable(true);

		Task<String> task = new Task<String>() {

			String message = "";

			@Override
			protected String call() throws Exception {
				LogStage.append("인터넷 사용 기록을 정리합니다.");
				ObservableList<StringProperty> result1 = FileHandler
						.Delete(new File("C:" + File.separator + "Windows" + File.separator + "Cookies").getPath());
				ObservableList<StringProperty> result2 = FileHandler
						.Delete(new File("C:" + File.separator + "Windows" + File.separator + "Temp").getPath());
				ObservableList<StringProperty> result3 = FileHandler
						.Delete(new File("C:" + File.separator + "Windows" + File.separator + "Tempory Internet Files")
								.getPath());

				result1.addAll(result2);
				result1.addAll(result3);

				int i = 0;
				for (StringProperty str : result1) {
					message = message.concat(str.get() + "\n");
					i++;
					if (i > 10) {
						message = message.concat("등 " + result1.size() + " 개의 파일을 정리하였습니다.");
					}
				}

				return message;
			}

		};

		progress1.progressProperty().bind(task.progressProperty());

		// LogStage logStage = new LogStage(null, "asdsd", "asdasd'");

		task.setOnSucceeded(event -> {

			Platform.runLater(() -> {
				progress1.progressProperty().unbind();
				progress1.setProgress(1.0);
				action1.setDisable(false);
				checkBox1.setSelected(true);
				text1.setText("인터넷 사용 기록을 정리하였습니다.");
				LogStage.append("인터넷 사용 기록 정리를 완료하였습니다.");

				progress1.setOnMouseEntered(new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent arg0) {

						if (logStage == null) {
							logStage = new LogPopup(arg0, "정리된 인터넷 사용기록은 다음과 같습니다.", task.getValue());
							logStage.show();
						}
					}

				});

				progress1.setOnMouseExited(new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent arg0) {
						if (logStage != null) {
							logStage.close();
							logStage = null;
						}
					}

				});

			});
		});

		Thread thread = new Thread(task);
		thread.start();

	}

	@FXML
	private void handleCleanProgramUseHistory() {

		action2.setDisable(true);

		Task<String> task = new Task<String>() {

			int i = 0;

			@Override
			protected String call() throws Exception {

				String message = "";
				LogStage.append("프로그램 사용 기록을 정리합니다.");

				ObservableList<StringProperty> result1 = FileHandler
						.Delete(new File("C:" + File.separator + "Windows" + File.separator + "History").getPath());
				ObservableList<StringProperty> result2 = FileHandler
						.Delete(new File("C:" + File.separator + "Windows" + File.separator + "Recent").getPath());

				result1.addAll(result2);

				int i = 0;
				for (StringProperty str : result1) {
					message = message.concat(str.get() + "\n");
					i++;
					if (i > 10) {
						message = message.concat("등 " + result1.size() + " 개의 파일을 정리하였습니다.");
					}
				}

				return message;
			}
		};

		progress2.progressProperty().bind(task.progressProperty());

		task.setOnSucceeded(event -> {
			Platform.runLater(() -> {
				progress2.progressProperty().unbind();
				progress2.setProgress(1.0);
				action2.setDisable(false);
				checkBox2.setSelected(true);
				text2.setText("프로그램 사용 기록을 삭제 하였습니다.");
				LogStage.append("프로그램 사용 기록 정리를 완료하였습니다.");
			});

			progress2.setOnMouseEntered(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent arg0) {

					if (logStage == null) {
						logStage = new LogPopup(arg0, "정리된 프로그램 사용 기록은 다음과 같습니다.", task.getValue());
						logStage.show();
					}
				}

			});

			progress2.setOnMouseExited(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent arg0) {
					if (logStage != null) {
						logStage.close();
						logStage = null;
					}
				}

			});

		});

		Thread thread = new Thread(task);
		thread.start();

	}

	@FXML
	private void handleFileUseHistory() {

		checkBox3.setSelected(true);
		text3.setText("파일 이동/복사/삭제 기록을 삭제하였습니다.");
	}

	@FXML
	private void handleSecurityCheck() {

		StringProperty result = new SimpleStringProperty();
		
		try{
			LogStage.append("로컬 보안 정책을 수정합니다.");
			
			ObservableList<String> builder = FXCollections.observableArrayList(
					"[Unicode]",
					"Unicode=yes",
					"[System Access]",
					"MinimumPasswordAge = 0",
					"MaximumPasswordAge = 35",
					"MinimumPasswordLength = 9",
					"PasswordComplexity = 1",
					"PasswordHistorySize = 0",
					"LockoutBadCount = 3",
					"ResetLockoutCount = 30",
					"LockoutDuration = 30",
					"RequireLogonToChangePassword = 0",
					"[Version]",
					"signature=\"$CHICAGO$\"",
					"Revision=1"
					);
			
			File file = new File("C:" + File.separator + "secpol.inf");
			BufferedWriter fout = new BufferedWriter(new FileWriter(file, false));
			
			for(String str:builder){
				LogStage.append("[C:" +File.separator+ "secpol.inf] " + str);
				fout.write(str);
				fout.newLine();
				fout.flush();
			}
			
			fout.close();
			
			String command[] = { "cmd", "/c", "secedit", "/configure", "/db", "C:\\secpol.sdb", "/cfg", "C:\\secpol.inf" };
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec(command);
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "EUC-KR"));
			
			String line = "\n";
			
			while ((line = br.readLine()) != null){
				result.set(result.get() +line+ "\n");
				LogStage.append("[Runtime] " + line);
			}
			
			result.set(result.get().replaceAll("null", ""));
			
			result.set(result.get() + "\n"
					+ "암호는 복잡성을 만족해야함 : 사용\n"
					+ "최근 암호 기억 : 0개\n"
					+ "최대 암호 사용 기간 : 35일\n"
					+ "최소 암호 길이 : 9문자\n"
					+ "최소 암호 사용 기간 : 0일\n"
					+ "계정 잠금 임계값 : 3회\n"
					+ "다음 시간 후 계정 잠금 수를 원래대로 설정 : 30분\n");
			
			file.delete();
		}catch (Exception e){
			LogStage.append("[주의] 로컬 로그인 보안 설정을 수정하던 도중 오류가 발생했습니다.");
		}
		
		
		progress4.progressProperty().unbind();
		progress4.setProgress(1.0);
		action4.setDisable(false);
		checkBox4.setSelected(true);
		text4.setText("로컬 로그인 보안 설정을 변경하였습니다.");
		LogStage.append("로컬 로그인 보안 설정을 완료하였습니다.");
		
		
		progress4.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {

				if (logStage == null) {
					logStage = new LogPopup(arg0, "로컬 로그인 보안 설정이 다음과 같이 변경되었습니다.", result.get());
					logStage.show();
				}
			}
			

		});

		progress4.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				if (logStage != null) {
					logStage.close();
					logStage = null;
				}
			}

		});
		
	}

	@FXML
	private void handleCleanRemonteDesktopHistory() {

		final StringProperty result = new SimpleStringProperty("레지스트리 HKEY_CURRENT_USER\\Software\\Microsoft\\Terminal Server Client 값을 삭제 합니다.\n");
		
		try {
			LogStage.append("원격 데스크탑 연결 기록을 정리합니다.");
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
			String command1[] = { "cmd", "/c", "reg", "delete", "HKEY_CURRENT_USER\\Software\\Microsoft\\Terminal Server Client", "/f" };
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec(command);
			Process process1 = runtime.exec(command1);
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "EUC-KR"));
			BufferedReader br1 = new BufferedReader(new InputStreamReader(process1.getInputStream(), "EUC-KR"));
			
			String line = "";
			
			while ((line = br.readLine()) != null){
				result.set(result.get() +line+ "\n");
				LogStage.append("[Runtime] " + line);
			}
			LogStage.append("레지스트리 사용 제한을 해제했습니다.");
				
			while ((line = br1.readLine()) != null){
				result.set(result.get() +line+ "\n");
				LogStage.append("[Runtime] " + line);
			}
			LogStage.append("원격 데스크탑 기록 레지스트리를 제거했습니다.");
				
			
			result.set(result.get().replaceAll("null", ""));

			String docuPath = System.getProperty("user.home");
			System.out.println(docuPath);

			File remontHistoryFile = new File(docuPath + File.separator + "Documents" + File.separator + "Default.rdp");
			remontHistoryFile.delete();
			file.delete();
			LogStage.append(remontHistoryFile.getPath() + " 를 삭제하였습니다.");
			
			result.set(result.get() + remontHistoryFile.getPath() + "파일 삭제");

		} catch (Exception e) {

		}

		progress5.progressProperty().unbind();
		progress5.setProgress(1.0);
		action5.setDisable(false);
		checkBox5.setSelected(true);
		text5.setText("원격 데스크탑 사용 기록을 삭제하였습니다.");
		LogStage.append("원격 데스크탑 사용 기록 삭제를 완료하였습니다.");
		
		progress5.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {

				if (logStage == null) {
					logStage = new LogPopup(arg0, "정리된 원격 데스크탑 기록은 다음과 같습니다.", result.get());
					logStage.show();
				}
			}

		});

		progress5.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				if (logStage != null) {
					logStage.close();
					logStage = null;
				}
			}

		});
		
	}

	@FXML
	private void handleCleanUNC() {

		
		final StringProperty result = new SimpleStringProperty();
		
		try {
			LogStage.append("공유 폴더 연결을 제거합니다.");
			String command[] = { "cmd", "/c", "net", "use", "*", "/delete", "/yes" };
			String command1[] = { "cmd", "/c", "net", "share", "*", "/delete", "/yes" };
			String command2[] = { "cmd", "/c", "net", "session", "*", "/delete", "/yes" };
			Runtime runtime = Runtime.getRuntime();

			Process process = runtime.exec(command);
			Process process1 = runtime.exec(command1);
			Process process2 = runtime.exec(command2);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "EUC-KR"));
			BufferedReader br1 = new BufferedReader(new InputStreamReader(process1.getInputStream(), "EUC-KR"));
			BufferedReader br2 = new BufferedReader(new InputStreamReader(process2.getInputStream(), "EUC-KR"));
			
			String line = "\n";
			
			while ((line = br.readLine()) != null){
				result.set(result.get() +line+ "\n");
				LogStage.append("[Runtime] " + line);
			}
			while ((line = br1.readLine()) != null){
				result.set(result.get() +line+ "\n");
				LogStage.append("[Runtime] " + line);
			}
			while ((line = br2.readLine()) != null){
				result.set(result.get() +line+ "\n");
				LogStage.append("[Runtime] " + line);
			}
			
			result.set(result.get().replaceAll("null", ""));

		} catch (Exception e) {

		}

		progress6.progressProperty().unbind();
		progress6.setProgress(1.0);
		action6.setDisable(false);
		checkBox6.setSelected(true);
		text6.setText("공유 폴더 / 네트워크 드라이브 연결을 해제 하였습니다.");
		LogStage.append("공유 폴더 / 네트워크 드라이브 연결을 해제를 완료하였습니다.");
		
		progress6.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {

				if (logStage == null) {
					logStage = new LogPopup(arg0, "정리된 공유폴더 해제 기록은 다음과 같습니다.", result.get());
					logStage.show();
				}
			}

		});

		progress6.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				if (logStage != null) {
					logStage.close();
					logStage = null;
				}
			}

		});
	}

	// rundll32 keymgr.dll KRShowKeyMgr

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

}
