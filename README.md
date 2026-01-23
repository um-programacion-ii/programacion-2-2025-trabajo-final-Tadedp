# Trabajo Final 2025 - Programación II

Implementación del trabajo final 2025 para la cátedra de **Programación II**.  
Consiste en un sistema que permite la venta de entradas para la asistencia a distintos tipos de eventos.

## Requerimientos

* **Java JDK 21**
* **Docker y Docker Compose**
* **Android Studio**

## Ejecución

Luego de navegar al directorio raíz correspondiente al proyecto en una terminal, continuar con los siguientes pasos:

### 1. Preparación Backend

```
cd backend/
./mvnw clean package -DskipTests
cd ..
```

### 2. Preparación Proxy

```
cd proxy/
./mvnw clean package -DskipTests
cd ..
```

### 3. Deploy con Docker

```
docker compose up --build
```

### 4. Preparación Cliente Móvil

1. Abrir proyecto en Android Studio.
2. Sincronizar proyecto y seleccionar o preparar un dispositivo android.
3. Ejecutar proyecto.

## Autor

Alumno: Tadeo Drube Perez \
Legajo: 62222
