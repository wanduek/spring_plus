# 프로젝트 요약

## 1. API 개발 및 에러 처리

- **@Transactional 개선**  
  `Connection is read-only`와 같은 에러를 해결하기 위해 트랜잭션 관리를 올바르게 구현하여 데이터 수정이 정상적으로 이루어지도록 합니다.

## 2. JWT 활용

- **User 엔티티에 닉네임 추가**  
  User 테이블에 `nickname` 컬럼을 추가하고, JWT에서 닉네임을 추출해 프론트엔드에서 표시할 수 있도록 합니다.

## 3. AOP 코드 개선

- **AOP 개선**  
  `UserAdminController`의 `changeUserRole()` 메소드가 실행되기 전에 AOP 로깅이 올바르게 동작하도록 수정합니다.

## 4. 컨트롤러 테스트 코드 수정

- **테스트 코드 수정**  
  할 일 단건 조회 시 예외가 발생하는 테스트를 수정하여 정상적으로 통과할 수 있도록 합니다.

## 5. JPA 검색 기능 추가

- **JPQL로 조건부 검색 구현**  
  `weather`와 수정일 기간 조건으로 할 일을 검색할 수 있는 기능을 추가합니다.

## 6. JPA Cascade 기능 활용

- **할 일 저장 시 담당자 자동 등록**  
  할 일을 생성한 유저를 자동으로 담당자로 등록하도록 JPA의 `cascade` 기능을 활용합니다.

## 7. N+1 문제 해결

- **N+1 문제 해결**  
  `CommentController`의 `getComments()` API 호출 시 발생하는 N+1 문제를 해결하여 성능을 최적화합니다.

## 8. QueryDSL 사용

- **QueryDSL로 검색 최적화**  
  JPQL로 작성된 쿼리를 QueryDSL로 변경하고, 성능 문제를 해결합니다.

## 9. Spring Security 적용

- **권한 및 인증 시스템**  
  기존 필터 및 `Argument Resolver`를 Spring Security로 변경하고, JWT를 사용한 토큰 기반 인증 방식을 유지하면서 권한 관리 기능을 구현합니다.

## 10. QueryDSL을 사용한 일정 검색 기능 구현

- **일정 검색 API 구현**  
  제목, 생성일, 담당자 닉네임으로 검색할 수 있는 일정 검색 기능을 QueryDSL을 통해 최적화하여 구현합니다.

## 11. Transaction 독립성 관리

- **매니저 등록 시 로그 기록**  
  매니저 등록과 별개로 로그가 항상 기록되도록 `@Transactional`의 옵션을 활용해 독립적인 트랜잭션을 관리합니다.

## 12. AWS 서비스 활용

### 12-1. EC2

- EC2 인스턴스에서 애플리케이션을 실행하고, 탄력적 IP를 설정하여 외부에서 접속할 수 있도록 설정합니다.
- 누구나 접속 가능한 health check API를 구현하여 `README.md`에 경로를 기재합니다.

### 12-2. RDS

- RDS에 데이터베이스를 구축하고 EC2에서 실행 중인 애플리케이션과 연결합니다.

### 12-3. S3

- S3 버킷을 생성하고, 유저 프로필 이미지를 업로드 및 관리하는 API를 구현합니다.

# SPRING PLUS AWS EC2, RDS, S3 설정 

## EC2
- 탄력적 ip 사용
  
  <img width="996" alt="스크린샷 2024-10-09 오후 8 40 46" src="https://github.com/user-attachments/assets/d52cd642-2ef5-4449-ad51-62659d62ab21">

- health check api

![스크린샷 2024-10-08 오후 4 44 36](https://github.com/user-attachments/assets/6365fd4b-e8fe-4162-aa1b-250037a76a09)

- 배포
  
![스크린샷 2024-10-10 오전 10 57 48](https://github.com/user-attachments/assets/7ce2ac1b-dd40-4f1d-bb98-8a2b2fa5bfec)
  
## RDS
- RDS 생성

  ![스크린샷 2024-10-08 오후 8 52 39](https://github.com/user-attachments/assets/23fcdedf-79db-481d-ada5-d1e17ebe8fe3)

- RDS EC2에 연결
  
<img width="910" alt="스크린샷 2024-10-09 오후 6 38 31" src="https://github.com/user-attachments/assets/86fe3a45-4ac5-4b82-b843-2a484798576d">

## S3 버킷 생성

<img width="879" alt="스크린샷 2024-10-09 오후 8 44 26" src="https://github.com/user-attachments/assets/06152430-069d-496d-8d64-66ed477ee1be">

# 트러블 슈팅

# 문제 해결: YAML 설정 문제

## 문제
![스크린샷 2024-10-10 오후 10 00 06](https://github.com/user-attachments/assets/4b0459b8-87d2-4960-b952-35bf40196325)

이 오류는 애플리케이션이 구성에서 `cloud.aws.s3.bucket` 속성을 찾을 수 없음을 나타냅니다.

## 해결 단계

### 1. YAML 구조 확인
`application.yml` 파일이 올바르게 구조화되어 있는지 확인하세요. 아래는 예상되는 형식입니다:

```yaml
spring:
  cloud:
    aws:
      credentials:
        access-key: access key # AWS IAM Access Key
        secret-key: secret key # AWS IAM Secret Key
      region:
        static: ap-southeast-2 # 예: ap-northeast-2
      s3:
        bucket: spring-plus-bucket # S3 버킷 이름

```

자세한건해당 
https://minjooig.tistory.com/143


