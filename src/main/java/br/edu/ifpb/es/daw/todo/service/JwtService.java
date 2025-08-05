package br.edu.ifpb.es.daw.todo.service;

import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import br.edu.ifpb.es.daw.todo.exception.JwtTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);

    private static final String CLAIM_ROLES = "roles";

	// Use Keys to generate a secure key (replace "secretKey" with a strong secret)
    private final SecretKey jwtSecret;

    @Value("${app.security.jwt.expiration-time}")
    private long jwtExpirationMs;

    @Autowired
    public JwtService(@Value("${app.security.jwt.secret-key}") String secret) {
        this.jwtSecret = Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Generate JWT token
    public String generateJwtToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim(CLAIM_ROLES, userDetails.getAuthorities()
                                                .stream()
                                                .map(GrantedAuthority::getAuthority)
                                                .toList())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(jwtSecret) // Use SecretKey directly
                .compact();
    }

    private Jws<Claims> getClaims(String authToken) throws JwtTokenException {
        try {
            return Jwts.parser()
                    .verifyWith(jwtSecret)
                    .build()
                    .parseSignedClaims(authToken);
        } catch (ExpiredJwtException ex) {
            throw new JwtTokenException("Token expired! Please login again.");
        } catch (UnsupportedJwtException | MalformedJwtException
                 | SignatureException ex) {
            throw new JwtTokenException("Invalid token! Authentication failed.");
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new JwtTokenException("Authentication error: " + ex.getMessage());
        }
    }

    // Validate JWT token
    public boolean validateJwtToken(String authToken) throws JwtTokenException {
        getClaims(authToken);
        return true;
    }

    // Get username from JWT token
    public String getUserNameFromJwtToken(String authToken) throws JwtTokenException {
        return getClaims(authToken)
                .getPayload()
                .getSubject();
    }

    // Get roles from JWT token
    public List<String> getRolesFromJwtToken(String authToken) throws JwtTokenException {
        @SuppressWarnings("unchecked")
        List<String> roles = (List<String>) getClaims(authToken).getPayload().get(CLAIM_ROLES);
        return roles;
    }
}
