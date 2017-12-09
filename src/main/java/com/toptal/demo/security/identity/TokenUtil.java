package com.toptal.demo.security.identity;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

    public Optional<Authentication> verifyToken(final HttpServletRequest request) throws UnsupportedEncodingException {
      final String token = request.getHeader(AUTH_HEADER_NAME);

      final String decoded = URLDecoder.decode(token, "UTF-8");

        if (decoded != null && !token.isEmpty()) {
            final TokenUser user = parseUserFromToken(decoded.replace("Bearer", "").trim());
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

        final User user= new User();
        user.setEmail((String) claims.get("email"));
        user.setId(Long.valueOf(claims.get("id").toString()));
        user.setRole(Role.valueOf((String)claims.get("role")));
        user.setPassword((String) claims.get("password"));
        return new TokenUser(user);
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
