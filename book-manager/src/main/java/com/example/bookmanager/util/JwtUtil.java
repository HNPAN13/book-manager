package com.example.bookmanager.util;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component   // ← 这个注解千万不能丢！！！
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretStr;

    @Value("${jwt.expire}")
    private Long expireTime;

    private Key key;

    @PostConstruct
    public void initKey() {
        byte[] bytes = Base64.getDecoder().decode(secretStr);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 就是这个方法！UserController 要调用的 createToken

    public String createToken(Long userId, String role) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("role", role)
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 后面拦截器要用：解析token拿角色
    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
