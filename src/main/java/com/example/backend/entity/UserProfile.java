package com.example.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Game profile, stored in the {@code user_profiles} table. Linked to {@link User}
 * by email (no JPA relation, mirroring the original two-table design). New players
 * start with 1000 chips.
 */
@Entity
@Table(name = "user_profiles")
public class UserProfile {

	private static final int STARTING_CHIPS = 1000;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String userName;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private Integer chips;

	protected UserProfile() {
		// for JPA
	}

	public UserProfile(String userName, String email) {
		this.userName = userName;
		this.email = email;
		this.chips = STARTING_CHIPS;
	}

	public Long getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getChips() {
		return chips;
	}

	public void setChips(Integer chips) {
		this.chips = chips;
	}
}
