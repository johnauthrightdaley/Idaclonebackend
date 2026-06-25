package com.example.backend.dto;

import com.example.backend.entity.Source;

/** A source as returned to the Connections list and detail header. */
public record SourceResponse(Long id, String name, String application, String description, String sourceType,
		String connectionType, String status) {

	public static SourceResponse from(Source source) {
		return new SourceResponse(source.getId(), source.getName(), source.getApplication(), source.getDescription(),
				source.getSourceType(), source.getConnectionType(), source.getStatus());
	}
}
