package gift.domainTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import gift.model.Product;
import gift.model.ProductOption;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductOptionTest {

  @Test
  @DisplayName("[1] 옵션 생성 테스트")
  void productOptionCreateTest() {
    Product product = new Product(1L, "초코파이", 2700,
        "https://contents.lotteon.com/itemimage/20250710070749/LM/88/01/11/75/33/41/0_/00/1/LM8801117533410_001_1.jpg");
    ProductOption option = new ProductOption(product, "추석선물용 500KG", 32);

    assertAll(
        () -> assertEquals(product, option.getProduct()),
        () -> assertEquals("추석선물용 500KG", option.getName()),
        () -> assertEquals(32, option.getQuantity())
    );
  }

  @Test
  @DisplayName("[2] 옵션 정보 갱신 테스트")
  void productOptionUpdateTest() {
    Product product1 = new Product(1L, "초코파이", 2700,
        "https://contents.lotteon.com/itemimage/20250710070749/LM/88/01/11/75/33/41/0_/00/1/LM8801117533410_001_1.jpg");
    Product product2 = new Product(2L, "마이쮸", 1870,
        "https://img.danawa.com/prod_img/500000/917/615/img/4615917_1.jpg?_v=20170316175643");

    ProductOption option = new ProductOption(product1, "추석선물용 500KG", 32);
    option.update(product2, "설날선물용 100KG", 12);

    assertAll(
        () -> assertEquals(product2, option.getProduct()),
        () -> assertEquals("설날선물용 100KG", option.getName()),
        () -> assertEquals(12, option.getQuantity())
    );
  }

  @Test
  @DisplayName("[3] 옵션 수량 setter 테스트")
  void productOptionQuantitySetterTest() {
    Product product = new Product(1L, "초코파이", 2700,
        "https://contents.lotteon.com/itemimage/20250710070749/LM/88/01/11/75/33/41/0_/00/1/LM8801117533410_001_1.jpg");
    ProductOption option = new ProductOption(product, "추석선물용 500KG", 32);

    option.setQuantity(99);

    assertEquals(99, option.getQuantity());
  }
}

