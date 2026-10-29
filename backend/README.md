# TecnoUAM — Backend (Spring Boot)

API REST para el préstamo de equipos tecnológicos. Usa Spring Boot 4, Spring Data JPA,
PostgreSQL 18, y Spring Security con JWT (HS256).

## Requisitos

- Java 21
- Docker (para PostgreSQL)

## Variables de entorno

Copie `.env.example` como `.env` dentro de `backend/` y complete los valores. El archivo `.env`
está en `.gitignore` y no se sube al repositorio. Spring Boot (`spring.config.import`) y
`docker compose` lo leen automáticamente.

| Variable               | Obligatoria | Descripción                                                  |
|------------------------|-------------|--------------------------------------------------------------|
| `JWT_SECRET_BASE64`    | Sí          | Clave HS256 en Base64, mínimo 32 bytes: `openssl rand -base64 32` |
| `DB_PASSWORD`          | Sí          | Contraseña de PostgreSQL                                     |
| `DB_USERNAME`          | No          | Usuario de PostgreSQL (por defecto `tecnouam`)               |
| `DB_URL`               | No          | Por defecto `jdbc:postgresql://localhost:5432/tecnouam`      |
| `CORS_ALLOWED_ORIGINS` | No          | Por defecto `http://localhost:5173,http://localhost:3000`    |

## Ejecución

```sh
cd backend
cp .env.example .env        # y completar JWT_SECRET_BASE64 y DB_PASSWORD
./mvnw spring-boot:run      # levanta PostgreSQL con compose.yml y la API en :9090
```

Si se ejecuta el jar (`java -jar target/*.jar`), primero hay que levantar la base de datos con
`docker compose up -d`.

## Base de datos

- `src/main/resources/schema.sql`: crea las tablas `equipo` y `prestamo`, con la FK, los CHECK
  de estados y fechas, y un índice único parcial que impide dos préstamos ACTIVOS para el mismo
  equipo.
- `src/main/resources/data.sql`: datos de demostración (5 equipos y 1 préstamo activo para
  `UAM-PRY-001`). Solo se insertan si las tablas están vacías.

Ambos scripts corren al iniciar la aplicación (`spring.sql.init.mode=always`). Hibernate solo
valida el esquema (`ddl-auto=validate`).

## Cuentas de prueba

| Usuario      | Contraseña       | Rol   |
|--------------|------------------|-------|
| `admin`      | `Admin123!`      | ADMIN |
| `instructor` | `Instructor123!` | USER  |

## Endpoints

Todas las rutas, salvo el login, requieren `Authorization: Bearer <token>`.

| Método | Ruta                            | Rol         | Éxito | Errores       |
|--------|---------------------------------|-------------|-------|---------------|
| POST   | `/api/auth/login`               | público     | 200   | 400, 401      |
| GET    | `/api/auth/perfil`              | autenticado | 200   | 401           |
| GET    | `/api/equipos`                  | USER, ADMIN | 200   | 401           |
| GET    | `/api/equipos/{id}`             | USER, ADMIN | 200   | 404           |
| POST   | `/api/equipos`                  | ADMIN       | 201   | 400, 403, 409 |
| PUT    | `/api/equipos/{id}`             | ADMIN       | 200   | 400, 404, 409 |
| DELETE | `/api/equipos/{id}`             | ADMIN       | 204   | 404, 409      |
| GET    | `/api/prestamos`                | USER, ADMIN | 200   | 401           |
| GET    | `/api/prestamos/{id}`           | USER, ADMIN | 200   | 404           |
| POST   | `/api/prestamos`                | ADMIN       | 201   | 400, 404, 409 |
| PUT    | `/api/prestamos/{id}/devolver`  | ADMIN       | 200   | 404, 409      |

Los listados devuelven un arreglo JSON directo: equipos ordenados por `id` y préstamos del más
reciente al más antiguo.

### Ejemplos de cuerpos

```jsonc
// POST /api/auth/login
{ "username": "admin", "password": "Admin123!" }
// -> { "token": "eyJ...", "type": "Bearer" }

// POST /api/equipos   (tipo: PORTATIL | PROYECTOR | TABLETA)
{ "codigoPatrimonial": "UAM-LAP-010", "nombre": "Laptop HP ProBook 450", "tipo": "PORTATIL" }

// POST /api/prestamos   (fechas en formato yyyy-MM-dd)
{ "equipoId": 1, "nombreSolicitante": "Carlos Ruiz",
  "fechaPrestamo": "2026-10-28", "fechaDevolucionPrevista": "2026-10-31" }
```

## Reglas de negocio

- Un equipo nuevo siempre empieza DISPONIBLE. Su estado solo cambia al prestarlo o devolverlo;
  `PUT /api/equipos/{id}` no lo modifica.
- Prestar un equipo que ya está PRESTADO responde **409**. La fila del equipo se bloquea
  (`PESSIMISTIC_WRITE`) durante el préstamo, y el índice único parcial lo garantiza también en la
  base de datos.
- Registrar un préstamo deja el equipo en PRESTADO. Registrar la devolución lo vuelve a dejar
  DISPONIBLE y guarda `fechaDevolucion`.
- Devolver un préstamo que ya fue DEVUELTO responde **409**.
- Si `fechaDevolucionPrevista` es anterior a `fechaPrestamo`, la API responde **400**.
- Eliminar un equipo con un préstamo ACTIVO responde **409**. Si solo tiene préstamos devueltos,
  se elimina junto con ese historial.
- Un código patrimonial repetido responde **409**.

## Seguridad (flujo JWT)

1. `POST /api/auth/login`: el `AuthenticationManager` valida las credenciales (BCrypt). Si son
   incorrectas, responde **401**.
2. `TokenService` firma un JWT HS256 con `sub`, `roles`, `iat`, `exp` (60 min) e `iss`.
3. En cada petición, el resource server de Spring Security (`BearerTokenAuthenticationFilter`)
   lee el header `Authorization`, verifica la firma y la expiración con `JwtDecoder`, y convierte
   el claim `roles` en autoridades `ROLE_*`.
4. La API no tiene sesión (`STATELESS`). Sin token, o con un token inválido o vencido, responde
   **401**. Con un token válido pero sin el rol necesario, responde **403**.
