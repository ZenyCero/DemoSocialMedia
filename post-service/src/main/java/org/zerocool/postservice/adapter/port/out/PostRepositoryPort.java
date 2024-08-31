package org.zerocool.postservice.adapter.port.out;

import org.springframework.web.multipart.MultipartFile;
import org.zerocool.postservice.adapter.entity.Post;
import org.zerocool.postservice.domain.dto.PostDTO;
import reactor.core.publisher.Mono;

public interface PostRepositoryPort {
    Mono<String> save(PostDTO postDTO, MultipartFile file);
    Mono<String> delete(int id);
}
