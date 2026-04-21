package com.prodbygus.javatextcomparion.infrastructure.persistence;

import com.prodbygus.javatextcomparion.domain.model.Comparison;
import com.prodbygus.javatextcomparion.domain.repository.ComparisonRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of ComparisonRepository using Spring Data JPA.
 */
@Component
public class ComparisonRepositoryAdapter implements ComparisonRepository {

    private final ComparisonJpaRepository jpaRepository;

    public ComparisonRepositoryAdapter(ComparisonJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Comparison save(Comparison comparison) {
        return jpaRepository.save(comparison);
    }

    @Override
    public Optional<Comparison> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Comparison> findByDocumentId(Long documentId) {
        return jpaRepository.findByDocumentId(documentId);
    }

    @Override
    public List<Comparison> findByDocumentPair(Long documentAId, Long documentBId) {
        return jpaRepository.findByDocumentPair(documentAId, documentBId);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<Comparison> findAll(int limit, int offset) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        return jpaRepository.findAll(pageable).getContent();
    }
}

