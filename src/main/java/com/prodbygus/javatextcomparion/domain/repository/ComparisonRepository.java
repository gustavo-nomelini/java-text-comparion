package com.prodbygus.javatextcomparion.domain.repository;

import com.prodbygus.javatextcomparion.domain.model.Comparison;

import java.util.List;
import java.util.Optional;

/**
 * Repository contract for Comparison persistence.
 * Implementations belong to the infrastructure layer.
 */
public interface ComparisonRepository {

    /**
     * Save or update a comparison.
     *
     * @param comparison the comparison to save
     * @return the saved comparison
     */
    Comparison save(Comparison comparison);

    /**
     * Find a comparison by its ID.
     *
     * @param id the comparison ID
     * @return an Optional containing the comparison if found
     */
    Optional<Comparison> findById(Long id);

    /**
     * Find all comparisons for a specific document.
     *
     * @param documentId the document ID
     * @return a list of comparisons
     */
    List<Comparison> findByDocumentId(Long documentId);

    /**
     * Find all comparisons between two documents.
     *
     * @param documentAId the first document ID
     * @param documentBId the second document ID
     * @return a list of comparisons
     */
    List<Comparison> findByDocumentPair(Long documentAId, Long documentBId);

    /**
     * Delete a comparison by its ID.
     *
     * @param id the comparison ID
     */
    void deleteById(Long id);

    /**
     * Get all comparisons with pagination support.
     *
     * @param limit maximum number of results
     * @param offset starting position
     * @return a list of comparisons
     */
    List<Comparison> findAll(int limit, int offset);
}

