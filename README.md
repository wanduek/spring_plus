# SPRING PLUS AWS EC2, RDS, S3 설정 캡쳐본

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


