package com.hjf.demo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

public class JWT_Utils {
    private static final String SIGNATURE = "hjfsTOKEN";

    public static String getToken(Map<String, Object> map){
        return Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setHeaderParam("alg","HS256")
                .setClaims(map)
                .setExpiration(Date.from(LocalDateTime
                                .now()
                                .plusHours(24)
                                .atZone(ZoneId.systemDefault())
                                .toInstant()))
                .signWith(SignatureAlgorithm.HS256,SIGNATURE)
                .compact();
    }

    public static void verify(String token){
        Jwts.parser()
                .setSigningKey(SIGNATURE)
                .parseClaimsJws(token);
    }

    public static Claims getClaims(String token){
        return Jwts.parser()
                .setSigningKey(SIGNATURE)
                .parseClaimsJws(token)
                .getBody();
    }
}
