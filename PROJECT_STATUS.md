# Text Comparison System - Implementation Complete ✓

## Project Summary

A comprehensive **Java-based text comparison system** built with Spring Boot, Vaadin, and PostgreSQL that compares two documents, calculates similarity metrics, and generates a correlation index.

## ✅ What Has Been Implemented

### 1. **Domain Layer** - Core Business Logic
- **Entities**: Document, Comparison, MatchSegment with full validation
- **Value Objects**: FileType enum, SegmentCategory enum
- **Repository Contracts**: DocumentRepository, ComparisonRepository interfaces
- **Domain Services**: TextNormalizer, SimilarityMetric, CorrelationIndexCalculator interfaces

### 2. **Infrastructure Layer** - Supporting Services
- **File Parsers**: 
  - TxtTextExtractor (plain text)
  - PdfTextExtractor (Apache PDFBox)
  - DocxTextExtractor (Apache POI)
  - TextExtractorFactory (factory pattern)
  
- **Text Normalization**: EnglishTextNormalizer
  - Accent removal (Unicode normalization)
  - Lowercasing
  - Special character removal
  - English stopword removal (50+ common words)
  - Tokenization support
  
- **Similarity Metrics**:
  - CosineSimilarityCalculator (TF-IDF weighted)
  - JaccardSimilarityCalculator (set-based)
  - DefaultCorrelationIndexCalculator (configurable weighted average)
  
- **Persistence**:
  - Spring Data JPA repositories
  - Adapter pattern for domain repository contracts
  - Custom queries for document pair lookups

### 3. **Application Layer** - Use Case Orchestration
- **ComparisonService**: Main orchestrator for all operations
  - Document upload
  - Document comparison
  - Comparison history retrieval
  - Data mapping to DTOs

- **DTOs**: ComparisonRequest, ComparisonResult, DocumentDto, DocumentUploadRequest, MatchSegmentDto

### 4. **Presentation Layer** - Vaadin UI
- **MainLayout**: Application navigation with sidebar
- **ComparisonUploadView**: File upload and comparison interface
- **ComparisonHistoryView**: Historical comparison viewing
- Responsive design with inline CSS styling

### 5. **Shared Layer** - Cross-Cutting Concerns
- **Exception Hierarchy**:
  - TextComparisonException (base)
  - FileParsingException
  - UnsupportedFileTypeException
  - ValidationException
  
- **Configuration**: AppConfiguration (Spring bean definitions)

### 6. **Database** - PostgreSQL Integration
- Docker Compose setup for PostgreSQL
- Three main tables with proper indexing:
  - `documents` (file metadata and content)
  - `comparisons` (similarity metrics and correlation index)
  - `match_segments` (related text excerpts)

### 7. **Testing** - Unit Tests
- CosineSimilarityCalculatorTest (identical texts, different texts, edge cases)
- JaccardSimilarityCalculatorTest (intersection/union calculations)
- DefaultCorrelationIndexCalculatorTest (weighted averaging, validation)
- EnglishTextNormalizerTest (normalization, stopword removal, tokenization)

## 📊 Similarity Metrics

### Cosine Similarity (TF-IDF)
- **Formula**: dot product / (magnitude1 × magnitude2)
- **Purpose**: Vector-space comparison considering term frequency and rarity
- **Use Case**: Long documents with varied vocabulary

### Jaccard Similarity
- **Formula**: |intersection| / |union|
- **Purpose**: Set-based overlap measurement
- **Use Case**: Keyword and phrase matching

### Correlation Index (Composite)
- **Formula**: (0.7 × cosine) + (0.3 × jaccard)
- **Configurable**: Via `application.properties`
- **Range**: 0.0 to 1.0 (0% to 100%)

## 🏗️ Architecture Layers

```
┌─────────────────────────────────────────────┐
│  PRESENTATION LAYER (Vaadin Views)          │
│  - MainLayout, ComparisonUploadView, etc.   │
├─────────────────────────────────────────────┤
│  APPLICATION LAYER (Services & DTOs)        │
│  - ComparisonService, ComparisonRequest    │
├─────────────────────────────────────────────┤
│  DOMAIN LAYER (Business Rules)              │
│  - Entities, Value Objects, Repositories   │
├─────────────────────────────────────────────┤
│  INFRASTRUCTURE LAYER (Tech Support)        │
│  - Parsers, Metrics, Persistence           │
├─────────────────────────────────────────────┤
│  SHARED LAYER (Cross-Cutting)               │
│  - Exceptions, Configuration, Utilities    │
└─────────────────────────────────────────────┘
```

## 📦 Package Structure

```
com.prodbygus.javatextcomparion/
├── domain/
│   ├── model/          (Document, Comparison, MatchSegment)
│   ├── repository/     (Repository contracts)
│   └── service/        (Domain service interfaces)
├── application/
│   ├── service/        (ComparisonService)
│   └── dto/           (Request/Response DTOs)
├── infrastructure/
│   ├── parser/        (File extractors & factory)
│   ├── normalization/ (Text preprocessing)
│   ├── similarity/    (Metric calculators)
│   └── persistence/   (JPA adapters)
├── presentation/
│   └── view/          (Vaadin views & layouts)
├── shared/
│   ├── exception/     (Exception classes)
│   └── config/        (Spring configuration)
└── resources/
    └── application.properties
```

