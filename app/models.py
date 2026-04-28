from sqlalchemy import Column, Integer, String, Boolean
from app.database import Base

class Tutorial(Base):
    __tablename__ = "tutorials"

    id = Column(Integer, primary_key=True, index=True, autoincrement=True)
    title = Column(String, index=True)
    description = Column(String)
    published = Column(Boolean, default=False)

    def __repr__(self):
        return f"Tutorial(id={self.id}, title={self.title}, desc={self.description}, published={self.published})"
