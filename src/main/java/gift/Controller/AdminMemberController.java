package gift.Controller;

import gift.dto.MemberResponseDto;
import gift.service.MemberService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/members")
public class AdminMemberController {

  private final MemberService memberService;

  public AdminMemberController(MemberService memberService) {
    this.memberService = memberService;
  }

  // ✅ 전체 회원 조회
  @GetMapping
  public String listMembers(Model model) {
    List<MemberResponseDto> members = memberService.findAll()
        .stream()
        .map(MemberResponseDto::from)
        .toList();
    model.addAttribute("members", members);
    return "admin/members";
  }
}
