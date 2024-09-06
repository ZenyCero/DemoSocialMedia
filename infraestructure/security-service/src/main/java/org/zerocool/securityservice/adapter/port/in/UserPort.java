package org.zerocool.securityservice.adapter.port.in;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface UserPort {
    Mono<ServerResponse> login(ServerRequest request);
    Mono<ServerResponse> registry(ServerRequest request);
    Mono<ServerResponse> refreshToken(ServerRequest request);
}
