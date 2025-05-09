# 👁️ BeYourEyes_2_Backend

AI 기반 건강 관리 앱 **BeYourEyes**의 백엔드 서버입니다.  
익명 로그인 기반 사용자 인증, 건강 정보 관리, JWT 보안, PostgreSQL 데이터 관리,  
Firebase 이미지 저장, CI/CD 배포까지 포함한 백엔드 프로젝트입니다.

---

## 🧾 작업 기록 히스토리 (Changelog)

<details>
  <summary>작업 기록 히스토리 보기</summary>

### 📅 2월 3일
- DB 모델링: `user`, `userinfo`, `allergy`, `disease`, `dailyfood` 테이블 설계

### 📅 2월 11일
- 익명 로그인 (device_id 기반) 및 회원가입 구현
- 공통 응답 DTO(ResponseDto) 구조 구현

### 📅 2월 18일
- `userinfo`, `allergy`, `disease` 모델 추가
- 개인정보 저장 및 조회 API 구현
- 알러지, 질환 정보 저장 및 조회 API 구현

### 📅 2월 23일
- JWT 토큰 설정
- 전체 개인정보 통합 조회 API 구현
- 정보 수정 API 구현
- 1년 이상 로그인하지 않은 사용자 soft delete 처리
- Swagger 세팅

### 📅 2월 25일
- 로그인 Swagger 문서 작성

### 📅 3월 5일
- Firebase 연결
- 오늘 섭취 기록 저장 및 조회 기능 구현
- 매일 자정마다 섭취 기록 자동 삭제
- 익명 로그인 방식을 access token + refresh token 구조로 변경
- AWS EC2 + RDS 임시 배포

### 📅 3월 6일
- 개인정보/질환/알러지 수정 기능 분리
- 닉네임 중복 검사 API 추가
- 회원 탈퇴 기능 추가
- 안드로이드 1차 연동 및 API 수정

### 📅 3월 10일
- 안드로이드 연동 중 발생한 API 오류 수정

### 📅 3월 21일
- GitHub Actions + Docker 기반 CI/CD 구축 시작
- API 일부 구조 수정

### 📅 3월 22일
- 안드로이드 2차 연동 및 API 수정
- CI/CD 배포 자동화 기능 개선

### 📅 3월 23일
- 테이블 구조 수정
- API 응답 구조 개선
- MySQL → PostgreSQL 마이그레이션 완료

### 📅 3월 25일
- GitHub Actions 자동 빌드 및 테스트 성공
- API 전반 수정
- 모든 API 안드로이드 연동 완료

### 📅 4월 13일
- DB 테이블 관계 리팩토링 (user_id → user_info_id 중심 구조)
- 전체 Entity / Mapper / Service / Controller 리팩토링
- PostgreSQL COALESCE 오류 해결 (`CAST` 활용)

</details>

---

## 🏗️ 개발 환경

| 항목 | 내용 |
|------|------|
| Language | Kotlin |
| Framework | Spring Boot |
| Database | PostgreSQL |
| ORM/Mapper | MyBatis |
| Auth | JWT (Access Token, Refresh Token) |
| Storage | Firebase Storage |
| 문서화 | Swagger |

---

## 📦 배포 환경
- 온프레미스 서버 (자체 미니 PC)에 Docker 기반 배포
- GitHub Actions → Docker Hub → SSH 원격 배포
  

## 🧑‍💻 개발자

<table>
  <tr>
    <td align="center">
      <strong>이수진</strong><br>
<a href="https://github.com/Soojin-Lee-01">@Soojin-Lee-01</a>
    </td>
  </tr>
</table>

---

## 📂 주요 기능

- 익명 로그인 (device_id 기반)
- JWT 기반 인증/인가
- 사용자 정보 등록, 수정, 조회
- 알러지, 질환 정보 등록/조회/수정
- 오늘 섭취 음식 기록 + 영양소 요약 조회
- Firebase Storage 이미지 업로드
- 매일 자정 데이터 초기화 Scheduler
- Swagger 기반 API 문서
- 안드로이드 연동
- GitHub Actions + Docker 기반 자동 배포

---

## 🗂️ ERD

![BeYourEyes ERD](https://github.com/user-attachments/assets/79fe5560-ba6f-4c7b-8cd4-a5653563079b)

---

## 📝 API 명세서

👉 [노션 API 문서 보러가기](https://www.notion.so/your-api-doc-link)

---

