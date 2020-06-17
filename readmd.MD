# kakaopay-PreExam
###카카오페이 경력 서버 과제
```
결제, 결제취소요청을 받아서 string 데이터로 카드사와 통신하는 서비스를 개발하려고 합니다.
기능명세에 대한 결제시스템 API를 개발하고 Test 코드를 작성.

```

### 개발환경
```
- Language : Java 1.8.
  익숙한 언어이며 웹서비스에 다양한 기능을 포함하고 있으며 Spring, Springboot와 같은 많이 사용하는 프레임웍이 있어서 선택
- Framework : SpringBoot 
  Spring과 embbaded tombat이 내장되어 있어 별도의 WAS가 필요없으며 빠르게 REST API 개발이 빠르게가능
- IDE : Intellij COMMUNITY 2020.1
  Eclipse를 오래 사용하였지만 IntelliJ가 속도도 빠르며 추천기능과 리펙토링, 디버깅이 편리하여 사용하기로 함
- Build : Gradle 6.5
  maven의 xml에 비하여 직관적이며, linux/windows에서도 쉽게 빌드가 가능하고 Groovy 언어 기반이라 분기처리가 가능하다는 장점이 있어서 선택
```

### Dependencies
Dependence         |Version        | Description
-------------------|--------------|-------------
|
spring-boot       | 2.1.7.RELEASE | Spring boot
spring-boot-starter-data-jpa | 2.1.7.RELEASE | Spring boot jpa
spring-boot-starter-web | 2.1.7.RELEASE | Spring boot web
spring-boot-starter-test | 2.1.7.RELEASE | Spring boot unit test
com.h2database:h2 | 1.4.200 | in memory db 사용
org.projectlombok:lombok | 1.18.12 | getter, setter, construction, bulider ... 등 JAVA 개발편의성 목적
org.apache.commons:commons-lang3 | 3.10 | StringUtil사용을 위함(nvl, isNull, isNumeric ...)


###  Gradle Build at IntelliJ 
``` bash
1. 좌측상단의 Grade 탭 클릭
2. Run Configurations > KakaopayPreExam > 더블클릭 또는 오른쪽버튼 누르고 Run 실행 
```

###  Gradle Build at Windows
``` bash
1. 환경변수에 JAVA를 위한 JAVA_HOME과 Path 설정
   JAVA_HOME = C:\Program Files\Java\jdk1.8.0_241
   Path = C:\Program Files\Java\jdk1.8.0_241\bin
2. 환경변수에 Gradle을 위한 Path 설정
   Path = D:\Programming\gradle-6.5\bin
3. 터미널창에 Build 실행
   > gradlew.bat boorjar 
4. build\libs\KakaoPreExam.jar 파일 생성확인
```

###  Gradle Build at linux
``` bash
# ./gradlew boorjar 
```

### Run 
``` bash
1. IntelliJ
   Application.java 파일 열고, 좌측 녹색버튼 Run 'Application.main()' 실행 
2. terminal
   # java -jar KakaoPreExam.jar
```

### Project folder structure
``` bash
KakaopayPreExam
├── .gradle                             : gradle Build 관련 자동생성
├── .idea                               : IntelliJ IDEA 프로젝트 설정관련 자동생성
├── build                               
│   └── libs                            : BoortJar 실행 시 실행가능한 KakaoPreExam.jar 파일 생성
├── gradle                              : gradle Build 관련 자동생성
│   └── wrapper
└── src
    ├── main
    │   └── java
    │       └── com.kakaopay.card
    │           └── common              : 관리번호 채번, 부가가치세 자동계산, 마스킹처리 등의 공통모듈 관련
    │               └── encrypt         : 암복호화 모듈
    │           └── domain              : 결제/취소타입 관련 열거자, 데이터 시간 관리 모듈
    │               └── cardinfo        : 카드사 정보 저장을 위한 JPA Entity/Repository 및 공통 모듈
    │               └── payment         : 결제/취소 데이터 저장을 위한 JPA Entity/Repository
    │           └── Exception           : 비즈니스 Exception 및 Exception Handler
    │           └── service.payment     : 비즈니스 구현을 위한 Service layer
    │           └── web                 : REST API를 위한 RestController
    │               └── dto             : Request -> RestController -> Response 를 위한 DTO
    │   └── resouces
    └── test                            
        └── java
            └── com.kakaopay.card 
                └── common              : 공통모듈 단위테스트
                    └── encrypt         : 암복호화모듈 : 단위테스트
                └── domain.payment      : JPA 및 DB 관련 단위테스트
                └── service             : 비즈니스 로직 관련 service 단위테스트
                └── web                 : REST API 관련 단위테스트
                    └── dto             : Validation 관련 단위테스트
```

