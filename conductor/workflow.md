# Project Workflow

## Guiding Principles

1. **The Plan is the Source of Truth:** All work must be specified and tracked in `plan.md` inside the active track directory.
2. **The Tech Stack is Deliberate:** Any modifications or additions to libraries or architecture must be documented in `tech-stack.md` before implementation.
3. **Test-Driven Development (TDD):** Business rules, data transformations, and repositories must follow Red -> Green -> Refactor.
4. **Coverage Standard:** Target >80% test coverage for domain use cases and repository mappers.
5. **Modern Android Platform:** Targeting `minSdk = 36` natively without legacy backport overhead.
6. **English Documentation:** All Conductor artifacts, code comments, commit messages, and symbols must be written in English.

---

## Standard Task Workflow

For every task identified in `plan.md`:

1. **Select Task:** Choose the next pending task in sequential order.
2. **Mark In Progress:** Update `plan.md` status marker from `[ ]` to `[~]`.
3. **Write Failing Tests (Red Phase):**
   - Create or update the corresponding unit test.
   - Run the test suite and verify that it fails for the expected reason.
4. **Implement to Pass (Green Phase):**
   - Write the minimum amount of production code required to satisfy the test.
   - Run the test suite and verify it passes.
5. **Refactor:**
   - Clean up code, optimize readability, and ensure style guide compliance without breaking tests.
6. **Stage & Commit:**
   - Review changes and prepare commit following conventional commit standards (`feat:`, `fix:`, `refactor:`, `test:`).
   - *Note: Commits must always be gated behind explicit user confirmation.*
7. **Update Plan:**
   - Mark task as complete `[x]` in `plan.md`.

---

## Phase Completion & Verification Protocol

When all tasks in a Phase are completed:

1. **Automated Verification:** Execute project compilation and unit tests:
   ```bash
   ./gradlew testDebugUnitTest
   ```
2. **Manual Verification Plan:** Formulate an actionable verification checklist for the user to validate UI and interactive behavior.
3. **Checkpoint:** Record the checkpoint and proceed to the next phase upon user approval.
