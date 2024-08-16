package org.zerocool.securityservice.domain.jwt;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.zerocool.securityservice.common.exception.CustomException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtProvider jwtProvider;

    /**
     * Metodo que realiza la autenticacion del token
     * @param authentication
     * @return Un `Mono` que emite el objeto de autenticación creado si el token es válido. En caso contrario, emite
     * un error con el estado HTTP 401.
     */
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        return Mono.just(token)
                .map(jwtProvider::getClaims)
                .map(this::createAuthenticationToken)
                .switchIfEmpty(Mono.error(new CustomException("Bad Token", HttpStatus.UNAUTHORIZED)));
    }

    /**
     * Crea un objeto `Authentication` a partir de las claims del token JWT.
     * @param claims
     * @return Un objeto `UsernamePasswordAuthenticationToken` que representa la autenticación del usuario con el
     * nombre de usuario y las autoridades (roles) extraídos de las reclamaciones.
     */
    private Authentication createAuthenticationToken(Claims claims){
        String username = claims.getSubject();
        List<SimpleGrantedAuthority> authorities = jwtProvider.getRoles(claims);

        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }
}
