# Spring Boot 도서관 관리 시스템 (Library Management System)

## 프로젝트 소개
본 프로젝트는 Spring Boot와 JPA를 기반으로 구현 중인 도서관 관리 시스템입니다.
단순한 데이터 CRUD를 넘어, **외부 도서 API 연동을 통한 실무적인 하이브리드 데이터 관리**와 **객체지향의 원칙을 지키는 아키텍처 설계**를 깊이 고민하며 개발하고 있습니다.

## 기술 스택
- **Backend:** Java, Spring Boot, Spring Data JPA
- **Database:** MySQL
- **Frontend:** Thymeleaf, HTML/CSS
- **External API:** 알라딘 Open API (도서 데이터 수집)

---

## 핵심 기능 및 기술적 고민 (Key Features)

### 1. 도서 관리 (Book Domain) - 하이브리드 입고 시스템
단순히 API 데이터를 화면에 뿌리는 것을 넘어, 실제 도서관의 재고 관리가 가능한 **하이브리드 아키텍처**를 구현했습니다.

* **알라딘 Open API 연동 (RestTemplate)**
  * 도서 검색 API를 호출하여 JSON 데이터를 수신하고, 이를 자체 시스템에 맞게 DTO로 안전하게 파싱하는 파이프라인을 구축했습니다.
  * 검색된 도서 중 관리자가 승인한 도서만 내부 DB(Entity)로 변환되어 입고 처리되도록 마스터 데이터와 로컬 데이터를 완벽히 분리했습니다.
* **객체지향적인 재고 관리 로직**
  * `totalQuantity`(총 재고)와 `availableQuantity`(대출 가능 수량)를 분리하여 현실적인 도서 상태 관리를 구현했습니다.
  * `decreaseStock()`, `increaseStock()` 등의 비즈니스 로직을 Service가 아닌 `Book` 엔티티 내부에 캡슐화하여, 무결성을 보장하고 객체지향적인 설계를 적용했습니다.

### 2. 커뮤니티 게시판 (Board Domain)
안정적이고 견고한 커뮤니티 서비스를 위해 로직의 역할 분리와 디테일한 사용자 경험을 개선했습니다.

* **어뷰징 방지: 쿠키(Cookie) 기반 조회수 중복 증가 제어**
  * `HttpServletRequest/Response`와 Cookie 수명을 활용하여, 악의적인 새로고침(F5)으로 인한 조회수 조작을 방어하도록 구현했습니다.
* **단일 책임 원칙(SRP) 기반 구조 리팩토링**
  * 권한 검증 및 데이터 조작(수정/삭제) 로직을 Service 계층으로 이관하고 예외(Exception)로 처리하여, Controller는 클라이언트의 요청 제어 및 뷰 매핑에만 집중하도록 역할을 분리했습니다.
* **디테일한 댓글 기능**
  * 댓글 작성 및 수정 기능을 구현하고, 내용이 변경된 댓글에 한해 '(수정됨)' 마크가 표시되도록 사용자 친화적인 UI/UX를 고려했습니다.

---

## 실행 방법 (Getting Started)

본 프로젝트는 보안을 위해 API 인증키와 데이터베이스 계정 정보가 포함된 설정 파일을 깃허브에 공유하지 않습니다. 프로젝트를 로컬에서 실행하려면 다음 설정이 필요합니다.

1. 레포지토리를 클론합니다.
2. MySQL 데이터베이스를 준비합니다.
3. `src/main/resources` 하위에 `application-secret.yml` 파일을 생성하고 아래 환경변수를 입력합니다.

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/library?serverTimezone=Asia/Seoul
    username: 본인의_DB_아이디
    password: 본인의_DB_비밀번호

# 알라딘 Open API 설정
aladin:
  api:
    key: "발급받은_TTBKey"
    search-url: "[http://www.aladin.co.kr/ttb/api/ItemSearch.aspx](http://www.aladin.co.kr/ttb/api/ItemSearch.aspx)"
    lookup-url: "[http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx](http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx)"
