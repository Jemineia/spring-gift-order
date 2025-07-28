package gift.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private Member member;

  @ManyToOne
  private ProductOption ProductOption;

  private int quantity;

  private String message;

  private LocalDateTime orderDateTime;

  protected Order() {}

  public Order(Member member, ProductOption ProductOption, int quantity, String message, LocalDateTime orderDateTime) {
    this.member = member;
    this.ProductOption = ProductOption;
    this.quantity = quantity;
    this.message = message;
    this.orderDateTime = orderDateTime;
  }

  public Long getId() {
    return id;
  }
  public Member getMember() {
    return member;
  }
  public ProductOption getProductOption() {
    return ProductOption;
  }
  public int getQuantity() {
    return quantity;
  }
  public String getMessage() {
    return message;
  }
  public LocalDateTime getOrderDateTime() {
    return orderDateTime;
  }
}
