package gift.Controller;

import gift.model.ProductOption;
import gift.service.ProductOptionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/options")
public class AdminProductOptionStockController {

  private final ProductOptionService productOptionService;

  public AdminProductOptionStockController(ProductOptionService productOptionService) {
    this.productOptionService = productOptionService;
  }

  // 재고 수정 폼
  @GetMapping("/{optionId}/edit")
  public String showUpdateQuantityForm(@PathVariable Long optionId, Model model) {
    ProductOption option = productOptionService.findById(optionId);
    model.addAttribute("option", option);
    return "admin/options/edit";
  }

  // 재고 차감 처리
  @PostMapping("/{optionId}/edit")
  public String updateQuantity(@PathVariable Long optionId,
      @RequestParam("amount") int amount) {
    productOptionService.decreaseQuantity(optionId, amount);
    return "redirect:/admin/options/" + optionId + "/edit";
  }
}

