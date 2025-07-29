package gift.authentication;

import gift.model.Member;
import jakarta.servlet.ServletException;

public interface AuthenticationExtractor {

  // 1. 토큰 유효성 검사
  boolean supports(String authorizationHeader);

  // 2. 토큰을 통해 사용자 유효 검사
  Member extract(String authorizationHeader) throws ServletException;
}
