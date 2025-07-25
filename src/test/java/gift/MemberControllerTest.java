package gift;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("사용자 기능 Test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)  // context 오염 방지

public class MemberControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @Order(1)
  @DisplayName("[1] 정상 회원 가입 Test")
  void validTestRegister() throws Exception {
    // given
    String email = "abc123@gmail.com";
    String password = "qwer1234!@";

    mockMvc.perform(post("/api/members/register")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .queryParam("email", email)
            .queryParam("password", password)
        )
        .andExpect(status().isCreated());
  }

  @Test
  @DisplayName("[2] 이미 존재하는 email 회원 가입 Test")
  @Order(2)
  void inValidRegister() throws Exception {
    String email = "duplicate@example.com";
    String password = "test1234!";

    // 첫번째 회원가입
    mockMvc.perform(post("/api/members/register")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .queryParam("email", email)
        .queryParam("password", password)
    );

    // 증복 회원가입
    mockMvc.perform(post("/api/members/register")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .queryParam("email", email)
            .queryParam("password", password))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("[3] 정상 로그인 Test")
  @Order(3)
  void ValidTestLogin() throws Exception {
    // given
    String email = "abc123@gmail.com";
    String password = "qwer1234!@";

    // 첫번째 회원가입
    mockMvc.perform(post("/api/members/register")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .queryParam("email", email)
        .queryParam("password", password)
    );

    // 로그인
    MvcResult result = mockMvc.perform(post("/api/members/login")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .queryParam("email", email)
            .queryParam("password", password))
        .andExpect(status().isFound())
        .andReturn();

    // 카카오 연동 인가코드 발급 - 실제 작동 아닌 MVC테스트 이므로 token발급은 나중에 테스트
    String redirectUrl = result.getResponse().getRedirectedUrl();
    assertTrue(redirectUrl.startsWith("https://kauth.kakao.com/oauth/authorize"));
    assertTrue(redirectUrl.contains("client_id="));
    assertTrue(redirectUrl.contains("redirect_uri=http://localhost:8080"));
    assertTrue(redirectUrl.contains("scope=talk_message"));

  }

  @Test
  @DisplayName("[4] 비정상 로그인-아이디가 틀린경우 Test")
  @Order(4)
  void inValidTestLogin() throws Exception {
    // given
    String email = "abc123@gmail.com";
    String password = "qwer1234!@";

    // 회원가입
    mockMvc.perform(post("/api/members/register")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .queryParam("email", email)
        .queryParam("password", password));

    password = "asdf1234!@";
    // 로그인
    mockMvc.perform(post("/api/members/login")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .queryParam("email", email)
            .queryParam("password", password))
        .andExpect(status().isUnauthorized());
  }
}
