# Arti-Server Repository

### url
http://lucy-arti.kro.kr:8080/

## spec
- Java 17
- spring-boot 3.1.3
- use `Lombok`
- use `JPA` ORM
- use `mysql` database
- use `h2` database
- use `validation`
- use `Spring Web`
- use `Spring security`
- use `oauth2` for kakao login
- infra : EC2, RDS, S3

### Code Conventions
Reformat Code 사용?
- 패키지 전략
    - 도메인 중심
    - ex. vote(투표 도메인) 내부에 controller, service, repository 등을 붙인다.
- 변수 네이밍
    - 패키지 - 소문자
    - 클래스 - 파스칼 케이스 (UpperCarmelCase를 사용. 이는 대문자로 시작하고 단어가 바뀔 때마다 다시 대문자로 표시. e.g. HelloWorld.java)
    - 변수명, 메서드명 - 카멜 케이스 (owerCarmelCase를 사용. 이는 소문자로 시작하고 단어가 바뀔 때마다 다시 대문자로 표시. e.g. printHelloWorld()
    - 상수, enum - 모두 대문자 (CONTANT_CASE 방식을 사용. 이는 모두 대문자를 사용하며 단어 사이에 밑줄을 표시. 당연히 명사나 명사구여야 한다)
    - 컨트롤러, 서비스, 엔티티 - ~Controller, ~Service, ~Entity (ex, MemberController, …)
- 어노테이션
    - 한 줄에 하나의 어노테이션만 작성한다.
- 접근제어자
    - Entity의 모든 필드 : private
    - Entity 기본생성자 : private
    - @RequestBody : public

### Commit Convetions
- `feat #issue`: 새로운 기능 추가
- `fix #issue`: 버그 수정
- `docs #issue`: 문서 수정
- `style #issue`: 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우
- `refactor #issue`: 코드 리펙토링
- `test #issue`: 테스트 코드, 리펙토링 테스트 코드 추가
- `chore #issue`: 빌드 업무 수정, 패키지 매니저 수정
- ex) feat #1: 1번 이슈에 올려놓은 로그인 기능 구현
    - : 뒤의 메시지는 알아보기 쉽게만 작성, 따로 컨벤션을 두진 않음

## Git Flow
- `main` : 제품으로 출시될 수 있는 브랜치
- `develop` : 다음 출시 버전을 개발하는 브랜치
- `feature` : 기능을 개발하는 브랜치
- `hotfix` : 출시 버전에서 발생한 버그를 수정 하는 브랜치

1. `main` → `develop` 분기
    - 최신 배포 직후에는 `main`과 `develop` 변경 사항이 동일함
2. `develop` → `feature/{기능 이름}` 분기
3. 작업 후 `feature` → `develop` PR
    1. 충돌 해결 및 테스트 코드 pass 확인 (CI)
4. 코드 리뷰 진행
    1. 최소 1번
5. `feature` → `develop` Merge
    1. Squash and Merge
    2. merge 후 `featrue` 브랜치 자동 삭제
6. 배포 시점에 `develop` → `main`  PR 및 Merge
    1. Merge commit or Rebase and Merge
    2. CI/CD 작동
7. 애플리케이션 장애가 발생하면 `main` → `hotfix/{문제상황}` 브랜치로 분기
    1. 버그를 고치고 `main`으로 merge


