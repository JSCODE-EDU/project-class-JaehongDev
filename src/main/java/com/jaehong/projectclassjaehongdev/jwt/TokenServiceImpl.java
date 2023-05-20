package com.jaehong.projectclassjaehongdev.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class TokenServiceImpl implements TokenService {
    private static final String SECRET_KEY = "SECRET_TOKEN_SECRET_TOKEN_SECRET_TOKEN_SECRET_TOKEN_SECRET_TOKEN";

    public String issuedToken(String subject, final long periodSecond) {
        final var claims = Jwts.claims().setSubject(subject);

        final Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + periodSecond * 1000))
                .signWith(getSigningKey())
                .compact();
    }

    public String getSubject(final String token) {
        final Claims claims = getBody(token);

        return claims.getSubject();
    }

    public boolean verifyToken(final String token) {
        try {
            final Claims claims = getBody(token);
            return claims.getExpiration().after(new Date());
        } catch (final Exception e) {
            return false;
        }
    }

    private Key getSigningKey() {
        final byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims getBody(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
