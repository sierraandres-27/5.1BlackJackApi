# Implementación Básica de una Aplicación Reactiva con Spring WebFlux

## Descripción

Este proyecto tiene como objetivo el desarrollo de una aplicación reactiva utilizando **Spring WebFlux** y **MongoDB**. La aplicación permite realizar operaciones relacionadas con un juego de **Blackjack**, como crear partidas, realizar jugadas, y gestionar el ranking de jugadores.

## Funcionalidades Principales

### Implementación Básica

- **Desarrollo de una aplicación reactiva** con Spring WebFlux, incluyendo la configuración de MongoDB reactiva.
- **Gestión de excepciones global** mediante la implementación de un `GlobalExceptionHandler`.
- **Configuración de bases de datos** para utilizar dos esquemas: MySQL y MongoDB.
- **Pruebas unitarias** de al menos un controlador y un servicio utilizando **JUnit** y **Mockito**.
- **Documentación automática** de la API utilizando **Swagger**.

## Endpoints para el Juego de Blackjack

### Crear partida

- **Método**: `POST`
- **Descripción**: Crear una nueva partida de Blackjack.
- **Endpoint**: `/game/new`
- **Cuerpo de la solicitud**:
  - `nombre`: Nombre del jugador.
- **Respuesta exitosa**:
  - Código `201 Created` con información sobre la partida creada.

### Obtener detalles de partida

- **Método**: `GET`
- **Descripción**: Obtiene los detalles de una partida específica de Blackjack.
- **Endpoint**: `/game/{id}`
- **Parámetros de entrada**: Identificador único de la partida (`id`).
- **Respuesta exitosa**:
  - Código `200 OK` con información detallada sobre la partida.

### Realizar jugada

- **Método**: `POST`
- **Descripción**: Realiza una jugada en una partida de Blackjack existente.
- **Endpoint**: `/game/{id}/play`
- **Parámetros de entrada**:
  - `id`: Identificador único de la partida.
  - `jugada`: Datos de la jugada (por ejemplo, tipos de jugada y cantidad apostada).
- **Respuesta exitosa**:
  - Código `200 OK` con el resultado de la jugada y el estado actual de la partida.

### Eliminar partida

- **Método**: `DELETE`
- **Descripción**: Elimina una partida de Blackjack existente.
- **Endpoint**: `/game/{id}/delete`
- **Parámetros de entrada**: Identificador único de la partida (`id`).
- **Respuesta exitosa**:
  - Código `204 No Content` si la partida se elimina correctamente.

### Obtener ranking de jugadores

- **Método**: `GET`
- **Descripción**: Obtiene el ranking de los jugadores basado en su rendimiento en las partidas de Blackjack.
- **Endpoint**: `/ranking`
- **Parámetros de entrada**: Ninguno.
- **Respuesta exitosa**:
  - Código `200 OK` con la lista de jugadores ordenada por su posición en el ranking y su puntaje.

### Cambiar nombre del jugador

- **Método**: `PUT`
- **Descripción**: Cambia el nombre de un jugador en una partida de Blackjack existente.
- **Endpoint**: `/player/{playerId}`
- **Cuerpo de la solicitud**:
  - `nombre`: Nuevo nombre del jugador.
- **Parámetros de entrada**: `playerId`: Identificador único del jugador.
- **Respuesta exitosa**:
  - Código `200 OK` con información actualizada del jugador.

---

## Nivel 2: Dockerización de la Aplicación

Para dockerizar la aplicación, sigue los siguientes pasos:

### Paso 1: Crear el archivo Dockerfile

Crea un archivo llamado `Dockerfile` en la raíz del proyecto con el siguiente contenido:

```dockerfile
# Usa una imagen base oficial de OpenJDK
FROM openjdk:17-jdk-slim

# Establece el directorio de trabajo
WORKDIR /app

# Copia los archivos del proyecto a la imagen
COPY target/*.jar app.jar

# Expone el puerto que usará la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
