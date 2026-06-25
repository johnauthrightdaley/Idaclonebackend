package com.example.backend.controller;

import java.time.Instant;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.service.UserService;

@RestController
@RequestMapping("/api")
public class HelloController {

	private final UserService userService;

	public HelloController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/hello")
	public Map<String, Object> hello() {
		return Map.of(
				"message", "Hello from Spring Boot!",
				"timestamp", Instant.now().toString());
	}

	/**
	 * Protected: requires a valid Bearer token. The {@code userEmail} attribute
	 * is set by {@code JwtFilter} after it validates the JWT. Returns the email
	 * and the profile username so the frontend can greet the user.
	 */
	@GetMapping("/me")
	public Map<String, Object> me(@RequestAttribute("userEmail") String userEmail) {
		String userName = userService.getUserName(userEmail);
		return Map.of("email", userEmail, "userName", userName == null ? "" : userName);
	}
}
