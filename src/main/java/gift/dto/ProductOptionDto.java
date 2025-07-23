package gift.dto;

import gift.model.ProductOption;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ProductOptionDto {

  @NotBlank(message = "옵션명을 입력해주세요.")
  @Pattern(regexp = "^[가-힣a-zA-Z0-9 \\(\\)\\[\\]\\+\\-\\&/_]+$", message = "옵션명은 특수문자를 제한하며 50자 이하여야 합니다.")
  private String name;

  @Min(value = 1, message = "수량은 1 이상이어야 합니다.")
  @Max(value = 99999999, message = "옵션 수량은 1억 미만이어야 합니다.")
  private int quantity;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public static ProductOptionDto from(ProductOption option) {
    ProductOptionDto dto = new ProductOptionDto();
    dto.setName(option.getName());
    dto.setQuantity(option.getQuantity());
    return dto;
  }

  // 옵션 제대로 전달되는지 log확인용
  @Override
  public String toString() {
    return "Option(name=" + name + ", quantity=" + quantity + ")";
  }
}
