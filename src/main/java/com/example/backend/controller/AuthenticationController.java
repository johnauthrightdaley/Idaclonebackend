package com.example.backend.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.LoginRequest;
import com.example.backend.dto.RegisterRequest;
import com.example.backend.service.UserService;

/**
 * Auth endpoints. Both are public (permitted in {@code SecurityConfig}); the
 * issued JWT gates every other {@code /api} route.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

	private final UserService userService;

	public AuthenticationController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/register")
	public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequest request) {
		userService.registerUser(request.email(), request.password(), request.userName());
		return ResponseEntity.ok(Map.of("message", "User + profile registered successfully!"));
	}

	@PostMapping("/login")
	public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
		String token = userService.loginUser(request.email(), request.password());
		return ResponseEntity.ok(Map.of("token", token));
	}
}
