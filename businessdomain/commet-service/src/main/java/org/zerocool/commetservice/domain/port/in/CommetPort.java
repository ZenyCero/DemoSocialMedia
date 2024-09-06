package org.zerocool.commetservice.domain.port.in;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface CommetPort {
    Mono<ServerResponse> getCommets(ServerRequest serverRequest);
    Mono<ServerResponse> saveCommet(ServerRequest saveCommet);
}
