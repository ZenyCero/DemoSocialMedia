package org.zerocool.postservice.controller.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.zerocool.postservice.adapter.port.in.PostPort;
import org.zerocool.postservice.adapter.port.out.PostRepositoryPort;
import org.zerocool.postservice.domain.dto.PostDTO;
import reactor.core.publisher.Mono;

@Component
public class PostHandler implements PostPort {
    private final PostRepositoryPort postRepositoryPort;

    public PostHandler(PostRepositoryPort postRepositoryPort) {
        this.postRepositoryPort = postRepositoryPort;
    }

    @Override
    public Mono<ServerResponse> save(ServerRequest request) {
        Mono<PostDTO> postDTO = request.bodyToMono(PostDTO.class);
        Mono<MultipartFile> multipartFile = request.multipartData()
                .mapNotNull(data -> data.getFirst("file"))
                .map(file -> (MultipartFile) file);
        return postDTO
                .flatMap(dto -> multipartFile.flatMap(multi -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(postRepositoryPort.save(dto, multi), PostDTO.class)));
    }

    @Override
    public Mono<ServerResponse> get(ServerRequest request) {
        return null;
    }
}
