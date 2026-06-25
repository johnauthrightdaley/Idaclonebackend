package com.example.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.CreateSourceRequest;
import com.example.backend.dto.SourceAccountResponse;
import com.example.backend.dto.SourceResponse;
import com.example.backend.dto.TestConnectionResponse;
import com.example.backend.service.SourceService;

/**
 * Admin -> Connections (Sources) endpoints. All routes are JWT-protected by
 * {@code SecurityConfig} (only /api/auth/** and /api/hello are public).
 *
 * Buttons backed here: Create New, Test Connection, Discover Accounts, Discover
 * Entitlements. The "Preview" and config "Save" buttons from the spec are not
 * implemented yet (not present in the reference screens) -- deferred.
 */
@RestController
@RequestMapping("/api/sources")
public class SourceController {

	private final SourceService sourceService;

	public SourceController(SourceService sourceService) {
		this.sourceService = sourceService;
	}

	@GetMapping
	public List<SourceResponse> list() {
		return sourceService.listSources();
	}

	@GetMapping("/{id}")
	public SourceResponse get(@PathVariable Long id) {
		return sourceService.getSource(id);
	}

	@PostMapping
	public ResponseEntity<SourceResponse> create(@RequestBody CreateSourceRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(sourceService.createSource(request));
	}

	@GetMapping("/{id}/accounts")
	public List<SourceAccountResponse> accounts(@PathVariable Long id,
			@RequestParam(defaultValue = "all") String filter) {
		return sourceService.listAccounts(id, filter);
	}

	@PostMapping("/{id}/test-connection")
	public TestConnectionResponse testConnection(@PathVariable Long id) {
		return sourceService.testConnection(id);
	}

	@PostMapping("/{id}/discover-accounts")
	public SourceResponse discoverAccounts(@PathVariable Long id) {
		return sourceService.discoverAccounts(id);
	}

	@PostMapping("/{id}/discover-entitlements")
	public Map<String, String> discoverEntitlements(@PathVariable Long id) {
		return sourceService.discoverEntitlements(id);
	}
}
