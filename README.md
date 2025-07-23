# spring-gift-enhancement

### Step1 [ 07/15 ]
- [x] JdbcTemplate -> JPA로 Refactoring
  - [x] Member 관련 기능 JPA화
  - [x] Product 관련 기능 JPA화
  - [x] Wishlist 관련 기능 JPA화
- [x] DataJpaTest를 통한 TestCode 작성

### Step2 [ 07/16 ]
- [x] 상품 목록 Pagination 적용
- [x] 위시리스트 Pagination 적용
- [x] Step1 코드 리뷰 수정
  - [x] 회원가입시, Member객체 생성시 id없는 생성자 만들기
  - [x] IllegalArgumentException -> EntityNotFoundException 으로 변경
  - [x] JpaTest AssertAll() 적용
  - [x] 사용하지 않는 반환값 void로 변경
  - [x] ProductService setter 대신 다른 method 사용
  - [x] Jpa Test에서 DataIntegrityViolationException 발생 테스트

### Step3 [ 07/18 ]
<details>
<summary>이전 Step에서 완료못한 코드 처리</summary>
<div markdown="1">

- [x] Google Style 적용 및 미사용 import문 최적화
- [x] @PageableDefault 공통 적용
- [x] Update Method에 EntityNotFoundException 적용
- [x] TestCode 중복 체크 제거
- [x] 도메인 객체 테스트 코드 작성
- [x] Application 실행 안해도 테스트 수행 가능하도록 수정
  - [x] MemberControllerTest를 MockMvc Test로 수정
  - [x] ProductControllerTest를 MockMvc Test로 수정
  - [x] WishlistControllerTest를 MockMvc Test로 수정
</div>
</details>

### step3 [ 07/21 ]
- [x] 모델과 DB에 옵션 추가
- [x] 옵션에 대한 제약사항 추가
  - [x] 공백포함 최대 길이는 50자
  - [x] 일부 특수문자만 사용가능
  - [x] 옵션 수량은 최소 1, 최대 1억
  - [x] 동일 상품 내 중복 옵션 불가
- [x] 옵션 수량 감소 기능 추가

### Step3 Refactoring [ 07/22 ]
- [x] 필드끼리 위치하도록 model 수정
- [x] model 기본생성자 protected로 변경
- [x] 상품-옵션 함께 생성되도록 ProductService 변경
- [x] 중복 옵션 체크 메서드 private로 변경
- [x] JPA를 활용해 객체간 관계를 사용해 save하도록 구조 변경
- [x] 재고 차감 @Query 대신 다른 방법으로 변경
- [x] 옵션명 option->name으로 변경
- [x] 옵션 관련 Domain Test 추가 및 기존 ProductControllerTest 코드 수정
  - [x] 일부 상황에서 Product삽입 테스트 실패하는 경우를 해결