package com.example.backend.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Signs and validates stateless HS256 JWTs. The token subject is the user's
 * email; tokens expire 5 hours after issue. The signing key comes from the
 * {@code JWT_KEY} env var (Base64-encoded, must decode to >= 32 bytes).
 */
@Component
public class JWTUtil {

	private static final long EXPIRATION_MS = 5L * 60 * 60 * 1000; // 5 hours

	private final SecretKey key;

	public JWTUtil(@Value("${JWT_KEY}") String base64Secret) {
		this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret));
	}

	public String generateToken(String email) {
		Date now = new Date();
		return Jwts.builder()
				.subject(email)
				.issuedAt(now)
				.expiration(new Date(now.getTime() + EXPIRATION_MS))
				.signWith(key)
				.compact();
	}

	public String extractEmail(String token) {
		return parse(token).getSubject();
	}

	public boolean isValid(String token) {
		try {
			parse(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	private Claims parse(String token) {
		return Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
}
