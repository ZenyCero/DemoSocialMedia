package org.zerocool.postservice.adapter.port.out;

import org.springframework.web.multipart.MultipartFile;
import org.zerocool.postservice.domain.dto.MediaDTO;
import org.zerocool.postservice.domain.dto.PostDTO;
import reactor.core.publisher.Mono;

public interface MediaRepositoryPort {
    Mono<String> save(Long postId, MultipartFile file);
    Mono<String> get(int id);
    Mono<String> delete(int id);
}