### API 상세
구분 | API | request|response
-------------------|-------------------|-------------------|-------------------|
1. 결제 |  PUT /api/v1/payment | cardnum(카드번호), expired(유효기간),  cvc(CVC), installment(할부개월수),  amount(금액), vat(부가가치세, optional)  | errorCode(에러코드, 정상이면 비어있음), errorMessage(에러메시지, 정상이면 비어있음), managementId(결제관리번호), transactionTime(결제거래시간) |
2. 결제취소 | POST /api/v1/cancel | managementId(결제관리번호), amount(금액), vat(부가가치세, optional) |  errorCode(에러코드, 정상이면 비어있음), errorMessage(에러메시지, 정상이면 비어있음), managementId(취소관리번호), transactionTime(취소거래시간)  | 
3. 데이터 조회 | GET /api/v1/search/{managementId} | managementId(결제/취소관리번호) | errorCode(에러코드, 정상이면 비어있음), errorMessage(에러메시지, 정상이면 비어있음), managementId(결제/취소관리번호), cardNum(마스킹된 카드번호), expired(유효기간), cvc(CVC), paymentType(결제/취소구분), amount(금액), vat(부가가치세), transactionTime(결제/취소거래시간) |

### 문제해결전략
####1. 관리번호
- 유니크한 20자리의 번호가 채번되어야 하는것이 관건 
- 카드번호는 유니크하므로 서비스 요청 시점에 카드번호와 요청일시를 기반으로 한 번호를 조합하는 것으로 고려함
- 결제타입 1자리 + 카드번호길이 1자리  + 카드번호 16자리 + 요청일시 14자리(yyMMddHHmmssSS형태의 밀리세크까지 ) + 난수 3자리로 구성된 유니크한 숫자에 거의 가까운 숫자를 62개의 문자열(0~9, a~z, A~Z)로 매핑(62진법 변환)하여 20자리로 축약하는 형태로 채번
- 문제점 : 만약 카드결제나 취소 요청이 한꺼번에 다건이 들어온다면 난수3자리가 겹칠 가능성도 있기에, 겹치지 않으려면 이미 채번된 관리번호를 한번 조회하는 방법도 고려되어야 함, 하지만 번호가 많아질수록 성능 이슈가 있을 수 있음.
- Base62 출처 : https://stackoverrun.com/ko/q/4102291
- ManagementIdGenerator.java에 구현
####2. 암복호화
- 암복호화를 직접 구현하기에는 많은 시간과 노력이 소요 되는 것으로 판단
- 인터넷진흥원에서 개발한 SEED CBC 를 사용하기로 함
  SEED는 전자상거래, 금융, 무선통신 등에서 전송되는 개인정보와 같은 중요한 정보를 보호하기 위해 1999년 2월 한국인터넷진흥 원과 국내 암호전문가들이 순수 국내기술로 개발한 128비트 블록 암호 알고리즘
  SEED는 128비트의 암⋅복호화키를 이용하여 임의의 길이를 갖는 입력 메시지를 128비트의 블록단위로 처리하는 128비트 블록암호 알고리즘이다. 따라서 임의의 길이를 가지는 평문 메시지를 128비트씩 블록단위로 나누어 암호화하여 암호문을 생성
  CBC(Cipher Block Chaining) 모드는 동일한 평문 블록과 암호문 블록 쌍이 발생하지 않도록 전 단계의 암․복호화 결과가 현 단계에 영향을 주는 운영모드
