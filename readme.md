# Health-Genie
❤️ 헬스 지니 - 설계부터 배포까지 실제 사용을 위한 큰 프로젝트 ( By Spring ) <br>
학교 헬스장에서 생기는 학생들에게 불편한 점들을 개선하며, 대학생들 사이에 1:1 PT 매칭을 해주는 프로젝트입니다. <br>



## ERD 설계
https://techj9972.tistory.com/205

## Package Explain
### Controller
define the path the end point of this api

### Dto
change data type for using in controller

### Email
duplicate and validate of email,  

### Entity
DB model 

### Exception
checking Exception

### Global
config, constants, exception, utils <br>

### Repository
installing service of program/package

### Service
business logic 


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

