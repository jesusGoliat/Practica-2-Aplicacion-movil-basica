from datetime import datetime
from typing import Optional

from pydantic import BaseModel, Field


class UserCreate(BaseModel):
    username: str = Field(min_length=3, max_length=50)
    password: str = Field(min_length=8, max_length=128)


class UserLogin(BaseModel):
    username: str
    password: str


class UserRead(BaseModel):
    id: int
    username: str
    created_at: datetime


class Token(BaseModel):
    access_token: str
    token_type: str = "bearer"
    expires_in: int


class TareaCreate(BaseModel):
    titulo: str = Field(min_length=1, max_length=200)
    descripcion: Optional[str] = Field(default=None, max_length=1000)
    completada: bool = False


class TareaUpdate(BaseModel):
    titulo: Optional[str] = Field(default=None, min_length=1, max_length=200)
    descripcion: Optional[str] = Field(default=None, max_length=1000)
    completada: Optional[bool] = None


class TareaRead(BaseModel):
    id: int
    titulo: str
    descripcion: Optional[str]
    completada: bool
    owner_id: int
    created_at: datetime
    updated_at: datetime
