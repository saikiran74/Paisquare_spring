package com.paisa_square.paisa.config;

import com.paisa_square.paisa.model.User;
import com.paisa_square.paisa.repository.UserRepository;
import io.jsonwebtoken.io.DecodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.GrantedAuthority;

import javax.management.relation.Role;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String token = getJwtFromRequest(request);
        String path = request.getRequestURI();
        // Skip JWT filter for public endpoints
        if (path.startsWith("/login") || path.startsWith("/registeruser") ||
                path.startsWith("/verifyOTP")  ||
                path.startsWith("/advertisements") ||
                path.startsWith("/getHashTags") ||
                path.startsWith("/getHashTagsAdvertisement") ||
                path.startsWith("/getpincodesadvertisement") ||
                path.startsWith("/idadvertisements") ||
                path.startsWith("/contactus")) {
            chain.doFilter(request, response);
            return;
        }
        if (token == null || token.trim().isEmpty() || "undefined".equals(token)) {
            System.out.println("Token is undefined or missing. User needs to log in.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else {
            try {
                // Validate token
                if (jwtUtil.validateToken(token, jwtUtil.extractEmail(token))) {
                    String email = jwtUtil.extractEmail(token);
                    User userDetails = userRepo.findByEmail(email);
                    if (userDetails != null) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, null);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else{
                        logger.error("userDetails is null");

                    }
                }
            } catch (DecodingException e) {
                logger.error("JWT decoding error: Invalid base64 encoding");
            } catch (Exception e) {
                logger.error("An error occurred during JWT validation",e);
            }
        }
        chain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
