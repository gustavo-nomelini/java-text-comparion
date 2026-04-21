package com.prodbygus.javatextcomparion.domain.repository;

import com.prodbygus.javatextcomparion.domain.model.Document;

import java.util.Optional;

/**
 * Repository contract for Document persistence.
 * Implementations belong to the infrastructure layer.
 */
public interface DocumentRepository {

    /**
     * Save or update a document.
     *
     * @param document the document to save
     * @return the saved document
     */
    Document save(Document document);

    /**
     * Find a document by its ID.
     *
     * @param id the document ID
     * @return an Optional containing the document if found
     */
    Optional<Document> findById(Long id);

    /**
     * Delete a document by its ID.
     *
     * @param id the document ID
     */
    void deleteById(Long id);
}

