package com.shopkart.ecommerce.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtProvider {

//    private final SecretKey key = Keys.hmacShaKeyFor(JwtConstants.SECRET_KEY.getBytes());


    private final SecretKey key;
    public JwtProvider() {
        // Generate a secure key with a size of 256 bits
        this.key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
    }

    public String generateToken(Authentication auth){

        String jwt = Jwts.builder().setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 846000000))
                .claim("email",auth.getName())
                .signWith(key)
                .compact();

        return jwt;

    }

    public String getEmailFromToken(String jwt){

        // Get the email part from the jwt token
        jwt = jwt.substring(7);

        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();

        String email = String.valueOf(claims.get("email"));

        return email;
    }

}
