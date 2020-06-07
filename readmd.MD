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
- Build : Gradle 6.1
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
- 유니크한 20자리의 번호가 채번되어야 하는것이 관건 
- 카드번호는 유니크하므로 서비스 요청 시점에 카드번호와 요청일시를 기반으로 한 번호를 조합하는 것으로 고려함
- 결제타입 1자리 + 카드번호길이 1자리  + 카드번호 16자리 + 요청일시 14자리(yyMMddHHmmssSS형태의 밀리세크까지 ) + 난수 3자리로 구성된 유니크한 숫자에 거의 가까운 숫자를 62개의 문자열(0~9, a~z, A~Z)로 매핑(62진법 변환)하여 20자리로 축약하는 형태로 채번
- 문제점 : 만약 카드결제나 취소 요청이 한꺼번에 다건이 들어온다면 난수3자리가 겹칠 가능성도 있기에, 겹치지 않으려면 이미 채번된 관리번호를 한번 조회하는 방법도 고려되어야 함, 하지만 번호가 많아질수록 성능 이슈가 있을 수 있음.
- Base62 출처 : https://stackoverrun.com/ko/q/4102291
- ManagementIdGenerator.java에 구현

####2. 암복호화
- 암복호화를 직접 구현하기에는 많은 시간과 노력이 소요 되는 것으로 판단
- 인터넷진흥원에서 개발한 SEED를 사용하기로 함
  SEED는 전자상거래, 금융, 무선통신 등에서 전송되는 개인정보와 같은 중요한 정보를 보호하기 위해 1999년 2월 한국인터넷진흥 원과 국내 암호전문가들이 순수 국내기술로 개발한 128비트 블록 암호 알고리즘
- 출처 : https://seed.kisa.or.kr/kisa/Board/17/detailView.do
- 사용이유 : 금융사 간 데이터를 주고 받는 과정에서 SEED를 이용해본 경험이 있고, 참고할 자료가 많기에 채택
- KISASeedEncryptor.java에 구현

### 테이블설계
####1. Payment
- 결제/취소 정보를 저장하는 테이블
- 결제와 취소, 관리번호만 제외하면 유사한 구조를 가지고 있기에 결제/취소 구분값을 두고 한 테이블(객체)에서 관리, 즉 결제/취소 테이블으로 따로 만들지 않음 
- PK는 DB에서 Long Type으로 자동으로 생성
- Payment.java에 구현
####2. CardInfo
- 결제/취소 데이터가 생성될 때마다 카드사와 인터페이스 하기위한 규약된 레이아웃의 데이터를 저장하는 테이블
- 결제/취소할때마다 유니크한 관리번호가 생성되므로 관리번호를 키로 하여 카드사와 인터페이스할 정보 저장
- CardInfo.java에 구현