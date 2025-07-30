package gift.authentication;

import gift.jwt.JwtUtil;
import gift.model.Member;
import gift.repository.MemberRepository;
import jakarta.servlet.ServletException;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationExtractor implements AuthenticationExtractor {

  private final JwtUtil jwtUtil;
  private final MemberRepository memberRepository;

  public JwtAuthenticationExtractor(JwtUtil jwtUtil, MemberRepository memberRepository) {
    this.jwtUtil = jwtUtil;
    this.memberRepository = memberRepository;
  }

  @Override
  public boolean supports(String header) {
    return header != null && header.startsWith("Bearer ");
  }

  @Override
  public Member extract(String header) throws ServletException {
    String token = header.substring(7);
    if(!jwtUtil.isValidToken(token)) {
      throw new ServletException("유효하지 않은 JWT 토큰입니다");
    }
    String email = jwtUtil.getEmailFromToken(token);
    return memberRepository.findByEmail(email)
        .orElseThrow(()-> new SecurityException("JWT로 사용자를 찾을 수 없습니다"));
  }
}
