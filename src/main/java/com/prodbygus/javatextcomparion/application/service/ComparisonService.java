package com.prodbygus.javatextcomparion.application.service;

import com.prodbygus.javatextcomparion.application.dto.*;
import com.prodbygus.javatextcomparion.domain.model.Comparison;
import com.prodbygus.javatextcomparion.domain.model.Document;
import com.prodbygus.javatextcomparion.domain.model.FileType;
import com.prodbygus.javatextcomparion.domain.model.SegmentCategory;
import com.prodbygus.javatextcomparion.domain.repository.ComparisonRepository;
import com.prodbygus.javatextcomparion.domain.repository.DocumentRepository;
import com.prodbygus.javatextcomparion.domain.service.CorrelationIndexCalculator;
import com.prodbygus.javatextcomparion.domain.service.SimilarityMetric;
import com.prodbygus.javatextcomparion.domain.service.TextNormalizer;
import com.prodbygus.javatextcomparion.infrastructure.parser.TextExtractorFactory;
import com.prodbygus.javatextcomparion.shared.exception.FileParsingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Application service for text comparison operations.
 * Orchestrates file upload, comparison, and history retrieval.
 */
@Service
@Transactional
public class ComparisonService {

    private final DocumentRepository documentRepository;
    private final ComparisonRepository comparisonRepository;
    private final TextExtractorFactory textExtractorFactory;
    private final TextNormalizer textNormalizer;
    private final SimilarityMetric cosineSimilarity;
    private final SimilarityMetric jaccardSimilarity;
    private final CorrelationIndexCalculator correlationCalculator;

