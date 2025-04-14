package com.castores.api.configs;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.castores.api.utils.JwtUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Log4j2
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    private static final List<String> PUBLIC_PATHS = Arrays.asList("/api/auth/","/test-key","/swagger-ui","/v3/api-docs");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestPath = request.getRequestURI();
        log.info("Requested path: {}", requestPath);

        response.setHeader("Access-Control-Allow-origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Headers", "authorization, content-type");
        response.setHeader("Access-Control-Allow-Methods","GET, POST, PUT, DELETE, OPTIONS, PATCH");

        if (PUBLIC_PATHS.stream().anyMatch(path -> requestPath.startsWith(path))) {
            filterChain.doFilter(request, response);
            return;
        }

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        try {
            String token = jwtUtil.extractToken(request);
            log.info(token);

            if (token == null) {
                log.warn("No token provided for path: {}", requestPath);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("No token provided");
                return;
            }

            if (!jwtUtil.validateToken(token)) {
                log.warn("Invalid or expired token");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or expired token");
                return;
            }

            String username = jwtUtil.getUsernameFromToken(token);
            List<SimpleGrantedAuthority> authorities = jwtUtil.getAuthoritiesFromToken(token);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username,null,authorities);
            SecurityContextHolder.getContext().setAuthentication(authToken);


            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("JwtAuthenticationFilter error: ", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authentication error: " + e.getMessage());
        }
    }
}