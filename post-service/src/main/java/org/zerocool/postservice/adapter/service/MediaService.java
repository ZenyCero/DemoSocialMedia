package org.zerocool.postservice.adapter.service;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.reactivestreams.Publisher;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.zerocool.postservice.adapter.entity.Media;
import org.zerocool.postservice.adapter.entity.Post;
import org.zerocool.postservice.adapter.port.out.MediaRepositoryPort;
import org.zerocool.postservice.adapter.port.out.PostRepositoryPort;
import org.zerocool.postservice.adapter.repository.MediaRepository;
import org.zerocool.postservice.common.exception.CustomException;
import org.zerocool.postservice.common.mapper.MapperPost;
import org.zerocool.postservice.domain.dto.MediaDTO;
import org.zerocool.postservice.domain.dto.PostDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class MediaService implements MediaRepositoryPort {

    private final ReactiveGridFsTemplate reactiveGridFsTemplate;

    private MediaRepository mediaRepository;

    @Override
    public Mono<String> save(Long postId, MultipartFile file) {

        return saveFile(file)
                .flatMap(fileId -> {
                    Media media = new Media();
                    media.setPostId(postId);
                    media.setMediaType(file.getContentType());
                    media.setFileId(fileId);
                    media.setFileName(file.getOriginalFilename());
                    media.setFileSize(file.getSize());

                    return mediaRepository.save(media)
                            .thenReturn("File saved Correctly");
                });
    }

    public Mono<String> saveFile(MultipartFile file){
        return DataBufferUtils
                .read(file.getResource(), new DefaultDataBufferFactory(), 4096) // Read file into DataBuffer
                .collectList() // Collect DataBuffers into a list
                .flatMapMany(Flux::fromIterable) // Convert List<DataBuffer> to Flux<DataBuffer>
                .collectList() // Collect Flux<DataBuffer> into a List
                .flatMap(dataBuffers -> reactiveGridFsTemplate.store(
                        Flux.fromIterable(dataBuffers), // Convert List<DataBuffer> to Flux<DataBuffer>
                        file.getOriginalFilename(), // Filename
                        file.getContentType() // Content type
                ))
                .map(ObjectId::toHexString);
    }
    @Override
    public Mono<Flux<DataBuffer>> get(int id) {
        return getFile(String.valueOf(id));
    }
    public Mono<Flux<DataBuffer>> getFile(String fileId) {
        return reactiveGridFsTemplate
                .findOne(new Query(Criteria.where("_id").is(fileId)))
                .flatMap(gridFSFile -> reactiveGridFsTemplate
                        .getResource(gridFSFile)
                        .flatMap(gridFsResource -> DataBufferUtils.read(
                                (Resource) gridFsResource.getDownloadStream(),
                                new DefaultDataBufferFactory(),
                                4096
                            ).collectList()
                                .map(Flux::fromIterable)
                )).switchIfEmpty(Mono.error(new CustomException("Not found file", HttpStatus.BAD_REQUEST)));
    }
    @Override
    public Mono<String> delete(int id) {
        return null;
    }
}
