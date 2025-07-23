package gift.dto;

import jakarta.validation.constraints.*;

public class MemberRequestDto {

  @NotBlank(message = "Email을 입력해 주세요")
  @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식으로 작성해주세요")
  private String email;

  @NotBlank(message = "비밀번호를 입력해 주세요")
  @Size(min = 8, max = 20, message = "8~20자로 입력해 주세요.")
  @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!~`<>,./?;:'\"\\[\\]{}\\\\()|_-])\\S*$", message = "영문, 숫자, 특수문자가 포함되어야 하고 공백이 포함될 수 없습니다.")
  private String password;

  public MemberRequestDto() {
  }

  public MemberRequestDto(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
