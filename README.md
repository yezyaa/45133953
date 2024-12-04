# 2024 동계 채용연계형 인턴십 코딩테스트

|분류|내용|
|---|---|
|주제|Spring Boot 기반의 게시판 및 사용자 인증 기능 개발|
|개발 기간|2024.11.30 ~ 2024.12.04|

<br/><br/>

## **🎯 프로젝트 목표**
Spring Boot를 활용하여 **사용자 인증**과 **게시판 기능**을 개발하고, 효율적인 데이터 관리 및 확장성을 고려한 프로젝트입니다.
- **기술 스택 활용:** Spring Boot와 JPA를 활용한 백엔드 구현
- **사용자 인증:** 회원가입, 로그인, 로그아웃
- **게시판 기능:** 게시글 작성, 수정, 삭제, 상세 조회, 목록 조회 및 검색, 첨부파일 관리
- **전역예외처리:** 애플리케이션 전반에서 발생하는 예외를 일관된 구조로 처리<br/><br/><br/>

## **📚 프로젝트 문서**
|분류|내용|
|---|---|
|WBS|[🔗Link](https://docs.google.com/spreadsheets/d/e/2PACX-1vQrv6SJgrWJoIr0ytnyXmzF3GF2RZwxueJVjzwHdWwofWeM06d7etkCBvRzkM3EcYt6ZM1ZCtQYIDeZ/pubhtml?gid=2095547522&single=true)|
|ERD|[🔗Link](https://www.erdcloud.com/d/uNDRShomiDLcnwbXg)|
|테이블 명세서|[🔗Link](https://yezyaa.notion.site/1511e6b4a0b98061a004fcb6a5f95acf?pvs=4)|
|API 명세서|[🔗Link](https://yezyaa.notion.site/API-1511e6b4a0b9806fbcb9c73bc425b243?pvs=4)|
|요구사항 정의서|[🔗Link](https://docs.google.com/spreadsheets/d/e/2PACX-1vQrv6SJgrWJoIr0ytnyXmzF3GF2RZwxueJVjzwHdWwofWeM06d7etkCBvRzkM3EcYt6ZM1ZCtQYIDeZ/pubhtml?gid=0&single=true)|

<br/>

### 플로우 차트
<img src="https://github.com/user-attachments/assets/e9a9a5ca-1ed5-4fcc-9667-ffa13a699efe" alt="sk-flow-chart" width="600">

<br/>

### ERD
<img src="https://github.com/user-attachments/assets/2e463d42-c1b7-4065-bbcd-ddacb227d31d" alt="sk-erd" width="600">
<br/><br/><br/>

## **🛠️ 개발 환경**
- **백엔드:** ![Java](https://img.shields.io/badge/Java17-%23ED8B00.svg?style=square&logo=openjdk&logoColor=white) <img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=square&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=square&logo=Spring Security&logoColor=white"> ![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=square&logo=Spring&logoColor=white) ![JWT](https://img.shields.io/badge/JWT-black?style=square&logo=JSON%20web%20tokens) ![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=square&logo=Gradle&logoColor=white)
- **프론트엔드:** ![HTML](https://img.shields.io/badge/HTML5-E34F26?style=square&logo=html5&logoColor=white) ![CSS](https://img.shields.io/badge/CSS3-1572B6?style=square&logo=css3&logoColor=white) ![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=square&logo=javascript&logoColor=black)
- **데이터베이스:** ![H2 Database](https://img.shields.io/badge/H2%20Database-4169E1.svg?style=square&logo=h2&logoColor=white)
- **버전 관리:** ![GitHub](https://img.shields.io/badge/Github-%23121011.svg?style=square&logo=github&logoColor=white)
- **기타:** ![GitHub](https://img.shields.io/badge/Github-%23121011.svg?style=square&logo=github&logoColor=white) ![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ%20IDEA-000000.svg?style=square&logo=intellij-idea&logoColor=white) ![Postman](https://img.shields.io/badge/Postman-FF6C37?style=square&logo=postman&logoColor=white)<br/><br/><br/>

## **🖥️ 구현 기능**
- [X] **회원가입**
  - **설명**: 사용자가 아이디(이메일), 이름, 비밀번호, 비밀번호 확인을 입력하여 계정을 생성한다.
  - **주요 기능**:
    - 이메일 중복 확인
    - 비밀번호와 비밀번호 확인 값 일치 검증
    - 필드 값 미입력 시 유효성 검사 예외 발생
    - 비밀번호 암호화 후 회원 데이터 저장

---

- [X] **로그인**
  - **설명**: 사용자가 아이디(이메일)와 비밀번호를 입력하여 인증을 하고, AccessToken을 발급 받아 로그인한다.
  - **주요 기능**:
    - 이메일과 비밀번호 확인
    - AccessToken 생성 및 발급
    - 발급된 토큰을 DB에 저장

---

- [X] **로그아웃**
  - **설명**: 로그인한 사용자가 발급받은 AccessToken을 무효화하여 로그아웃한다.
  - **주요 기능**:
    - 사용자의 AccessToken 삭제

---

- [X] **게시글 작성**
  - **설명**: 로그인한 사용자는 게시글을 작성한다.
  - **주요 기능**:
    - 비로그인 시 로그인 페이지로 이동
    - 제목과 내용을 입력받아 게시글 생성 후 게시글 목록으로 리다이렉트
    - 첨부파일이 있는 경우 최대 5개 업로드 가능

---

- [X] **게시글 수정**
  - **설명**: 본인이 작성한 게시글만 수정이 가능하다.
  - **주요 기능**:
    - 제목, 내용 수정
    - 첨부파일 추가/삭제

---

- [X] **게시글 삭제**
  - **설명**: 본인이 작성한 게시글만 삭제가 가능하다.
  - **주요 기능**:
    - 소프트 딜리트 방식으로 삭제 처리
    - 삭제된 게시글은 조회 불가

---

- [X] **게시글 상세 조회**
  - **설명**: 모든 사용자는 특정 게시글을 조회한다.
  - **주요 기능**:
    - 게시글을 조회할 때마다 조회수 1 증가

---

- [X] **게시글 목록 조회 및 검색**
  - **설명**: 모든 사용자는 게시글의 목록을 조회하고, 사용자의 아이디(이메일) 또는 제목으로 게시글을 검색한다.
  - **주요 동작**:
    - 검색 조건: 아이디(이메일) 또는 제목
    - 게시글 정렬: 최신순
    - 1페이지당 10개 목록 조회하는 페이지네이션 기능

---

- [X] **예외 처리**
  - **설명**: 전역 예외 처리를 구현하여 애플리케이션 전역 발생하는 예외를 중앙에서 관리한다.
  - **주요 동작**:
    - 비즈니스 로직의 커스텀 예외 검증
    - 이메일 중복, 비밀번호 미입력 등 유효성 검증
    - json 검증    
<br/><br/>

## **📃컨벤션**
### 커밋 메세지
```
<type>: <subject>
<BLANK LINE>
<body>

예시
feat: 회원가입 기능 구현

- 회원 데이터 저장 로직 추가
- 중복 이메일 검증 로직 구현
```
<br/>

### 커밋 키워드
|타입|내용|
|---|---|
|Feat|새로운 기능 추가|
|Fix|기능 수정, 버그 해결|
|!HOTFIX|급하게 치명적인 버그를 고쳐야 하는 경우|
|Setting|전역 설정 변경|
|ReadMe|리드미 파일 작업|
|Chore|패키지 매니저 수정, 그 외 기타(폴더구조 등) 수|
|Docs|문서 추가/수정|
|Style|코드 formatting, 세미콜론 누락, 코드 자체의 변경이 없는 경우|
|Refactor|코드 리팩토링|
|Test|테스트 코드, 리팩토링 테스트 코드 추가/수정|
|Comment|필요한 주석 추가 및 변경|
|Rename|파일 또는 폴더 명을 수정하거나 옮기는 작업만인 경우|
|Remove|폴더/파일을 삭제하는 작업만 수행한 경우|
|Design|UI 변경|
<br/>

### **브랜치 전략(Github Flow)**
![sk-github-flow](https://github.com/user-attachments/assets/76c1b389-b237-4ade-8c2e-b3387db057c3)<br/><br/>

### **패키지 구조**
- domain: 핵심 비즈니스 로직 계층
  - api: 외부와 데이터를 주고받는 계층 (Controller 및 Dto)
  - application: 비즈니스 로직 처리 및 서비스 계층
  - repository: 데이터베이스 접근 계층
  - exception: 커스텀 예외 처리 계층
- global: 공통 구성 요소 계층
  - config: 전역 설정
  - dto: 공통적으로 사용되는 DTO 클래스
  - exception: 전역 예외 처리
  - security: 인증 및 보안 관련 클래스
<br/><br/><br/>
