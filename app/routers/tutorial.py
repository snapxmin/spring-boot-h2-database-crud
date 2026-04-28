from fastapi import APIRouter, Depends, HTTPException, Query, status
from sqlalchemy.orm import Session
from typing import List, Optional
from app import models, schemas
from app.database import get_db

router = APIRouter(
    prefix="/tutorials",
    tags=["tutorials"]
)

@router.get("", response_model=List[schemas.Tutorial])
def get_all_tutorials(
    title: Optional[str] = Query(None),
    db: Session = Depends(get_db)
):
    try:
        if title is None:
            tutorials = db.query(models.Tutorial).all()
        else:
            tutorials = db.query(models.Tutorial).filter(
                models.Tutorial.title.ilike(f"%{title}%")
            ).all()
        
        if not tutorials:
            raise HTTPException(status_code=status.HTTP_204_NO_CONTENT)
        
        return tutorials
    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(status_code=status.HTTP_500_INTERNAL_SERVER_ERROR, detail=str(e))

@router.get("/published", response_model=List[schemas.Tutorial])
def get_published_tutorials(db: Session = Depends(get_db)):
    try:
        tutorials = db.query(models.Tutorial).filter(
            models.Tutorial.published == True
        ).all()
        
        if not tutorials:
            raise HTTPException(status_code=status.HTTP_204_NO_CONTENT)
        
        return tutorials
    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(status_code=status.HTTP_500_INTERNAL_SERVER_ERROR, detail=str(e))

@router.get("/{id}", response_model=schemas.Tutorial)
def get_tutorial_by_id(id: int, db: Session = Depends(get_db)):
    tutorial = db.query(models.Tutorial).filter(models.Tutorial.id == id).first()
    
    if tutorial is None:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Tutorial not found")
    
    return tutorial

@router.post("", response_model=schemas.Tutorial, status_code=status.HTTP_201_CREATED)
def create_tutorial(tutorial: schemas.TutorialCreate, db: Session = Depends(get_db)):
    try:
        db_tutorial = models.Tutorial(
            title=tutorial.title,
            description=tutorial.description,
            published=False
        )
        db.add(db_tutorial)
        db.commit()
        db.refresh(db_tutorial)
        return db_tutorial
    except Exception as e:
        db.rollback()
        raise HTTPException(status_code=status.HTTP_500_INTERNAL_SERVER_ERROR, detail=str(e))

@router.put("/{id}", response_model=schemas.Tutorial)
def update_tutorial(id: int, tutorial: schemas.TutorialUpdate, db: Session = Depends(get_db)):
    db_tutorial = db.query(models.Tutorial).filter(models.Tutorial.id == id).first()
    
    if db_tutorial is None:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Tutorial not found")
    
    if tutorial.title is not None:
        db_tutorial.title = tutorial.title
    if tutorial.description is not None:
        db_tutorial.description = tutorial.description
    if tutorial.published is not None:
        db_tutorial.published = tutorial.published
    
    try:
        db.commit()
        db.refresh(db_tutorial)
        return db_tutorial
    except Exception as e:
        db.rollback()
        raise HTTPException(status_code=status.HTTP_500_INTERNAL_SERVER_ERROR, detail=str(e))

@router.delete("/{id}", status_code=status.HTTP_204_NO_CONTENT)
def delete_tutorial(id: int, db: Session = Depends(get_db)):
    try:
        db_tutorial = db.query(models.Tutorial).filter(models.Tutorial.id == id).first()
        if db_tutorial is None:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Tutorial not found")
        
        db.delete(db_tutorial)
        db.commit()
        return None
    except HTTPException:
        raise
    except Exception as e:
        db.rollback()
        raise HTTPException(status_code=status.HTTP_500_INTERNAL_SERVER_ERROR, detail=str(e))

@router.delete("", status_code=status.HTTP_204_NO_CONTENT)
def delete_all_tutorials(db: Session = Depends(get_db)):
    try:
        db.query(models.Tutorial).delete()
        db.commit()
        return None
    except Exception as e:
        db.rollback()
        raise HTTPException(status_code=status.HTTP_500_INTERNAL_SERVER_ERROR, detail=str(e))
