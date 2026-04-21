package com.prodbygus.javatextcomparion.infrastructure.persistence;

import com.prodbygus.javatextcomparion.domain.model.Comparison;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for Comparison entity.
 */
@Repository
public interface ComparisonJpaRepository extends JpaRepository<Comparison, Long> {

    /**
     * Find all comparisons where a document is involved.
     *
     * @param documentId the document ID
     * @return list of comparisons
     */
    @Query("SELECT c FROM Comparison c WHERE c.documentA.id = :documentId OR c.documentB.id = :documentId ORDER BY c.createdAt DESC")
    List<Comparison> findByDocumentId(@Param("documentId") Long documentId);

    /**
     * Find all comparisons between two specific documents.
     *
     * @param documentAId the first document ID
     * @param documentBId the second document ID
     * @return list of comparisons
     */
    @Query("SELECT c FROM Comparison c WHERE (c.documentA.id = :documentAId AND c.documentB.id = :documentBId) " +
            "OR (c.documentA.id = :documentBId AND c.documentB.id = :documentAId) ORDER BY c.createdAt DESC")
    List<Comparison> findByDocumentPair(@Param("documentAId") Long documentAId, @Param("documentBId") Long documentBId);
}

