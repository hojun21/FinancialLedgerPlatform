# Milestone 1 — Login and internal transfer

**Status:** In progress (Phase 1, writing MemberService)

Goal: user signs up, opens an account, sends money to another user, and the other user sees it in their transaction history. Money must never be off by 1 won, even with concurrent or duplicate requests.

Done when:

- [ ] Sign up → open account → transfer → receiver checks history works on screen
- [ ] 100 concurrent withdrawals on the same account: balance is correct and never negative
- [ ] Sum of debits = sum of credits across the whole ledger
- [ ] Same idempotency key sent twice is processed only once

Decisions:

- Money is `long` (won). No double/float.
- Time is `Instant` (stored with time zone).
- H2 for now, switch to PostgreSQL in Phase 5 (locks need a real DB).
- Packages by layer: domain, repository, service, controller, config.
- Screens with Thymeleaf (fits session login).


## Phase 1 - In progress

Sign up and log in with members stored in the DB.

- [x] Default Security login page (auto-configuration)
- [x] Custom `SecurityFilterChain` (`/signup`, `/error` open)
- [x] JPA + H2 + Lombok, `BaseTimeEntity` with auditing, `Member` entity
- [x] `MemberRepository` with `findByUsername`
- [x] BCrypt `PasswordEncoder` bean
- [x] `MemberService.signup()`. Reject duplicate username, hash password before saving.
- [x] `SignupController` for `/signup`. Show a proper error on duplicate username. (`existsByUsername` is only for the message, the real guard is the DB UNIQUE constraint)
- [x] `UserDetailsService` so login checks our `members` table instead of the in-memory user.
- [x] Get the logged-in user in a controller (`@AuthenticationPrincipal`).

Check: log in with a signed-up account. DB only has the BCrypt hash, never the raw password.


## Phase 2 - Not started

Accounts.

- [ ] `Account` entity. Account number (UNIQUE), owner (`@ManyToOne` Member), balance `long`.
- [ ] Decide account number format.
- [ ] API to open an account.
- [ ] List my accounts / view one. Can't see other users' accounts.


## Phase 3 - Not started

Ledger with double-entry bookkeeping. Ledger rows are never updated or deleted, mistakes are fixed with a reversing entry.

- [ ] `JournalEntry` 1 : N `LedgerLine` (debit / credit lines).
- [ ] Make ledger immutable. No setters, `updatable = false`, no delete API.
- [ ] System account as the other side for outside money.
- [ ] Test deposit. Journal entry + balance update in one transaction.
- [ ] Check debit sum = credit sum when creating an entry.
- [ ] Query that checks debit sum = credit sum for the whole ledger.


## Phase 4 - Not started

Internal transfer.

- [ ] `TransferService`. Withdraw + deposit + journal entry in one transaction.
- [ ] Throw on insufficient balance, same account, amount <= 0. Check everything rolls back.
- [ ] Transfer API with request DTO validation (`@Valid`).
- [ ] `@RestControllerAdvice` to turn exceptions into consistent error responses.


## Phase 5 - Not started

Concurrency. No `synchronized`, only DB locks.

- [ ] Switch to PostgreSQL (Docker).
- [ ] Test with 100 concurrent withdrawals. Run it without locks first and watch it break.
- [ ] Pessimistic lock (`@Lock(PESSIMISTIC_WRITE)`, `SELECT ... FOR UPDATE`). Test should pass.
- [ ] Lock order: smaller account ID first. Test A→B and B→A at the same time, no deadlock.
- [ ] (Optional) Compare with optimistic lock `@Version`.


## Phase 6 - Not started

Idempotency.

- [ ] `Idempotency-Key` header + idempotency key table with UNIQUE.
- [ ] Same key again: don't process, return the previous result.
- [ ] Test the same key sent concurrently.


## Phase 7 - Not started

Transaction history and screens.

- [ ] History API with paging (`Pageable`), my accounts only.
- [ ] Screens: sign up, login, account list, transfer, history.
- [ ] Run the whole flow: sign up → open account → transfer → receiver checks history.
