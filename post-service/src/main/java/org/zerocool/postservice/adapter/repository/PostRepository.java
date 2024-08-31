package org.zerocool.postservice.adapter.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import org.zerocool.postservice.adapter.entity.Post;
@Repository
public interface PostRepository extends ReactiveMongoRepository<Post, Long> {
}
