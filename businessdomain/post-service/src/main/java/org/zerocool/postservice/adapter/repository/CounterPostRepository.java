package org.zerocool.postservice.adapter.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import org.zerocool.postservice.adapter.entity.CounterPost;

@Repository
public interface CounterPostRepository extends ReactiveMongoRepository<CounterPost, String> {
}
