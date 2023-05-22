package com.jaehong.projectclassjaehongdev.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.lang.Maps;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    private static final String SECRET_KEY = "SECRET_TOKEN_SECRET_TOKEN_SECRET_TOKEN_SECRET_TOKEN_SECRET_TOKEN";

    @Override
    public String issuedToken(Object value, long periodSecond) {
        final var claims = Jwts.claims(Maps.of("id", value).build());
        final var now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + periodSecond))
                .signWith(getSigningKey())
                .compact();
    }

    @Override
    public Long getById(final String token) {
        final var claims = getBody(token);
        return Long.parseLong(claims.get("id").toString());
    }


    @Override
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
