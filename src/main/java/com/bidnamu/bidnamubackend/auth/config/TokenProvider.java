package com.bidnamu.bidnamubackend.auth.config;

import com.bidnamu.bidnamubackend.auth.exception.UnknownTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class TokenProvider {

  private static final String AUTHORITIES_KEY = "authorities";
  private final JwtProperties jwtProperties;
  private final Key key;

  public TokenProvider(final JwtProperties jwtProperties) {
    this.jwtProperties = jwtProperties;
    this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecretKey()));
  }

  public String generateRefreshToken() {
    final String uuid = UUID.randomUUID().toString();
    final Date now = new Date();
    final Date expirationDate = new Date(now.getTime() + jwtProperties.getRefreshTokenExpiration());

    return Jwts.builder()
        .setSubject(uuid)
        .setIssuedAt(now)
        .setExpiration(expirationDate)
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public Authentication getAuthentication(final String token) {
    final Claims claims = getClaims(token);
    final String authoritiesStr = claims.get(AUTHORITIES_KEY).toString()
        .replaceAll("[\\[\\]\\s]", "");
    final Collection<? extends GrantedAuthority> authorities =
        Arrays.stream(authoritiesStr.split(","))
            .map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    final var user = new org.springframework.security.core.userdetails.User(claims.getSubject(), "",
        authorities);

    return new UsernamePasswordAuthenticationToken(user, token, authorities);
  }

  public String generateAccessToken(final Authentication authentication) {
    if (authentication.getAuthorities().isEmpty()) {
      throw new UnknownTokenException("유효하지 않은 토큰입니다.");
    }
    final Set<String> authorities = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority).collect(
            Collectors.toSet());
    final Date now = new Date();
    final Date expirationDate = new Date(now.getTime() + jwtProperties.getAccessTokenExpiration());

    return Jwts.builder()
        .setSubject(authentication.getName())
        .setIssuedAt(now)
        .setExpiration(expirationDate)
        .claim(AUTHORITIES_KEY, authorities)
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public boolean validToken(final String token) {
    getClaims(token);
    return true;
  }

  private Claims getClaims(final String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }
}
