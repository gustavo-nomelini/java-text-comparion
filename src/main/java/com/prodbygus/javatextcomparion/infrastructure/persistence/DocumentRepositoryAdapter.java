package com.prodbygus.javatextcomparion.infrastructure.persistence;

import com.prodbygus.javatextcomparion.domain.model.Document;
import com.prodbygus.javatextcomparion.domain.repository.DocumentRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Implementation of DocumentRepository using Spring Data JPA.
 */
@Component
public class DocumentRepositoryAdapter implements DocumentRepository {

    private final DocumentJpaRepository jpaRepository;

    public DocumentRepositoryAdapter(DocumentJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Document save(Document document) {
        return jpaRepository.save(document);
    }

    @Override
    public Optional<Document> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}

