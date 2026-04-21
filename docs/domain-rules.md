# Domain Rules

## Main business concept
The system compares two textual documents and produces a correlation result.

## Core domain objects

### Document
Represents an uploaded file and its extracted textual content.

Rules:
- must have an original filename
- must have a supported file type
- extracted content cannot be null after successful parsing

### Comparison
Represents the result of comparing two documents.

Rules:
- always references exactly two documents in the MVP
- must contain at least one similarity metric result
- must produce a final correlation index
- final correlation index must be normalized to a defined range, preferably 0 to 1 or 0 to 100

### Match Segment
Represents a related excerpt or matched unit between documents.

Rules:
- must reference a comparison
- must contain a score or strength of relation
- may be classified as exact, similar, related, or divergent

## Validation rules
- reject unsupported file formats
- reject empty files
- reject comparisons when text extraction fails
- reject comparisons when normalized content is blank for both documents

## Metric rules
- each metric should define its own calculation responsibility
- metric results should be stored separately before composing the final score
- the final correlation index must be deterministic for the same processed input

## Correlation rule
The final correlation index should be based on a weighted combination of selected metrics.

Example:
- cosine similarity weight: 0.7
- jaccard similarity weight: 0.3

This weighting must be easy to change for future experiments.

## Traceability rule
A comparison result should preserve enough metadata to explain how it was generated.

Recommended metadata:
- metric versions
- algorithm weights
- processing timestamp
- file names
- optional preprocessing flags
