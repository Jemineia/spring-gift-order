package gift.service;

import gift.exception.DuplicateOptionException;
import gift.exception.InsufficientStockException;
import gift.model.Product;
import gift.model.ProductOption;
import gift.repository.ProductOptionRepository;
import gift.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductOptionService {

  private final ProductOptionRepository productOptionRepository;
  private final ProductRepository productRepository;

  public ProductOptionService(ProductOptionRepository productOptionRepository,
      ProductRepository productRepository) {
    this.productOptionRepository = productOptionRepository;
    this.productRepository = productRepository;
  }

  public List<ProductOption> findOptionsByProductId(Long productId) {
    return productOptionRepository.findByProductId(productId);
  }

  public ProductOption findById(Long optionId) {
    return productOptionRepository.findById(optionId)
        .orElseThrow(() -> new EntityNotFoundException("해당 옵션을 찾을 수 없습니다"));
  }

  private void validateDuplicateOption(Long productId, String optionName) {
    boolean exists = productOptionRepository.existsByProductIdAndOption(productId, optionName);
    if (exists) {
      throw new DuplicateOptionException("같은 상품에 이미 존재하는 옵션명입니다.");
    }
  }

  @Transactional
  public ProductOption save(Long productId, String option, int quantity) {

    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다"));

    validateDuplicateOption(productId, option);

    ProductOption productOption = new ProductOption(product, option, quantity);

    return productOptionRepository.save(productOption);
  }


  @Transactional
  public void decreaseQuantity(Long optionId, int amount) {
    if (amount <= 0) {
      throw new IllegalArgumentException("차감 수량은 0보다 커야 합니다.");
    }

    ProductOption option = productOptionRepository.findById(optionId)
        .orElseThrow(() -> new EntityNotFoundException("옵션을 찾을 수 없습니다."));

    if (option.getQuantity() < amount) {
      throw new InsufficientStockException("재고가 부족합니다.", optionId);
    }

    option.setQuantity(option.getQuantity() - amount);
  }
}
