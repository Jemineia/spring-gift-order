package gift.Controller;

import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.model.Member;
import gift.service.OrderService;
import gift.util.LoginMember;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
  private final OrderService orderService;

  OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping
  public ResponseEntity<OrderResponseDto> order(
      @RequestBody OrderRequestDto request,
      @RequestHeader(value = "Kakao-AccessToken", required = false) String kakaoAccessToken,
      @LoginMember Member member) {
    OrderResponseDto response = orderService.order(member.getEmail(), request, kakaoAccessToken);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
