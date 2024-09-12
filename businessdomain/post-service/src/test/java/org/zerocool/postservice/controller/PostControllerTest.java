package org.zerocool.postservice.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.zerocool.postservice.adapter.entity.Post;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    private String message;

    private Post post = new Post("21","2","contenido", LocalDate.now(),LocalDate.now());


    @Test
    @Order(1)
    void guardarPost(){
        Flux<String> postGuardado = webTestClient.post()
                .uri("/post")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(post))
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class).getResponseBody().log();

        postGuardado.next().subscribe(message -> this.message = message);

        Assertions.assertEquals("Post saved successfully", message);
    }

    @Test
    @Order(2)
    void ObtenerPost(){
        Flux<Post> postObtenido = webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/post/{id}") // Ruta base
                    .queryParam("page", "0")
                    .queryParam("size", "1")
                .build(post.getIdUser()))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Post.class).getResponseBody().log();

        StepVerifier.create(postObtenido)
                .expectSubscription()
                .expectNextCount(1)
                .expectComplete()
                .verify();
    }

    @Test
    @Order(3)
    void EliminarPost(){
        Flux<String> postEliminado = webTestClient.delete()
                .uri("/post/{id}",post.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class).getResponseBody().log();

        StepVerifier.create(postEliminado)
                .expectSubscription()
                .expectNext("Post deleted successfully")
                .verifyComplete();
    }
}
