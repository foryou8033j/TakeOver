# 통합 인수인계 도구 개발 문서

## 개요
### 개발 목적
	- 인수인계의 편의성 도모와 정확성, 신속성을 위함.
### 개발 자원
	- 언어 : Java
	- 개발 버전 : JDK 1.8
	- 최소동작 버전 : JRE 1.8
	- GUI : JavaFX
	- Wrapping : JAXBXML Wrapper
	- 외부 리소스 : DS Desktop Notify (JavaSwing)

## 기본 동작 구조
### MainApp 클래스와의 연결
	- MainApp 클래스는 기본적으로 RootLayoutController 와 연결되어 있으며,
	각 데이터 클래스를 정의하여 모든 클래스에서 접근이 가능하도록 허용하도록 한다.
	- MainApp 클래스에서 기본적인 파일의 입/출력과 레이아웃 초기화 동작이 이루어진다.
	- MainApp 클래서의 initTakeOverTool 메소드를 통해 레이아웃의 초기화 동작이 시작된다.
	- MainApp 클래스에서 인수인계 데이터 파일의 로드가 일괄적으로 이루어 진다. 데이터의 유효성 확인도 이루어진다.
### 각 Data 클래스
	- 각 데이터 클래스는 그 특성에 맞게 분리되어 있으며, 클래스 데이터를 Wrapping 할 수 있는 Wrapping 클래스를 둔다.
	- Data 클래스 (통합인수인계데이터 라고 한다), 는 내부에 TakeOverData 클래스와 InternetAcceptData 클래스를 통합으로 관리하여 Data 클래스를 Wrapping 한다.
	- 그 외 모든 데이터는 각 각 Wrapping 된다.
### Util 패키지
#### DateUtil
	- LocalDate 타입과 문자열간의 변환을 지원한다.
#### ExceptionDialog
	- 예외 발생에서 생성되는 Exception 로그를 UI 상으로 보여준다.
#### FileChecker
	- 00 시가 지났을 때 인수인계 새로운 인수인계 데이터의 생성을 감지한다.
#### IcmpProtocol
	- 특정 호스트에 ICMP 프로토콜을 발생시켜 Inet 연결 상태를 확인한다.
#### LocalDateAdapter
	- Data 클래스에서 LocalDate 타입과 문자열간의 변환을 지원한다.
#### MemberNameDapter
	- Data 클래스에서 저장된 멤버 문자열을 각각 인원으로 분활하는 것을 지원한다.
#### Regiedit
	- 레지스트리 접근을 지원한다.
#### FileHandler
	- 파일 삭제를 하는 static 메소드가 있다.
#### FileNameConverter
	- Local 상에 출력된 파일명을 Data 클래스의 안의 LocalDate 형태로 변환한다.
#### ModifyHandler
	- 데이터 폴더에 위치한 파일 변경 시 변경 자의 이력을 기록하는 파일을 관리한다.
#### NIOFileCopy
	- NIO 방식으로 파일을 복사한다.

### RootLayout
	- RootLayout 의 하단부에 위치한 TabPane과 상단부에 위치한 MenuBar 로 구성된다.
#### TabPane
##### TakeOverTab
	- 인수인계 데이터를 구성하는 탭, 인수인계 데이터와 인터넷 운용승인 데이터를 구현하는 부분으로 나뉘어 진다.
##### OfficerPasswordTab
	- 개개인의 패스워드를 등록하고 관리할 수 있는 탭
##### OtherPasswordTab
	- 기타 패스워드를 관리 할 수 있는 탭
##### TipTab
	- 간단한 내용 저장이 가능 한 탭
##### PhoneTab
	- 전화번호 입력이 가능 한 탭
##### SecurityCheckTab
	- PC 정리 기능이 포함 된 탭
##### GetOffTab
	- PC 전원 상태 확인이 가능 한 탭

#### Menubar
##### 환경설정
	- 간단한 환경 설정이 가능한 메뉴
##### 추가기능
	- 간단한 확장 기능 설정이 가능한 메뉴
##### 정보
	- 개발자 정보가 포함된 탭
	
#### 기타
	- 각 레이아웃 클래스의 패키지 안에는 파일 변화를 감지하는 ModifyListener 클래스가 포함되어 있다.

## 정리
	- 소스코드에 모든 주석을 서술 하지는 않았지만 전역전에 도움이 되고자 하는 마음에 제작을 하였다.
	- 3달만에 제작한 그 나마도 훈련기간과 당직 때 조금씩 짬나는 시간에 조금씩 만들어 완성도도 부족하고 
	구조, 내용도 부실하지만 분석하고 수정 할 수 있는 능력자가 있다면 고쳐나가길 바래 그럼 20000
	
	fin. 2017.03.19 병장 서정삼
		전역 79일 남았을 때, 귀찮아서 마무리.