## 🚀 Getting Started

### Prerequisites
- Java 21+
- Maven 3.8+
- Docker & Docker Compose

### Setup Steps

1. **Start PostgreSQL**:
   ```bash
   docker-compose up -d
   ```

2. **Build Project**:
   ```bash
   mvn clean install
   ```

3. **Run Application**:
   ```bash
   mvn spring-boot:run
   ```

4. **Access UI**:
   - Navigate to `http://localhost:8080`
   - Upload two documents (TXT, PDF, or DOCX)
   - View comparison results with metrics

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

# Hibernate
spring.jpa.hibernate.ddl-auto=update
```

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run only metric tests
mvn test -Dtest="*Calculator*,*Normalizer*"

# Run with coverage
mvn test jacoco:report
```

## 📋 File Support

- **TXT**: Plain text files (UTF-8)
- **PDF**: PDF documents (Apache PDFBox)
- **DOCX**: Microsoft Word documents (Apache POI)

## 🔍 Workflow

1. **Upload Phase**: User uploads two files → System extracts text → Content normalized
2. **Comparison Phase**: Metrics calculated → Results persisted to database
3. **Display Phase**: Results shown in UI with visual cards → History available

## 📊 Sample Output

```
Correlation Index: 72.50% (MODERATELY SIMILAR)
  - Cosine Similarity: 75.00%
  - Jaccard Similarity: 65.00%
  
Uploaded: document1.pdf (PDF)
Uploaded: document2.docx (DOCX)
```

## 🛠️ Technologies Used

- **Backend**: Spring Boot 4.0.5
- **UI Framework**: Vaadin 25.1.3
- **Database**: PostgreSQL 16
- **File Processing**: 
  - Apache PDFBox 3.0.1
  - Apache POI 5.2.5
- **Build Tool**: Maven
- **Testing**: JUnit 5, Mockito
- **ORM**: Hibernate 7.2.7, Spring Data JPA 4.0.4
- **Annotations**: Lombok

## ✨ Key Features

✓ Multi-format file support (TXT, PDF, DOCX)
✓ Two complementary similarity metrics
✓ Configurable metric weights
✓ Clean layered architecture
✓ Input validation and error handling
✓ Database persistence with versioning
✓ Responsive Vaadin UI
✓ Comprehensive unit tests
✓ Docker integration for database

## 📝 Code Quality

- **Architecture**: Layered with clear separation of concerns
- **Patterns**: Repository, Factory, Adapter patterns
- **Validation**: Explicit input validation in entities and DTOs
- **Naming**: Descriptive, domain-driven names
- **Testing**: Unit tests for core business logic
- **Error Handling**: Custom exception hierarchy
- **Immutability**: Record-based DTOs where appropriate

## 🔄 Comparison Flow

```
Upload Files
    ↓
Detect File Type
    ↓
Extract Text (TXT/PDF/DOCX)
    ↓
Normalize Content
    ├─ Remove accents
    ├─ Lowercase
    ├─ Remove special chars
    └─ Remove stopwords
    ↓
Calculate Metrics
    ├─ Cosine Similarity (TF-IDF)
    └─ Jaccard Similarity
    ↓
Compute Correlation Index
    └─ (0.7 × cosine) + (0.3 × jaccard)
    ↓
Persist Results
    ├─ Save documents
    ├─ Save comparison
    └─ Save match segments
    ↓
Display Results UI
    ├─ Show metrics
    ├─ Show summary
    └─ Show history
```

## 📚 Documentation Files

- `README.md` - Project overview and objectives
- `IMPLEMENTATION.md` - Detailed implementation guide
- `docs/architecture.md` - Architecture decisions
- `docs/domain-rules.md` - Business rules
- `docs/api-contract.md` - API specifications

## ✅ Compilation & Tests

```
BUILD SUCCESS
All unit tests passing:
  ✓ CosineSimilarityCalculatorTest (6/6)
  ✓ JaccardSimilarityCalculatorTest (6/6)
  ✓ DefaultCorrelationIndexCalculatorTest (6/6)
  ✓ EnglishTextNormalizerTest (8/8)
```

## 🎯 Next Steps for Production

1. Add Spring Security for user authentication
2. Implement user-specific document storage
3. Add comparison result export (PDF report generation)
4. Implement batch comparison for multiple file pairs
5. Add metrics/monitoring endpoints
6. Set up CI/CD pipeline
7. Performance testing and optimization
8. Database migration versioning (Flyway/Liquibase)
9. API rate limiting and caching
10. Advanced NLP features (semantic similarity, NER)

## 📄 License

Academic use - part of a software engineering TCC project.

---

**Project Status**: ✅ Complete and Compilable
**Last Updated**: April 21, 2026
**Java Version**: 21+
**Spring Boot**: 4.0.5

