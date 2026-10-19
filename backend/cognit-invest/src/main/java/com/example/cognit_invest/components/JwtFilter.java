package com.example.cognit_invest.components;

import com.example.cognit_invest.services.internal.JwtService;
import com.example.cognit_invest.services.internal.Logger;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private String tokenType;

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = extractTokenFromCookies(request);

        if (token != null && jwtService.isTokenValid(token,tokenType)) {
//        System.out.println(">>> JwtFilter | path=" + request.getRequestURI()
//                + " | token=" + (token != null ? "presente" : "NULL"));


//            System.out.println(">>> JwtFilter | autenticado como: " + userId + " role=" + role);

            // Principal carrega id + name para uso nos controllers via @AuthenticationPrincipal
            SessionUser principal = jwtService.getPrincipal(token,tokenType);

            List<SimpleGrantedAuthority> authorities = principal.name() != null
                    ? List.of(new SimpleGrantedAuthority("ROLE_" + principal.name()))
                    : List.of();

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(principal, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);

//            Logger.debug("Tá tudo certo");
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if ("AUTH_TOKEN".equals(cookie.getName())) {
                tokenType = "USER";
                return cookie.getValue();
            }

            if("ADM_TOKEN".equals(cookie.getName())){
                Logger.debug("Token de adminstrador encontrado");
                tokenType = "ADM";
                return cookie.getValue();
            }
        }
        return null;
    }
}