package gift.util;

import gift.jwt.JwtUtil;
import gift.model.Member;
import gift.repository.MemberRepository;
import jakarta.servlet.ServletException;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

  private final JwtUtil jwtUtil;
  private final MemberRepository memberRepository;

  public LoginMemberArgumentResolver(JwtUtil jwtUtil, MemberRepository memberRepository) {
    this.jwtUtil = jwtUtil;
    this.memberRepository = memberRepository;
  }


  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(LoginMember.class) &&
        parameter.getParameterType().equals(Member.class);
  }

  @Override
  public Object resolveArgument(MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory) throws Exception {
    String authHeader = webRequest.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new ServletException("Authorization 헤더가 없습니다");
    }
    String token = authHeader.substring(7);
    if (!jwtUtil.isValidToken(token)) {
      throw new ServletException("유효하지 않은 토큰입니다");
    }
    String email = jwtUtil.getEmailFromToken(token);
    return memberRepository.findByEmail(email)
        .orElseThrow(() -> new SecurityException("사용자를 찾을 수 없습니다"));
  }
}
