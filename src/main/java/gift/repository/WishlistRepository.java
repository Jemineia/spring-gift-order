package gift.repository;

import gift.model.Member;
import gift.model.Product;
import gift.model.WishItem;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<WishItem, Long> {

  Optional<WishItem> findByMemberAndProduct(Member member, Product product);

  Page<WishItem> findAllByMember(Member member, Pageable pageable);

  void deleteByMemberAndProduct(Member member, Product product);

  boolean existsByMemberAndProduct(Member member, Product product);
}
