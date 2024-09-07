package org.zerocool.commetservice.controller.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.zerocool.commetservice.controller.handler.CommetHandler;

@Configuration
public class CommetRouter {
    private static final String PATH = "/commet";

    @Bean
    RouterFunction<ServerResponse> routerCommet(CommetHandler handler) {
        return RouterFunctions.route()
                .GET(PATH + "/{id}", handler::getCommets)
                .POST(PATH, handler::saveCommet)
                .build();
    }
}
