# Getting Started

### Reference Documentation

## 접속정보 암호화
- JayptTest를 통해 암호화한 값을 설정파일에 추가 
- application-datasource.yml 파일에 접속정보 추가
- 구동 시 VM 설정
```
-Dspring.profiles.active=local -Djasypt.encryptor.password="암호화비밀번호"
```
