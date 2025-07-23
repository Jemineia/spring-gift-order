package gift.service;

import gift.model.Product;
import gift.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Page<Product> findAll(Pageable pageable) {
    return productRepository.findAll(pageable);
  }

  public Optional<Product> findById(Long id) {
    return productRepository.findById(id);
  }

  @Transactional
  public Product save(Product product) {
    return productRepository.save(product);
  }

  public void update(Long id, Product updateProduct) {
    Product existing = productRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 상품입니다."));
    existing.update(updateProduct.getName(), updateProduct.getPrice(), updateProduct.getImageUrl());
  }

  public boolean delete(Long id) {
    if (productRepository.existsById(id)) {
      productRepository.deleteById(id);
      return true;
    }
    return false;
  }
}
