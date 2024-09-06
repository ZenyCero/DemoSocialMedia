package org.zerocool.postservice.controller.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.zerocool.postservice.adapter.entity.Post;
import org.zerocool.postservice.adapter.port.in.PostPort;
import org.zerocool.postservice.adapter.port.out.PostRepositoryPort;
import org.zerocool.postservice.domain.dto.PostDTO;
import org.zerocool.sharedlibrary.validation.ObjectValidator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PostHandler implements PostPort {
    private final PostRepositoryPort postRepositoryPort;
    private final ObjectValidator objectValidator;

    @Override
    public Mono<ServerResponse> savePost(ServerRequest request) {
        Mono<PostDTO> postDTO = request.bodyToMono(PostDTO.class).doOnNext(objectValidator::validate);
        return postDTO
                .flatMap(dto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(postRepositoryPort.savePost(dto), String.class));
    }

    @Override
    public Mono<ServerResponse> getPostsByIdUser(ServerRequest request) {
        Long idUser = Long.parseLong(request.queryParam("id").get());
        int page = Integer.parseInt(request.queryParam("page").orElse("1"));

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(postRepositoryPort.getPost(idUser), Post.class);
    }

    @Override
    public Mono<ServerResponse> deletePostById(ServerRequest request) {
        Long idPost = Long.parseLong(request.queryParam("id").get());
        return postRepositoryPort.deletePost(idPost)
                .flatMap(post -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(post));
    }
}
