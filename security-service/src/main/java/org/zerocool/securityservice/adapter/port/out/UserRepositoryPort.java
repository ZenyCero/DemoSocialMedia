package org.zerocool.securityservice.adapter.port.out;

import org.zerocool.securityservice.adapter.dto.LoginDTO;
import org.zerocool.securityservice.domain.dto.TokenDTO;
import org.zerocool.securityservice.adapter.dto.UserDTO;
import reactor.core.publisher.Mono;

public interface UserRepositoryPort {
    Mono<String> registry(UserDTO userDTO);
    Mono<TokenDTO> login(LoginDTO userDTO);

}
