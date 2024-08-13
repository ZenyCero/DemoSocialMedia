package org.zerocool.securityservice.security.repository;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.zerocool.securityservice.security.jwt.JwtAuthenticationManager;
import reactor.core.publisher.Mono;

@Component
public class SecurityContextRepository implements ServerSecurityContextRepository {
    private JwtAuthenticationManager jwtAuthenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    /**
     * Carga la información de autenticación del usuario en el contexto de seguridad basado en el token JWT.
     * @param exchange
     * @return {@link SecurityContext} que contiene la informacion de autenticacion exitosa
     * de otro modo se emitira un error
     */
    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        String path = exchange.getRequest().getPath().value();
        if (path.contains("auth")){
            return Mono.empty();
        }
        String token = exchange.getAttribute("roles");
        return jwtAuthenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(token,token))
                .map(SecurityContextImpl::new);
    }
}
