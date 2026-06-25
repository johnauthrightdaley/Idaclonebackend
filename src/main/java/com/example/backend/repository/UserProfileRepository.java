package com.example.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.entity.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

	boolean existsByUserName(String userName);

	Optional<UserProfile> findByEmail(String email);
}
