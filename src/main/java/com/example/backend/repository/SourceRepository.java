package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.entity.Source;

public interface SourceRepository extends JpaRepository<Source, Long> {
}
