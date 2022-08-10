package com.bitcamp.todo.security;

import com.bitcamp.todo.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class TokenProvider {

    private static final String SECRET_KEY = "KB1341SLMg4M4EsQ4252B4e5S7422acSz63L";

    Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));


    // 사용자 정보를 받아서 JWT 토큰 생성
    public String create(UserEntity userEntity) {
        // 유효기한 설정 ( 하루 )
        Date expiryDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

        // JWT 토큰 생성
        return Jwts.builder()
                // header 에 들어갈 내용 및 서명을 하기 위한 SECRET_KEY
                .signWith(key,SignatureAlgorithm.HS256)
                // payload 들어갈 내용 ( entity로 받아온 고유 식별자 id )
                .setSubject(userEntity.getId()) // sub :
                .setIssuer("todo app")
                .setIssuedAt(new Date())
                .setExpiration(expiryDate) // 설정 유효기간
                .compact(); // 토큰 생성
    }

    // 사용자로부터 토큰을 받아와 그 토큰을 가진 사용자 id 추출
    // 토큰을 디코딩 및 파싱하여 토큰의 위조 여부 확인하는 작업
    public String validateAndGetUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject(); // 계정 id를 반환
    }

}
