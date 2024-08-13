package org.zerocool.securityservice.controller.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.zerocool.securityservice.controller.handle.AuthHandler;

@Configuration
public class AuthRouter {
    private static final String PATH = "auth/";

    @Bean
    RouterFunction<ServerResponse> routerAuth(AuthHandler handler){
        return RouterFunctions.route()
                .POST(PATH + "login", handler::login)
                .POST(PATH + "registry", handler::registry)
                .build();
    }
}
