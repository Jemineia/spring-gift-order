package gift.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.orderRequestDto;
import gift.dto.orderResponseDto;
import gift.model.Member;
import gift.model.Order;
import gift.model.ProductOption;
import gift.repository.MemberRepository;
import gift.repository.OrderRepository;
import gift.repository.ProductOptionRepository;
import gift.repository.WishlistRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {

  private final MemberRepository memberRepository;
  private final ProductOptionRepository productOptionRepository;
  private final OrderRepository orderRepository;
  private final WishlistRepository wishRepository;

  private final ProductOptionService productOptionService;

  private final RestTemplate restTemplate = new RestTemplate();

  public OrderService(MemberRepository memberRepository,
      ProductOptionRepository productOptionRepository,
      OrderRepository orderRepository,
      WishlistRepository wishRepository,
      ProductOptionService productOptionService) {
    this.memberRepository = memberRepository;
    this.productOptionRepository = productOptionRepository;
    this.orderRepository = orderRepository;
    this.wishRepository = wishRepository;
    this.productOptionService = productOptionService;
  }

  @Transactional
  public orderResponseDto order(String email, orderRequestDto request, String kakaoAccessToken) {
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다"));
    ProductOption option = productOptionRepository.findById(request.getOptionId())
        .orElseThrow(() -> new EntityNotFoundException("옵션이 존재하지 않습니다"));

    // 수량 차감
    productOptionService.decreaseQuantity(option.getId(), request.getQuantity());

    // Request 기반으로 Order 생성
    Order order = new Order(member, option, request.getQuantity(), request.getMessage(),
        LocalDateTime.now());
    orderRepository.save(order);

    wishRepository.deleteByMemberAndProduct(member, option.getProduct());

    // 카카오 AccessToken이 있는경우, 메세지를 전송
    if(kakaoAccessToken != null && !kakaoAccessToken.isBlank()) {
      sendCommerceMessage(kakaoAccessToken, order);
    }

    return new orderResponseDto(order.getId(), option.getId(), order.getQuantity(),
        order.getOrderDateTime(), order.getMessage());
  }

  private void sendCommerceMessage(String accessToken, Order order) {
    String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

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
      templateJson = new ObjectMapper().writeValueAsString(template);
    } catch (Exception e) {
      throw new RuntimeException("카카오 메시지 JSON 직렬화 실패", e);
    }

    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("template_object", templateJson); // JSON 문자열 그대로 넣어야 함

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

    ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

    if (!response.getStatusCode().is2xxSuccessful()) {
      throw new RuntimeException("카카오톡 메시지 전송 실패: " + response.getBody());
    }
  }

}
