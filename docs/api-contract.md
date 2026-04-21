# Internal Contract Guide

This project may start with Vaadin-only flows, but internal service contracts should still be explicit.

## Application services

### CompareDocumentsUseCase
Responsibilities:
- receive two files
- validate files
- call parsers
- normalize text
- execute metrics
- compute correlation index
- persist result
- return a comparison result DTO

Suggested input:
- file A
- file B
- optional comparison options

Suggested output:
- comparison id
- document names
- metric scores
- final correlation index
- matched segments summary

### ListComparisonHistoryUseCase
Responsibilities:
- retrieve past comparisons
- support ordering by date

### GetComparisonDetailsUseCase
Responsibilities:
- retrieve a saved comparison with all detailed results

## DTO recommendations

### ComparisonResultDTO
Suggested fields:
- comparisonId
- fileNameA
- fileNameB
- cosineSimilarity
- jaccardSimilarity
- correlationIndex
- summary
- createdAt

### MatchSegmentDTO
Suggested fields:
- sourceExcerpt
- targetExcerpt
- similarityScore
- category

## Error contract
Use application-specific exceptions for:
- invalid file type
- empty file
- parsing failure
- comparison execution failure
- persistence failure

## Future REST endpoints
If REST is added later, prefer endpoints like:
- `POST /api/comparisons`
- `GET /api/comparisons`
- `GET /api/comparisons/{id}`
