# ADR-002: React (Vite) frontend instead of Thymeleaf

- Status: Accepted
- Date: 2026-11-25
- Stage: Milestone 1, between Phase 5 (PostgreSQL done) and the rest of Phase 5

## Context
Milestone 1 originally planned screens with Thymeleaf in Phase 7, because it fits session login out of the box.

We want the screens now (sign up, log in, account list, deposit, transfer, trial balance), and we want them in React:
- Portfolio value: a separate SPA consuming a JSON API is the common setup in modern banking front offices.
- A later zero-sum ledger visualization will be built in React anyway, so the same frontend can host it.
- The backend is already a JSON API (`AccountController` returns DTOs, errors are `ProblemDetail`), so no server-rendered views exist to throw away.

## Decision
- Create a Vite + React app in `frontend/` inside this repository (one repo, two apps).
- Keep **session-cookie authentication** (Spring Security session, `JSESSIONID`, HttpOnly). No JWT. A bank-style app gains nothing from tokens stored in JavaScript, and sessions are what the backend already does.
- The frontend calls `/api/**`. In development, the Vite dev server proxies `/api/**` to `http://localhost:8080` and strips the `/api` prefix. The browser sees a single origin, so **no CORS configuration** is needed, and React page routes (e.g. `/accounts`) never collide with backend routes.
- Replace browser-oriented login behavior with JSON-friendly behavior: login and logout return status codes instead of redirects, and a `GET /me` endpoint tells the SPA who is logged in.
- Build the screens before finishing Phase 5 (locking tests) and Phase 6 (idempotency).

## Alternatives
- **Thymeleaf (original plan):** least security work, but less relevant for the portfolio and not reusable for the visualization.
- **JWT in localStorage:** stateless, but exposes the token to any XSS and needs refresh-token handling. Not worth it here.
- **Separate repository for the frontend:** cleaner separation, but more overhead for a one-person project.
- **Prefix all backend routes with `/api`:** works, but changes every controller mapping and test. The proxy rewrite gives the same result with no backend changes.

## Consequences
- Spring Security config changes: JSON login/logout responses, an authentication entry point that returns 401 instead of redirecting to a login page, and a proper CSRF strategy for a cookie-based SPA (the current blanket CSRF exemption for `/accounts/**` must go).
- Node.js is required for frontend development.
- Phase 6 will add an `Idempotency-Key` header to transfers, so the transfer screen will need a small update then.
- Production would need the same `/api` routing (e.g. a reverse proxy, or serving the built frontend from Spring). Out of scope for Milestone 1.
