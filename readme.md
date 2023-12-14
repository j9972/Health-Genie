# Health-Genie
❤️ 헬스 지니 - 설계부터 배포까지 실제 사용을 위한 큰 프로젝트 ( By Spring ) <br>
학교 헬스장에서 생기는 학생들에게 불편한 점들을 개선하며, 대학생들 사이에 1:1 PT 매칭을 해주는 프로젝트입니다. <br>



## ERD 설계
https://techj9972.tistory.com/205
![ERD](images/health_genie_erd.png)

## USER FLOW
![USER_FLOW](images/health_genie_user_flow.png)

## TEST CODE
- 테스트 코드는 단순히 애플리케이션의 기능이 제대로 동작하는 것만 검증한다고 생각하지 않고, 제 3자가 봤을 때 해당 프로젝트에 어떤 기능들이 있으며 그에 따른 응답을 확인할 수 있는 수단이라고 생각합니다.

### UNIT TEST
- 단위 테스트는 비즈니스 로직의 가장 작은 단위인 각 메서드를 기반으로 테스트를 진행했습니다.
- 기존의 단위 테스트는 스프링을 사용하지 않고 빠른 속도와 독립적인 실행을 보장하는 것이 일반적이지만, 작업 일정과 개발 진행 상황에 따라 통합 테스트 작성을 하지 않고 단위 테스트에서 `@SpringBootTest` 애노테이션을 사용 함으로써 통합/단위 테스트를 진행했습니다.
- 각 메서드의 정상적인 기능 동작과 실행 속도 또한 통합 테스트에 비해 빠르며, 함께 필요한 의존성의 올바른 관계를 확인할 수 있습니다.
- 또한 각각의 테스트 클래스마다 중복되는 작업들은 테스트 유틸 클래스를 만들고 메서드 네이밍을 명확히 하여 사용 및 유지 보수하고 있습니다.
![TEST_CODE](images/test_code_ex.png)

### INTEGRATION TEST
- 통합 테스트는 아직 진행하지 않았습니다.


## ERROR 모음집
https://techj9972.tistory.com/207 <br>
https://techj9972.tistory.com/208 <br>
https://techj9972.tistory.com/209 <br>


## 기술 스택 


## 디자인


## End Point
### 커뮤니티에 게시글 작성
Post /community/post/add

ex) http://localhost:1234/community/post/add

### 커뮤니티에 게시글 조회
Get /community/post/get

ex) http://localhost:1234/community/post/get

### 프로필 작성
Post /profile/add

ex) http://localhost:1234/profile/add

### 프로필 수정
Post /profile/modify

ex) http://localhost:1234/profile/modify

### pt 후기 작성
Post /User/Pt/review

ex) http://localhost:1234/User/Pt/review


## 배운점
1. 스프링 시큐리티에서는 기본적으로 CSRF 기능이 활성화 되어 있다.<br>
-> CSRF Token 정보를 Header 정보에 포함하여 요청을 하게 되는데 테스트 할 때도 CSRF 토큰값을 같이 넘겨줘야 한다.
