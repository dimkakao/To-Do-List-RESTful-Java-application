package com.softserve.itacademy.todolist.config;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    @Value("${myjwttoken.app.jwtSecret}")
    private String secret;

    @Value("${myjwttoken.app.jwtExpirationMs}")
    private Long expiration;

    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (MalformedJwtException e) {
            System.out.println("Invalid token " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("Expired token " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported token " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("String token is empty" + e.getMessage());
        } catch (SignatureException e) {
            System.out.println("Signature validation token failed" + e.getMessage());
        }
        return false;
    }

    private boolean isTokenExpired(String token) {
        Date expirationDate = extractExpiration(token);
        return expirationDate.before(new Date());
    }

    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return parseClaims(token).getExpiration();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}