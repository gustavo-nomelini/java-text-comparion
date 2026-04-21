# Architecture Guide

## Architectural style
The project should be implemented as a **modular monolith**.

This is the preferred approach because it keeps the TCC implementation simpler, easier to explain, and easier to deploy while still preserving separation of concerns.

## Layers

### 1. Presentation Layer
Responsible for user interaction.

Examples:
- Vaadin views
- form components
- upload components
- result screens

### 2. Application Layer
Responsible for orchestration.

Examples:
- compare documents use case
- save comparison use case
- list comparison history use case

### 3. Domain Layer
Responsible for business rules.

Examples:
- document model
- comparison model
- similarity calculator contracts
- correlation index rules

### 4. Infrastructure Layer
Responsible for technical details.

Examples:
- PostgreSQL persistence
- PDF and DOCX parsing
- JPA repositories
- metric implementations

## Dependency rule
Allowed direction:
- presentation -> application
- application -> domain
- infrastructure -> domain

Avoid:
- domain depending on presentation
- domain depending directly on Vaadin
- domain tightly coupled to Spring framework classes when not needed

## Core modules

### Document Module
Handles uploaded files and extracted text.

### Comparison Module
Handles comparison execution, metrics and result generation.

### History Module
Handles storage and retrieval of previous comparisons.

## Comparison pipeline
1. receive file A and file B
2. validate file type and size
3. parse text
4. normalize content
5. tokenize or vectorize
6. calculate metrics
7. compute final correlation index
8. persist results
9. render results

## Design recommendation
Use interfaces in the domain for:
- document repository
- comparison repository
- text normalizer
- similarity metric
- correlation index calculator

Provide implementations in infrastructure or application as needed.

## Suggested extension points
- semantic similarity using embeddings
- sentence-level alignment
- PDF report export
- batch comparison
