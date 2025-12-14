package com.hangrong.backend.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        System.out.println("JwtFilter checking path: " + path + " method: " + method); // Debug log

        // ✅ BỎ QUA OPTIONS request (CORS preflight)
        if ("OPTIONS".equalsIgnoreCase(method)) {
            System.out.println("Bypassing JWT filter for OPTIONS request");
            filterChain.doFilter(request, response);
            return;
        }

        // ✅ CHỈ BỎ QUA login, register và public endpoints
        if (path.equals("/auth/login") ||
                path.equals("/auth/register") ||
                path.equals("/hello") ||
                path.equals("/") ||
                path.startsWith("/error") ||
                path.endsWith("/favicon.ico")) {
            System.out.println("Bypassing JWT filter for: " + path);
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Missing or invalid Authorization header\"}");
            return;
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = JwtUtil.parseToken(token);
            // Có thể set user info vào request attribute nếu cần
            request.setAttribute("userId", claims.get("userId"));
            request.setAttribute("username", claims.get("username"));
            System.out.println("JWT token valid for user: " + claims.get("username"));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Invalid or expired token: " + e.getMessage() + "\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }

}
