package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.entity.SourceAccount;

public interface SourceAccountRepository extends JpaRepository<SourceAccount, Long> {

	List<SourceAccount> findBySourceId(Long sourceId);

	long countBySourceId(Long sourceId);
}
