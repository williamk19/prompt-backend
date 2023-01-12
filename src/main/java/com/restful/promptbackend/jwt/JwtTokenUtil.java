package com.restful.promptbackend.jwt;

import com.restful.promptbackend.User.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {
  @Value("${app.jwt.issuer-name}")
  private String ISSUER_NAME;

  @Value("${app.jwt.expire-duration}")
  private long EXPIRE_DURATION;

  @Value("${app.jwt.secret}")
  private String SECRET_KEY;

  private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);

  public String generateAccessToken(User user) {
    return Jwts.builder()
            .setSubject(user.getUsername())
            .setIssuer(ISSUER_NAME)
            .claim("roles", user.getRoles().toString())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
            .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
            .compact();
  }

  public boolean validateAccessToken(String accessToken) {
    if (parseClaims(accessToken) != null) {
      return true;
    } else {
      return false;
    }
  }

  public String[] getRoles(String accessToken) {
    String roles = (String) parseClaims(accessToken).getBody().get("roles");
    roles = roles.replace("[", "").replace("]", "");
    String[] rolesList = roles.split(",");
    return rolesList;
  }

  public String  getUsername(String accessToken) {
    return getSubject(accessToken);
  }

  private String getSubject(String accessToken) {
    return parseClaims(accessToken).getBody().getSubject();
  }

  private Jws<Claims> parseClaims(String token) {
    try {
      return Jwts.parser()
              .setSigningKey(SECRET_KEY)
              .parseClaimsJws(token);
    } catch (ExpiredJwtException ex) {
      LOGGER.error("JWT expired", ex.getMessage());
    } catch (IllegalArgumentException ex) {
      LOGGER.error("Token is null, empty or only whitespace", ex.getMessage());
    } catch (MalformedJwtException ex) {
      LOGGER.error("JWT is invalid", ex);
    } catch (UnsupportedJwtException ex) {
      LOGGER.error("JWT is not supported", ex);
    } catch (SignatureException ex) {
      LOGGER.error("Signature validation failed");
    }

    return null;
  }
}
