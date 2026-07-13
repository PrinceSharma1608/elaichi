# 🍃 Jishu Hozen Backend

Robust, high-performance Java Spring Boot backend engine powering the Tata Motors Lucknow **Jishu Hozen (JH) Management Portal**. Built to streamline autonomous maintenance, Elaichi drives the plant floor's digital transformation in the Digital Manufacturing Lab.

## 🚀 Key Features

* **Auto-Missed Scheduler**: An automated cron scheduler executing nightly at 8:00 PM (Kolkata Zone) that identifies incomplete daily maintenance tasks, updates their status to `MISSED`, increments the delay counter, and inserts an audit log entry with `"Auto marks as MISSED"`.
* **Aggregated Metrics & Aggregation Engine**: Group-by native SQL aggregates and projections preventing duplicate counts when a single machine has multiple maintenance frequencies or checklists.
* **Role-Based Scope Security**: Dedicated REST controllers exposing resource filters for Line Incharges, Supervisors, Team Leaders, and Jishu Hozen Owners.
* **Database Mapping & History Logs**: Automatically logs completed checklists, Operator remarks, completion timestamps, and audit results.

## 🛠️ Tech Stack

* **Language**: Java 21
* **Framework**: Spring Boot 3.x
* **Database**: PostgreSQL (Hosted on Neon)
* **ORM**: Spring Data JPA & Hibernate
* **Dependency & Build Management**: Maven
* **Build Utility**: Lombok (for boilerplate-free models and builders)

## 📦 Directory Structure

```
JishuHozen/
├── src/main/java/tata/JishuHozen/
│   ├── Controller/      # REST API endpoints (Dashboard, Auth, Users, Audits)
│   ├── DTO/             # Data Transfer Objects for clean payloads
│   ├── Entity/          # JPA Entity definitions (MaintenanceLogs, machines, etc.)
│   ├── Repository/      # Spring Data Repositories with custom JPQL/native queries
│   └── services/        # Core business logic (MaintenanceService, Scheduler, UserService)
└── pom.xml              # Project dependencies
```

## ⚙️ Development Setup

### Prerequisites
* Java 21 JDK
* Maven 3.x

### Run Local Server
```bash
./mvnw spring-boot:run
or 
mvn.cmd spring-boot:run

```
The server will boot up and start listening currently on port `1608`.
