package com.booking.hotelapplication.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class JWTUtils {


    private static final long EXPIRATION_TIME=1000*60*24*7; // 7 days in milliseconds

    private final SecretKey Key;



    public JWTUtils(){
        String secreteString = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";
        byte[] keyBytes =Base64.getDecoder().decode(secreteString.getBytes(StandardCharsets.UTF_8));
        this.Key=new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    public String generateToken(UserDetails userDetails){
        return Jwts.builder().subject(userDetails.getUsername())
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
                    .signWith(Key)
                    .compact();
    }

    public String extractUserName(String token){
        return extractClaims(token,Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims, T> ClaimsTFunction){
        return ClaimsTFunction.apply(
            Jwts.parser().verifyWith(Key).build().parseSignedClaims(token).getPayload()
        );
    }

    public boolean isValidToken(String token,UserDetails userDetails){
        final String username=extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        return extractClaims(token, Claims:: getExpiration).before(new Date());
    }
}
