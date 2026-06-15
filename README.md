# TaskFlow — Task Management Microservices

Aplikacja do zarządzania zadaniami zbudowana w architekturze mikroserwisowej.
Umożliwia tworzenie projektów, zarządzanie zadaniami w stylu Kanban oraz
komunikację w czasie rzeczywistym przez WebSocket.

## 🛠 Tech Stack

**Backend:**
- Java 21 + Spring Boot 4.x
- Spring Cloud (Eureka, API Gateway)
- Spring Security + JWT
- MongoDB
- WebSocket / STOMP
- Docker

**Frontend:**
- React 18
- Axios
- SockJS + STOMP.js

## 🏗 Architektura

```
Client (React :3000)
        ↓
API Gateway (:8765) — JWT verification
        ↓
┌─────────────────────────────┐
│     Eureka Registry (:8761) │
└─────────────────────────────┘
        ↓
┌─────────────┬───────────────┬─────────────┬────────────────┐
│auth-service │project-service│ task-service│comment-service │
│   :8081     │    :8082      │    :8083    │    :8084       │
└─────────────┴───────────────┴─────────────┴────────────────┘
```

## ✅ Funkcjonalności

- Rejestracja i logowanie z JWT + Refresh Token
- Tworzenie i zarządzanie projektami
- Tablica Kanban z drag & drop
- Tworzenie, edycja i usuwanie zadań w czasie rzeczywistym (WebSocket)
- System komentarzy z live updates
- Komunikacja między serwisami przez OpenFeign

## 🚀 Uruchomienie lokalne

### Wymagania
- Java 21
- Maven
- Docker + Docker Compose

### Kroki

1. Sklonuj repozytorium
```bash
git clone https://github.com/username/task-management-microservices
cd task-management-microservices
```

2. Zbuduj każdy serwis
```bash
mvn clean package -DskipTests
```

3. Uruchom przez Docker Compose
```bash
docker-compose up --build
```

4. Aplikacja dostępna pod:
- Frontend: http://localhost:3000
- API Gateway: http://localhost:8765
- Eureka: http://localhost:8761

## 🔐 Zmienne środowiskowe

| Zmienna | Opis |
|---------|------|
| `JWT_SECRET` | Klucz do podpisywania tokenów JWT |
| `MONGO_URI` | URI do bazy MongoDB |
| `EUREKA_URI` | Adres Eureka Server |

## 📡 API Endpoints

### Auth Service
| Metoda | Endpoint | Opis |
|--------|----------|------|
| POST | `/api/auth/register` | Rejestracja |
| POST | `/api/auth/login` | Logowanie |
| POST | `/api/auth/refresh` | Odświeżenie tokenu |

### Project Service
| Metoda | Endpoint | Opis |
|--------|----------|------|
| POST | `/api/projects/addProject` | Utwórz projekt |
| GET | `/api/projects/{id}` | Pobierz projekt |
| PUT | `/api/projects/update` | Aktualizuj projekt |
| DELETE | `/api/projects/{id}` | Usuń projekt |

### Task Service (WebSocket)
| Destination | Opis |
|-------------|------|
| `/app/taskManagement.createTask` | Utwórz task |
| `/app/taskManagement.updateTask` | Aktualizuj task |
| `/app/taskManagement.deleteTask` | Usuń task |
| `/app/taskManagement.moveTask` | Przenieś task |

