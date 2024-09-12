package org.zerocool.postservice.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.zerocool.postservice.adapter.entity.Post;
import org.zerocool.postservice.adapter.port.out.PostRepositoryPort;
import org.zerocool.postservice.adapter.repository.PostRepository;
import org.zerocool.postservice.domain.dto.PostDTO;
import org.zerocool.sharedlibrary.exception.CustomException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.zerocool.sharedlibrary.mapper.Mapper;
import reactor.core.scheduler.Schedulers;

@Service
@Slf4j
public class PostService implements PostRepositoryPort{

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    @Override
    public Mono<String> savePost(PostDTO postDTO) {
        Post post = Mapper.convertToOtherClass(postDTO, Post.class);
        return postRepository.save(post)
                .thenReturn("Post saved successfully");
    }

    @Override
    public Mono<String> deletePostByIdPost(String idPost) {
        Mono<Boolean> exitsPost = postRepository.existsById(idPost);
        return exitsPost.flatMap(exits -> exits
                        ? postRepository.deleteById(idPost).thenReturn("Post deleted successfully")
                        : Mono.error(new CustomException("Post not found", HttpStatus.NOT_FOUND)));
    }

    @Override
    public Flux<PostDTO> getPostsByIdUserPageable(String idUser, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Flux<Post> postFlux = postRepository.findAllByIdUserOrderByUpdatedDesc(idUser, pageable);
        Mono<Long> countMono = postRepository.countAllByIdUser(idUser);

        return postFlux
                .collectList()
                .zipWith(countMono)
                .map(tuple ->
                        new SliceImpl<>
                        (tuple.getT1(), pageable, tuple.getT2() >
                                (pageable.getPageNumber() + 1) * pageable.getPageSize()))
                .flatMapMany(slice -> Flux.fromIterable(slice.getContent())) // Extract posts from Slice
                .flatMap(post -> Mono.fromCallable(() -> Mapper.convertToOtherClass(post, PostDTO.class))) // Convert each Post to PostDTO
                .subscribeOn(Schedulers.boundedElastic());
    }
}
