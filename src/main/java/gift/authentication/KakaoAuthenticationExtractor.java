package gift.authentication;

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

    String kakaoAccessToken = header.substring(6);

    String url = "https://kapi.kakao.com/v1/user/access_token_info";
    var headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + kakaoAccessToken);
    var request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> response = restTemplate.exchange(request, String.class);

    return null;
  }
}
