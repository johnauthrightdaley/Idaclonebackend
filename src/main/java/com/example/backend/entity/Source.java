package com.example.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * A connection "source" (an external application idaclone integrates with), shown
 * on the Admin -> Connections list. Backs the {@code sources} table. Accounts
 * discovered from a source live in {@link SourceAccount}, linked by {@code id}.
 */
@Entity
@Table(name = "sources")
public class Source {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	// Optional: the screenshots show "--" when there is no parent application.
	@Column
	private String application;

	@Column(length = 512)
	private String description;

	@Column(nullable = false)
	private String sourceType;

	@Column
	private String connectionType;

	// Health/aggregation state shown as a pill ("Healthy", "Aggregating").
	@Column(nullable = false)
	private String status;

	protected Source() {
		// for JPA
	}

	public Source(String name, String application, String description, String sourceType, String connectionType,
			String status) {
		this.name = name;
		this.application = application;
		this.description = description;
		this.sourceType = sourceType;
		this.connectionType = connectionType;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getConnectionType() {
		return connectionType;
	}

	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
