# ADR-001: Switch from H2 to PostgreSQL

- Status: Accepted
- Date: 2026-11-25
- Stage: Milestone 1, Phase 5

## Context
Up to Phase 4 we developed against an H2 file database (`jdbc:h2:file:./data/ledger`). It needs no installation, which was enough for building features.

Phase 5 must prove that "100 concurrent withdrawals on the same account leave the balance correct". That depends entirely on the database's locks, isolation levels, and MVCC behavior.
- H2 is a lightweight test database. Its locking and concurrency behavior differs from production databases, so a concurrency test that passes on H2 does not prove correctness in production.
- Behavior such as `SELECT ... FOR UPDATE` and `REPEATABLE READ` snapshots only means something when verified on a production-grade database.
- The ledger's integrity rule is "consistency is enforced by the database, not by Java memory". That database has to be the one we would actually run.

## Decision
- Use PostgreSQL as the development database, started with Docker Compose (`compose.yaml`).
- Remove the H2 dependency and the H2 console configuration.
- Keep `ddl-auto=update` for schema generation for now.

## Alternatives
- **Keep H2:** easy to set up, but concurrency verification becomes meaningless for the reasons above.
- **Install PostgreSQL locally:** works, but version management and resets are tedious. With Docker, `docker compose down -v` resets everything in one line.
- **Testcontainers:** starts a database automatically per test run. Revisit when writing the concurrency tests.

## Consequences
- The Docker container must be running before starting the app or running tests (`@SpringBootTest`).
- Existing H2 data is not migrated. Members and accounts must be created again. The system account is recreated automatically by `SystemAccountInitializer`.
- `Instant` fields are stored as `timestamp with time zone` in PostgreSQL, which satisfies the "store all timestamps with time zone" rule.
- The database password is in plain text in `application.properties`. This is acceptable for local development only; move it to environment variables before deployment.
- Follow-up: replace `ddl-auto=update` with a migration tool such as Flyway so that schema changes to ledger tables are versioned.
