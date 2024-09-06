package org.zerocool.postservice.adapter.port.in;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface PostPort {
    Mono<ServerResponse> savePost (ServerRequest request);
    Mono<ServerResponse> getPostsByIdUserPageable(ServerRequest request);
    Mono<ServerResponse> deletePostByIdPost(ServerRequest request);
}
