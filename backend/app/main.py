from contextlib import asynccontextmanager

from fastapi import FastAPI

from .database import init_db
from .routers import auth, tareas


@asynccontextmanager
async def lifespan(app: FastAPI):
    init_db()
    yield


app = FastAPI(
    title="Practica 2 - Tareas API",
    description=(
        "API REST con autenticacion (registro/login con JWT) y CRUD de tareas. "
        "Practica 2, Desarrollo de aplicaciones moviles nativas, ESCOM-IPN."
    ),
    version="1.0.0",
    lifespan=lifespan,
)

app.include_router(auth.router)
app.include_router(tareas.router)


@app.get("/")
def health_check():
    return {"status": "ok", "service": "practica2-tareas-api"}
