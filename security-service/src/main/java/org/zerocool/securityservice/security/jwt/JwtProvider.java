package org.zerocool.securityservice.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.zerocool.securityservice.security.dto.TokenDTO;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class JwtProvider {

    @Value("${jwt.secret}")
    private String SECRET;

    @Value("${jwt.expiration}")
    private int EXPIRATION;

    /**
     * Metodo para generar el JWT
    * @param userDetails: del cual se extrae el username y authorities
    * @return JWT como {@code String} el token incluye los datos del usuario y el tiempo de expiracion
     */
    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles",userDetails.getAuthorities())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(EXPIRATION, ChronoUnit.MINUTES)))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Metodo para generar una llave para el JWT encriptacion
     * @return a {@link Key} que sera usado por HMAC-SHA256 para firmar el JWT
     */
    private Key getSignKey(){
        byte[] keyByte = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyByte);
    }

    /**
     * Metodo para validar el JWT
     * @param token: se extrae el token para la validacion
     */
    public void validateToken(TokenDTO token){
        Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token.getToken());
    }
}
