# 🚀 Quick Start Guide - Text Comparison System

## 5-Minute Setup

### Step 1: Start PostgreSQL (60 seconds)
```bash
cd /Users/prodbygus/IdeaProjects/java-text-comparion
docker-compose up -d
```
✓ PostgreSQL running on localhost:5433

### Step 2: Build the Application (90 seconds)
```bash
mvn clean package -DskipTests
```
✓ JAR file created: `target/java-text-comparion-0.0.1-SNAPSHOT.jar` (94MB)

### Step 3: Run the Application (30 seconds)
```bash
java -jar target/java-text-comparion-0.0.1-SNAPSHOT.jar
```

Or using Maven:
```bash
mvn spring-boot:run
```

### Step 4: Access the Application
- Open browser: **http://localhost:8080**
- Wait for Vaadin application to load

## Using the Application

### Upload Documents
1. Click **"Upload & Compare"** in the sidebar
2. Click file upload area or drag-and-drop
3. Select first document (TXT, PDF, or DOCX)
4. Select second document
5. Click **"Compare Documents"** button

### View Results
- **Correlation Index**: Overall similarity percentage (0-100%)
- **Cosine Similarity**: Vector-space similarity (TF-IDF based)
- **Jaccard Similarity**: Set overlap percentage
- **Summary**: Interpretation (Highly Similar, Moderately Similar, etc.)

### View History
- Click **"History"** in the sidebar
- See all past comparisons
- Sort by date and metrics

## File Format Support

| Format | Status | Parser        |
|--------|--------|---------------|
| TXT    | ✅     | Native UTF-8  |
| PDF    | ✅     | Apache PDFBox |
| DOCX   | ✅     | Apache POI    |

## Configuration

Edit `src/main/resources/application.properties`:

```properties
# Database credentials
spring.datasource.username=textuser
spring.datasource.password=textpassword

# Similarity weights (must sum to 1.0)
similarity.weights.cosine=0.7      # Weight for TF-IDF cosine
similarity.weights.jaccard=0.3     # Weight for Jaccard

# Hibernate
spring.jpa.hibernate.ddl-auto=update   # Auto-create tables
```

## Running Tests

```bash
# Run all unit tests
mvn test

# Run only metric calculators
mvn test -Dtest="*Calculator*"

# Run only normalizer tests
mvn test -Dtest="*Normalizer*"
```

## Troubleshooting

### PostgreSQL Connection Error
```bash
# Check if container is running
docker ps | grep postgres

# Restart if needed
docker-compose restart

# Check logs
docker-compose logs postgres
```

### Port Already in Use
```bash
# Change database port in compose.yaml
# Then update application.properties:
spring.datasource.url=jdbc:postgresql://localhost:NEW_PORT/postgres
```

### Out of Memory
```bash
# Run with more heap
java -Xmx2g -jar target/*.jar
```

### Slow Comparison
- For documents > 10MB, expect 1-2 second delay
- Metrics computation is CPU-bound
- Consider document chunking for very large files

## Architecture Quick Reference

```
┌─ Presentation Layer
│  └─ Vaadin UI (MainLayout, ComparisonUploadView, ComparisonHistoryView)
│
├─ Application Layer
│  └─ ComparisonService (orchestration)
│
├─ Domain Layer
│  ├─ Entities (Document, Comparison, MatchSegment)
│  ├─ Repositories (contracts)
│  └─ Services (TextNormalizer, SimilarityMetric)
│
├─ Infrastructure Layer
│  ├─ Parsers (TXT, PDF, DOCX)
│  ├─ Normalization (stopwords, accent removal)
│  ├─ Metrics (Cosine, Jaccard, Correlation)
│  └─ Persistence (JPA adapters)
│
└─ Shared Layer
   ├─ Exceptions
   └─ Configuration
```

## Key Metrics Explained

### Cosine Similarity (TF-IDF)
- Compares documents as vectors
- Weights rare terms higher (IDF)
- Best for: Long documents with varied vocabulary
- Range: 0% (completely different) to 100% (identical)

### Jaccard Similarity
- Counts unique token overlap
- Formula: common_tokens / total_unique_tokens
- Best for: Keyword matching, short texts
- Range: 0% to 100%

### Correlation Index
- Composite metric combining both
- Default: 70% Cosine + 30% Jaccard
- Best for: Balanced similarity assessment
- Range: 0% to 100%

## Example Workflows

### Workflow 1: Compare Two Articles
1. Upload article1.pdf
2. Upload article2.docx
3. View correlation index (typically 20-60%)
4. Check history for past comparisons

### Workflow 2: Check for Plagiarism
1. Upload original_essay.txt
2. Upload suspect_essay.txt
3. High correlation (>70%) = likely plagiarism
4. Low correlation (<30%) = original work

### Workflow 3: Compare Document Versions
1. Upload document_v1.docx
2. Upload document_v2.docx
3. View how much changed (mid-range typically 40-80%)

## Performance Expectations

| Document Size | Processing Time | Notes |
|--------------|-----------------|-------|
| < 100 KB     | < 100ms        | Very fast |
| 100 KB - 1MB | 100-500ms      | Normal |
| 1 MB - 10MB  | 500ms - 2s     | Acceptable |
| > 10MB       | > 2s           | Consider chunking |

## Database

PostgreSQL schema auto-created on startup:

```sql
-- Documents table
CREATE TABLE documents (
    id BIGSERIAL PRIMARY KEY,
    original_file_name VARCHAR(255),
    file_type VARCHAR(50),
    content TEXT,
    normalized_content TEXT,
    uploaded_at TIMESTAMP,
    version BIGINT
);

-- Comparisons table
CREATE TABLE comparisons (
    id BIGSERIAL PRIMARY KEY,
    document_a_id BIGINT REFERENCES documents,
    document_b_id BIGINT REFERENCES documents,
    cosine_similarity DOUBLE,
    jaccard_similarity DOUBLE,
    correlation_index DOUBLE,
    summary TEXT,
    created_at TIMESTAMP,
    version BIGINT
);

-- Match segments table
CREATE TABLE match_segments (
    id BIGSERIAL PRIMARY KEY,
    comparison_id BIGINT REFERENCES comparisons,
    source_excerpt TEXT,
    target_excerpt TEXT,
    similarity_score DOUBLE,
    category VARCHAR(50),
    version BIGINT
);
```

## Stopping the Application

```bash
# From command line:
Ctrl+C

# Stop Docker containers:
docker-compose down

# Remove database volume:
docker-compose down -v
```

## Next Steps

1. ✅ Explore the UI with sample documents
2. ✅ Check application logs for debug info
3. ✅ Review database tables: `SELECT * FROM comparisons;`
4. ✅ Modify weights in properties and recompile
5. ✅ Run tests to verify metric calculations

## Support & Debugging

### Enable debug logging
```properties
logging.level.com.prodbygus.javatextcomparion=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

### Check database connection
```bash
psql -h localhost -p 5433 -U postgres -d postgres
```

### View application logs
```bash
tail -f /tmp/application.log
```

## Resources

- 📖 Full Documentation: `IMPLEMENTATION.md`
- 🏗️ Architecture: `docs/architecture.md`
- 📋 API Contract: `docs/api-contract.md`
- 📝 Domain Rules: `docs/domain-rules.md`

---

**Status**: ✅ Ready to Use
**Compile Time**: ~90 seconds
**Startup Time**: ~15 seconds
**First Comparison**: ~2-5 seconds (depending on file size)

