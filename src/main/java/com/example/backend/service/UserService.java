package com.example.backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.entity.User;
import com.example.backend.entity.UserProfile;
import com.example.backend.repository.UserProfileRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.JWTUtil;

/**
 * Registration and login. Passwords are hashed with BCrypt on register and
 * verified with the encoder's constant-time {@code matches} on login -- never
 * stored or compared as plaintext.
 */
@Service
public class UserService {

	private final UserRepository userRepository;
	private final UserProfileRepository userProfileRepository;
	private final PasswordEncoder passwordEncoder;
	private final JWTUtil jwtUtil;

	public UserService(UserRepository userRepository,
			UserProfileRepository userProfileRepository,
			PasswordEncoder passwordEncoder,
			JWTUtil jwtUtil) {
		this.userRepository = userRepository;
		this.userProfileRepository = userProfileRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
	}

	/** Rejects a signup whose email or username is already taken. */
	public void validateRegister(String email, String userName) {
		if (userRepository.existsByEmail(email)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
		}
		if (userProfileRepository.existsByUserName(userName)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken");
		}
	}

	/**
	 * Creates the auth identity ({@code users}) and game profile
	 * ({@code user_profiles}) in one transaction.
	 */
	@Transactional
	public void registerUser(String email, String rawPassword, String userName) {
		validateRegister(email, userName);
		userRepository.save(new User(email, passwordEncoder.encode(rawPassword)));
		userProfileRepository.save(new UserProfile(userName, email));
	}

	/** Returns the profile's username for an email, or null if no profile exists. */
	public String getUserName(String email) {
		return userProfileRepository.findByEmail(email)
				.map(UserProfile::getUserName)
				.orElse(null);
	}

	/** Verifies credentials and returns a signed JWT on success. */
	public String loginUser(String email, String rawPassword) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
		if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
		}
		return jwtUtil.generateToken(email);
	}
}
