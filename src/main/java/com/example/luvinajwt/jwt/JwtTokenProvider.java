package com.example.luvinajwt.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Service
@Slf4j
public class JwtTokenProvider {

    private final String JWT_SECRET_KEY = "bHV2aW5hX2p3dA==";

    /**
     * 24 * 60 * 60 * 1000
     */
    private final Long JWT_EXPIRATION = 86400000L;

    /**
     * 24 * 60 * 60 * 1000 * 7
     */
    private final Long REFRESH_JWT_EXPIRATION = 604800000L;

    public String generateToken(UserDetails userDetails) {
        Date current = new Date();
        Date expDate = new Date(current.getTime() + JWT_EXPIRATION);
        return Jwts.builder()
                .setIssuedAt(current)
                .setExpiration(expDate)
                .setSubject(userDetails.getUsername())
                .signWith(SignatureAlgorithm.HS256, getKey())
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Date current = new Date();
        Date expDate = new Date(current.getTime() + REFRESH_JWT_EXPIRATION);
        return Jwts.builder()
                .setIssuedAt(current)
                .setExpiration(expDate)
                .setSubject(userDetails.getUsername())
                .signWith(SignatureAlgorithm.HS256, getKey())
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes = Base64.decodeBase64(JWT_SECRET_KEY);
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    public String getUserNameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Token Invalid");
        } catch (ExpiredJwtException ex) {
            log.error("Token Expired");
        } catch (UnsupportedJwtException ex) {
            log.error("Token Unsupported");
        } catch (IllegalArgumentException ex) {
            log.error("Token Empty");
        }
        return false;
    }
}
