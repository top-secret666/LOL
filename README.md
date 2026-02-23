
<div align="center">
<img src="https://github.com/user-attachments/assets/0b685d4c-231b-4126-8a3d-06975e64d41b"/>

# Tournament Arena

**Full-stack LoL tournament platform**: Spring Boot (API + JWT) + MySQL + Next.js (front).

</div>

---

## Quest Log
This repository contains 3 artifacts:
- `rgr/` — backend API (**Spring Boot 3**, **Java 17**, **Spring Security**, **JPA**, **JWT**)
- `front/` — UI (**Next.js 15**, **React 19**, **TypeScript**, **Tailwind**)
- `database/` — MySQL schema + seed data

---

## Tech Stack
- **Backend**: Java 17, Spring Boot 3.5, Spring Web, Spring Security, Spring Validation, Spring Data JPA, JWT (jjwt)
- **DB**: MySQL (`loltournament`)
- **Frontend**: Next.js 15.2.4, React 19, TypeScript, Tailwind + Radix UI

<p align="center">
	<img src="https://github.com/user-attachments/assets/35960604-ce6d-4908-b644-82fae08aa006"/>
</p>

---
## Quickstart (Local)
### 0) Requirements
- Java 17+
- Maven 3.9+
- Node.js 18+ (or 20+)
- MySQL 8+

### 1) Database
Create schema and seed data:
```bash
# from repo root
# run this in your MySQL client (Workbench / CLI)
# database/loltournament.sql
```
Ensure the database name is `loltournament`.

### 2) Backend (Spring Boot)
The backend listens on port **8080**.

Run:
```bash
cd rgr
mvn spring-boot:run
```

Config lives in:
- `rgr/src/main/resources/application.properties`
- `rgr/src/main/resources/application.yml`

Important: credentials in these files are meant for **local development**. For real use, keep secrets out of git (env vars / local override).

### 3) Frontend (Next.js)
Run dev server:
```bash
cd front
npm install
npm run dev
```

Default Next.js dev URL:
- http://localhost:3000

---

## Project Structure
- `database/loltournament.sql` — schema + seed data
- `rgr/` — Spring Boot backend
- `front/` — Next.js frontend

---

## API Notes
- Auth uses **JWT** (see `loltournament.app.jwtSecret` and expiration settings in backend config).
- If you change DB connection settings, update datasource url/username/password in the backend config.

---

<div align="center">

**GG. Next quest: add `.env.example` + docker-compose.**

</div>
