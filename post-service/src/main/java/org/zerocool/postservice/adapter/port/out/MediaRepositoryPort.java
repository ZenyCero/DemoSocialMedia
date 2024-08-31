package org.zerocool.postservice.adapter.port.out;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.web.multipart.MultipartFile;
import org.zerocool.postservice.domain.dto.MediaDTO;
import org.zerocool.postservice.domain.dto.PostDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MediaRepositoryPort {
    Mono<String> save(Long postId, MultipartFile file);
    Mono<Flux<DataBuffer>> get(int id);
    Mono<String> delete(int id);
}
