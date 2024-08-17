package org.zerocool.securityservice.domain.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.zerocool.securityservice.common.exception.CustomException;
import org.zerocool.securityservice.domain.dto.TokenDTO;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class JwtProvider {

    @Value("${jwt.secret}")
    private String SECRET;
    @Value("${jwt.expiration}")
    private int EXPIRATION;
    @Value("${jwt.expiration_refresh}")
    private int REFRESH_EXPIRATION;

    /**
     * Crea un TokenDTO que contiene un token de acceso y un token de actualización
     * basados en los detalles del usuario proporcionados.
     *
     * @param userDetails los detalles del usuario para los cuales se generarán los tokens.
     * @return un TokenDTO que contiene el token de acceso y el token de actualización.
     */
    public TokenDTO createToken(UserDetails userDetails){
        return TokenDTO.builder()
                .token(generateToken(userDetails))
                .tokenRefresh(generateRefreshToken(userDetails))
                .build();
    }

    /**
     * Genera un JSON Web Token (JWT) utilizando los detalles del usuario proporcionado.
     * <p>
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
     * Genera un JSON Web Token (JWT) para los detalles del usuario proporcionados.
     * <p>
     * Este token está destinado a ser utilizado como un token de actualización,
     * lo que permite al usuario obtener un nuevo token de acceso sin volver a autenticarse.
     *
     * @param userDetails los detalles del usuario para los cuales se está generando el token de actualización.
     * @return una cadena JWT firmada que representa el token de actualización.
     */
    private String generateRefreshToken(UserDetails userDetails){
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(REFRESH_EXPIRATION, ChronoUnit.MINUTES)))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Genera una clave secreta para firmar el JWT.
     * <p>
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
     * <p>
     * Este método verifica la validez del token utilizando la clave secreta para asegurarse de que el token
     * no ha sido manipulado y que es auténtico. Si el token no es válido, se lanzará una excepción.
     *
     * @param token: un objeto {@link TokenDTO} que contiene el JWT a validar.
     */
    @SneakyThrows
    public void validateToken(String token){
        try {
            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new CustomException("Expired Token",HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            throw new CustomException("Bad Request",HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Metodo que extrae las claims de un JWT
     * @param token: token El JWT del cual se extraerán las claims.
     * @return {@link Claims} obtenidas del token
     */
    public Claims getClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
    }

    /**
     * Metodo que extrae los roles de las {@link Claims}
     * @param claims: de donde se va extraer las authorities
     * @return {@link List<SimpleGrantedAuthority>} lista de roles
     */
    public List<SimpleGrantedAuthority> getRoles(Claims claims){
        Object objectRoles = claims.get("roles");

        if (objectRoles instanceof List<?>){
            @SuppressWarnings("unchecked")
            List<Map<String, String >> listMapRoles = (List<Map<String, String >>) objectRoles;

            return listMapRoles.stream()
                    .map(role -> role.get("authority"))
                    .map(SimpleGrantedAuthority::new)
                    .toList();
        }
        return List.of();
    }
}
