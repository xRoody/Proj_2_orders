package com.example.orders.configs.securityFilters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.orders.configs.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class AuthenticationHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().equals(SecurityConfig.LOGIN_PATH) || request.getServletPath().equals(SecurityConfig.TOKEN_REFRESH_PATH)){
            filterChain.doFilter(request, response);
        }else {
            String authHeader=request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authHeader!=null && authHeader.startsWith("Bearer ")){
                try{
                    String token=authHeader.substring("Bearer ".length());
                    JWTVerifier jwtVerifier= JWT.require(AuthenticationFilter.algorithm).build();
                    DecodedJWT decodedJWT=jwtVerifier.verify(token);
                    String username=decodedJWT.getSubject();
                    Collection<GrantedAuthority> authorities=decodedJWT.getClaim("roles").asList(String.class).stream().map(x->new SimpleGrantedAuthority(x)).collect(Collectors.toList());
                    UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(username,null,authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request,response);
                }catch (Exception e){
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    Map<String, String> errors=new HashMap<>();
                    errors.put("Error", e.getClass().getSimpleName());
                    errors.put("Reason", "Something wrong with JWT access token"); //e.getMessage() ?
                    new ObjectMapper().writeValue(response.getWriter(),errors);
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                }
            }else{
                filterChain.doFilter(request,response);
            }
        }
    }
}