- 출처 : https://seed.kisa.or.kr/kisa/Board/17/detailView.do
- 사용이유 : 금융사 간 데이터를 주고 받는 과정에서 SEED를 이용해본 경험이 있고, 참고할 자료가 많기에 채택, 블럭단위 암호화방식이므로 스트림 암호화보다 속도면에서 유리
- KISASeedEncryptor.java에 구현
####3. Request, Response 및 REST API 처리 흐름 
- Request : RequestDto interface를 만들고 유효값 체크를 위한 isValid()를 구현하도록 설계,
  각 Request마다 필요한 필수값과 유효한 값인지 체크하도록 RequestDto를 isValid()를 구현하고 RestController에서 체크
- Response : errorCode/errorMessage 필드를 가지는 ResponseDto Class를 만들고 각 Response마다 ResponseDto를 상속함, 정상응답 시 error 관련 필드는 빈값이며 관련 데이터 세팅함
- REST API 처리 흐름 : Request 시 RestController에서 Mapping된 method 호출 => RequestDto isValid 체크 => 정상적이면 Service 및 domain에 위임하여 비즈니스 로직 처리 후 해당되는 ResponseDto 객체 생성 => Response 객체 리턴
####4. Exception 
- BizExceptionHandler : 비즈니스 프로세스 수행 과정에서 발생되는 Exception들을 처리하기 위한 Handler
- BizExceptionType : 비즈니스 프로세스 수행 과정에서 발생되는 비정상적인 상황들을 열거자로 나열, code와 message로 구성됨
- BizException : RuntimeException을 상속, BizExceptionType 필드를 가지고 있어 비즈니스 수행과정에서 비정상적인 상황이 발생되면 BizExceptionType을 세팅하여 throw함
- PaymentBizException : BizException을 상속, payment API(결제) 수행 시 발생되는 BizException
- SearchBizException : BizException을 상속, search API(조회) 수행 시 발생되는 BizException
- CancelBizException : BizException을 상속, cancel API(취소) 수행 시 발생되는 BizException
####5. DataBase 
- JPA(Java Persistence API) 사용 : iBatis나 MyBatis와 같은 Mapper 대신 JAVA 표준 ORM(Object Relation Mapping)인 JPA를 활용하여 SQL보다는 JAVA에만 집중
- h2 DBMS 사용 : 인메모리 데이터베이스, dependencies만 넣어주면 바로 사용 가능, API 기동시 초기화되기에 테스트로 적합
####6. 결제 프로세스(payment API)
1) RequestDto의 validation 체크
2) vat가 비어있으면 자동계산 : CalcUtil.getVatFromAmount()
3) 결제관리번호 20자리 채번 : ManagementIdGenerator.getManagementIdByPaymentTypeAndCardNum()
4) 카드번호, 유효기간, CVC는 암호화 : KISASeedEncryptor.encrypt()
5) 결제정보 저장을 위한 Payment Entity를 생성
6) Spring-JPA를 통해 Payment 데이터 저장
7) "카드번호,유효기간,CVC" 형태로 암호화하여 String객체에 저장
8) 결제정보를 기반으로 TransactionDto 객체 생성
9) TransactionDto객체를 이용하여 450자리의 카드사 정보 문자열 생성 : CardInfoDataBuider.getCardInfoDataByTransactionDto()
10) 카드사 정보 저장을 위한 CardInfo Entity를 생성
11) Spring-JPA를 통해 CardInfo 데이터 저장
12) PaymentResponseDto 객체생성하여 최종 리턴
####7. 조회 프로세스(search API)
1) 관리번호의 validation 체크
2) 취소관리번호(cancelId)에 데이터가 있는지 조회 : paymentRepository.findByCancelId()
3) 데이터가 없으면 결제관리번호(paymentId)에 결제건으로 데이터가 있는지 조회 : paymentRepository.findByPaymentIdAndPaymentType()
4) 데이터가 없으면 BizExceptionType.DATA_NOT_FONUD를 type으로 가지는 SearchBizException 발생
5) 데이터가 있으면 카드번호는 복호화 후 마스킹 처리 : MaskingUtil.getMaskingCardNum(KISASeedEncryptor.decrypt(카드번호))
6) 유효기간, CVC 복호화
7) 결제타입, 금액, 부가가치세, 거래시간를을 String으로 변환
8) SearchResponseDto 객체 생성하여 최종 리턴
####8. 취소 프로세스(cancel API)
1) CancelReqeustDto의 validation 체크
2) 결제관리번호(paymentId)에 결제건으로 데이터가 있는지 조회 : paymentRepository.findByPaymentIdAndPaymentType()
3) 데이터가 없으면 취소를 할 수 없으므로 BizExceptionType.DATA_PAYMENT_NOT_FONUD를 type으로 가지는 CancelBizException 발생 
4) 결제관리번호(paymentId)의 취소 데이터가 있는지 조회 : paymentRepository.findByPaymentIdAndPaymentType()
5) 취소 데이터가 있으면 이미 취소된 건이므로 진행불가하여 BizExceptionType.CANCEL_FINISHED를 type으로 가지는 CancelBizException 발생
6) 취소 데이터가 없으면 취소 가능여부 체크 : 결제금액 < 취소금액, 취소금액 < 취소부가가치세
7) 결제타입은 취소(PAYMENT), 할부개월수는 00으로 세팅
8) 취소관리번호(cancelId) 채번
9) 취소정보 저장을 위한 Payment Entity를 생성, 이때 카드번호, 유효기간, CVC는 암호화
10) Spring-JPA를 통해 Payment 데이터 저장
11) 취소정보를 기반으로 TransactionDto 객체 생성
12) TransactionDto객체를 이용하여 450자리의 카드사 정보 문자열 생성 : CardInfoDataBuider.getCardInfoDataByTransactionDto()
13) 카드사 정보 저장을 위한 CardInfo Entity를 생성
14) Spring-JPA를 통해 CardInfo 데이터 저장
15) CancelResponseDto 객체생성하여 최종 리턴

