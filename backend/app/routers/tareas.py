from datetime import datetime, timezone

from fastapi import APIRouter, Depends, HTTPException, status
from sqlmodel import Session, select

from ..database import get_session
from ..models import Tarea, User
from ..schemas import TareaCreate, TareaRead, TareaUpdate
from ..security import get_current_user

router = APIRouter(prefix="/tareas", tags=["tareas"])


def _get_owned_tarea(tarea_id: int, session: Session, user: User) -> Tarea:
    tarea = session.get(Tarea, tarea_id)
    if not tarea or tarea.owner_id != user.id:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND, detail="Tarea no encontrada"
        )
    return tarea


@router.post("", response_model=TareaRead, status_code=status.HTTP_201_CREATED)
def crear_tarea(
    payload: TareaCreate,
    session: Session = Depends(get_session),
    user: User = Depends(get_current_user),
):
    tarea = Tarea(**payload.model_dump(), owner_id=user.id)
    session.add(tarea)
    session.commit()
    session.refresh(tarea)
    return tarea


@router.get("", response_model=list[TareaRead])
def listar_tareas(
    session: Session = Depends(get_session),
    user: User = Depends(get_current_user),
):
    return session.exec(select(Tarea).where(Tarea.owner_id == user.id)).all()


@router.get("/{tarea_id}", response_model=TareaRead)
def obtener_tarea(
    tarea_id: int,
    session: Session = Depends(get_session),
    user: User = Depends(get_current_user),
):
    return _get_owned_tarea(tarea_id, session, user)


@router.put("/{tarea_id}", response_model=TareaRead)
def actualizar_tarea(
    tarea_id: int,
    payload: TareaUpdate,
    session: Session = Depends(get_session),
    user: User = Depends(get_current_user),
):
    tarea = _get_owned_tarea(tarea_id, session, user)
    updates = payload.model_dump(exclude_unset=True)
    if not updates:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="No se recibio ningun campo para actualizar",
        )

    for field, value in updates.items():
        setattr(tarea, field, value)
    tarea.updated_at = datetime.now(timezone.utc)

    session.add(tarea)
    session.commit()
    session.refresh(tarea)
    return tarea


@router.delete("/{tarea_id}", status_code=status.HTTP_200_OK)
def borrar_tarea(
    tarea_id: int,
    session: Session = Depends(get_session),
    user: User = Depends(get_current_user),
):
    tarea = _get_owned_tarea(tarea_id, session, user)
    session.delete(tarea)
    session.commit()
    return {"detail": "Tarea eliminada correctamente", "id": tarea_id}
