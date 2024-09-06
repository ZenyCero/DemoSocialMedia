package org.zerocool.postservice.adapter.port.out;

import org.springframework.web.multipart.MultipartFile;
import org.zerocool.postservice.adapter.entity.Post;
import org.zerocool.postservice.domain.dto.PostDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PostRepositoryPort {
    Mono<String> savePost(PostDTO postDTO);
    Mono<String> deletePost(Long idPost);
    Flux<Post> getPost(Long idUser);
}
