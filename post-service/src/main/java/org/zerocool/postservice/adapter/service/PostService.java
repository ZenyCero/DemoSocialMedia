package org.zerocool.postservice.adapter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.zerocool.postservice.adapter.entity.Post;
import org.zerocool.postservice.adapter.port.out.MediaRepositoryPort;
import org.zerocool.postservice.adapter.port.out.PostRepositoryPort;
import org.zerocool.postservice.adapter.repository.MediaRepository;
import org.zerocool.postservice.adapter.repository.PostRepository;
import org.zerocool.postservice.common.mapper.MapperPost;
import org.zerocool.postservice.domain.dto.PostDTO;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PostService implements PostRepositoryPort{

    private final PostRepository postRepository;
    private final MediaService mediaService;
    @Override
    public Mono<String> save(PostDTO postDTO, MultipartFile file) {
        Post post = MapperPost.postDTOToEntity(postDTO);
        if (file != null) {
            return postRepository.save(post)
                    .flatMap(postSave -> mediaService.save(post.getId(), file));
        } else {
            return postRepository.save(post)
                    .thenReturn("post Saved");
        }
    }
    @Override
    public Mono<String> delete(int id) {
        return null;
    }
}
