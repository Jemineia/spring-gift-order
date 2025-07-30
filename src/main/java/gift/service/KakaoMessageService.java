package gift.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import gift.model.Order;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

@Service
public class KakaoMessageService {

  private final RestTemplate restTemplate = new RestTemplate();
  private final ObjectMapper objectMapper = new ObjectMapper();

  public void sendCommerceMessage(String accessToken, Order order) {
    String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

    // "Kakao " prefix 제거
    accessToken = accessToken.substring(6);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    headers.setBearerAuth(accessToken);

    Map<String, Object> template = new LinkedHashMap<>();
    template.put("object_type", "commerce");

    Map<String, Object> content = new HashMap<>();
    content.put("title", order.getProductOption().getProduct().getName());
    content.put("image_url", order.getProductOption().getProduct().getImageUrl());
    content.put("image_width", 640);
    content.put("image_height", 640);
    content.put("link", Map.of(
        "web_url", "http://localhost:8080/api/products/" + order.getProductOption().getProduct().getId(),
        "mobile_web_url", "http://localhost:8080/api/products/" + order.getProductOption().getProduct().getId()
    ));
    template.put("content", content);

    Map<String, Object> commerce = new HashMap<>();
    commerce.put("regular_price", order.getProductOption().getProduct().getPrice());
    template.put("commerce", commerce);

    String templateJson;
    try {
      templateJson = objectMapper.writeValueAsString(template);
    } catch (Exception e) {
      throw new RuntimeException("카카오 메시지 JSON 직렬화 실패", e);
    }

    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("template_object", templateJson);

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

    ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

    if (!response.getStatusCode().is2xxSuccessful()) {
      throw new RuntimeException("카카오톡 메시지 전송 실패: " + response.getBody());
    }
  }
}
