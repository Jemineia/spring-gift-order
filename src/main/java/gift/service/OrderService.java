package gift.service;

import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
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
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {

  private final MemberRepository memberRepository;
  private final ProductOptionRepository productOptionRepository;
  private final OrderRepository orderRepository;
  private final WishlistRepository wishRepository;
  private final KakaoMessageService kakaoMessageService;

  private final ProductOptionService productOptionService;

  private final RestTemplate restTemplate = new RestTemplate();

  public OrderService(MemberRepository memberRepository,
      ProductOptionRepository productOptionRepository,
      OrderRepository orderRepository,
      WishlistRepository wishRepository,
      ProductOptionService productOptionService,
      KakaoMessageService kakaoMessageService) {
    this.memberRepository = memberRepository;
    this.productOptionRepository = productOptionRepository;
    this.orderRepository = orderRepository;
    this.wishRepository = wishRepository;
    this.productOptionService = productOptionService;
    this.kakaoMessageService = kakaoMessageService;
  }

  @Transactional
  public OrderResponseDto order(String email, OrderRequestDto request, String kakaoAccessToken) {
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
    if (kakaoAccessToken != null && !kakaoAccessToken.isBlank()) {
      kakaoMessageService.sendCommerceMessage(kakaoAccessToken, order);
    }

    return new OrderResponseDto(order.getId(), option.getId(), order.getQuantity(),
        order.getOrderDateTime(), order.getMessage());
  }
}
