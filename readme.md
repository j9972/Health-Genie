# Health-Genie
❤️ 헬스 지니 - 설계부터 배포까지 실제 사용을 위한 큰 프로젝트 ( By Spring )


## ERD 설계
https://techj9972.tistory.com/

## End Point


## 어노테이션 및 여러 공부한거 정리
1. @CreationTimestamp
- 타임스탬프 값을 수동으로 설정할 필요 없이 애플리케이션에서 객체 생성 시간을 추적하는 데 유용합니다
- 다양한 데이터베이스 작업에서 타임스탬프 값의 일관성과 정확성을 보장하는 데 도움이 됩니다.

2. @NonNull vs @NotNull
- @NonNull : java 에서 주로 사용, null 값 전달시 컴파일러 에러
- @NotNull : Kotlin 에서 주로 사용, null 값 전달시 컴파일러 에러