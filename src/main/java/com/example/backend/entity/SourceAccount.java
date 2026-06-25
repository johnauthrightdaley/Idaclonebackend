package com.example.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * An account discovered on a {@link Source}, shown in the App Page "Accounts"
 * table. Linked to its source by {@code sourceId} (a plain value, matching the
 * User/UserProfile convention of not using JPA relations).
 */
@Entity
@Table(name = "source_accounts")
public class SourceAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long sourceId;

	@Column(nullable = false)
	private String accountName;

	@Column(length = 512)
	private String accountId;

	@Column
	private String identity;

	@Column(nullable = false)
	private String status;

	// Whether the account is correlated to an identity (drives the
	// All / Correlated / Uncorrelated tabs).
	@Column(nullable = false)
	private boolean correlated;

	protected SourceAccount() {
		// for JPA
	}

	public SourceAccount(Long sourceId, String accountName, String accountId, String identity, String status,
			boolean correlated) {
		this.sourceId = sourceId;
		this.accountName = accountName;
		this.accountId = accountId;
		this.identity = identity;
		this.status = status;
		this.correlated = correlated;
	}

	public Long getId() {
		return id;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public String getAccountName() {
		return accountName;
	}

	public String getAccountId() {
		return accountId;
	}

	public String getIdentity() {
		return identity;
	}

	public String getStatus() {
		return status;
	}

	public boolean isCorrelated() {
		return correlated;
	}
}
