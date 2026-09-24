# Court Hearing Scheduler

A professional desktop application designed for law firms to efficiently track court cases, monitoring upcoming hearings.
The application integrates official court portals (portal.just.ro) and Google Calendar.

***
## Key Features
* **Client Management:** Create, view and manage clients and their contract details.
* **Case Tracking :** Register court cases linked to specific clients, select courts from a dropdown list, add custom notes and track delegation filing status.
* **Interactive Dashboard:** 
    - **7-Day Hearing Calendar:** Upcoming court hearings scheduled for the next 7 days.
    - **Advanced Search and Filtering:** Quickly look up cases by case number of filter cases by individual clients.
* **Court Portal Integration:** Service integration layer for fetching case details and hearing schedules programmatically from the official Romanian Ministry of Justice portal.
* **Google Calendar Ready:** Architecture structured for future event synchronization with Google Calendar API.

***
## Tech Stack
* **Desktop UI:** JavaFX 21 (FXML-based responsive layout)
* **Backend Core:** Spring Boot 3.2.3 (Dependency Injection & Application Lifecycle)
* **Persistence Layer:** Spring Data JPA / Hibernate
* **Database:** PostgreSQL (Containerized via Docker)
* **Build System:** Apache Maven
* **Utilities:** Lombok for boilerplate reduction

***
## Project Architecture
Responsibilities are separated into distinct packages:
```text
src/main/java/com/legal/courtscheduler/
│
├── controller/        # JavaFX UI Controllers (MainController)
├── entity/            # JPA Entities (Client, TrackedCase, Hearing)
├── repository/        # Spring Data JPA Repositories
├── service/           # Business Logic & External API Handlers
├── CourtSchedulerApplication.java # Spring Boot Bootstrap
└── JavaFxApplication.java         # JavaFX Launcher & Spring Context Integration
```
***
## Getting Started & Prerequisites
1. Java Version: Ensure JDK 17 or higher
2. Database Setup: Run the PostgreSQL container locally using Docker:

```
docker run --name my-postgres -e POSTGRES_PASSWORD=secret -p 5432:5432 -d postgres
```

3. Build and Run: Open the project in IntelliJ IDEA, let Maven resolve dependencies and run `CourtSchedulerApplication`.