from fastapi.testclient import TestClient
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from app.main import app
from app.database import Base, get_db
from app.models import Tutorial

SQLALCHEMY_DATABASE_URL = "sqlite:///./test.db"

engine = create_engine(
    SQLALCHEMY_DATABASE_URL, connect_args={"check_same_thread": False}
)
TestingSessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

Base.metadata.create_all(bind=engine)

def override_get_db():
    try:
        db = TestingSessionLocal()
        yield db
    finally:
        db.close()

app.dependency_overrides[get_db] = override_get_db

client = TestClient(app)

def test_read_root():
    response = client.get("/")
    assert response.status_code == 200
    assert "message" in response.json()

def test_create_tutorial():
    response = client.post(
        "/api/tutorials",
        json={"title": "Test Tutorial", "description": "Test Description", "published": False}
    )
    assert response.status_code == 201
    assert response.json()["title"] == "Test Tutorial"
    assert response.json()["description"] == "Test Description"
    assert response.json()["published"] == False

def test_get_tutorial_by_id():
    response = client.post(
        "/api/tutorials",
        json={"title": "Get Test", "description": "Get Description", "published": False}
    )
    tutorial_id = response.json()["id"]
    
    response = client.get(f"/api/tutorials/{tutorial_id}")
    assert response.status_code == 200
    assert response.json()["title"] == "Get Test"

def test_update_tutorial():
    response = client.post(
        "/api/tutorials",
        json={"title": "Update Test", "description": "Update Description", "published": False}
    )
    tutorial_id = response.json()["id"]
    
    response = client.put(
        f"/api/tutorials/{tutorial_id}",
        json={"title": "Updated Title", "description": "Updated Description", "published": True}
    )
    assert response.status_code == 200
    assert response.json()["title"] == "Updated Title"
    assert response.json()["published"] == True

def test_delete_tutorial():
    response = client.post(
        "/api/tutorials",
        json={"title": "Delete Test", "description": "Delete Description", "published": False}
    )
    tutorial_id = response.json()["id"]
    
    response = client.delete(f"/api/tutorials/{tutorial_id}")
    assert response.status_code == 204
    
    response = client.get(f"/api/tutorials/{tutorial_id}")
    assert response.status_code == 404
