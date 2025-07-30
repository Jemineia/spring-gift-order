package gift.domainTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import gift.model.Member;
import gift.model.Order;
import gift.model.Product;
import gift.model.ProductOption;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTest {
  @Test
  @DisplayName("[1] 주문 생성 테스트")
  void orderCreateTest(){
    Member member = new Member(1L, "abcd@gmail.com", "qwer1234!");
    Product product = new Product(1L, "초코파이", 2700, "https://contents.lotteon.com/itemimage/20250710070749/LM/88/01/11/75/33/41/0_/00/1/LM8801117533410_001_1.jpg");
    ProductOption option = new ProductOption(product, "TestOption1", 100);

    int quantity = 3;
    String message = "배송 전 연락 부탁드려요~";
    LocalDateTime orderTime = LocalDateTime.now();

    Order order = new Order(member, option, quantity, message, orderTime);

    assertAll(
        () -> assertEquals(member, order.getMember()),
        () -> assertEquals(option, order.getProductOption()),
        () -> assertEquals(quantity, order.getQuantity()),
        () -> assertEquals(message, order.getMessage()),
        () -> assertEquals(orderTime, order.getOrderDateTime())
    );
  }
}
