package gift.domainTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gift.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductTest {

  @Test
  @DisplayName("[1] 상품 생성 테스트")
  void productCreateTest(){
    Product product = new Product(1L, "초코파이", 2700, "https://contents.lotteon.com/itemimage/20250710070749/LM/88/01/11/75/33/41/0_/00/1/LM8801117533410_001_1.jpg");

    assertAll(
        () -> assertEquals(1L, product.getId()),
        () -> assertEquals("초코파이", product.getName()),
        () -> assertEquals(2700, product.getPrice()),
        () -> assertEquals("https://contents.lotteon.com/itemimage/20250710070749/LM/88/01/11/75/33/41/0_/00/1/LM8801117533410_001_1.jpg", product.getImageUrl())
    );
  }

  @Test
  @DisplayName("[2] 상품 정보 갱신 테스트")
  void productUpdateTest(){
    Product product = new Product(1L, "초코파이", 2700, "https://contents.lotteon.com/itemimage/20250710070749/LM/88/01/11/75/33/41/0_/00/1/LM8801117533410_001_1.jpg");
    product.update("마이쮸", 1870, "https://img.danawa.com/prod_img/500000/917/615/img/4615917_1.jpg?_v=20170316175643");

    assertAll(
        () -> assertEquals("마이쮸", product.getName()),
        () -> assertEquals(1870, product.getPrice()),
        () -> assertEquals("https://img.danawa.com/prod_img/500000/917/615/img/4615917_1.jpg?_v=20170316175643", product.getImageUrl())
    );
  }

  @Test
  @DisplayName("[3] 상품명 제약조건 테스트")
  void productConstraintTest(){
    Product product = new Product(1L, "카카오 초콜릿", 2700, "https://contents.lotteon.com/itemimage/20250710070749/LM/88/01/11/75/33/41/0_/00/1/LM8801117533410_001_1.jpg");

    assertTrue(product.hasProhibitedName());
    assertEquals("'카카오'는 담당 MD 협의 시에만 사용할 수 있습니다.", product.prohibitedMessage());
  }

}