### 테이블설계
####1. Payment
- 결제/취소 정보를 저장하는 테이블
- 결제와 취소, 관리번호만 제외하면 유사한 구조를 가지고 있기에 결제/취소 구분값을 두고 한 테이블(객체)에서 관리, 즉 결제/취소 테이블으로 따로 만들지 않음 
- PK는 DB에서 Long Type으로 자동으로 생성
- Payment.java에 구현
- 세부명세

컬럼명 | KEY | Nullable | TYPE | LENGTH | COMMENT
-------|-------|-------|-------|-------|-------|
id | PK | NO | Long |  |  PK GenerationType.IDENTITY 으로 자동생성 |
paymentType | | NO | ENUM |  | 결제타입 (결제 PAYMENT, 취소 CALCEL) |
paymentId | | NO | String | 20 | 결제관리번호 |
cancelId | | YES | String | 20 | 취소관리번호, paymentType이 취소일 경우에만 존재  |
cardnum | | NO | String | | 카드번호, 10~16자리 |
expired | | NO | String | | 유효기간, MM(00~12)/YY |
cvc | | NO | String | | CVC | |
installment | | NO | String | | 할부개월(0이면 일시불, 취소건은 00이면 일시불) |
amount | | NO | long | 10 | 금액 |
vat | | NO | long | 10 | 부가가치세, 비어있으면 amount를 11로 나눈 후 소수점 첫째자리 반올림하여 자동계산 |
####2. CardInfo
- 결제/취소 데이터가 생성될 때마다 카드사와 인터페이스 하기위한 규약된 레이아웃의 데이터를 저장하는 테이블
- 결제/취소할때마다 유니크한 관리번호가 생성되므로 관리번호를 키로 하여 카드사와 인터페이스할 정보 저장
- CardInfo.java에 구현
- 세부명세

