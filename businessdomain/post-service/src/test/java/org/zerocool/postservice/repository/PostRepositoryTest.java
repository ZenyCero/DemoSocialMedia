package org.zerocool.postservice.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.zerocool.postservice.adapter.entity.Post;
import org.zerocool.postservice.adapter.repository.PostRepository;
import org.zerocool.postservice.domain.dto.PostDTO;
import reactor.test.StepVerifier;
import org.zerocool.sharedlibrary.mapper.Mapper;

import java.time.LocalDate;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @BeforeAll
    public void insertarDatos(){
        LocalDate localDate = LocalDate.of(2024, 9, 11);
        PostDTO postDTO = PostDTO.builder()
                .idUser("2")
                .content("Elias now")
                .created(localDate)
                .updated(localDate)
                .build();
        Post post = Mapper.convertToOtherClass(postDTO, Post.class);

        StepVerifier.create(postRepository.save(post))
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @Order(1)
    public void getByIdPost(){
        Pageable pageable = PageRequest.of(0, 1);
        StepVerifier.create(postRepository.findAllByIdUserOrderByUpdatedDesc("1",pageable))
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @Order(2)
    public void deletePostById(){
        StepVerifier.create(postRepository.deleteById("1"))
                .expectSubscription()
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    @Order(3)
    public void deleteAll(){
        StepVerifier.create(postRepository.deleteAll())
                .expectSubscription()
                .expectNextCount(0)
                .verifyComplete();
    }
}
