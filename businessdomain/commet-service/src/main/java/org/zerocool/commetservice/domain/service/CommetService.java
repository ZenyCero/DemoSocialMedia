package org.zerocool.commetservice.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.zerocool.commetservice.adapter.entity.Commet;
import org.zerocool.commetservice.adapter.repository.CommetRepository;
import org.zerocool.commetservice.domain.dto.CommetDTO;
import org.zerocool.commetservice.domain.port.out.CommetRepositoryPort;
import org.zerocool.sharedlibrary.exception.CustomException;
import org.zerocool.sharedlibrary.mapper.Mapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CommetService implements CommetRepositoryPort {

    private final CommetRepository commetRepository;

    @Override
    public Mono<Commet> save(CommetDTO commetDTO) {
        Commet commet = Mapper.convertToOtherClass(commetDTO, Commet.class);
        return commetRepository.save(commet);
    }

    @Override
    public Flux<Commet> getAllByIdPost(Long idPost) {
        return commetRepository.existsByIdPost(idPost)
                .flatMapMany(exists -> exists
                        ? commetRepository.getAllByIdPost(idPost)
                        : Flux.error(new CustomException("No Comments Found", HttpStatus.BAD_REQUEST))
                );
    }
}
