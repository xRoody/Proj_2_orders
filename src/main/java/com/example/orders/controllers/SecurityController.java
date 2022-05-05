package com.example.orders.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.orders.configs.securityFilters.AuthenticationFilter;
import com.example.orders.entityes.Other;
import com.example.orders.exceptions.BodyExceptionWrapper;
import com.example.orders.repositories.OtherRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.orders.configs.securityFilters.AuthenticationFilter.algorithm;

@RestController
@RequestMapping("/security")
public class SecurityController {
    private OtherRepo otherRepo;

    @GetMapping("/refresh")
    public ResponseEntity<Object> refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring("Bearer ".length());
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(token);
                String username = decodedJWT.getSubject();
                Other other = otherRepo.getById(username);
                String accessToken = JWT.create()
                        .withSubject(username)
                        .withExpiresAt(new Date(System.currentTimeMillis() + 20 * 60 * 1000))
                        .withClaim("roles", Stream.of(other.getRole()).collect(Collectors.toList()))
                        .withIssuer(request.getRequestURL().toString())
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access", accessToken);
                tokens.put("refresh", token);
                return ResponseEntity.ok(tokens);
            } catch (Exception e) {
                return new ResponseEntity<>(new BodyExceptionWrapper("403", "Something wrong with JWT refresh token"), HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(new BodyExceptionWrapper("403", "Authorization is wrong or absent"), HttpStatus.FORBIDDEN);
    }
}
