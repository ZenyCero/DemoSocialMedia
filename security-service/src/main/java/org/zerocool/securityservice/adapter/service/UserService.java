package org.zerocool.securityservice.adapter.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerocool.securityservice.adapter.dto.LoginDTO;
import org.zerocool.securityservice.adapter.dto.UserDTO;
import org.zerocool.securityservice.adapter.port.out.UserRepositoryPort;
import org.zerocool.securityservice.adapter.repository.UserRepository;
import org.zerocool.securityservice.common.exception.CustomException;
import org.zerocool.securityservice.domain.dto.TokenDTO;
import org.zerocool.securityservice.domain.emuns.Roles;
import org.zerocool.securityservice.domain.entity.Users;
import org.zerocool.securityservice.domain.jwt.JwtProvider;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserRepositoryPort {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    public Mono<String> registry(UserDTO userDTO) {
        Users user = Users.builder()
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .roles(Roles.ROLE_USER.name())
                .enabled(true)
                .build();

        Mono<Boolean> existsUser = userRepository
                .findByUsernameOrEmail(userDTO.getUsername(), userDTO.getUsername()).hasElement();

        return existsUser
                .flatMap(exists -> Boolean.TRUE.equals(exists)
                        ? Mono.error(new CustomException("User already exits", HttpStatus.BAD_REQUEST))
                        : userRepository.save(user))
                .thenReturn("User saved success");
    }

    @Override
    public Mono<TokenDTO> login(LoginDTO loginDTO) {
        return userRepository
                .findByUsernameOrEmail(loginDTO.getUsername(), loginDTO.getUsername())
                .filter(users -> passwordEncoder.matches(loginDTO.getPassword(),users.getPassword()))
                .map(jwtProvider::createToken)
                .switchIfEmpty(Mono.error(new CustomException("User not found", HttpStatus.BAD_REQUEST)));
    }

    @Override
    public Mono<TokenDTO> refreshToken(TokenDTO tokenDTO) {

        Claims claimsRefreshToken = jwtProvider.getClaims(tokenDTO.getTokenRefresh());
        Claims claimsToken = jwtProvider.getClaims(tokenDTO.getToken());

        if(claimsRefreshToken.getExpiration().before(new Date())){
            return Mono.error(new CustomException("Refresh Token expired", HttpStatus.UNAUTHORIZED));
        }

        List<SimpleGrantedAuthority> roles = jwtProvider.getRoles(claimsToken);

        UserDetails userDetails = User.builder()
                .username(claimsToken.getSubject())
                .authorities(roles)
                .password("")
                .build();

        return Mono.just(jwtProvider.createToken(userDetails));
    }
}
