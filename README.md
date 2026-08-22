# CareerNest - Employers and Job Seekers Platform

CareerNest is a job portal that connects **job seekers** with **employers**. Users
register as a Job Seeker or an Employer and then get role-specific features:

- **Job Seekers** browse and search job listings, view details, and apply.
- **Employers** post and manage job listings, and review the applications they receive.

SMS notifications are sent on key events (registration, a new application, and an
application status change).

## Deployment URLs

- Backend API: **TBD (added after deployment to Render)**
- Frontend: **TBD (added after deployment to Vercel/Netlify)**

## Tech Stack

- **Backend:** Java 21, Spring Boot 3.5.16, Spring Security + JWT, Spring Data MongoDB
- **Database:** MongoDB (Atlas)
- **SMS:** Twilio (pluggable - disabled and logged until credentials are set)
- **Frontend:** React + Redux Toolkit + TypeScript + Tailwind CSS (Vite)
- **Docs:** Swagger / OpenAPI, Postman

## Repository Structure

```
backend/    Spring Boot REST API (this is the deployable service)
frontend/   React + TypeScript single-page app
```

## Roles and Access

Every endpoint except register and login needs a JWT. Access is role-based:

- Posting, editing, deleting jobs and reviewing applications: **EMPLOYER**
- Applying to jobs and viewing your own applications: **JOB_SEEKER**
- Browsing and viewing jobs: any logged-in user

Employers can only edit or delete their own postings and only see applications to
their own jobs.

## Backend

### Configuration (environment variables)

The backend reads everything sensitive from the environment - nothing is committed.
See `.env.example`.

```
MONGODB_URI         MongoDB Atlas connection string (with /careernest_db)
MONGODB_DATABASE    careernest_db   (optional; this is the default)
JWT_SECRET          a long random string (32+ characters)
TWILIO_ACCOUNT_SID  optional - leave blank to disable SMS
TWILIO_AUTH_TOKEN   optional
TWILIO_FROM_NUMBER  optional - your Twilio sender number
```

### Run locally

```
cd backend
# set the env vars above (PowerShell: $env:MONGODB_URI="..."; ...), then:
./mvnw spring-boot:run
```

The API starts on http://localhost:8080 (Swagger UI at `/swagger-ui.html`).

### API Endpoints

All under `/api`. Send the token as `Authorization: Bearer <token>`.

**Auth**
- `POST /api/auth/register` - create a user `{ name, email, password, phone, role }` (role = JOB_SEEKER or EMPLOYER)
- `POST /api/auth/login` - login, returns a JWT

**Jobs**
- `POST /api/jobs` - create a job (EMPLOYER)
- `PUT /api/jobs/{id}` - edit a job (EMPLOYER, owner only)
- `DELETE /api/jobs/{id}` - delete a job (EMPLOYER, owner only; its applications are removed too)
- `GET /api/jobs/mine` - the employer's own jobs (EMPLOYER)
- `GET /api/jobs?location=&keyword=&page=&size=` - search/browse (keyword matches title or description)
- `GET /api/jobs/{id}` - one job

**Applications**
- `POST /api/applications` - apply to a job `{ jobId, coverLetter? }` (JOB_SEEKER, one per job)
- `GET /api/applications/mine` - the seeker's own applications (JOB_SEEKER)
- `GET /api/applications/employer` - every application across the employer's jobs (EMPLOYER)
- `GET /api/applications/job/{jobId}` - applications for one job (EMPLOYER, owner only)
- `PATCH /api/applications/{id}/status` - set REVIEWED / ACCEPTED / REJECTED (EMPLOYER)

### SMS notifications (Twilio)

SMS is wired but stays **disabled** and only logs messages until the Twilio
credentials are set. To turn on real SMS, set `TWILIO_ACCOUNT_SID`,
`TWILIO_AUTH_TOKEN` and `TWILIO_FROM_NUMBER` (locally or in Render) and restart -
the app logs `Twilio SMS enabled` on boot. Notes: a Twilio trial account can only
text verified numbers, and phone numbers should be stored in E.164 form (`+9198...`).

### Testing

- Swagger UI at `/swagger-ui.html` (Authorize with your token).
- Postman: import `CareerNest.postman_collection.json`. Run Register Employer and
  Register Seeker first to capture the two role tokens.

## Frontend

A Vite + React + TypeScript app using Redux Toolkit for state and Tailwind CSS for
styling.

```
cd frontend
npm install
# set VITE_API_BASE_URL (see .env.example) to the backend URL
npm run dev
```

Pages: register/login, a jobs list with location/keyword search and a job detail
view, an "apply" flow and a seeker's applications page, and an employer dashboard to
post jobs and review applications.

## Deployment

**Backend on Render (Docker):**
1. Push the repo to GitHub (public).
2. Render -> New -> Web Service -> connect the repo. Set **Root Directory** to
   `backend` (the Dockerfile lives there).
3. Add env vars: `MONGODB_URI`, `JWT_SECRET` (and the `TWILIO_*` vars when you want
   real SMS). Render provides `PORT` automatically.
4. In Atlas, allow `0.0.0.0/0` in Network Access so Render can connect.

**Frontend on Vercel/Netlify:**
1. Import the repo, set the project **Root Directory** to `frontend`.
2. Set `VITE_API_BASE_URL` to the deployed backend URL.
3. Build command `npm run build`, output directory `dist`.

## Design Decisions

- **MongoDB** documents fit the mostly-independent User/Job/Application data and the
  keyword/location search; the (jobId, seekerId) unique index makes "apply once" a
  database guarantee, not just a code check.
- **JWT with the userId as the subject and the role as the authority** keeps one
  identity model; `@PreAuthorize` gates EMPLOYER-only and JOB_SEEKER-only endpoints,
  and services additionally enforce ownership.
- **SMS is isolated behind one service** and degrades to logging when unconfigured,
  so the app runs and is testable without an SMS account; enabling it is env-only.
- **Twilio vs MSG91:** the brief names Twilio; it is used here. For Indian numbers a
  provider like MSG91 is often easier, and because SMS sits behind a single service
  it could be swapped without touching the rest of the code.
