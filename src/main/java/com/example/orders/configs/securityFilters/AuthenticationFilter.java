package com.example.orders.configs.securityFilters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.orders.DTOs.LoginFormDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@PropertySource("classpath:application.properties")
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private final static ObjectMapper objectMapper=new ObjectMapper();
    public static final Algorithm algorithm=Algorithm.HMAC256("9t7%C[XZpn=m;xP4".getBytes());


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username;
        String password;
        try{
            LoginFormDTO loginFormDTO=objectMapper.readValue(request.getReader(),LoginFormDTO.class);
            username=loginFormDTO.getUsername();
            password=loginFormDTO.getPassword();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user=(User)authResult.getPrincipal();
        String accessToken= JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+20*60*1000))
                .withClaim("roles",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
        String refreshToken=JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+12*60*60*1000))
                .withClaim("roles",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Map<String,String> tokens=new HashMap<>();
        tokens.put("access", accessToken);
        tokens.put("refresh", refreshToken);
        objectMapper.writeValue(response.getWriter(), tokens);
    }
}
