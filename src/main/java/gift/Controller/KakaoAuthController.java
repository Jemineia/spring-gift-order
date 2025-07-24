package gift.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.KakaoTokenResponseDto;
import gift.jwt.JwtUtil;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class KakaoAuthController {
  
  @GetMapping("/")
  public ResponseEntity<String> kakaoCallback(@RequestParam ("code") String code) {

    System.out.println("인가코드 : "+ code);

    var url = "https://kauth.kakao.com/oauth/token";

    var headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
    var body = new LinkedMultiValueMap<String, String>();
    body.add("grant_type", "authorization_code");
    body.add("client_id", "@@@@@");
    body.add("redirect_uri", "http://localhost:8080");
    body.add("code", code);
    var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> response = restTemplate.exchange(request, String.class);

    HttpHeaders responseHeaders = new HttpHeaders();
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      KakaoTokenResponseDto tokenDto = objectMapper.readValue(response.getBody(), KakaoTokenResponseDto.class);
      responseHeaders.add("Access-Token", tokenDto.getAccess_token());
      responseHeaders.add("TokenType", tokenDto.getToken_type());
      responseHeaders.add("Refresh-Token", tokenDto.getRefresh_token());
      responseHeaders.add("Expires-In", String.valueOf(tokenDto.getExpires_in()));
      responseHeaders.add("scope",  tokenDto.getScope());
      responseHeaders.add("refresh_token_expires_in", String.valueOf(tokenDto.getRefresh_token_expires_in()));
    } catch (JsonProcessingException e) {
      System.out.println("JSON필드와 제대로 매칭되지 않습니다");
      throw new RuntimeException(e);
    }
    return new ResponseEntity<>("정상적으로 토큰이 발급되었습니다!", responseHeaders, HttpStatus.CREATED);
  }
}
