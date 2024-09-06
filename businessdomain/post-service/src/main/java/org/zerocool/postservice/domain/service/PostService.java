package org.zerocool.postservice.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.zerocool.postservice.adapter.entity.CounterPost;
import org.zerocool.postservice.adapter.entity.Post;
import org.zerocool.postservice.adapter.port.out.PostRepositoryPort;
import org.zerocool.postservice.adapter.repository.PostRepository;
import org.zerocool.postservice.domain.dto.PostDTO;
import org.zerocool.sharedlibrary.exception.CustomException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.zerocool.sharedlibrary.mapper.Mapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService implements PostRepositoryPort{

    private final PostRepository postRepository;
    private final CounterPostService counterPostService;

    @Override
    public Mono<String> savePost(PostDTO postDTO) {
        Post post = Mapper.convertToOtherClass(postDTO, Post.class);
        log.info("Post saved: {}", post);
        return counterPostService.getNextSequenceValue("idPost")
                .flatMap(counter -> {
                    post.setId(counter);
                    return postRepository.save(post)
                            .doOnSuccess(savedPost -> log.info("Post saved: {}", savedPost))
                            .thenReturn("Post saved successfully");
                });
    }

    @Override
    public Mono<String> deletePost(Long idPost) {
        Mono<Boolean> exitsPost = postRepository.existsById(idPost);
        return exitsPost.flatMap(exits -> exits
                        ? postRepository.deleteById(idPost).thenReturn("Post deleted successfully")
                        : Mono.error(new CustomException("Post not found", HttpStatus.NOT_FOUND)));
    }

    @Override
    public Flux<Post> getPost(Long idUser) {
        return postRepository.findAllByIdUserOrderByUpdatedAsc(idUser)
                .switchIfEmpty(Mono.error(new CustomException("Post not found", HttpStatus.NOT_FOUND)));
    }
}
