package gift.service;

import gift.dto.orderRequestDto;
import gift.dto.orderResponseDto;
import gift.model.Member;
import gift.model.Order;
import gift.model.ProductOption;
import gift.repository.MemberRepository;
import gift.repository.OrderRepository;
import gift.repository.ProductOptionRepository;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  private final MemberRepository memberRepository;
  private final ProductOptionRepository productOptionRepository;
  private final OrderRepository orderRepository;
  private final WishlistRepository wishRepository;

  private final ProductOptionService productOptionService;

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
  public orderResponseDto order(String email, orderRequestDto request) {
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

    return new orderResponseDto(order.getId(), option.getId(), order.getQuantity(),
        order.getOrderDateTime(), order.getMessage());
  }
}
