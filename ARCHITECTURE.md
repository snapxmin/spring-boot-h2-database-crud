# Architecture Documentation

This document describes the architecture of both implementations in this repository.

## Overview

Both implementations follow the **3-tier layered architecture** pattern, with slight variations based on framework conventions.

## Spring Boot (Java) Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     Client (Browser/API)                     │
└─────────────────────────────────────────────────────────────┘
                              │
                              │ HTTP Request
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                   Presentation Layer                         │
│  ┌───────────────────────────────────────────────────────┐  │
│  │         @RestController                               │  │
│  │         TutorialController.java                       │  │
│  │                                                       │  │
│  │  - @GetMapping("/tutorials")                         │  │
│  │  - @PostMapping("/tutorials")                        │  │
│  │  - @PutMapping("/tutorials/{id}")                    │  │
│  │  - @DeleteMapping("/tutorials/{id}")                 │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                              │
                              │ @Autowired
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                   Data Access Layer                          │
│  ┌───────────────────────────────────────────────────────┐  │
│  │         @Repository                                   │  │
│  │         TutorialRepository.java                       │  │
│  │         extends JpaRepository<Tutorial, Long>         │  │
│  │                                                       │  │
│  │  - findByPublished(boolean)                          │  │
│  │  - findByTitleContainingIgnoreCase(String)           │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                              │
                              │ JPA/Hibernate
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      Domain Layer                            │
│  ┌───────────────────────────────────────────────────────┐  │
│  │         @Entity                                       │  │
│  │         Tutorial.java                                 │  │
│  │                                                       │  │
│  │  - id: long                                          │  │
│  │  - title: String                                     │  │
│  │  - description: String                               │  │
│  │  - published: boolean                                │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                              │
                              │ JDBC
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                    Database Layer                            │
│                   H2 Database (File)                         │
│                   jdbc:h2:file:./testdb                      │
└─────────────────────────────────────────────────────────────┘
```

## FastAPI (Python) Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     Client (Browser/API)                     │
└─────────────────────────────────────────────────────────────┘
                              │
                              │ HTTP Request
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                   Presentation Layer                         │
│  ┌───────────────────────────────────────────────────────┐  │
│  │         APIRouter                                     │  │
│  │         tutorial.py                                   │  │
│  │                                                       │  │
│  │  - @router.get("/tutorials")                         │  │
│  │  - @router.post("/tutorials")                        │  │
│  │  - @router.put("/tutorials/{id}")                    │  │
│  │  - @router.delete("/tutorials/{id}")                 │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                              │
                              │ Depends(get_db)
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                   Schema/Validation Layer                    │
│  ┌───────────────────────────────────────────────────────┐  │
│  │         Pydantic BaseModel                            │  │
│  │         schemas.py                                    │  │
│  │                                                       │  │
│  │  - TutorialBase                                      │  │
│  │  - TutorialCreate                                    │  │
│  │  - TutorialUpdate                                    │  │
│  │  - Tutorial (with validation)                        │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                              │
                              │ Session
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                   Data Access Layer                          │
│  ┌───────────────────────────────────────────────────────┐  │
│  │         Database Session                              │  │
│  │         database.py                                   │  │
│  │                                                       │  │
│  │  - SessionLocal (session maker)                      │  │
│  │  - get_db() (dependency)                             │  │
│  │  - engine (SQLAlchemy engine)                        │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                              │
                              │ SQLAlchemy ORM
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      Domain Layer                            │
│  ┌───────────────────────────────────────────────────────┐  │
│  │         SQLAlchemy Model                              │  │
│  │         models.py                                     │  │
│  │                                                       │  │
│  │  class Tutorial(Base):                               │  │
│  │    - id: Column(Integer, primary_key=True)           │  │
│  │    - title: Column(String)                           │  │
│  │    - description: Column(String)                     │  │
│  │    - published: Column(Boolean)                      │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                              │
                              │ SQLite Driver
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                    Database Layer                            │
│                   SQLite Database (File)                     │
│                   sqlite:///./testdb.db                      │
└─────────────────────────────────────────────────────────────┘
```

## Request Flow Comparison

### Spring Boot Request Flow

```
1. HTTP Request arrives
   └─> DispatcherServlet (Spring MVC)

2. Route to Controller
   └─> TutorialController.getAllTutorials()
       └─> @RequestParam validation

3. Business Logic (Controller)
   └─> tutorialRepository.findAll()
       └─> Spring Data JPA generates query

4. Database Query
   └─> Hibernate generates SQL
       └─> H2 Database executes

5. Entity Mapping
   └─> Tutorial entities returned
       └─> JPA hydrates objects

6. Response Serialization
   └─> Jackson converts to JSON
       └─> ResponseEntity<List<Tutorial>>

7. HTTP Response sent
```

