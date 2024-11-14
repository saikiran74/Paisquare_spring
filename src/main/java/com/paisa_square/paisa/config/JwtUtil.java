package com.paisa_square.paisa.config;

import com.paisa_square.paisa.model.Role;
import com.paisa_square.paisa.model.User;
import com.paisa_square.paisa.repository.RoleRepository;
import com.paisa_square.paisa.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.*;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.function.Function;

@Component
public class JwtUtil {
    @Autowired
    private UserRepository userRepository;

    final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        User user = userRepository.findByEmail(email);
        claims.put("user", user);
        return createToken(claims, email);
    }

    private String createToken(Map<String, Object> claims, String gmail) {
        return Jwts.builder()
                .setClaims(claims) // Set claims here
                .setSubject(gmail)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(secretKey)
                .compact();

    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }


    // Method to extract a specific claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Method to check if a token is expired
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Method to extract expiration date
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Method to validate token
    public Boolean validateToken(String token, String email) {
        final String extractedEmail = extractEmail(token);
        return (extractedEmail.equals(email) && !isTokenExpired(token));
    }
    public String extractEmail(String token) {
       return extractClaim(token, Claims::getSubject);
    }

}










