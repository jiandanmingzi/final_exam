package com.hjf.demo.utils;

import io.jsonwebtoken.*;

import java.util.Date;
import java.util.Map;

public class JWT_Utils {
    private static final long EXPIRE_TIME = 60 * 1000 * 30;
    // TOKEN密钥
    private static final String TOKEN_SECRET = "f26e587c28064d0e855e72c0a6a0e618";

    public static String getToken(Map<String, Object> map){
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        JwtBuilder jwtBuilder = Jwts.builder();
        return jwtBuilder
                // header
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                // payload
                .claim("id", map.get("id"))
                .claim("admin", map.get("admin"))
                .claim("authenticated",map.get("authenticated"))
                // 过期时间
                .setExpiration(date)
                // signature
                .signWith(SignatureAlgorithm.HS256, TOKEN_SECRET)
                .compact();
    }

    public static void verify(String token){
        Jwts.parser()
                .setSigningKey(TOKEN_SECRET)
                .parseClaimsJws(token);
    }

    public static Claims getClaims(String token){
        return Jwts.parser()
                .setSigningKey(TOKEN_SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}
