# 💱 MoneyChanger Spring Boot Enhanced API

This is a production-ready Spring Boot project for managing money changers, designed with:

- Role-based logic (`admin` vs `staff`)
- Soft delete support
- REST API using Spring Web
- MySQL-compatible schema (can connect to AWS RDS)

---

## 🏗️ Setup Instructions

### 1. Requirements
- Java 17 or higher
- Maven
- MySQL (local or AWS RDS)

### 2. Database Setup
Run the provided `database.sql` to create tables:
```sql
CREATE DATABASE moneychanger;
USE moneychanger;
-- then run the schema script
```

### 3. Application Properties
Create `src/main/resources/application.properties`:

```
spring.datasource.url=jdbc:mysql://localhost:3306/moneychanger
spring.datasource.username=your_mysql_user
spring.datasource.password=your_mysql_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
server.port=8080
```

### 4. Run the Application
Use IntelliJ IDEA or run:
```bash
./mvnw spring-boot:run
```

### 5. Test the API with Postman

| Method | URL | Description |
|--------|-----|-------------|
| GET | /api/moneychangers | List all |
| GET | /api/moneychangers/{id} | Get by ID |
| POST | /api/moneychangers | Create |
| PUT | /api/moneychangers/{id} | Update |
| DELETE | /api/moneychangers/{id}?role=admin | Soft delete (only if role is admin) |

---

## 📁 Structure

- `model/` - Entity definitions
- `repository/` - DB access
- `service/` - Business logic
- `controller/` - REST API endpoints

---

## ✅ Additional Notes

- `isDeleted` field is used for soft delete.
- Accounts table includes `role` to differentiate between admin and staff.
- Prevents non-admin from deleting.