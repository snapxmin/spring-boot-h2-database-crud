# FastAPI SQLite Tutorial CRUD API

This is a Python FastAPI implementation converted from the original Spring Boot JPA + H2 example. It provides the same REST API endpoints for managing tutorials using SQLAlchemy ORM with SQLite database.

## Technology Stack

- **Framework**: FastAPI 0.109.0
- **Python Version**: 3.8+
- **Database**: SQLite (file-based)
- **ORM**: SQLAlchemy 2.0
- **Validation**: Pydantic 2.5
- **Server**: Uvicorn

## Architecture

This application follows a 3-tier layered architecture:

### 1. Presentation Layer (Router/Controller)
- `app/routers/tutorial.py` - REST endpoints for tutorial operations

### 2. Data Access Layer (Models & Database)
- `app/models.py` - SQLAlchemy ORM models
- `app/database.py` - Database configuration and session management

### 3. Schema/DTO Layer
- `app/schemas.py` - Pydantic models for request/response validation

## Project Structure

```
.
├── app/
│   ├── __init__.py
│   ├── main.py              # FastAPI application entry point
│   ├── database.py          # Database configuration
│   ├── models.py            # SQLAlchemy ORM models
│   ├── schemas.py           # Pydantic schemas
│   └── routers/
│       ├── __init__.py
│       └── tutorial.py      # Tutorial REST endpoints
├── requirements.txt         # Python dependencies
├── run.py                   # Development server script
├── .env.example            # Environment variables template
└── README_PYTHON.md        # This file
```

## Installation

1. Create a virtual environment:
```bash
python -m venv venv
source venv/bin/activate  # On Windows: venv\Scripts\activate
```

2. Install dependencies:
```bash
pip install -r requirements.txt
```

## Running the Application

### Option 1: Using the run script
```bash
python run.py
```

### Option 2: Using uvicorn directly
```bash
uvicorn app.main:app --host 0.0.0.0 --port 8080 --reload
```

The application will start at `http://localhost:8080`

## API Endpoints

All endpoints are prefixed with `/api`:

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/tutorials` | Get all tutorials (optional `?title=` query param for filtering) |
| GET | `/api/tutorials/{id}` | Get a specific tutorial by ID |
| POST | `/api/tutorials` | Create a new tutorial |
| PUT | `/api/tutorials/{id}` | Update an existing tutorial |
| DELETE | `/api/tutorials/{id}` | Delete a tutorial by ID |
| DELETE | `/api/tutorials` | Delete all tutorials |
| GET | `/api/tutorials/published` | Get all published tutorials |

## API Documentation

FastAPI automatically generates interactive API documentation:

- **Swagger UI**: http://localhost:8080/docs
- **ReDoc**: http://localhost:8080/redoc

## Database

- **Type**: SQLite (file-based at `./testdb.db`)
- **Schema Management**: Auto-create on startup
- **SQL Logging**: Enabled (set `echo=True` in `database.py`)

## Example API Requests

### Create a Tutorial
```bash
curl -X POST "http://localhost:8080/api/tutorials" \
  -H "Content-Type: application/json" \
  -d '{"title":"Python Tutorial","description":"Learn Python","published":false}'
```

### Get All Tutorials
```bash
curl "http://localhost:8080/api/tutorials"
```

### Get Tutorial by ID
```bash
curl "http://localhost:8080/api/tutorials/1"
```

### Update a Tutorial
```bash
curl -X PUT "http://localhost:8080/api/tutorials/1" \
  -H "Content-Type: application/json" \
  -d '{"title":"Updated Title","description":"Updated Description","published":true}'
```

### Delete a Tutorial
```bash
curl -X DELETE "http://localhost:8080/api/tutorials/1"
```

### Search Tutorials by Title
```bash
curl "http://localhost:8080/api/tutorials?title=python"
```

### Get Published Tutorials
```bash
curl "http://localhost:8080/api/tutorials/published"
```

## Comparison with Original Java Implementation

### Similarities
- Same REST API endpoints and behavior
- Same 3-tier architecture (Controller/Router, Repository/Database, Model)
- CORS configuration for frontend integration
- SQLite database with similar schema

### Differences
- **Framework**: FastAPI instead of Spring Boot (lighter, faster)
- **Language**: Python instead of Java
- **ORM**: SQLAlchemy instead of Hibernate/JPA
- **Validation**: Pydantic instead of Bean Validation
- **Documentation**: Auto-generated Swagger/OpenAPI docs
- **Database**: SQLite instead of H2 (both are file-based)

## Features

✅ Full CRUD operations  
✅ Search by title (case-insensitive)  
✅ Filter by published status  
✅ CORS enabled for cross-origin requests  
✅ Auto-generated API documentation  
✅ Request/response validation  
✅ Exception handling  
✅ SQLAlchemy ORM integration  

## Future Enhancements

Potential improvements (similar to the Java version):
- Add service layer for business logic separation
- Implement authentication and authorization
- Add comprehensive unit and integration tests
- Add request validation constraints
- Implement logging with proper log levels
- Add pagination support
- Docker containerization
- Environment-based configuration

## Development

The application runs in reload mode by default during development. Any changes to the code will automatically restart the server.

## Production Deployment

For production, use a production-ready ASGI server:

```bash
uvicorn app.main:app --host 0.0.0.0 --port 8080 --workers 4
```

Or with Gunicorn + Uvicorn workers:

```bash
gunicorn app.main:app --workers 4 --worker-class uvicorn.workers.UvicornWorker --bind 0.0.0.0:8080
```
