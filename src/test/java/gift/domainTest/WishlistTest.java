package gift.domainTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import gift.model.Member;
import gift.model.Product;
import gift.model.WishItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class WishlistTest {
  @Test
  @DisplayName("[1] 위시리스트 추가 테스트")
  void wishitemCreateTest() {
    Member member = new Member(1L, "abcd@gmail.com", "qwer1234!");
    Product product = new Product(1L, "초코송이", 1456, "https://img.danawa.com/prod_img/500000/826/577/img/3577826_1.jpg?_v=20161108161614&shrink=360:360");

    WishItem item = new WishItem(member, product, 72);

    assertAll(
        () -> assertEquals(member, item.getMember()),
        () -> assertEquals(product, item.getProduct()),
        () -> assertEquals(72, item.getQuantity())
    );
  }

  @Test
  @DisplayName("[2] 수량 수정 테스트")
  void wishitemQuantityUpdateTest() {
    Member member = new Member(1L, "abcd@gmail.com", "qwer1234!");
    Product product = new Product(1L, "초코송이", 1456, "https://img.danawa.com/prod_img/500000/826/577/img/3577826_1.jpg?_v=20161108161614&shrink=360:360");

    WishItem item = new WishItem(member, product, 72);
    item.setQuantity(365);

    assertEquals(365, item.getQuantity());
  }
}
