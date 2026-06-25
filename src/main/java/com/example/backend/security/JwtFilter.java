package com.example.backend.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Validates the {@code Authorization: Bearer <token>} header on each request.
 * On a valid token it exposes the email as the {@code userEmail} request
 * attribute and populates the Spring Security context so downstream
 * authorization rules pass. Auth endpoints ({@code /api/auth/**}) are skipped.
 *
 * <p>Registered explicitly in {@code SecurityConfig} (not {@code @Component}) so
 * the servlet container does not also auto-register it as a plain filter.
 */
public class JwtFilter extends OncePerRequestFilter {

	private final JWTUtil jwtUtil;

	public JwtFilter(JWTUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return request.getServletPath().startsWith("/api/auth/");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		String header = request.getHeader("Authorization");
		if (header != null && header.startsWith("Bearer ")) {
			String token = header.substring(7);
			if (jwtUtil.isValid(token)) {
				String email = jwtUtil.extractEmail(token);
				request.setAttribute("userEmail", email);
				var authentication = new UsernamePasswordAuthenticationToken(email, null, List.of());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		chain.doFilter(request, response);
	}
}
