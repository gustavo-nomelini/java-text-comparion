# Text Comparison System - Implementation

This document describes the implemented text comparison system.

## Architecture

The system follows a layered monolithic architecture with clear separation of concerns:

### Layers

1. **Presentation Layer** (`presentation.view`)
   - Vaadin views for file upload and comparison results
   - Main layout with navigation
   - No business logic, delegates to application services

2. **Application Layer** (`application.service`, `application.dto`)
   - Orchestrates use cases (file upload, comparison)
   - Application services handle business logic coordination
   - DTOs for request/response objects

3. **Domain Layer** (`domain.model`, `domain.repository`, `domain.service`)
   - Core business entities: Document, Comparison, MatchSegment
   - Repository contracts (interfaces)
   - Service interfaces: TextNormalizer, SimilarityMetric, CorrelationIndexCalculator
   - Independent from UI and framework concerns

4. **Infrastructure Layer** (`infrastructure.*`)
   - Repository implementations using Spring Data JPA
   - File parsers (TXT, PDF, DOCX)
   - Text normalization implementation
   - Similarity metrics implementation
   - Persistence adapters

5. **Shared Layer** (`shared.exception`, `shared.util`)
   - Exception hierarchy
   - Common utilities

## Key Components

### Domain Model

- **Document**: Represents an uploaded file with original and normalized content
- **Comparison**: Result of comparing two documents with metrics and summary
- **MatchSegment**: Paired excerpts from two documents with similarity scores
- **FileType**: Enum for supported formats (TXT, PDF, DOCX)
- **SegmentCategory**: Categorizes matches by similarity level

### File Parsing

- **TextExtractor**: Interface for extracting text from files
- **TxtTextExtractor**: Plain text parser
- **PdfTextExtractor**: PDF parser using Apache PDFBox
- **DocxTextExtractor**: DOCX parser using Apache POI
- **TextExtractorFactory**: Factory for selecting appropriate parser

### Text Normalization

- **EnglishTextNormalizer**: Implements TextNormalizer interface
  - Removes accents using Unicode normalization
  - Converts to lowercase
  - Removes special characters
  - Removes English stopwords
  - Tokenizes text

### Similarity Metrics

- **CosineSimilarityCalculator**: Cosine similarity using TF-IDF weighting
- **JaccardSimilarityCalculator**: Set-based intersection over union
- **DefaultCorrelationIndexCalculator**: Composite index using weighted average
  - Default weights: 70% cosine + 30% jaccard (configurable)

### Persistence

- **DocumentJpaRepository**: Spring Data JPA for documents
- **ComparisonJpaRepository**: Spring Data JPA for comparisons with custom queries
- **DocumentRepositoryAdapter**: Implements domain repository interface
- **ComparisonRepositoryAdapter**: Implements domain repository interface

### Application Services

- **ComparisonService**: Main orchestrator
  - Upload documents
  - Compare documents
  - Retrieve comparison history
  - Map entities to DTOs

## Getting Started

### Prerequisites

- Java 21+
- Maven 3.8+
- Docker and Docker Compose (for PostgreSQL)

### Setup

1. **Start PostgreSQL**:
   ```bash
   docker-compose up -d
   ```

2. **Build the project**:
   ```bash
   mvn clean install
   ```

3. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**:
   - Open browser to `http://localhost:8080`
   - Upload two documents (TXT, PDF, or DOCX)
   - View comparison results with correlation index
   - Check history of past comparisons

### Configuration

Edit `src/main/resources/application.properties`:

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/textcomparison
spring.datasource.username=textuser
spring.datasource.password=textpassword

# Metric Weights (must sum to 1.0)
similarity.weights.cosine=0.7
similarity.weights.jaccard=0.3
```

## Running Tests

```bash
mvn test
```

Tests are included for:
- Cosine Similarity Calculator
- Jaccard Similarity Calculator
- Correlation Index Calculator
- Text Normalizer

## Supported File Formats

- **TXT**: Plain text files
- **PDF**: PDF documents (using Apache PDFBox)
- **DOCX**: Microsoft Word documents (using Apache POI)

## Comparison Workflow

1. **Upload Phase**:
   - User uploads two files
   - System detects file type from extension
   - Text is extracted using appropriate parser
   - Content is normalized (lowercased, special chars removed, stopwords removed)
   - Document is persisted to database

2. **Comparison Phase**:
   - System retrieves both documents
   - Calculates cosine similarity using TF-IDF
   - Calculates jaccard similarity using set intersection
   - Computes correlation index: (0.7 × cosine) + (0.3 × jaccard)
   - Generates summary text describing the result
   - Persists comparison to database

3. **Display Phase**:
   - Results dialog shows:
     - Correlation index percentage
     - Cosine similarity percentage
     - Jaccard similarity percentage
     - Summary interpretation
   - History view shows all past comparisons

## Metrics Explanation

### Cosine Similarity (TF-IDF)
- Treats documents as vectors in high-dimensional space
- Weights terms by frequency and rarity
- Effective for longer documents with varied vocabulary

### Jaccard Similarity
- Set-based metric: intersection ÷ union
- Simple and interpretable
- Good for short texts and keyword-based comparison

### Correlation Index
- Weighted combination of both metrics
- Default: 70% cosine + 30% jaccard
- Configurable via properties for experimentation

## Category Levels

Matched segments are categorized by similarity:
- **HIGHLY_SIMILAR**: > 70% similarity
- **RELATED**: 40% - 70% similarity
- **DIVERGENT**: < 40% similarity

## Database Schema

### documents
- id (PK)
- original_file_name
- file_type
- content
- normalized_content
- uploaded_at
- version

### comparisons
- id (PK)
- document_a_id (FK)
- document_b_id (FK)
- cosine_similarity
- jaccard_similarity
- correlation_index
- summary
- created_at
- version

### match_segments
- id (PK)
- comparison_id (FK)
- source_excerpt
- target_excerpt
- similarity_score
- category
- version

## Future Enhancements

1. **Multi-language Support**:
   - Configurable stopword lists per language
   - Language detection

2. **Advanced Metrics**:
   - Levenshtein distance for character-level comparison
   - Semantic similarity using embeddings
   - N-gram based metrics

3. **Export Functionality**:
   - Export results to PDF report
   - Generate comparison visualizations

4. **Parallel Processing**:
   - Batch comparison operations
   - Async processing for large files

5. **User Management**:
   - Authentication and authorization
   - User-specific document storage
   - Comparison sharing

## Code Quality

- Unit tests for metrics and normalization
- Integration tests ready to add
- 80%+ coverage target for core business logic
- Clear separation of concerns
- No framework code in domain layer
- Explicit input validation

## Troubleshooting

### Database Connection Error
- Ensure PostgreSQL container is running: `docker-compose ps`
- Check database credentials in `application.properties`

### File Upload Issues
- Ensure file size < 10MB (configurable in ComparisonUploadView)
- Verify file format is TXT, PDF, or DOCX

### Comparison Not Working
- Check database has both documents: `SELECT * FROM documents`
- Review application logs for exceptions
- Verify metric weights sum to 1.0

## License

Academic use - part of a software engineering TCC project.

