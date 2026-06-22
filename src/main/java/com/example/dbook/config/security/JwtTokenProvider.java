package com.example.dbook.config.security;

import com.example.dbook.auth.dto.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;
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

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();

        String authorities = authentication.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.joining(","));

        long now = System.currentTimeMillis();

        String token = Jwts.builder()
                .setSubject(username)
                .claim("auth", authorities)
                .claim("userId", userId)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + accessTokenExpiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return new TokenDto("Bearer", token, accessTokenExpiration);
    }

    public Authentication getAuthentication(String token) {

        Claims claims = parseClaims(token);
        String username = claims.getSubject();

        Long userId = claims.get("userId", Long.class);

        Object authClaim = claims.get("auth");
        Collection<GrantedAuthority> authorities;

        if (authClaim != null && !authClaim.toString().trim().isEmpty()) {
            authorities = java.util.Arrays.stream(authClaim.toString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        } else {
            authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }

        UserDetails userDetails = new CustomUserDetails(userId, username, authorities);

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                authorities
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