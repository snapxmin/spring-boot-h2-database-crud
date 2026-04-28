# Tutorial CRUD API - Dual Implementation Repository

This repository contains **two complete implementations** of the same Tutorial CRUD REST API:

1. **Java Spring Boot** with JPA + H2 Database (original implementation)
2. **Python FastAPI** with SQLAlchemy + SQLite (new implementation)

Both implementations provide identical API endpoints and functionality, allowing you to compare approaches, choose your preferred stack, or learn equivalent patterns across languages.

---

## 🚀 Quick Start

### Python FastAPI (Recommended for Quick Testing)

```bash
# Switch to Python implementation branch
git checkout cursor/python-implementation-709b

# Install dependencies
pip install -r requirements.txt

# Run the server
python3 run.py

# Access interactive API docs
# http://localhost:8080/docs
```

### Java Spring Boot (Original)

```bash
# Ensure you're on master branch
git checkout master

# Run the application
./mvnw spring-boot:run

# Access H2 console
# http://localhost:8080/h2-ui
```

**📖 For detailed instructions, see [QUICKSTART.md](QUICKSTART.md)**

---

## 📊 Comparison

| Feature | Spring Boot | FastAPI |
|---------|-------------|---------|
| **Language** | Java 17 | Python 3.8+ |
| **Framework** | Spring Boot 3.1.0 | FastAPI 0.109.0 |
| **Database** | H2 (in-memory) | SQLite (file-based) |
| **ORM** | Hibernate/JPA | SQLAlchemy 2.0 |
| **Startup Time** | 2-3 seconds | < 1 second |
| **Memory Usage** | ~150-200 MB | ~30-50 MB |
| **API Docs** | Manual setup | Auto-generated |
| **Lines of Code** | 213 | 161 (24% less) |

**📖 For detailed comparison, see [COMPARISON.md](COMPARISON.md)**

---

## 🎯 API Endpoints

Both implementations expose identical REST API endpoints at `/api`:

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/tutorials` | Create a new tutorial |
| GET | `/tutorials` | Get all tutorials |
| GET | `/tutorials?title=search` | Search tutorials by title |
| GET | `/tutorials/{id}` | Get tutorial by ID |
| PUT | `/tutorials/{id}` | Update a tutorial |
| DELETE | `/tutorials/{id}` | Delete a tutorial |
| DELETE | `/tutorials` | Delete all tutorials |
| GET | `/tutorials/published` | Get published tutorials only |

---

## 🏗️ Architecture

Both implementations follow a **3-tier layered architecture**:

### Spring Boot (Java)
```
Controller → Repository → Entity
  ↓            ↓           ↓
Request    Database    Table
```

### FastAPI (Python)
```
Router → Database Session → Model
  ↓            ↓              ↓
Schema    SQLAlchemy      Table
```

---

## 📁 Project Structure

<table>
<tr>
<th>Spring Boot (master)</th>
<th>FastAPI (cursor/python-implementation-709b)</th>
</tr>
<tr>
<td>

```
src/main/java/
├── controller/
│   └── TutorialController.java
├── model/
│   └── Tutorial.java
├── repository/
│   └── TutorialRepository.java
└── SpringBootJpaH2Application.java
```

</td>
<td>

```
app/
├── main.py
├── database.py
├── models.py
├── schemas.py
└── routers/
    └── tutorial.py
```

</td>
</tr>
</table>

---

## ✅ Features

### Common Features (Both Implementations)
- ✅ Full CRUD operations
- ✅ Search by title (case-insensitive)
- ✅ Filter by published status
- ✅ CORS enabled
- ✅ Exception handling
- ✅ File-based database
- ✅ CI/CD pipeline (GitHub Actions)

### Spring Boot Specific
- ✅ H2 Database Console (`/h2-ui`)
- ✅ Spring Data JPA query methods
- ✅ Enterprise-grade ecosystem
- ✅ Traditional MVC pattern

### FastAPI Specific
- ✅ Auto-generated Swagger UI (`/docs`)
- ✅ Auto-generated ReDoc (`/redoc`)
- ✅ Pydantic validation
- ✅ Native async support
- ✅ Faster startup time

---

## 🧪 Testing

### Python FastAPI
```bash
pytest tests/ -v
# 5 passed in 0.47s

# Or run manual API tests
./test_api_manual.sh
```

### Java Spring Boot
```bash
./mvnw test
```

---

## 📖 Documentation

| Document | Description |
|----------|-------------|
| [QUICKSTART.md](QUICKSTART.md) | Quick start guide for both implementations |
| [COMPARISON.md](COMPARISON.md) | Detailed side-by-side comparison |
| [README_PYTHON.md](README_PYTHON.md) | Python FastAPI specific documentation |
| [Original README](https://www.bezkoder.com/spring-boot-jpa-h2-example/) | Spring Boot tutorial link |

---

## 🎓 Learning Resources

This repository is perfect for:
- **Comparing** Java and Python web frameworks
- **Learning** REST API design patterns
- **Understanding** equivalent concepts across languages
- **Evaluating** technology choices for your project

### Related Tutorials

**Spring Boot:**
- [Spring Boot + JPA + H2 Tutorial](https://www.bezkoder.com/spring-boot-jpa-h2-example/)
- [Spring Boot + MySQL](https://www.bezkoder.com/spring-boot-jpa-crud-rest-api/)
- [Spring Boot + PostgreSQL](https://www.bezkoder.com/spring-boot-postgresql-example/)

**Frontend Integration:**
- [Angular + Spring Boot](https://www.bezkoder.com/angular-spring-boot-crud/)
- [React + Spring Boot](https://www.bezkoder.com/react-spring-boot-crud/)
- [Vue + Spring Boot](https://www.bezkoder.com/spring-boot-vue-js-crud-example/)

---

## 🚀 Deployment

### FastAPI (Python)
```bash
# Production with Uvicorn
uvicorn app.main:app --host 0.0.0.0 --port 8080 --workers 4

# Or with Gunicorn
gunicorn app.main:app -w 4 -k uvicorn.workers.UvicornWorker
```

### Spring Boot (Java)
```bash
# Build JAR
./mvnw clean package

# Run JAR
java -jar target/spring-boot-jpa-h2-0.0.1-SNAPSHOT.jar
```

---

## 🤝 Contributing

Contributions are welcome! Feel free to:
- Report bugs
- Suggest features
- Submit pull requests
- Improve documentation

---

## 📝 License

This project is open source and available under standard licensing terms.

---

## 🌟 Why Two Implementations?

Having both implementations allows developers to:

1. **Learn by Comparison**: See how the same concepts are implemented in different languages
2. **Technology Evaluation**: Compare performance, code style, and developer experience
3. **Team Flexibility**: Choose the stack that matches your team's expertise
4. **Best of Both Worlds**: Use Spring Boot for enterprise apps, FastAPI for microservices

---

## 📞 Support

- **FastAPI Docs**: https://fastapi.tiangolo.com/
- **Spring Boot Docs**: https://spring.io/projects/spring-boot
- **Issues**: https://github.com/snapxmin/spring-boot-h2-database-crud/issues

---

<div align="center">

**Choose Your Path:**

[🐍 Python FastAPI →](https://github.com/snapxmin/spring-boot-h2-database-crud/tree/cursor/python-implementation-709b) | [☕ Java Spring Boot →](https://github.com/snapxmin/spring-boot-h2-database-crud)

</div>
