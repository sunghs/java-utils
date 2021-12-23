# java-utils 자바 유틸 모음
자주 쓰이는 내용들인데, docs 보고 구현하기엔 시간 소모가 많고, 
구글링 하기는 애매한 기능들을 개발해서 넣은 프로젝트입니다.

각종 테스트 코드를 거쳐 pass 한 소스만 commit 하고 있습니다.

단일 클래스로 바로 사용이 가능하도록 구현하고 있습니다. (클래스 간의 의존성이 없습니다)

최대한 dependency는 줄이고, Pure Java 형태로 구현할 수 있도록 하였습니다.


[블로그](https://sunghs.tistory.com/category/Copy%26Paste)의 바로 사용 가능한 복사&붙여넣기 소스 입니다.

### 개발환경 (Dev Environment)
#### 2021-12-23 updated
- IDE : IntelliJ 2021
- JDK : Java11 (Amazon-corretto)
- Gradle : 7.3

입니다.


---
### 2021-12-23
불필요한 SpringBoot 구조를 없애고 Gradle을 빌드툴로 사용하는 Java 프로젝트로 재편한 뒤, 필요한 dependency만 따로 추가
- slf4j 구현체 logback 추가
- spring boot 플러그인 및 dependency 제거 및 spring-core 추가
- Gradle 빌드 인코딩 지정 (UTF-8)
- Gradle 캐싱 전략 지정 (캐싱하지 않음)
- 테스트코드 리팩토링 한 뒤 테스트하는 운영체제에 따라 종속되게 변경