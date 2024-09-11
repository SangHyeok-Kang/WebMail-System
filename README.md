# 웹 메일 시스템

<div align = "center">
  <img src = "https://github.com/SangHyeok-Kang/DataRepository/blob/e0054669340984348233bf33f851e10b64d2861d/%EC%9B%B9%20%EB%A9%94%EC%9D%BC%20%EC%8B%9C%EC%8A%A4%ED%85%9C/banner.png">
</div>

<!--
<br>
## 💡 주제

- 웹 메일 시스템
- 실습실 등록, 예약 기능 제공

<br>
-->

## 📝 설명

- **웹 메일 시스템**은 메일 쓰기 및 읽기 등과 같은 메일에 가장 기본이 되는 기능들로만 구현되어 있던 기존의 시스템을 **유지보수 방법(교정, 적응, 완전화, 예방)** 을 적용하여 시스템이 올바르게 작동하도록 하고 Spring Boot를 사용하여 전반적인 시스템을 리팩토링 하였습니다.


<br>


## 🖥️ 시스템 구조도

<div align = "center">
  <img src = "https://github.com/SangHyeok-Kang/DataRepository/blob/e0054669340984348233bf33f851e10b64d2861d/%EC%9B%B9%20%EB%A9%94%EC%9D%BC%20%EC%8B%9C%EC%8A%A4%ED%85%9C/%EC%8B%9C%EC%8A%A4%ED%85%9C%20%EC%95%84%ED%82%A4%ED%85%8D%EC%B3%90.png">
</div>


## 🖥️ 기술 스택
- **Language** : JAVA JDK 11, HTML5, JSP
- **Server** : Spring Framework 2.7.9, Apache Tomcat 9, Docker , Apache James, Nginx 1.25.0
- **Database** : MySQL 8.0.29
- **OS** : Ubuntu LTS 20.04, Windows 10
- **Tools** : Apache NetBeans IDE, Git, SonarCloud

<br>

## ✨ 주요 기능

- **메일 기능**
    - 사용자는 메일을 전송할 수 있으며, 메일 작성 중 뒤로가기 선택할 시 작성중인 메일이 **임시저장** 가능합니다.
    - 수신한 메일을 다른 사용자에게 **전달**할 수 있도록 기능을 추가하였습니다.
      
- **주소록 관리**
    - 사용자는 자신의 주소록을 관리할 수 있습니다.
    - 이름, 연락처 등 정보를 입력하여 등록을 할 수 있으며, 주소록을 선택하여 수정 및 삭제를 할 수 있습니다.
 
<br>

## 💫 팀 협업 방식

- **Github Issue & Pull-Request** 관리를 통해 팀원 간의 업무 파악 및 진행 사항을 기록하고, 일정 및 회의 내용을 기록하기 위해 **Notion**을 사용하였습니다.
- **Github CI/CD**를 통해 통합 및 배포 환경을 자동화하였으며, 코딩 컨벤션을 맞추는 등의 방식으로 더 나은 코드를 작성할 수 있도록 노력하였습니다.

<br>

## 🔥 트러블 슈팅 & 리팩토링
> 💡 **데이터베이스 사용**
- 기존의 시스템은 모든 데이터들이 파일 처리 시스템으로 저장되고 있어 데이터 관리와 속도 측에 있어서 비효율적이라고 생각하였습니다.
- 데이터베이스 서버를 구축하고 데이터를 관리함으로써 데이터 처리의 효율성과 속도 측에서 더욱 향상 시켰습니다.

<br>

> 💡 **회원가입 기능 추가**
- 기존의 시스템은 관리자가 추가한 사용자만 시스템을 사용할 수 있었습니다. 회원가입 기능을 추가함으로써 일반 사용자들도 자유롭게 사용할 수 있도록 수정하였습니다.

<br>

> 💡 **로그아웃 후 뒤로가기 방지**

- 로그아웃 후 Session은 없어지므로 url을 통한 이동은 막지만 뒤로가기 캐시를 이용해 컨트롤러 호출 없이 접근이 되어, 권한 없이 페이지의 내용들을 볼 수 있었고 사용자 추가/제거까지 가능하였습니다.
- 헤더파일에 페이지가 캐시되지 않도록 no-cache 설정을 하여 페이지들에 적용함으로써, 로그아웃 후 접근이 되지 않도록 수정하였습니다.

