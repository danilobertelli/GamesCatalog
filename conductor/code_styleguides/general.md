# General Code Style Principles

This document outlines general coding principles that apply across all files in this project.

## Readability
- Code should be easy to read and self-explanatory.
- Favor descriptive naming over overly concise or cryptic abbreviations.
- Avoid unnecessarily complex or clever single-line constructs when multi-line expressions are clearer.

## Consistency
- Follow established idiomatic patterns across the project.
- Maintain consistent formatting, naming conventions, and file structure.

## Simplicity & Separation of Concerns
- Prefer simple, decoupled solutions over complex abstractions.
- Break down complex business rules into atomic, testable use cases or helper functions.
- Keep UI components passive, hoisting business logic into ViewModels.

## Maintainability & Documentation
- Document *why* a particular decision or workaround was introduced, rather than just narrating *what* the syntax does.
- Keep documentation and docstrings up-to-date with code modifications.
