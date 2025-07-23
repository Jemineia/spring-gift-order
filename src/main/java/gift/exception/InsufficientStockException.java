package gift.exception;

public class InsufficientStockException extends RuntimeException {
  private final Long optionId;

  public InsufficientStockException(String message, Long optionId) {
    super(message);
    this.optionId = optionId;
  }

  public Long getOptionId() {
    return optionId;
  }
}