### FastAPI Request Flow

```
1. HTTP Request arrives
   └─> Uvicorn ASGI Server

2. Route Matching
   └─> FastAPI router finds @router.get("/tutorials")
       └─> Path parameters extracted

3. Dependency Injection
   └─> Depends(get_db) creates database session
       └─> Query parameter validation

4. Request Validation
   └─> Pydantic validates query params
       └─> Type checking

5. Business Logic (Router)
   └─> db.query(Tutorial).all()
       └─> SQLAlchemy builds query

6. Database Query
   └─> SQLAlchemy generates SQL
       └─> SQLite executes

7. Response Validation
   └─> response_model=List[Tutorial]
       └─> Pydantic serializes to JSON

8. HTTP Response sent
```

## Key Differences

### Dependency Injection

**Spring Boot:**
- Uses `@Autowired` annotation
- Field/constructor injection
- Container-managed beans

**FastAPI:**
- Uses `Depends()` function
- Function parameter injection
- Explicit dependency graph

### Data Validation

**Spring Boot:**
- Bean Validation (@Valid, @NotNull, etc.)
- Runtime validation
- Manual error handling

**FastAPI:**
- Pydantic models
- Type-based validation
- Automatic error responses

### Database Access

**Spring Boot:**
- Repository pattern with Spring Data JPA
- Method name query derivation
- Automatic CRUD methods

**FastAPI:**
- Direct SQLAlchemy session usage
- Explicit query construction
- Manual CRUD implementation

### Configuration

**Spring Boot:**
- application.properties / application.yml
- Profile-based configuration
- Spring Boot auto-configuration

**FastAPI:**
- Python modules (database.py)
- Environment variables
- Explicit configuration

## Scalability Patterns

### Spring Boot

```
┌─────────────┐
│ Load        │
│ Balancer    │
└──────┬──────┘
       │
   ┌───┴───┬───────┬───────┐
   │       │       │       │
┌──▼──┐ ┌──▼──┐ ┌──▼──┐ ┌──▼──┐
│App  │ │App  │ │App  │ │App  │
│Inst1│ │Inst2│ │Inst3│ │Inst4│
└──┬──┘ └──┬──┘ └──┬──┘ └──┬──┘
   │       │       │       │
   └───┬───┴───────┴───────┘
       │
   ┌───▼────────┐
   │ Database   │
   │ (MySQL/    │
   │ PostgreSQL)│
   └────────────┘
```

### FastAPI

```
┌─────────────┐
│ Nginx       │
│ (Reverse    │
│  Proxy)     │
└──────┬──────┘
       │
   ┌───┴───┬───────┬───────┐
   │       │       │       │
┌──▼──┐ ┌──▼──┐ ┌──▼──┐ ┌──▼──┐
│Uvic │ │Uvic │ │Uvic │ │Uvic │
│orn  │ │orn  │ │orn  │ │orn  │
│Work1│ │Work2│ │Work3│ │Work4│
└──┬──┘ └──┬──┘ └──┬──┘ └──┬──┘
   │       │       │       │
   └───┬───┴───────┴───────┘
       │
   ┌───▼────────┐
   │ Database   │
   │ (PostgreSQL│
   │  or MySQL) │
   └────────────┘
```

## Testing Architecture

### Spring Boot

```
JUnit 5 + Spring Boot Test
    │
    ├─> @SpringBootTest
    │   └─> Full application context
    │
    ├─> @WebMvcTest
    │   └─> Controller layer tests
    │
    └─> @DataJpaTest
        └─> Repository layer tests
```

### FastAPI

```
pytest + TestClient
    │
    ├─> TestClient(app)
    │   └─> Full application tests
    │
    ├─> Dependency Overrides
    │   └─> Mock database for testing
    │
    └─> Fixture-based setup
        └─> Test database isolation
```

## Security Considerations

Both implementations should add:
- Authentication (JWT, OAuth2)
- Authorization (Role-based access)
- Input validation (already present in FastAPI via Pydantic)
- SQL injection prevention (handled by ORM in both)
- CORS configuration (already present)
- Rate limiting
- HTTPS enforcement

## Monitoring & Observability

### Spring Boot
- Spring Boot Actuator
- Micrometer metrics
- Distributed tracing (Sleuth)

### FastAPI
- Prometheus metrics
- OpenTelemetry
- Custom middleware for logging

## Conclusion

Both architectures achieve the same goals using language-specific best practices:

- **Spring Boot**: Enterprise-grade, convention-heavy, extensive ecosystem
- **FastAPI**: Modern, explicit, high-performance, developer-friendly

The choice depends on team expertise, ecosystem requirements, and specific use cases.
