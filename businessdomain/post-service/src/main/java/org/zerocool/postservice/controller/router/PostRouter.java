package org.zerocool.postservice.controller.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.zerocool.postservice.controller.handler.PostHandler;

@Configuration
public class PostRouter {
    private static final String PATH = "/post";
    @Bean
    RouterFunction<ServerResponse> routerPost(PostHandler handler){
        return RouterFunctions.route()
                .POST(PATH, handler::savePost)
                .GET(PATH + "/{id}", handler::getPostsByIdUserPageable)
                .DELETE(PATH + "/{id}", handler::deletePostByIdPost)
                .build();
    }
}
