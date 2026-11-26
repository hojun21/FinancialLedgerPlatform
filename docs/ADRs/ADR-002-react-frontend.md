# ADR-002: Considered React, kept Thymeleaf for screens

- Status: Rejected (React). Thymeleaf stays, as originally planned.
- Date: 2026-11-25
- Stage: Milestone 1, after the PostgreSQL switch in Phase 5

## Context
We wanted screens (sign up, log in, account list, deposit, transfer, trial balance) earlier than Phase 7, and briefly chose a React (Vite) SPA in `frontend/`.

Looking at what React would require, most of the new work was not about the ledger:
- Spring Security would need JSON login/logout responses, a 401 entry point instead of a login redirect, and a CSRF strategy for a cookie-based SPA.
- A dev proxy (or CORS) and a second toolchain (Node, npm) would be needed.
- The goal of this step is to practice the Spring side: how a button turns into an HTTP request, how a controller handles it, and how the result comes back to the page.

## Decision
- Keep Thymeleaf (server-side rendering). Build the screens now instead of waiting for Phase 7.
- Page routes live under `/web/**` in a separate `@Controller` (`PageController`). The existing JSON API (`/accounts/**`, `/signup`, `/ledger/**`) stays unchanged, so both can coexist and the API can still be tested with Postman.
- Use Spring Security's form login with a custom page (`/web/login`). The session cookie and CSRF token work with no extra code, because Thymeleaf adds the CSRF hidden field to every form that uses `th:action`.
- Start from a static shell (layout, pages, unwired buttons). Wire each button to the backend as a learning exercise.

## Alternatives
- **React (Vite) SPA:** more modern and reusable for a later ledger visualization, but needs the security and tooling work listed above. Revisit if the visualization project needs it; the JSON API is already there.
- **Thymeleaf pages calling the JSON API with `fetch()`:** possible, but mixes two styles and skips learning the standard form → controller → redirect flow.

## Consequences
- New dependency: `spring-boot-starter-thymeleaf`.
- Every screen action has two entry points: the JSON API and the page controller. Both must call the same service methods so business rules (balance check, double entry) live in one place.
- Form submissions follow Post/Redirect/Get: handle the POST, then redirect, so refreshing the page does not resubmit a deposit or transfer. Real protection against double submission still comes from the idempotency key in Phase 6.