    public ComparisonService(
            DocumentRepository documentRepository,
            ComparisonRepository comparisonRepository,
            TextExtractorFactory textExtractorFactory,
            TextNormalizer textNormalizer,
            List<SimilarityMetric> similarityMetrics,
            CorrelationIndexCalculator correlationCalculator) {
        this.documentRepository = documentRepository;
        this.comparisonRepository = comparisonRepository;
        this.textExtractorFactory = textExtractorFactory;
        this.textNormalizer = textNormalizer;
        this.correlationCalculator = correlationCalculator;

        // Resolve the specific metrics
        this.cosineSimilarity = similarityMetrics.stream()
                .filter(m -> m.getName().contains("Cosine"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("CosineSimilarityCalculator not found"));

        this.jaccardSimilarity = similarityMetrics.stream()
                .filter(m -> m.getName().contains("Jaccard"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("JaccardSimilarityCalculator not found"));
    }

    /**
     * Upload a document.
     *
     * @param request the upload request
     * @return the uploaded document DTO
     * @throws FileParsingException if file parsing fails
     */
    public DocumentDto uploadDocument(DocumentUploadRequest request) throws FileParsingException {
        request.validate();

        // Parse content
        FileType fileType = FileType.fromFileName(request.filename());
        String normalizedContent = textNormalizer.normalize(request.content());

        // Create and save document
        Document document = Document.builder()
                .originalFileName(request.filename())
                .fileType(fileType)
                .content(request.content())
                .normalizedContent(normalizedContent)
                .uploadedAt(LocalDateTime.now())
                .build();

        document.validate();
        Document saved = documentRepository.save(document);

        return new DocumentDto(
                saved.getId(),
                saved.getOriginalFileName(),
                saved.getFileType().name(),
                saved.getUploadedAt()
        );
    }

    /**
     * Compare two documents.
     *
     * @param request the comparison request
     * @return the comparison result
     */
    public ComparisonResult compareDocuments(ComparisonRequest request) {
        request.validate();

        // Retrieve documents
        Document docA = documentRepository.findById(request.documentAId())
                .orElseThrow(() -> new IllegalArgumentException("Document A not found"));
        Document docB = documentRepository.findById(request.documentBId())
                .orElseThrow(() -> new IllegalArgumentException("Document B not found"));

        // Calculate metrics
        double cosineScore = cosineSimilarity.calculate(
                docA.getNormalizedContent(),
                docB.getNormalizedContent()
        );
        double jaccardScore = jaccardSimilarity.calculate(
                docA.getNormalizedContent(),
                docB.getNormalizedContent()
        );
        double correlationIndex = correlationCalculator.calculate(cosineScore, jaccardScore);

        // Create comparison entity
        Comparison comparison = Comparison.builder()
                .documentA(docA)
                .documentB(docB)
                .cosineSimilarity(cosineScore)
                .jaccardSimilarity(jaccardScore)
                .correlationIndex(correlationIndex)
                .summary(generateSummary(cosineScore, jaccardScore, correlationIndex))
                .createdAt(LocalDateTime.now())
                .build();

        comparison.validate();
        Comparison saved = comparisonRepository.save(comparison);

        return mapComparisonToResult(saved);
    }

    /**
     * Get comparison by ID.
     *
     * @param comparisonId the comparison ID
     * @return the comparison result
     */
    @Transactional(readOnly = true)
    public Optional<ComparisonResult> getComparison(Long comparisonId) {
        return comparisonRepository.findById(comparisonId)
                .map(this::mapComparisonToResult);
    }

    /**
     * Get all comparisons for a document.
     *
     * @param documentId the document ID
     * @return list of comparison results
     */
    @Transactional(readOnly = true)
    public List<ComparisonResult> getComparisonHistory(Long documentId) {
        return comparisonRepository.findByDocumentId(documentId).stream()
                .map(this::mapComparisonToResult)
                .toList();
    }

    /**
     * Get all comparisons (with pagination).
     *
     * @param limit maximum results
     * @param offset starting position
     * @return list of comparison results
     */
    @Transactional(readOnly = true)
    public List<ComparisonResult> getAllComparisons(int limit, int offset) {
        return comparisonRepository.findAll(limit, offset).stream()
                .map(this::mapComparisonToResult)
                .toList();
    }

    /**
     * Delete a comparison and associated data.
     *
     * @param comparisonId the comparison ID
     */
    public void deleteComparison(Long comparisonId) {
        comparisonRepository.deleteById(comparisonId);
    }

    /**
     * Generate a summary text for the comparison.
     *
     * @param cosineScore cosine similarity
     * @param jaccardScore jaccard similarity
     * @param correlationIndex correlation index
     * @return summary text
     */
    private String generateSummary(double cosineScore, double jaccardScore, double correlationIndex) {
        String correlationLabel;
        if (correlationIndex >= 0.8) {
            correlationLabel = "HIGHLY SIMILAR";
        } else if (correlationIndex >= 0.5) {
            correlationLabel = "MODERATELY SIMILAR";
        } else if (correlationIndex >= 0.2) {
            correlationLabel = "WEAKLY SIMILAR";
        } else {
            correlationLabel = "LARGELY DIFFERENT";
        }

        return String.format(
                "Correlation Index: %.2f%% (%s). Cosine Similarity: %.2f%%, Jaccard Similarity: %.2f%%",
                correlationIndex * 100, correlationLabel,
                cosineScore * 100, jaccardScore * 100
        );
    }

    /**
     * Map Comparison entity to ComparisonResult DTO.
     *
     * @param comparison the comparison
     * @return the result DTO
     */
    private ComparisonResult mapComparisonToResult(Comparison comparison) {
        List<MatchSegmentDto> segments = comparison.getMatchSegments().stream()
                .map(seg -> new MatchSegmentDto(
                        seg.getId(),
                        seg.getSourceExcerpt(),
                        seg.getTargetExcerpt(),
                        seg.getSimilarityScore(),
                        seg.getCategory().name()
                ))
                .toList();

        return new ComparisonResult(
                comparison.getId(),
                comparison.getDocumentA().getId(),
                comparison.getDocumentB().getId(),
                comparison.getDocumentA().getOriginalFileName(),
                comparison.getDocumentB().getOriginalFileName(),
                comparison.getCosineSimilarity(),
                comparison.getJaccardSimilarity(),
                comparison.getCorrelationIndex(),
                comparison.getSummary(),
                comparison.getCreatedAt(),
                segments
        );
    }
}

