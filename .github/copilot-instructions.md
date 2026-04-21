# GitHub Copilot Instructions

## Context
This repository contains an academic software engineering project focused on comparing the content of two text files and generating a correlation index.

Stack:
- Java
- Spring Boot
- Vaadin
- PostgreSQL
- Maven

## Main objective
Generate code for a modular monolith that compares two documents, extracts textual relationships between them, and calculates similarity metrics and a final correlation index.

## Architectural rules
- Follow a layered architecture:
  - presentation
  - application
  - domain
  - infrastructure
- Domain rules must stay independent from UI and framework-specific concerns whenever possible.
- Vaadin views belong to the presentation layer.
- Spring services that orchestrate use cases belong to the application layer.
- Persistence adapters, file parsers and metric implementations belong to the infrastructure layer.
- Entities, value objects, business rules and repository contracts belong to the domain layer.

## Package conventions
Use the base package:
`com.prodbygus.textcomparison`

Suggested packages:
- `presentation.view`
- `presentation.component`
- `application.usecase`
- `application.service`
- `application.dto`
- `domain.model`
- `domain.valueobject`
- `domain.service`
- `domain.repository`
- `infrastructure.persistence`
- `infrastructure.parser`
- `infrastructure.similarity`
- `shared.exception`
- `shared.util`

## Coding style
- Prefer clear and descriptive names.
- Keep classes focused on a single responsibility.
- Avoid god classes.
- Prefer constructor injection.
- Avoid field injection.
- Keep methods short and cohesive.
- Validate inputs explicitly.
- Use immutable DTOs when reasonable.
- Use records where appropriate.
- Favor readability over cleverness.
- Avoid unnecessary abstractions.

## Spring Boot rules
- Use `@Service` for application services.
- Use `@Repository` only in persistence adapters.
- Keep controllers minimal if REST endpoints are added later.
- Centralize exception handling.
- Use transactions deliberately.
- Avoid putting business logic inside JPA entities unless it is truly domain behavior.

## Vaadin rules
- Views must focus on interaction and rendering.
- Do not place parsing or similarity calculation logic inside Vaadin views.
- Delegate actions from the UI to application services or use cases.
- Build a clean and simple UI with good feedback states.

## Persistence rules
- PostgreSQL is the source of truth.
- Store original file metadata and processed comparison results.
- Use Flyway or Liquibase if migrations are introduced.
- Repositories in the domain are contracts; database-specific implementations belong to infrastructure.

## Text-processing rules
- Always separate these stages:
  1. file parsing
  2. text normalization
  3. tokenization
  4. metric calculation
  5. correlation index composition
- Keep each metric implementation isolated behind an interface when helpful.
- Make metric weights configurable when possible.

## Testing rules
- Create unit tests for domain services and metric calculators.
- Prefer integration tests for persistence and file parsing.
- Avoid fragile tests tightly coupled to UI layout.
- Use representative document samples in tests.

## Output expectations for Copilot
When generating code, prefer:
- production-style Java code
- JavaDoc on public domain services when useful
- meaningful exception messages
- null-safe handling
- examples aligned with this project domain

## Anti-patterns to avoid
- mixing Vaadin UI with business logic
- placing SQL or parsing logic directly in views
- creating large service classes with too many responsibilities
- duplicating normalization logic across classes
- using static utility classes for everything
- leaking JPA entities into every layer without care

## First implementation priorities
1. domain model for Document and Comparison
2. file upload flow
3. parsers for TXT, PDF and DOCX
4. normalization service
5. cosine similarity and jaccard similarity
6. correlation index calculator
7. comparison result view
8. history view
