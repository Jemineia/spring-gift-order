package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoTokenResponseDto {

  @JsonProperty("access_token")
  private String accessToken;

  @JsonProperty("token_type")
  private String tokenType;

  @JsonProperty("refresh_token")
  private String refreshToken;

  @JsonProperty("expires_in")
  private int expiresIn;

  private String scope;

  @JsonProperty("refresh_token_expires_in")
  private int refreshTokenExpiresIn;

  public String getAccess_token() { return accessToken; }

  public String getToken_type() {
    return tokenType;
  }

  public String getRefresh_token() {
    return refreshToken;
  }


  public int getExpires_in() {
    return expiresIn;
  }


  public String getScope() {
    return scope;
  }

  public int getRefresh_token_expires_in() {
    return refreshTokenExpiresIn;
  }

}


