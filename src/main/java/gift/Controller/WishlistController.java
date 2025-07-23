package gift.Controller;

import gift.model.Member;
import gift.model.WishItem;
import gift.service.WishlistService;
import gift.util.LoginMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/wishlist")
public class WishlistController {

  private final WishlistService wishlistService;

  public WishlistController(WishlistService wishlistService) {
    this.wishlistService = wishlistService;
  }

  // ✅ 전체 찜 목록 조회
  @GetMapping
  public String getWishlist(
      @PageableDefault(size = 1) Pageable pageable,
      @LoginMember Member member, Model model) {
    Page<WishItem> wishList = wishlistService.getWishList(member.getId(), pageable);

    model.addAttribute("wishList", wishList.getContent());
    model.addAttribute("page", wishList);
    return "wishlist/list"; // list.html
  }

  // ✅ 개별 찜 항목 조회
  @GetMapping("/{productId}")
  public String getWishItem(@PathVariable Long productId,
      @LoginMember Member member,
      Model model) {
    WishItem wishItem = wishlistService.getWishItem(member.getId(), productId);
    model.addAttribute("wishItem", wishItem);
    return "wishlist/detail"; // detail.html
  }

  // ✅ 찜 상품 수량 조절
  @PutMapping("/{productId}/quantity")
  public String updateQuantity(@PathVariable Long productId,
      @RequestParam("quantity") int quantity,
      @LoginMember Member member,
      RedirectAttributes redirectAttributes) {

    wishlistService.updateQuantity(member.getId(), productId, quantity);
    redirectAttributes.addFlashAttribute("message", "수량이 변경되었습니다.");

    return "redirect:/api/wishlist";
  }


  // ✅ 찜 상품 삭제
  @DeleteMapping("/{productId}/delete")
  public String deleteWishlistItem(@PathVariable Long productId,
      @LoginMember Member member,
      RedirectAttributes redirectAttributes) {
    wishlistService.deleteWishListItem(member.getId(), productId);
    redirectAttributes.addFlashAttribute("message", "상품이 찜 목록에서 삭제되었습니다");
    return "redirect:/api/wishlist";
  }
}

