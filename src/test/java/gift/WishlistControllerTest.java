package gift;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gift.Controller.WishlistController;
import gift.jwt.JwtUtil;
import gift.jwt.TestJwtUtil;
import gift.repository.WishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)  // context 오염 방지

class WishlistControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JwtUtil jwtUtil;

  private String jwtToken;
  @Autowired
  private WishlistRepository wishlistRepository;

  @BeforeEach
  void setUp() {
    // 테스트 토큰 발급
    jwtToken = "Bearer " + jwtUtil.createToken("test@gmail.com");
    wishlistRepository.deleteAll();
  }

  @Test
  @Order(1)
  @DisplayName("[1] 찜하기 기능 정상 동작 테스트")
  void testAddToWishlist() throws Exception {

    mockMvc.perform(post("/api/products/1/wishlist")
            .header(HttpHeaders.AUTHORIZATION, jwtToken))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/api/products"));
  }

  @Test
  @Order(2)
  @DisplayName("[2] 중복 찜시 409 Conflict 반환")
  void testWishlistDuplicateAdd() throws Exception {
    mockMvc.perform(post("/api/products/1/wishlist")
            .header(HttpHeaders.AUTHORIZATION, jwtToken))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/api/products"));

    // 1번 상품 중복찜
    mockMvc.perform(post("/api/products/1/wishlist")
            .header(HttpHeaders.AUTHORIZATION, jwtToken))
        .andExpect(status().isConflict()); // 409 Conflict
  }


  @Test
  @Order(3)
  @DisplayName("[3] 찜 수량을 양수로 수정하면 성공")
  void testUpdateQuantitySuccess() throws Exception {
    int newQuantity = 72;

    mockMvc.perform(post("/api/products/1/wishlist")
            .header(HttpHeaders.AUTHORIZATION, jwtToken))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/api/products"));

    // 수량 변경 (상품 ID: 1, 수량: 72)
    mockMvc.perform(put("/api/wishlist/1/quantity")
            .param("quantity", String.valueOf(newQuantity))
            .header(HttpHeaders.AUTHORIZATION, jwtToken))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/api/wishlist"));
  }

  @Test
  @Order(4)
  @DisplayName("[4] 찜 수량을 0 이하로 수정하면 실패")
  void testUpdateQuantityFailure() throws Exception {
    int newQuantity = -72;

    mockMvc.perform(post("/api/products/1/wishlist")
            .header(HttpHeaders.AUTHORIZATION, jwtToken))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/api/products"));

    // 수량 변경 (상품 ID: 1, 수량: -72)
    mockMvc.perform(put("/api/wishlist/1/quantity")
            .param("quantity", String.valueOf(newQuantity))
            .header(HttpHeaders.AUTHORIZATION, jwtToken))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Order(5)
  @DisplayName("[5] 찜 상품 삭제 성공 시 리다이렉트")
  void testDeleteWishlistItem_Success() throws Exception {
    mockMvc.perform(post("/api/products/1/wishlist")
            .header(HttpHeaders.AUTHORIZATION, jwtToken))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/api/products"));

    // 찜 상품 삭제
    mockMvc.perform(delete("/api/wishlist/1/delete")
            .header(HttpHeaders.AUTHORIZATION, jwtToken))
        .andExpect(status().isFound()) // 삭제 성공시 wishlist 목록으로 돌아감
        .andExpect(redirectedUrl("/api/wishlist"));
  }

  @Test
  @Order(6)
  @DisplayName("[6] 존재하지 않는 찜 상품 삭제 시 404 Not Found")
  void testDeleteWishlistItem_NotFound() throws Exception {
    mockMvc.perform(post("/api/products/1/wishlist")
            .header(HttpHeaders.AUTHORIZATION, jwtToken))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/api/products"));

    // 존재하지 않는 찜 상품 삭제
    mockMvc.perform(delete("/api/wishlist/98765/delete")
            .header(HttpHeaders.AUTHORIZATION, jwtToken))
        .andExpect(status().isNotFound()); // 찜목록에 존재하지 않는 경우 NotFoundDeleteWishlistException
  }

}
