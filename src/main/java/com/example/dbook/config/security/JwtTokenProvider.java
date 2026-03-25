package com.example.dbook.config.security;

import com.example.dbook.auth.dto.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;
    private final Key key;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            UserDetailsService userDetailsService
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.userDetailsService = userDetailsService;
    }

    public TokenDto createToken(Authentication authentication) {

        String username = authentication.getName();

        String authorities = authentication.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.joining(","));

        long now = System.currentTimeMillis();

        String token = Jwts.builder()
                .setSubject(username)
                .claim("auth", authorities)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + accessTokenExpiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return new TokenDto("Bearer", token, accessTokenExpiration);
    }

    public Authentication getAuthentication(String token) {
        String username = getUsername(token);

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT");
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("잘못된 JWT");
        }
        return false;
    }

    private String getUsername(String token) {
        return parseClaims(token).getSubject();
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}