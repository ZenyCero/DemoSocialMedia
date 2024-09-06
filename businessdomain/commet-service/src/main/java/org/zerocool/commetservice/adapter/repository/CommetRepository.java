package org.zerocool.commetservice.adapter.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import org.zerocool.commetservice.adapter.entity.Commet;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CommetRepository extends ReactiveMongoRepository<Commet, String> {
    Flux<Commet> getAllByIdPost(Long idPost);
    Mono<Boolean> existsByIdPost(Long idPost);
}
