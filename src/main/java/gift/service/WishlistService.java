package gift.service;

import gift.exception.DuplicateWishItemException;
import gift.exception.InvalidQuantityException;
import gift.exception.NotFoundDeletewishlistException;
import gift.model.Member;
import gift.model.Product;
import gift.model.WishItem;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WishlistService {

  private final WishlistRepository wishlistRepository;
  private final MemberRepository memberRepository;
  private final ProductRepository productRepository;

  public WishlistService(WishlistRepository wishlistRepository,
      MemberRepository memberRepository,
      ProductRepository productRepository) {
    this.wishlistRepository = wishlistRepository;
    this.memberRepository = memberRepository;
    this.productRepository = productRepository;
  }


  // ✅ 개별 찜 항목 조회
  public WishItem getWishItem(Long memberId, Long productId) {
    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다"));
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new EntityNotFoundException("상품이 존재하지 않습니다"));

    return wishlistRepository.findByMemberAndProduct(member, product)
        .orElseThrow(() -> new EntityNotFoundException("찜 항목이 존재하지 않습니다"));
  }

  // ✅ 전체 찜 목록 조회
  public Page<WishItem> getWishList(Long memberId, Pageable pageable) {
    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다"));
    return wishlistRepository.findAllByMember(member, pageable);
  }

  // ✅ 찜 추가
  @Transactional
  public void addToWishlist(Long memberId, Long productId) {
    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다"));
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new EntityNotFoundException("상품이 존재하지 않습니다"));

    if (wishlistRepository.existsByMemberAndProduct(member, product)) {
      throw new DuplicateWishItemException("이미 찜한 상품입니다.");
    }

    WishItem wish = new WishItem(member, product, 1);
    wishlistRepository.save(wish);
  }

  // ✅ 수량 조정
  @Transactional
  public void updateQuantity(Long memberId, Long productId, int quantity) {
    if (quantity < 1) {
      throw new InvalidQuantityException("수량은 1 이상이어야 합니다.");
    }

    WishItem wishItem = getWishItem(memberId, productId);
    wishItem.setQuantity(quantity);
    // 변경감지를 통한 자동 반영
  }

  // ✅ 찜 삭제
  @Transactional
  public void deleteWishListItem(Long memberId, Long productId) {
    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다"));
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new EntityNotFoundException("상품이 존재하지 않습니다"));

    WishItem wish = wishlistRepository.findByMemberAndProduct(member, product)
        .orElseThrow(() -> new NotFoundDeletewishlistException("삭제할 찜 항목이 존재하지 않습니다."));

    wishlistRepository.delete(wish);
  }
}

