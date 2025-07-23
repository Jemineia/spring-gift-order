package gift.Controller;

import gift.dto.ProductOptionDto;
import gift.model.Product;
import gift.model.ProductOption;
import gift.service.ProductOptionService;
import gift.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/products/{productId}/options")
public class AdminProductOptionController {

  private final ProductOptionService productOptionService;
  private final ProductService productService;

  public AdminProductOptionController(ProductOptionService productOptionService,
      ProductService productService) {
    this.productOptionService = productOptionService;
    this.productService = productService;
  }

  // 옵션 목록
  @GetMapping
  public String list(@PathVariable Long productId, Model model) {
    Product product = productService.findById(productId)
        .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다"));

    List<ProductOption> options = productOptionService.findOptionsByProductId(productId);
    model.addAttribute("product", product);
    model.addAttribute("options", options);
    return "admin/options/list";
  }

  // 옵션 등록 폼
  @GetMapping("/new")
  public String createForm(@PathVariable Long productId, Model model) {
    model.addAttribute("productId", productId);
    model.addAttribute("productOptionForm", new ProductOptionDto());
    return "admin/options/new";
  }

  // 옵션 등록 처리
  @PostMapping
  public String create(@PathVariable Long productId,
      @Valid @ModelAttribute("productOptionForm") ProductOptionDto form,
      BindingResult bindingResult,
      Model model) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("productId", productId);
      return "admin/options/new";
    }

    productOptionService.save(productId, form.getName(), form.getQuantity());
    return "redirect:/admin/products/{productId}/options";
  }
}
