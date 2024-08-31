package org.zerocool.postservice.adapter.port.in;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.zerocool.postservice.domain.dto.PostDTO;
import reactor.core.publisher.Mono;

public interface PostPort {
    Mono<ServerResponse> save (ServerRequest request);
    Mono<ServerResponse> get (ServerRequest request);
}
