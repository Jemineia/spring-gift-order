package gift.dto;

import gift.model.Product;
import gift.model.ProductOption;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDto {

  private Long id;

  @NotBlank(message = "상품 이름은 필수입니다.")
  @Size(max = 15, message = "상품 이름은 최대 15자까지 입력가능 합니다")
  @Pattern(
      regexp = "^[\\p{L}\\p{N}\\s\\(\\)\\[\\]\\+\\-&/_]*$",
      message = "상품 이름에는 (), [], +, -, &, /, _ 외의 특수문자는 사용 불가합니다"
  )
  private String name;

  @PositiveOrZero(message = "가격은 0 이상이어야 합니다")
  private int price;

  private String imageUrl;

  @Valid
  @NotEmpty(message = "최소 하나 이상의 옵션이 필요합니다.")
  private List<ProductOptionDto> options = new ArrayList<>();

  public ProductDto() {}

  public ProductDto(Long id, String name, int price, String imageUrl, List<ProductOptionDto> options) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.imageUrl = imageUrl;
    this.options = options;
  }

  public static ProductDto from(Product product) {
    if (product == null) return null;

    List<ProductOptionDto> optionDtos = product.getOptions().stream()
        .map(ProductOptionDto::from)
        .collect(Collectors.toList());

    return new ProductDto(
        product.getId(),
        product.getName(),
        product.getPrice(),
        product.getImageUrl(),
        optionDtos
    );
  }

  public Product toEntity() {
    Product product = new Product(id, name, price, imageUrl);
    for (ProductOptionDto dto : options) {
      product.addOption(new ProductOption(product, dto.getName(), dto.getQuantity()));
    }
    return product;
  }

  // getter & setter
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }
  public List<ProductOptionDto> getOptions() {
    return options;
  }

}