<br>

> 💡 **페이지 요청 시 세션 확인**

- 페이지의 URL 주소로 직접 접근 시, 로그인 하지 않아도 기능을 사용 가능한 문제가 발생하였습니다.
- Interceptor를 사용하여 URL로 들어오는 모든 요청을 진행하기 전에 먼저 세션의 존재 여부를 확인하고 실행하도록 수정하였습니다.

<br>

> 💡 **하드코드 된 중요 정보**

- 서버 정보나 계정 정보들이 프로그램 코드 내부에 하드코드로 되어있어, 내부 인증에 사용하거나 외부 컴포넌트와 통신을 하는 경우, 관리자 정보가 노출될 수 있어 위험하였습니다.
- **Jasypt** 라이브러리를 이용하여 프로퍼티 내에 작성된 설정 정보를 암호화하여 노출시키지 않도록 수정하였습니다.

<br>

## 📲 UI

<div align = "center">
  
| 메인 페이지 | 메일 전송 |
| :---: | :---: |
| <img width="350" alt="메인 페이지" src="https://github.com/SangHyeok-Kang/DataRepository/blob/e0054669340984348233bf33f851e10b64d2861d/%EC%9B%B9%20%EB%A9%94%EC%9D%BC%20%EC%8B%9C%EC%8A%A4%ED%85%9C/%EB%A9%94%EC%9D%B8%20%ED%8E%98%EC%9D%B4%EC%A7%80.png"> | <img width="350" alt="메일 전송" src="https://github.com/SangHyeok-Kang/DataRepository/blob/e0054669340984348233bf33f851e10b64d2861d/%EC%9B%B9%20%EB%A9%94%EC%9D%BC%20%EC%8B%9C%EC%8A%A4%ED%85%9C/%EB%A9%94%EC%9D%BC%20%EC%A0%84%EC%86%A1.png"> |

| 주소록 추가 | 예약 승인 |
| :---: | :---: |
| <img width="350" alt="주소록 추가" src="https://github.com/SangHyeok-Kang/DataRepository/blob/e0054669340984348233bf33f851e10b64d2861d/%EC%9B%B9%20%EB%A9%94%EC%9D%BC%20%EC%8B%9C%EC%8A%A4%ED%85%9C/%EC%A3%BC%EC%86%8C%EB%A1%9D%20%EC%B6%94%EA%B0%80.png"> | <img width="350" alt="주소록 수정" src="https://github.com/SangHyeok-Kang/DataRepository/blob/e0054669340984348233bf33f851e10b64d2861d/%EC%9B%B9%20%EB%A9%94%EC%9D%BC%20%EC%8B%9C%EC%8A%A4%ED%85%9C/%EC%A3%BC%EC%86%8C%EB%A1%9D%20%EC%88%98%EC%A0%95.png"> |

</div>

<br>

## 📅 일정 관리
<div align = "center">
  <img src = "https://github.com/SangHyeok-Kang/DataRepository/blob/e0054669340984348233bf33f851e10b64d2861d/%EC%9B%B9%20%EB%A9%94%EC%9D%BC%20%EC%8B%9C%EC%8A%A4%ED%85%9C/%EC%9D%BC%EC%A0%95.png" width="70%">
</div>

<br>

## 👨‍👦팀 구성
<div align="center">

|강상혁 ``` Fullstack Dev ```| 김준 ```Backend Dev``` | 이주혁 ```Backend Dev``` |
|:-:|:-:|:-:|
| <img src="https://avatars.githubusercontent.com/u/104892909?s=400&v=4" width=130> | <img src="https://avatars.githubusercontent.com/u/30451538?v=4" width=130> | <img src="https://avatars.githubusercontent.com/u/101574770?v=4" width=130> |
| [@SangHyeok-Kang](https://github.com/SangHyeok-Kang)|[@rlawns0327](https://github.com/rlawns0327)|[@cantstoptolaugh](https://github.com/cantstoptolaugh)|

</div>
