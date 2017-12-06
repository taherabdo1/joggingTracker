package com.toptal.demo.security.identity;


import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.toptal.demo.entities.User;
import com.toptal.demo.entities.enums.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenUtil {

    private static final long VALIDITY_TIME_MS = 12 * 60 * 60 * 1000; // 2 hours validity
    private static final String AUTH_HEADER_NAME = "Authorization";

    private String secret="taherSecret123456_+$We";

    public Optional<Authentication> verifyToken(final HttpServletRequest request) {
      final String token = request.getHeader(AUTH_HEADER_NAME);

      if (token != null && !token.isEmpty()){
        final TokenUser user = parseUserFromToken(token.replace("Bearer","").trim());
        if (user != null) {
            return  Optional.of(new UserAuthentication(user));
        }
      }
      return Optional.empty();

    }

    //Get User Info from the Token
    public TokenUser parseUserFromToken(final String token){

        final Claims claims = Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .getBody();

        final User driver= new User();
        driver.setEmail((String) claims.get("email"));
        driver.setId(Long.valueOf(claims.get("id").toString()));
        driver.setRole(Role.valueOf((String)claims.get("role")));
        driver.setPassword((String) claims.get("password"));
        return new TokenUser(driver);
    }

    public String createTokenForUser(final TokenUser tokenUser) {
      return createTokenForUser(tokenUser.getUser());
    }

    public String createTokenForUser(final User user) {
      return Jwts.builder()
        .setExpiration(new Date(System.currentTimeMillis() + VALIDITY_TIME_MS))
                .setSubject(user.getEmail())
        .claim("id", user.getId())
                .claim("email", user.getEmail())
        .claim("role", user.getRole().toString())
        .claim("password", user.getPassword())
        .signWith(SignatureAlgorithm.HS256, secret)
        .compact();
    }

}
