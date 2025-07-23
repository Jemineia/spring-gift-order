package gift.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "product_option")
public class ProductOption {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @Size(max = 50, message = "옵션명은 최대 50자까지 입력할 수 있습니다.")
  @Column(length = 50, nullable = false)
  @Pattern(regexp = "^[가-힣a-zA-Z0-9 \\(\\)\\[\\]\\+\\-\\&/_]+$", message = "옵션명에 허용되지 않은 문자가 포함되어 있습니다.")
  private String option;

  @Min(value = 1, message = "옵션 수량은 최소 1개 이상이어야 합니다.")
  @Max(value = 99999999, message = "옵션 수량은 1억 미만이어야 합니다.")
  @Column(nullable = false)
  private int quantity;

  protected ProductOption() {
  }

  public ProductOption(Product product, String option, int quantity) {
    this.product = product;
    this.option = option;
    this.quantity = quantity;
  }

  public void update(Product product, String option, int quantity) {
    this.product = product;
    this.option = option;
    this.quantity = quantity;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return option;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }
}
