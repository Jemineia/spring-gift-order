package gift.interceptor;

import gift.jwt.JwtUtil;
import gift.model.Member;
import gift.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

  private final JwtUtil jwtUtil;
  private final MemberRepository memberRepository;

  public JwtInterceptor(JwtUtil jwtUtil, MemberRepository memberRepository) {
    this.jwtUtil = jwtUtil;
    this.memberRepository = memberRepository;
  }

  @Override
  public boolean preHandle(HttpServletRequest request,
      HttpServletResponse response,
      Object handler) throws Exception {
    String auth = request.getHeader("Authorization");

    if (auth == null || !auth.startsWith("Bearer ")) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization 헤더가 유효하지 않습니다");
      return false;
    }

    String token = auth.substring(7);
    if (!jwtUtil.isValidToken(token)) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 Token입니다");
      return false;
    }

    String email = jwtUtil.getEmailFromToken(token);
    Optional<Member> member = memberRepository.findByEmail(email);

    if (member.isEmpty()) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "사용자를 찾을 수 없습니다");
      return false;
    }

    request.setAttribute("member", member.get());
    return true;
  }
}
