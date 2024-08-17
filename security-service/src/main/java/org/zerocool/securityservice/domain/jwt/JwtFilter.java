package org.zerocool.securityservice.domain.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.zerocool.securityservice.common.exception.CustomException;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtFilter implements WebFilter {

    private final JwtProvider jwtProvider;
    /**
     * Filtra las solicitudes HTTP para manejar la autenticación de tokens.
     *
     * Este método realiza las siguientes acciones:
     * - Si la URL del endpoint contiene 'auth', la solicitud pasa sin validación de token.
     * - Si no se encuentra un token en el encabezado de autorización, se lanza una excepción con un mensaje de error.
     * - Si el token no empieza con 'Bearer', se lanza una excepción indicando que el token es inválido.
     *
     * @param exchange el contexto de la solicitud y respuesta para esta petición.
     * @param chain la cadena de filtros que se debe aplicar después de la validación del token.
     * @return un objeto Mono<Void> que completa la ejecución del filtro y permite continuar con la cadena de filtros.
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();

        if (path.contains("auth"))
            return chain.filter(exchange);

        String auth = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (auth == null)
            return Mono.error(new CustomException("Token wasn't found", HttpStatus.BAD_REQUEST));

        if (!auth.startsWith("Bearer"))
            return Mono.error(new CustomException("Invalid token", HttpStatus.BAD_REQUEST));

        String token = auth.replace("Bearer ", "");

        jwtProvider.validateToken(token);

        exchange.getAttributes().put("token", token);
        return chain.filter(exchange);
    }
}
