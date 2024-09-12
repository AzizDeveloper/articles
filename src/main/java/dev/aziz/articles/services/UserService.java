package dev.aziz.articles.services;

import dev.aziz.articles.dtos.CredentialsDto;
import dev.aziz.articles.dtos.SignUpDto;
import dev.aziz.articles.dtos.UserDto;
import dev.aziz.articles.entities.Role;
import dev.aziz.articles.entities.User;
import dev.aziz.articles.exceptions.AppException;
import dev.aziz.articles.mappers.UserMapper;
import dev.aziz.articles.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        return userMapper.toUserDto(user);
    }

    public UserDto login(CredentialsDto credentialsDto) {
        log.info("User {} logged in.", credentialsDto.getEmail());
        User user = userRepository.findByEmail(credentialsDto.getEmail())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), user.getPassword())) {
            return userMapper.toUserDto(user);
        }

        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public UserDto register(SignUpDto signUpDto) {
        Optional<User> optionalUser = userRepository.findByEmail(signUpDto.getEmail());

        if (optionalUser.isPresent()) {
            throw new AppException("Login already exists", HttpStatus.BAD_REQUEST);
        }

        Role.valueOf(signUpDto.getRole().toUpperCase());
        Role[] roles = Role.values();
        Role foundRole = Arrays.stream(roles)
                .filter(r -> r.name().equals(signUpDto.getRole())).findFirst()
                .orElseThrow(() -> new AppException("Role not found.", HttpStatus.NOT_FOUND));
        User user = userMapper.signUpToUser(signUpDto);
        user.setRole(foundRole);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(signUpDto.getPassword())));
        User savedUser = userRepository.save(user);
        log.info("User by login {} has been registered.", savedUser.getEmail());
        return userMapper.toUserDto(user);
    }
}
