package dev.aziz.articles.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.aziz.articles.dtos.UserDto;
import dev.aziz.articles.entities.Role;
import dev.aziz.articles.exceptions.AppException;
import dev.aziz.articles.services.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
public class UserAuthProvider {

    @Value("${jwt.secret:secret-value}")
    private String secretKey;

    private final UserService userService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String email) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 3_600_000);
        UserDto userDto = userService.findByEmail(email);
        String role = userDto.getRole().toString();
        return JWT.create()
                .withIssuer(email)
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withClaim("role", role)
                .sign(Algorithm.HMAC256(secretKey));
    }

    public Authentication validateToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey)).build();

        DecodedJWT decodedJWT = verifier.verify(token);

        UserDto user = userService.findByEmail(decodedJWT.getIssuer());

        String roleName = decodedJWT.getClaim("role").asString();

        Role.valueOf(roleName.toUpperCase());
        Role[] roles = Role.values();
        Role foundRole = Arrays.stream(roles)
                .filter(r -> r.name().equals(roleName)).findFirst()
                .orElseThrow(() -> new AppException("Role not found.", HttpStatus.NOT_FOUND));

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(foundRole.name()));

        return new UsernamePasswordAuthenticationToken(user, null, authorities);
    }

    public Long getUserId(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey)).build();

        DecodedJWT decodedJWT = verifier.verify(token);

        UserDto user = userService.findByEmail(decodedJWT.getIssuer());
        return user.getId();
    }

}













