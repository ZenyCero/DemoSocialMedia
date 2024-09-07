package org.zerocool.commetservice.controller.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.zerocool.commetservice.domain.dto.CommetDTO;
import org.zerocool.commetservice.domain.port.in.CommetPort;
import org.zerocool.commetservice.domain.port.out.CommetRepositoryPort;
import org.zerocool.sharedlibrary.exception.CustomException;
import org.zerocool.sharedlibrary.validation.ObjectValidator;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CommetHandler implements CommetPort {

    private final CommetRepositoryPort commetRepository;
    private final ObjectValidator objectValidator;

    @Override
    public Mono<ServerResponse> getCommets(ServerRequest serverRequest) {
        Long idPost = Long.parseLong(serverRequest.pathVariable("id"));
        return commetRepository.getAllByIdPost(idPost)
                .collectList()
                .flatMap(commet -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(commet, CommetDTO.class));
    }

    @Override
    public Mono<ServerResponse> saveCommet(ServerRequest serverRequest) {
        Mono<CommetDTO> commetDTO = serverRequest.bodyToMono(CommetDTO.class)
                .doOnNext(objectValidator::validate);
        return commetDTO
                .flatMap(dto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(commetRepository.save(dto), CommetDTO.class));
    }
}
