# Imagen base ligera con Python 3.12
FROM python:3.12-slim

WORKDIR /app

# Instalar dependencias primero para aprovechar la cache de capas de Docker
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copiar el codigo de la aplicacion
COPY app ./app

# Puerto en el que escucha uvicorn dentro del contenedor
EXPOSE 8000

# Arranca el servidor ASGI escuchando en todas las interfaces del contenedor
CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8000"]
