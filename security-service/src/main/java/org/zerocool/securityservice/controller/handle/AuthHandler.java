package org.zerocool.securityservice.controller.handle;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.zerocool.securityservice.adapter.dto.LoginDTO;
import org.zerocool.securityservice.adapter.dto.UserDTO;
import org.zerocool.securityservice.adapter.port.in.UserPort;
import org.zerocool.securityservice.adapter.port.out.UserRepositoryPort;
import org.zerocool.securityservice.common.validation.ObjectValidator;
import org.zerocool.securityservice.domain.dto.TokenDTO;
import reactor.core.publisher.Mono;

@Component
public class AuthHandler implements UserPort {
    private final UserRepositoryPort userRepositoryPort;
    private final ObjectValidator objectValidator;

    public AuthHandler(UserRepositoryPort userRepositoryPort, ObjectValidator objectValidator) {
        this.userRepositoryPort = userRepositoryPort;
        this.objectValidator = objectValidator;
    }

    @Override
    public Mono<ServerResponse> login(ServerRequest request) {
        Mono<LoginDTO> login = request.bodyToMono(LoginDTO.class).doOnNext(objectValidator::validate);
        return login
                .flatMap(dto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(userRepositoryPort.login(dto), TokenDTO.class)
                );
    }

    @Override
    public Mono<ServerResponse> registry(ServerRequest request) {
        Mono<UserDTO> user = request.bodyToMono(UserDTO.class).doOnNext(objectValidator::validate);
        return user
                .flatMap(dto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(userRepositoryPort.registry(dto), String.class)
                );
    }
}
