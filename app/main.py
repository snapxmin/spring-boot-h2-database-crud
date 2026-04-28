from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.database import engine
from app.models import Base
from app.routers import tutorial

Base.metadata.create_all(bind=engine)

app = FastAPI(
    title="Tutorial API",
    description="Spring Boot JPA + H2 example converted to Python FastAPI + SQLite",
    version="0.0.1"
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:8081"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(tutorial.router, prefix="/api")

@app.get("/")
def root():
    return {"message": "Tutorial API - Python FastAPI Implementation"}
