//disabled due to jwt-microservice authentication

//package com.example.security.security;
//
//import com.example.security.SecurityApplication;
//import com.example.security.utils.JwtTokenProvider;
//import io.jsonwebtoken.ExpiredJwtException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@RequiredArgsConstructor
//@Component
//public class JwtPerRequestFilter extends OncePerRequestFilter {
//
//    private final JwtTokenProvider jwtTokenProvider;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String header = request.getHeader("Authorization");
//        String token = null;
//        String username = null;
//
//        if (header != null && header.startsWith("Bearer ")) {
//            token = header.substring(7);
//
//            try {
//                username = jwtTokenProvider.getUsername(token);
//            } catch (ExpiredJwtException e) {
//                throw new RuntimeException("jwt token was expired");
//            }
//        }
//
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                    username,
//                    null,
//                    jwtTokenProvider.getRoles(token)
//                            .stream()
//                            .map(SimpleGrantedAuthority::new)
//                            .toList());
//
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//
//        doFilter(request, response, filterChain);
//    }
//}
