package gift.authentication;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.Controller.KakaoAuthController;
import gift.model.Member;
import jakarta.servlet.ServletException;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoAuthenticationExtractor implements AuthenticationExtractor {

  private static final Logger logger = LoggerFactory.getLogger(KakaoAuthController.class);

  @Override
  public boolean supports(String header) {
    return header != null && header.startsWith("Kakao ");
  }

  @Override
  public Member extract(String header) throws ServletException {
    String kakaoAccessToken = header.substring(6); // "Kakao " 제거

    String url = "https://kapi.kakao.com/v1/user/access_token_info";
    var headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + kakaoAccessToken);
    var request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));

    RestTemplate restTemplate = new RestTemplate();
    try {
      ResponseEntity<String> response = restTemplate.exchange(request, String.class);

      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode json = objectMapper.readTree(response.getBody());

      if (json.has("id")) {
        logger.info("✅ 유효한 카카오 AccessToken입니다. 사용자 ID: {}", json.get("id").asText());
        // 현재는 카카오 ID 기반 회원 정보를 생성/조회하지 않으므로 null 반환
        return null;
      } else if (json.has("code") && json.get("code").asInt() == -401) {
        logger.warn("❌ 유효하지 않은 AccessToken: {}", json.get("msg").asText());
        return null;
      } else {
        logger.warn("❓ 예기치 않은 응답 형식: {}", response.getBody());
        return null;
      }

    } catch (Exception e) {
      logger.error("🔥 카카오 AccessToken 검증 중 오류 발생", e);
      return null;
    }
  }

}
