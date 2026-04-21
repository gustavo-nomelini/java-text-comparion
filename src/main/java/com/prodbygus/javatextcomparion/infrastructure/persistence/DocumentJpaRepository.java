package com.prodbygus.javatextcomparion.infrastructure.persistence;

import com.prodbygus.javatextcomparion.domain.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for Document entity.
 */
@Repository
public interface DocumentJpaRepository extends JpaRepository<Document, Long> {
}

