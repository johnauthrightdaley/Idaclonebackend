package com.example.backend.dto;

import com.example.backend.entity.SourceAccount;

/** An account row for the App Page "Accounts" table. */
public record SourceAccountResponse(Long id, String accountName, String accountId, String identity, String status,
		boolean correlated) {

	public static SourceAccountResponse from(SourceAccount account) {
		return new SourceAccountResponse(account.getId(), account.getAccountName(), account.getAccountId(),
				account.getIdentity(), account.getStatus(), account.isCorrelated());
	}
}
