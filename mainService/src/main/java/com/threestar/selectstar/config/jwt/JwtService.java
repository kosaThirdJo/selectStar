package com.threestar.selectstar.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Value("${SECRET_KEY}")
    private String sKey;

    //private String issuer =""; //토큰 발행자

    public DecodedJWT verifyToken(String token) {
        try{
            Algorithm algorithm = Algorithm.HMAC512(sKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    //.withIssuer(issuer);  이슈어 확인 안함
                    .build();
            return verifier.verify(token); //토큰을 검증하고, 유효하면 decodedjwt 반환
        }catch (JWTVerificationException jwtE){
            throw new RuntimeException("Invalid token", jwtE);
        }

    }
}