컬럼명 | KEY | Nullable | TYPE | LENGTH | COMMENT
-------|-------|-------|-------|-------|-------|
managementId | PK | NO | String | 20 | 결제/취소관리번호 |
data | | NO | String | 450 | 카드사 저장 데이터 |

### 문제점 및 본인 피드백
####1. 관리번호 채번
- 만약 카드결제나 취소 요청이 한꺼번에 다건이 들어온다면 난수3자리가 겹칠 가능성도 있음
- 또한 멀티스래드 환경에서도 관리번호의 중복이 일어날 가능성이 있음
- 관리번호가 겹치지 않으려면 이미 채번된 관리번호를 다시 한번 조회하는 방법도 고려되어야 함,
  하지만 번호가 많아질수록 재조회에 대한 성능 이슈가 있을 수 있기에 관리번호 채번 프로세스에 대한 개선이 필요해 보임
####2. ApiControllerTest.invlidCardNumTest() 단위 테스트 수행
- 카드번호를 9자리로 넣어서 유효하지 않은 카드번호임을 확인하는 payment API 관련 단위 테스트
- BizExceptionHandler객체가 catch하여 handlePaymentBizException() 메소드가 수행되고 최종적으로 PaymentResponseDto 객체에 에러코드를 설정하여 리턴될 것으로 예상
- 하지만, TestRestTemplate의 PUT 메소드를 사용하기 위해 exchange()를 실행하여 유효하지 않은 카드번호를 넣어 PaymentBizException을 발생시켰지만 Exception이 발생되며 BizExceptionHandler에서 처리가 안됨
- 결국, 단위테스트에서 Exception을 catch하여 exception객체가 null이 아닌지 체크하는 테스트 코드로 작성
- GET/POST 메소드가 사용되는 search와 cancel API 단위테스트의 경우 RestController에서 BizException이 발생되면 BizExceptionHandler에서 처리되어 원하는 ResponseDto로 리턴됨
- payment API를 POST 메소드로 바꾸기에는 영향범위가 크기에 SKIP 하였음
####3. REST API HTTP METHOD 선택
- METHOD에 관련된 REST API의 정확한 표준은 없음. 해당과제 역시 자유스럽게 선택 가능함
- 일반적으로 CRUD와 매핑되는 것은 C : POST / R : GET / U : PUT / D : DELETE 가 일반적이라고 하나, C : PUT / U : POST 를 사용하는 경우도 있음
- PUT의 단어가 놓다, 만들다의 의미가 있으며 JAVA Collection을 사용할때도 객체를 넣는데 PUT(Object)을 사용하기에 결제관련 API인 payment API를 PUT으로 사용하기로 함
- 하지만 개발과정에서 TestRestTemplate의 PUT과 DELETE는 void로 리턴되는 것이 없다는걸 알게되었으며, 결제 API는 관리번호를 리턴해주고 있기에 의미상 맞지 않음. 
  또한 문제점인 2. ApiControllerTest.invlidCardNumTest() 단위 테스트 수행의 과정이 발생되어 어려움을 겪음
- 따라서 payment API를 POST 메소드로 바꾸기에는 영향범위가 크며 개발일정을 지연시키는 큰 원인이 됨
- 결론 :  REST API HTTP METHOD 선택은 단순하지 않으며 단위 테스트 및 개발전반적으로 영향을 미치기 때문에 단순 단어의 의미로 접근했던 것은 잘못 된 선택
####4. 단위테스트 코드의 중요성
- 현재 근무중인 사이트의 테스트는 컴파일-빌드 후 바로 서버에 올려서 수동으로 테스트 한 결과를 스크린샷으로 저장하여 문서를 만들어서 진행함
  비즈니스 로직 하나를 수정하고 빌드결과를 기다리며 상황에 따라 전체를 다시 테스트해야하는 경우가 빈번함(엔터프라이즈에서는 단위테스트를 하기위한 환경 구성이 어려운 점도 있음.)
