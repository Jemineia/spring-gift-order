package gift.domainTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import gift.model.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MemberTest {

  @Test
  @DisplayName("[1] 유저 생성 테스트")
  void memberCreateTest(){
    Member member = new Member(1L, "abcd@gmail.com", "qwer1234!");

    assertAll(
        () -> assertEquals(1L, member.getId()),
        () -> assertEquals("abcd@gmail.com", member.getEmail()),
        () -> assertEquals("qwer1234!", member.getPassword())
    );
  }
}
