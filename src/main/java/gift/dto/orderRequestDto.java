package gift.dto;

public class orderRequestDto {
  private Long optionId;
  private int quantity;
  private String message;

  public Long getOptionId() {
    return optionId;
  }
  public int getQuantity() {
    return quantity;
  }
  public String getMessage() {
    return message;
  }
}
