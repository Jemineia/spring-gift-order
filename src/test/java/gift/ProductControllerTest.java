package gift;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@DisplayName("Product 삽입 Test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)  // context 오염 방지
public class ProductControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("정상 물건 생성")
  void validProductName() throws Exception {
    // given
    String name = "초코파이";
    int price = 5700;
    String imageUrl = "https://example.com/image.jpg";
    String option = "추석 선물용 500KG";
    int optionQuantity = 72;

    mockMvc.perform(post("/admin/products")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .queryParam("name", name)
            .queryParam("price", String.valueOf(price))
            .queryParam("imageUrl", imageUrl)
            .param("options[0].name", option)
            .param("options[0].quantity", String.valueOf(optionQuantity))
        )
        .andExpect(status().isFound()); // redirect되어 list로 넘어가므로
  }

  @Test
  @DisplayName("[Product.name] 최대 글자 수 길이 제한 초과")
  void overLength_ProductName() throws Exception {
    // given
    String name = "초코파이는15자가넘을까요안넘을까요";

    int price = 5700;
    String imageUrl = "https://example.com/image.jpg";
    String option = "추석 선물용 500KG";
    int optionQuantity = 72;

    mockMvc.perform(post("/admin/products")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .queryParam("name", name)
            .queryParam("price", String.valueOf(price))
            .queryParam("imageUrl", imageUrl)
            .param("options[0].name", option)
            .param("options[0].quantity", String.valueOf(optionQuantity))
        )
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("[Product.name] 허용되지 않은 특수문자 사용")
  void invalidHyperText_ProductName() throws Exception {
    // given
    String name = "초코파이!@#$%**";

    int price = 5700;
    String imageUrl = "https://example.com/image.jpg";
    String option = "추석 선물용 500KG";
    int optionQuantity = 72;

    mockMvc.perform(post("/admin/products")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .queryParam("name", name)
            .queryParam("price", String.valueOf(price))
            .queryParam("imageUrl", imageUrl)
            .param("options[0].name", option)
            .param("options[0].quantity", String.valueOf(optionQuantity))
        )
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("[Product.name] '카카오'가 상품명에 포함")
  void invalidWord_ProductName() throws Exception {
    // given
    String name = "카카오 초코파이";

    int price = 5700;
    String imageUrl = "https://example.com/image.jpg";
    String option = "추석 선물용 500KG";
    int optionQuantity = 72;

    mockMvc.perform(post("/admin/products")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .queryParam("name", name)
            .queryParam("price", String.valueOf(price))
            .queryParam("imageUrl", imageUrl)
            .param("options[0].name", option)
            .param("options[0].quantity", String.valueOf(optionQuantity))
        )
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("[Product.price] 음수 가격 삽입")
  void minusPrice_ProductPrice() throws Exception {
    // given
    int price = -7200;

    String name = "초코파이";
    String imageUrl = "https://example.com/image.jpg";
    String option = "추석 선물용 500KG";
    int optionQuantity = 72;

    mockMvc.perform(post("/admin/products")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .queryParam("name", name)
            .queryParam("price", String.valueOf(price))
            .queryParam("imageUrl", imageUrl)
            .param("options[0].name", option)
            .param("options[0].quantity", String.valueOf(optionQuantity))
        )
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("[Product.option] 옵션을 비우고 상품 등록")
  void inValidOptionInsert() throws Exception {
    // given
    int price = 7200;
    String name = "초코파이";
    String imageUrl = "https://example.com/image.jpg";

    mockMvc.perform(post("/admin/products")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .queryParam("name", name)
            .queryParam("price", String.valueOf(price))
            .queryParam("imageUrl", imageUrl)
        )
        .andExpect(status().isBadRequest());
  }
}
