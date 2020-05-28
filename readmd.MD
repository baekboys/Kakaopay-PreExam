# kakaopay-PreExam
###카카오페이 경력 서버 과제
```
결제, 결제취소요청을 받아서 string 데이터로 카드사와 통신하는 서비스를 개발하려고 합니다.
기능명세에 대한 결제시스템 API를 개발하고 Test 코드를 작성.

```

### 개발환경
```
- 프레임웍 : SpringBoot 
- 언어 : Java 1.8.
- IDE : Intellij COMMUNITY 2020.1
```

### Dependencies
Dependence         |Version
-------------------|-------
|
spring-boot       |2.1.7.RELEASE
spring-boot-starter-data-jpa |
spring-boot-starter-web |
com.h2database:h2 |
org.projectlombok:lombok |
org.apache.commons:commons-lang3 | 3.10

### 기동방법
``` bash
# Using terminal
./gradlew bootRun

# Using IntelliJ
1. Sync gradle
2. Run Application
```

### API 상세
구분 |API | request|response
-------------------|-------| 
1. 결제 |  PUT /api/v1/payment | cardnum, expired,  cvc, installment,  amount, vat(optional)  | managementId, transactionTime, errorCode, errorMessage |
2. 결제취소 | POST /api/v1/cancel | managementId, amount, vat(optional) |  managementId, transactionTime, errorCode, errorMessage  | 
3. 데이터 조회 | GET /api/v1/search/{managementId} | managementId | managementId, cardNum, expired, cvc, paymentType, amount, vat, transactionTime, errorCode, errorMessage  |

### 문제해결
####1. 관리번호
- 유니크한 20자리의 번호가 채번되어야 함. 
- 카드번호는 유니크하므로 서비스 요청 시점에 카드번호 + 시간(yyMMddHHmmssSS) + 난수로 구성된 숫자를 62진법으로  ASCII코드값과 매핑하여 20자리로 축약하는 형태로 채번
- ManagementIdGenerator.java 구현

####2. 암복호화
- 암복호화를 직접 구현하기에는 많은 시간이 소요 되는 것으로 판단
- 인터넷진흥원에서 개발한 SEED를 사용하기로 함
-SEED는 전자상거래, 금융, 무선통신 등에서 전송되는 개인정보와 같은 중요한 정보를 보호하기 위해 1999년 2월 한국인터넷진흥 원과 국내 암호전문가들이 순수 국내기술로 개발한 128비트 블록 암호 알고리즘
- 금융사 간 데이터를 주고 받는 과정에서 SEED를 이용해본 경험이 있고, 참고할 자료가 많기에 채택
- KISASeedEncryptor.java 구현

### 테이블설계
####1. Payment
- 결제/취소 정보를 저장하는 테이블
- 결제와 취소, 관리번호만 제외하면 유사한 구조를 가지고 있기에 결제/취소 구분값을 두고 저장 
- PK는 DB에서 자동으로 생성
####2. CardInfo
- 결제/취소 데이터가 생성될 때마다 카드사와 인터페이스 하기위한 규약된 레이아웃의 데이터를 저장하는 테이블
- 결제/취소할때마다 유니크한 관리번호가 생성되므로 관리번호를 키로 하여 카드사와 인터페이스할 정보 저장