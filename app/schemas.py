from pydantic import BaseModel, ConfigDict
from typing import Optional

class TutorialBase(BaseModel):
    title: str
    description: str
    published: bool = False

class TutorialCreate(TutorialBase):
    pass

class TutorialUpdate(TutorialBase):
    title: Optional[str] = None
    description: Optional[str] = None
    published: Optional[bool] = None

class Tutorial(TutorialBase):
    model_config = ConfigDict(from_attributes=True)
    
    id: int