- JUnit을 통해 단위테스트를 잘 활용할 경우, 비즈니스 로직을 수정하여도 기존의 단위테스트 및 변경분에 해당되는 단위테스트를 수행하여 전체적으로 테스트가 가능함. Test 코드 자체가 테스트문서가 될 수 있음
- JUnit과 SpringBoot 테스트를 이용하는 것이 익숙치 않아서 어려움을 겪었지만, 개발자로써 발전할 수 있는 큰 경험으로 작용됨

### 과제 후기
- REST API를 직접 구현해보면서, 새로운 IDE툴과 새로운 빌드툴 등 새로운 시도를 많이 하였다. 새로운 것을 한다는 것이 이렇게 재밌는 경험일 줄이야...
- 결과는 어떻게 될지 몰라도 방법을 고민하고 코딩하고 테스트하며 하나씩 구현해 나가는 시간이 너무나도 즐거웠다.
- readme.MD 문법이 익숙치 않아서, 생각대로 양식을 만들기가 어렵다. 공부해야할게 또 생겼다.

### 인터뷰 시 부족했던 사항과 앞으로 보완해야할 점
1. Spring DI 3가지 방법 : 생성자와 autowired만 대답을 했다..
2. 생성자 주입방법이 좋은 이유 : 지나치고 넘어가서 그게 좋다라고만 들었지 좋은 이유를 몰랐다. 부족했다...
3. DI를 하는 이유 : 제대로 대답을 못했다. 약한 결합, IoC등 대해서 대답을 했어야 했는데...
4. 멀티스래드 구현 : 멀티스래드에 대해서 구현하지 못했고 할수 있는 방법에 대해서 미리 준비하지 못했다. 즉흥적인 대답은 오히려 마이너스 요소 였다.
   멀티스래드에서 취소건을 구현 할 수 있는 방법을 많이 고민해 봐야겠다.
5. 코드가 너무 길다 : 다른 사람이 봐도 쉽게 이해할수 있도록 세세하게 길게 코드를 나열하는것으로 대답했으나 지나치게 길며 정리가 필요하다고 하였다.
   클린코드를 참고하면 좋을꺼 같다는 의견을 줬고, 역시 공감한다. 때마침 막 서적을 읽기 시작했지만 도움이 되지는 않았다.
6. REST API에서 PUT을 사용한 이유 : 자유롭게 선택가능하다고 하였기에 PUT도 사용했다는 것을 보여주고 싶었지만 크게 어필될 사항은 아니었다. 덕분에 테스트에 어려움만 있었다. 마이너스로 작용
7. Controller에서 try catch를 한 이유 : validate 를 구현하려고 했다가 그걸로는 자세한 메시지 처리가 안될꺼 같아서 Exception을 throw하는 형식으로 바꾸는 과정에서 불필요하게 넣었다. 역시나 마이너스로 작용
8. 암호화 모듈 : 좋은 암호화 모듈이 많지만 Seed 를 사용한 이유는 익숙하고 기한내에 구현해야 하는 상황이라 선택하였다고 하였다. 좀 더 대중적인 암호화모듈을 사용하는 편이 좋았을듯 하다.
9. 관심있는 분야 : 클라우드 분야를 이야기 하였지만, 여기에서 실제로 업무에 크게 도움될만 것은 아니다. 실무자들의 질문의도를 파악하지 못했다. 지금까지의 부족한 대답들을 보완할 수 있는 것이 오히려 나아보인다.
결론 : 스프링과 멀티스래드에 대한 처리의 부족한점이 많았다. 그동안 너무 비즈니스 로직과 이미 구현된 모듈에만 의존했다. 기술적인 부분에 대해서 보완이 필요하다.
