# Quick Start Guide

This repository contains two implementations of the same Tutorial CRUD API:
1. **Java Spring Boot** (original, on `master` branch)
2. **Python FastAPI** (new, on `cursor/python-implementation-709b` branch)

## Python FastAPI Implementation (Recommended for Quick Start)

### Prerequisites
- Python 3.8 or higher
- pip (Python package manager)

### Installation & Running

```bash
# 1. Clone the repository
git clone https://github.com/snapxmin/spring-boot-h2-database-crud.git
cd spring-boot-h2-database-crud

# 2. Switch to Python implementation branch
git checkout cursor/python-implementation-709b

# 3. (Optional) Create virtual environment
python3 -m venv venv
source venv/bin/activate  # On Windows: venv\Scripts\activate

# 4. Install dependencies
pip install -r requirements.txt

# 5. Run the application
python3 run.py
```

The server will start at: **http://localhost:8080**

### Access API Documentation
- **Swagger UI**: http://localhost:8080/docs (Interactive API testing)
- **ReDoc**: http://localhost:8080/redoc (Clean API documentation)

### Quick API Test

```bash
# Create a tutorial
curl -X POST "http://localhost:8080/api/tutorials" \
  -H "Content-Type: application/json" \
  -d '{"title":"Python Basics","description":"Learn Python programming","published":false}'

# Get all tutorials
curl "http://localhost:8080/api/tutorials"

# Search tutorials by title
curl "http://localhost:8080/api/tutorials?title=python"

# Get tutorial by ID
curl "http://localhost:8080/api/tutorials/1"

# Update tutorial
curl -X PUT "http://localhost:8080/api/tutorials/1" \
  -H "Content-Type: application/json" \
  -d '{"title":"Python Advanced","description":"Advanced Python","published":true}'

# Get published tutorials
curl "http://localhost:8080/api/tutorials/published"

# Delete tutorial
curl -X DELETE "http://localhost:8080/api/tutorials/1"
```

### Run Tests

```bash
pytest tests/ -v
```

---

## Java Spring Boot Implementation (Original)

### Prerequisites
- Java 17 or higher
- Maven 3.6+ (or use included mvnw)

### Installation & Running

```bash
# 1. Clone the repository
git clone https://github.com/snapxmin/spring-boot-h2-database-crud.git
cd spring-boot-h2-database-crud

# 2. Ensure you're on master branch
git checkout master

# 3. Run the application
./mvnw spring-boot:run
# Or on Windows: mvnw.cmd spring-boot:run
```

The server will start at: **http://localhost:8080**

### Access H2 Console
- **H2 Database Console**: http://localhost:8080/h2-ui
  - JDBC URL: `jdbc:h2:file:./testdb`
  - Username: `sa`
  - Password: (leave empty)

### Quick API Test

```bash
# Same curl commands as above - APIs are identical!
curl -X POST "http://localhost:8080/api/tutorials" \
  -H "Content-Type: application/json" \
  -d '{"title":"Java Basics","description":"Learn Java programming","published":false}'

curl "http://localhost:8080/api/tutorials"
```

### Run Tests

```bash
./mvnw test
```

---

## Side-by-Side Comparison

| Feature | Spring Boot | FastAPI |
|---------|-------------|---------|
| Startup Time | 2-3 seconds | < 1 second |
| Lines of Code | More verbose | More concise |
| Auto API Docs | Requires setup | Built-in |
| Database Console | H2 Console | SQLite CLI |
| Memory Usage | ~150-200 MB | ~30-50 MB |

---

## API Endpoints (Same for Both)

All endpoints are prefixed with `/api`:

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/tutorials` | Create new tutorial |
| GET | `/tutorials` | Get all tutorials |
| GET | `/tutorials?title=search` | Search by title |
| GET | `/tutorials/{id}` | Get tutorial by ID |
| PUT | `/tutorials/{id}` | Update tutorial |
| DELETE | `/tutorials/{id}` | Delete tutorial |
| DELETE | `/tutorials` | Delete all tutorials |
| GET | `/tutorials/published` | Get published tutorials |

---

## Choose Your Implementation

### Use Python FastAPI if you want:
- ✅ Faster startup and development
- ✅ Auto-generated interactive docs
- ✅ Modern Python async features
- ✅ Lighter memory footprint
- ✅ Simpler codebase

### Use Java Spring Boot if you want:
- ✅ Enterprise-grade features
- ✅ Extensive Spring ecosystem
- ✅ Production-proven at scale
- ✅ Strong type safety
- ✅ H2 console for DB inspection

---

## Project Structure Quick Reference

### Python FastAPI
```
app/
├── main.py          # Application entry
├── database.py      # DB configuration
├── models.py        # SQLAlchemy models
├── schemas.py       # Pydantic schemas
└── routers/
    └── tutorial.py  # API endpoints
```

### Java Spring Boot
```
src/main/java/.../
├── SpringBootJpaH2Application.java
├── controller/
│   └── TutorialController.java
├── model/
│   └── Tutorial.java
└── repository/
    └── TutorialRepository.java
```

---

## Next Steps

1. **Try the APIs**: Use the interactive Swagger UI (FastAPI) or curl commands
2. **Read the code**: Compare implementations in `COMPARISON.md`
3. **Extend**: Add your own features to either implementation
4. **Deploy**: Both are ready for production deployment

## Getting Help

- FastAPI Documentation: https://fastapi.tiangolo.com/
- Spring Boot Documentation: https://spring.io/projects/spring-boot
- Repository Issues: https://github.com/snapxmin/spring-boot-h2-database-crud/issues

Happy coding! 🚀
