package org.zerocool.commetservice.domain.port.out;

import org.zerocool.commetservice.adapter.entity.Commet;
import org.zerocool.commetservice.domain.dto.CommetDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CommetRepositoryPort {
    Mono<Commet> save(CommetDTO commetDTO);
    Flux<Commet> getAllByIdPost(Long idPost);
}
