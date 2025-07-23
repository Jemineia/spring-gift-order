package gift.Controller;

import gift.dto.ProductDto;
import gift.dto.ProductOptionDto;
import gift.exception.ValidationException;
import gift.model.Product;
import gift.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

  private final ProductService productService;

  public AdminProductController(ProductService productService) {
    this.productService = productService;
  }

  // ✅ 전체 상품 조회
  @GetMapping
  public String adminGetAllProducts(
      @PageableDefault(size = 1) Pageable pageable,
      Model model) {
    Page<Product> productPage = productService.findAll(pageable);
    List<ProductDto> dtoList = productPage.getContent().stream()
        .filter(Objects::nonNull) // ← 이중 체크
        .map(ProductDto::from)
        .collect(Collectors.toList());

    model.addAttribute("products", dtoList);
    model.addAttribute("page", productPage);
    return "admin/products";
  }


  // ✅ 상품 추가 페이지
  @GetMapping("/add")
  public String showAddForm(Model model) {
    model.addAttribute("product", new ProductDto());
    model.addAttribute("action", "/admin/products");
    model.addAttribute("method", "post");
    return "admin/product-form";
  }

  // ✅ 상품 수정 페이지
  @GetMapping("/{id}/edit")
  public String showEditForm(@PathVariable Long id, Model model) {
    Product product = productService.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Product not found"));

    model.addAttribute("product", ProductDto.from(product));
    model.addAttribute("action", "/admin/products/" + id);
    model.addAttribute("method", "put");
    return "admin/product-form";
  }

  // ✅ 상품 추가
  @PostMapping
  public String addProduct(@Valid @ModelAttribute("product") ProductDto productDto,
      BindingResult bindingResult,
      Model model) {

    // 부적합한 상품명 체크
    containsProhibitedName(productDto, bindingResult);

    if (bindingResult.hasErrors()) {
      System.out.println("Validation errors found:");
      bindingResult.getAllErrors().forEach(err -> System.out.println(err.toString())); // 검증 오류 log 확인용
      model.addAttribute("product", productDto); // 검증 오류가 있을 경우, 모델에 product를 담아줌
      throw new ValidationException(bindingResult); // 검증 예외를 던짐
    }

    // 상품 저장
    productService.save(productDto.toEntity());
    return "redirect:/admin/products"; // 성공 시 목록으로 리다이렉트
  }


  // ✅ 상품 수정
  @PutMapping("/{id}")
  public String updateProduct(@PathVariable Long id,
      @Valid @ModelAttribute("product") ProductDto productDto,
      BindingResult bindingResult,
      Model model) {

    containsProhibitedName(productDto, bindingResult);

    if (bindingResult.hasErrors()) {
      throw new ValidationException(bindingResult);
    }

    productService.update(id, productDto.toEntity());
    return "redirect:/admin/products";
  }

  // 금지된 단어를 포함하고 있는지 체크하는 함수
  private void containsProhibitedName(ProductDto productDto, BindingResult bindingResult) {
    Product product = productDto.toEntity();
    // Entity로 바꾸어서 해당 함수를 불러와 가지고있는지 체크 수행
    if (product.hasProhibitedName()) {
      bindingResult.rejectValue("name", "invalid", product.prohibitedMessage());
    }
  }
}
