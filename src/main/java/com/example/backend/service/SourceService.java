package com.example.backend.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.dto.CreateSourceRequest;
import com.example.backend.dto.SourceAccountResponse;
import com.example.backend.dto.SourceResponse;
import com.example.backend.dto.TestConnectionResponse;
import com.example.backend.entity.Source;
import com.example.backend.repository.SourceAccountRepository;
import com.example.backend.repository.SourceRepository;

/**
 * Connections/Sources business logic. Mirrors the read/validate/throw style of
 * {@code UserService}: validation failures and missing records surface as
 * {@link ResponseStatusException}s, which {@code GlobalExceptionHandler} turns
 * into clean JSON error bodies.
 */
@Service
public class SourceService {

	private final SourceRepository sourceRepository;
	private final SourceAccountRepository accountRepository;

	public SourceService(SourceRepository sourceRepository, SourceAccountRepository accountRepository) {
		this.sourceRepository = sourceRepository;
		this.accountRepository = accountRepository;
	}

	public List<SourceResponse> listSources() {
		return sourceRepository.findAll().stream().map(SourceResponse::from).toList();
	}

	public SourceResponse getSource(Long id) {
		return SourceResponse.from(loadSource(id));
	}

	public SourceResponse createSource(CreateSourceRequest request) {
		if (request.name() == null || request.name().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Application name is required");
		}
		if (request.sourceType() == null || request.sourceType().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Source type is required");
		}
		// New sources default to a Direct Connection and a Healthy status (the PDF's
		// "instantiate default configuration"). Application is left blank.
		Source saved = sourceRepository.save(new Source(request.name().trim(), null,
				request.description() == null ? null : request.description().trim(), request.sourceType().trim(),
				"Direct Connection", "Healthy"));
		return SourceResponse.from(saved);
	}

	public List<SourceAccountResponse> listAccounts(Long sourceId, String filter) {
		loadSource(sourceId); // 404 if the source does not exist
		List<com.example.backend.entity.SourceAccount> accounts = accountRepository.findBySourceId(sourceId);
		String normalized = filter == null ? "all" : filter.toLowerCase();
		return accounts.stream().filter(account -> switch (normalized) {
			case "correlated" -> account.isCorrelated();
			case "uncorrelated" -> !account.isCorrelated();
			default -> true;
		}).map(SourceAccountResponse::from).toList();
	}

	/**
	 * Compiles a (simulated) connection handshake. On-prem connectors (AD/LDAP/JDBC)
	 * go through the VA cluster; everything else is treated as a SaaS API handshake.
	 */
	public TestConnectionResponse testConnection(Long id) {
		Source source = loadSource(id);
		String type = source.getSourceType() == null ? "" : source.getSourceType().toLowerCase();
		boolean onPrem = type.contains("active directory") || type.contains("ldap") || type.contains("jdbc");
		String message = onPrem
				? "On-Prem Virtual Appliance handshake succeeded (LDAP/JDBC)."
				: "SaaS API handshake succeeded with the cloud vendor.";
		return new TestConnectionResponse("Success", message);
	}

	/**
	 * Kicks off account aggregation: flips the source to "Aggregating" and schedules
	 * a self-heal back to "Healthy" so seeded rows don't get stuck. This stands in
	 * for the real asynchronous batch-processing job described in the spec.
	 */
	public SourceResponse discoverAccounts(Long id) {
		Source source = loadSource(id);
		source.setStatus("Aggregating");
		sourceRepository.save(source);
		scheduleAggregationReset(id);
		return SourceResponse.from(source);
	}

	public java.util.Map<String, String> discoverEntitlements(Long id) {
		Source source = loadSource(id);
		return java.util.Map.of("message", "Entitlement aggregation started for " + source.getName());
	}

	private void scheduleAggregationReset(Long id) {
		CompletableFuture.runAsync(() -> {
			try {
				Thread.sleep(6000);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
				return;
			}
			sourceRepository.findById(id).ifPresent(s -> {
				if ("Aggregating".equals(s.getStatus())) {
					s.setStatus("Healthy");
					sourceRepository.save(s);
				}
			});
		});
	}

	private Source loadSource(Long id) {
		return sourceRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Source not found"));
	}
}
