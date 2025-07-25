package gift.Controller;

import gift.dto.MemberRequestDto;
import gift.jwt.JwtUtil;
import gift.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/api/members")
public class MemberController {

  private final MemberService memberService;
  private final JwtUtil jwtUtil;

  @Value("${kakao-rest-api-key}")
  private String rest_api_key;

  public MemberController(MemberService memberService, JwtUtil jwtUtil) {
    this.memberService = memberService;
    this.jwtUtil = jwtUtil;
  }

  // 회원가입 기능
  @GetMapping("/register")
  public String showRegisterForm(Model model) {
    model.addAttribute("memberRequestDto", new MemberRequestDto());
    return "user/register";
  }

  @PostMapping("/register")
  public ResponseEntity<Void> register(@Valid @ModelAttribute MemberRequestDto req,
      BindingResult bindingResult) throws BindException {
    if (bindingResult.hasErrors()) {
      throw new BindException(bindingResult);
    }

    memberService.register(req.getEmail(), req.getPassword());

    // 201 Created + 로그인 페이지로 리다이렉트 안내
    URI loginUri = URI.create("/api/members/login");
    return ResponseEntity.created(loginUri).build();
  }

  // 로그인 기능
  @GetMapping("/login")
  public String showLoginForm() {
    return "user/login";
  }

  @PostMapping("/login")
  public ResponseEntity<Void> login(@ModelAttribute MemberRequestDto req,
      HttpServletResponse response) throws IOException {
    HttpHeaders headers = memberService.login(req.getEmail(), req.getPassword());

    // 카카오톡 인증토큰 발급
    final String redirectUri = "http://localhost:8080";
    String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize" +
        "?response_type=code" +
        "&client_id=" + rest_api_key +
        "&redirect_uri=" + redirectUri +
        "&scope=talk_message";
    response.sendRedirect(kakaoAuthUrl);

    return new ResponseEntity<>(headers, HttpStatus.OK);
  }

}
