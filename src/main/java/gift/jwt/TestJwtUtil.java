package gift.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import javax.crypto.SecretKey;

public class TestJwtUtil {

  static String secret = "${jwt.secret}";
  static Key key = Keys.hmacShaKeyFor(secret.getBytes());

  private static final long EXPIRATION = 3600 * 1000; // 1시간 (밀리초 단위)

  public static String createTestToken(String email) {
    return Jwts.builder()
        .setSubject(email)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
        .signWith(key)
        .compact();
  }
}
