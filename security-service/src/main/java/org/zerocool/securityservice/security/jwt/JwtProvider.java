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
     * Genera un JSON Web Token (JWT) utilizando los detalles del usuario proporcionado.
     *
     * Este método crea un JWT que incluye el nombre de usuario, los roles del usuario,
     * y los timestamps de emisión y expiración del token.
     *
     * @param userDetails el objeto que contiene los detalles del usuario, incluyendo el nombre de usuario y las autoridades (roles).
     * @return un {@code String} que representa el JWT generado, el cual incluye la información del usuario y el tiempo de expiración.
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
     * Genera una clave secreta para firmar el JWT.
     *
     * Esta clave es utilizada para el algoritmo HMAC-SHA256 para asegurar la integridad y autenticidad del token.
     *
     * @return un {@link Key} que será utilizado para firmar y verificar el JWT usando el algoritmo HMAC-SHA256.
     */
    private Key getSignKey(){
        byte[] keyByte = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyByte);
    }

    /**
     * Valida el JWT proporcionado.
     *
     * Este método verifica la validez del token utilizando la clave secreta para asegurarse de que el token
     * no ha sido manipulado y que es auténtico. Si el token no es válido, se lanzará una excepción.
     *
     * @param token un objeto {@link TokenDTO} que contiene el JWT a validar.
     */
    public void validateToken(TokenDTO token){
        Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token.getToken());
    }
}
