package gift.exception;

import gift.dto.MemberRequestDto;
import gift.model.Member;
import gift.model.ProductOption;
import gift.model.WishItem;
import gift.service.ProductOptionService;
import gift.service.WishlistService;
import gift.util.LoginMember;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  private final WishlistService wishlistService;
  private final ProductOptionService productOptionService;

  public GlobalExceptionHandler(WishlistService wishlistService,  ProductOptionService productOptionService) {
    this.wishlistService = wishlistService;
    this.productOptionService = productOptionService;
  }

  // 잘못된 상품형식을 입력하는 경우
  @ExceptionHandler(ValidationException.class)
  public String handleValidationException(ValidationException ex, Model model,
      HttpServletResponse response) {
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    // BindingResult에서 에러 메시지 추출
    BindingResult bindingResult = ex.getBindingResult();
    model.addAttribute("errors", bindingResult.getAllErrors()); // 에러 메시지를 모델에 추가

    // 검증 실패한 상품 객체를 모델에 다시 추가
    model.addAttribute("product", ex.getBindingResult().getTarget());

    // 상품 추가 폼으로 돌아가도록 처리
    return "admin/product-form"; // 상품 등록 폼으로 돌아가도록 반환
  }


  // ✅ 이메일/비밀번호 형식 오류 등 바인딩 예외
  @ExceptionHandler(BindException.class)
  public String handleBindException(BindException ex,
      HttpServletRequest request,
      Model model,
      HttpServletResponse response) {
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    BindingResult bindingResult = ex.getBindingResult();

    model.addAttribute("memberRequestDto", bindingResult.getTarget());
    model.addAttribute("org.springframework.validation.BindingResult.memberRequestDto",
        bindingResult);

    return "user/register";
  }

  // ✅ 중복 이메일 예외
  @ExceptionHandler(DuplicateEmailException.class)
  public String handleDuplicateEmail(DuplicateEmailException ex,
      HttpServletRequest request,
      Model model,
      HttpServletResponse response) {
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

    MemberRequestDto dto = new MemberRequestDto();
    dto.setEmail(request.getParameter("email"));
    dto.setPassword(request.getParameter("password"));

    model.addAttribute("memberRequestDto", dto);
    model.addAttribute("error", ex.getMessage());

    return "user/register";
  }

  // ✅ 로그인 실패 (SecurityException) 처리
  @ExceptionHandler(SecurityException.class)
  public ResponseEntity<String> handleLoginFail(SecurityException ex) {
    return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .header("X-Error-Message", ex.getMessage())
        .body("로그인 실패: " + ex.getMessage());
  }

  // 위시리스트 수량이 0 이하인 경우
  @ExceptionHandler(InvalidQuantityException.class)
  public String handleInvalidQuantity(InvalidQuantityException ex,
      HttpServletRequest request,
      Model model,
      HttpServletResponse response,
      @LoginMember Member member,
      @PageableDefault(size = 1) Pageable pageable) {

    response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 상태코드 유지

    // 다시 wishlist를 채워서 렌더링
    Page<WishItem> wishPage = wishlistService.getWishList(member.getId(), pageable);
    model.addAttribute("wishList", wishPage.getContent());
    model.addAttribute("page", wishPage);
    model.addAttribute("error", ex.getMessage());

    return "wishlist/list"; // 리디렉션 아님, 직접 렌더링
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public String handleIllegalArgument(IllegalArgumentException ex,
      Model model,
      HttpServletResponse response,
      @LoginMember Member member,
      @PageableDefault(size = 1) Pageable pageable) {
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);

    Page<WishItem> wishPage = wishlistService.getWishList(member.getId(), pageable);
    model.addAttribute("wishList", wishPage.getContent());
    model.addAttribute("page", wishPage);
    model.addAttribute("error", ex.getMessage());

    return "wishlist/list";
  }


  @ExceptionHandler(NotFoundDeletewishlistException.class)
  public String handleNotFoundDeletewishlistException(NotFoundDeletewishlistException ex,
      HttpServletResponse response,
      Model model,
      @LoginMember Member member,
      @PageableDefault(size = 1) Pageable pageable) {
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);

    Page<WishItem> wishPage = wishlistService.getWishList(member.getId(), pageable);
    model.addAttribute("wishList", wishPage.getContent());
    model.addAttribute("page", wishPage);
    model.addAttribute("error", ex.getMessage());

    return "wishlist/list";
  }


  @ExceptionHandler(DuplicateWishItemException.class)
  public String handleDuplicateWishItem(DuplicateWishItemException ex,
      Model model,
      HttpServletResponse response,
      @LoginMember Member member,
      @PageableDefault(size = 1) Pageable pageable) {
    response.setStatus(HttpServletResponse.SC_CONFLICT); // 409 Conflict

    Page<WishItem> wishPage = wishlistService.getWishList(member.getId(), pageable);
    model.addAttribute("wishList", wishPage.getContent());
    model.addAttribute("page", wishPage);
    model.addAttribute("error", ex.getMessage());

    return "wishlist/list";
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public String handleEntityNotFoundException(EntityNotFoundException ex,
      Model model,
      HttpServletResponse response,
      @LoginMember Member member,
      @PageableDefault(size = 1) Pageable pageable) {
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    Page<WishItem> wishPage = wishlistService.getWishList(member.getId(), pageable);
    model.addAttribute("wishList", wishPage.getContent());
    model.addAttribute("page", wishPage);
    model.addAttribute("error", ex.getMessage());

    return "wishlist/list";
  }


  @ExceptionHandler(DuplicateOptionException.class)
  public String handleDuplicateOptionException(DuplicateOptionException ex,
      Model model,
      HttpServletResponse response) {
    response.setStatus(HttpServletResponse.SC_CONFLICT);
    model.addAttribute("errorMessage", ex.getMessage());
    return "admin/options/new";  // 옵션 입력 form 페이지 경로
  }

  @ExceptionHandler(InsufficientStockException.class)
  public String handleInsufficientStock(InsufficientStockException ex,
      Model model,
      HttpServletResponse response) {
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

    // 예외 객체에 optionId가 포함되어 있다고 가정
    Long optionId = ex.getOptionId();
    ProductOption option = productOptionService.findById(optionId);  // 서비스에서 조회

    model.addAttribute("option", option);  // 필수
    model.addAttribute("error", ex.getMessage());  // 템플릿에 맞게 "error"로 전달

    return "admin/options/edit";
  }
}


