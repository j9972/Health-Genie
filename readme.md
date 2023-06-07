# Health-Genie
❤️ 헬스 지니 - 설계부터 배포까지 실제 사용을 위한 큰 프로젝트 ( By Spring ) <br>
학교 헬스장에서 생기는 학생들에게 불편한 점들을 개선하며, 대학생들 사이에 1:1 PT 매칭을 해주는 프로젝트입니다. <br>



## ERD 설계
https://techj9972.tistory.com/205

## ERROR 모음집
https://techj9972.tistory.com/207
https://techj9972.tistory.com/208
https://techj9972.tistory.com/209


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
