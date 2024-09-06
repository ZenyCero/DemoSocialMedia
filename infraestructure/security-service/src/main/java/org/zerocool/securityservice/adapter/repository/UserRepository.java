package org.zerocool.securityservice.adapter.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.zerocool.securityservice.domain.entity.Users;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<Users, Long> {
    Mono<Users> findByUsernameOrEmail(String username, String email);
}
